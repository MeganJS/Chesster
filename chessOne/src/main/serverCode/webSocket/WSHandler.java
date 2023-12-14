package serverCode.webSocket;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serverCode.DAOs.SQLGameDAO;
import serverCode.DAOs.SQLUserAuthDAO;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;
import userCommandClasses.MakeMoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WSHandler {
    private ConnectionManager connMan = new ConnectionManager();
    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();
    static SQLGameDAO gameDAO = new SQLGameDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            if (command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
                handleJoinGame(session, message);
            } else if (command.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
                handleJoinGame(session, message);
            } else {
                var conn = connMan.getConnection(command.getAuthString());
                if (conn != null) {
                    switch (command.getCommandType()) {
                        case LEAVE:
                            handleLeave(session, command);
                            break;
                        case MAKE_MOVE:
                            handleMakeMove(session, message);
                            break;
                        case RESIGN:
                            handleResign(session, command);
                    }
                } else {
                    //send an error that the user does not have a current connection
                }
            }
        } catch (Throwable e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            System.out.println(e);
            //session.getRemote().sendString(new Gson().toJson(error));
        }
    }

    /***
     * this will:
     *  check if the user has a valid authToken
     *  if so, add them to connections
     *  (if not send an error)
     *  send them a LOAD message
     *  find the game they're part of and send the notification to all other connections in that game
     */
    private void handleJoinGame(Session session, String message) {
        Gson json = new Gson();
        int gameID;
        ChessGame.TeamColor playerColor;
        String authString;
        try {
            UserGameCommand command = json.fromJson(message, UserGameCommand.class);
            authString = command.getAuthString();
            playerColor = command.getPlayerColor();
            AuthToken userAuthToken = userAuthDAO.readAuthToken(authString);
            gameID = command.getGameID();
            connMan.addConnection(authString, new Connection(authString, session, gameID));
            loadGameOnJoin(session, gameID);

            Game chessModel = gameDAO.readGame(gameID);
            String username = userAuthToken.getUsername();
            ServerMessageNotify notify = new ServerMessageNotify(username + " has joined " + chessModel.getGameName() + " as " + createColorMessage(playerColor) + ".\n");
            connMan.broadcast(gameID, userAuthToken.getAuthToken(), json.toJson(notify));

        } catch (DataAccessException | IOException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }


    private String createColorMessage(ChessGame.TeamColor playerColor) {
        if (playerColor == ChessGame.TeamColor.WHITE) {
            return "white player";
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            return "black player";
        } else {
            return "observer";
        }
    }

    private void loadGameOnJoin(Session session, int gameID) throws IOException {
        Gson json = new Gson();
        try {
            Game chessModel = gameDAO.readGame(gameID);
            ChessGame chessGame = chessModel.getChessGame();
            ServerMessageLoad loadGame = new ServerMessageLoad(json.toJson(chessGame));
            session.getRemote().sendString(json.toJson(loadGame));
        } catch (IOException | DataAccessException e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getRemote().sendString(json.toJson(error));
        }
    }

    private void loadGameAll(int gameID) throws IOException {
        Gson json = new Gson();
        try {
            Game chessModel = gameDAO.readGame(gameID);
            ChessGame chessGame = chessModel.getChessGame();
            ServerMessageLoad loadGame = new ServerMessageLoad(json.toJson(chessGame));
            connMan.broadcast(gameID, "", json.toJson(loadGame));
        } catch (IOException | DataAccessException e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            connMan.broadcast(gameID, "", json.toJson(error));
        }
    }

    /***
     * this will:
     *  make the move on the board
     *  sends a LOAD message to all in game clients
     *  sends a NOTIFY message to all in game clients
     *  if move results in Stalemate or Checkmate, ends the game
     */
    private void handleMakeMove(Session session, String message) throws IOException {
        try {
            MakeMoveCommand moveCommand = createMoveCommand(message);
            AuthToken userAuthToken = userAuthDAO.readAuthToken(moveCommand.getAuthString());

            ChessGame chessGame = gameDAO.readGame(moveCommand.getGameID()).getChessGame();
            if (chessGame.getWinningTeam() != null) {
                ServerMessageError error = new ServerMessageError("Error: the game is over. The time for moves is past.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            if (moveCommand.getPlayerColor() != chessGame.getTeamTurn()) {
                ServerMessageError error = new ServerMessageError("Error: it's not your turn to move!");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            chessGame.makeMove(moveCommand.getMove());
            gameDAO.updateGame(moveCommand.getGameID(), chessGame);
            //load game
            loadGameAll(moveCommand.getGameID());
            //notify
            String username = userAuthToken.getUsername();
            ServerMessageNotify notify = new ServerMessageNotify(username + " made move" + moveToString(moveCommand.getMove()) + ".\n");
            connMan.broadcast(moveCommand.getGameID(), userAuthToken.getAuthToken(), new Gson().toJson(notify));
            notifyForWin(chessGame, moveCommand.getGameID());
        } catch (DataAccessException | InvalidMoveException | IOException e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(error));
        }
    }

    private String moveToString(ChessMove move) {
        char startCol = (char) (move.getStartPosition().getColumn() + 96);
        char startRow = (char) (move.getStartPosition().getRow() + '0');
        char endCol = (char) (move.getEndPosition().getColumn() + 96);
        char endRow = (char) (move.getEndPosition().getRow() + '0');
        return " " + startCol + startRow + " " + endCol + endRow;
    }


    private void notifyForWin(ChessGame chessGame, int gameID) throws IOException, DataAccessException {
        if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            chessGame.setWinningTeam(ChessGame.TeamColor.WHITE);
            gameDAO.updateGame(gameID, chessGame);
            ServerMessageNotify notifyWin = new ServerMessageNotify("Black is in checkmate. White team wins!\n");
            connMan.broadcast(gameID, "", new Gson().toJson(notifyWin));
        } else if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            chessGame.setWinningTeam(ChessGame.TeamColor.BLACK);
            gameDAO.updateGame(gameID, chessGame);
            ServerMessageNotify notifyWin = new ServerMessageNotify("White is in checkmate. Black team wins!\n");
            connMan.broadcast(gameID, "", new Gson().toJson(notifyWin));
        } else if (chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
            chessGame.setWinningTeam(ChessGame.TeamColor.BLACK); //TODO fix for stalemate
            gameDAO.updateGame(gameID, chessGame);
            ServerMessageNotify notifyDraw = new ServerMessageNotify("Black is in stalemate. It's a draw!\n");
            connMan.broadcast(gameID, "", new Gson().toJson(notifyDraw));
        } else if (chessGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
            chessGame.setWinningTeam(ChessGame.TeamColor.WHITE);
            gameDAO.updateGame(gameID, chessGame);
            ServerMessageNotify notifyDraw = new ServerMessageNotify("White is in stalemate. It's a draw!\n");
            connMan.broadcast(gameID, "", new Gson().toJson(notifyDraw));
        }
    }

    private MakeMoveCommand createMoveCommand(String message) {
        Gson jsonChess = createChessGson();
        return jsonChess.fromJson(message, MakeMoveCommand.class);
    }

    /***
     * this will:
     *  remove user from game
     *  notify other users
     *  remove connection from connections
     */
    private void handleLeave(Session session, UserGameCommand command) throws IOException {
        Gson json = new Gson();
        try {
            String authString = command.getAuthString();
            AuthToken userAuthToken = userAuthDAO.readAuthToken(authString);
            int gameID = connMan.getConnection(authString).gameID;
            gameDAO.claimGameSpot(gameID, null, command.getPlayerColor());

            Game chessModel = gameDAO.readGame(gameID);
            String username = userAuthToken.getUsername();
            ServerMessageNotify notify = new ServerMessageNotify(username + " has left the game " + chessModel.getGameName() + ".\n");
            connMan.broadcast(gameID, userAuthToken.getAuthToken(), json.toJson(notify));
            connMan.removeConnection(authString);
        } catch (Exception e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getRemote().sendString(json.toJson(error));
        }
    }


    /***
     * this will:
     *  end the game (how?)
     *  notify all clients in game that this player resigned and thus the other player won
     */
    private void handleResign(Session session, UserGameCommand command) throws IOException {
        Gson json = new Gson();
        try {
            String authString = command.getAuthString();
            AuthToken userAuthToken = userAuthDAO.readAuthToken(authString);
            int gameID = command.getGameID();

            Game chessModel = gameDAO.readGame(gameID);
            ChessGame chessGame = chessModel.getChessGame();
            String winTeamColor;
            if (command.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                chessGame.setWinningTeam(ChessGame.TeamColor.WHITE);
                winTeamColor = "White";
            } else {
                chessGame.setWinningTeam(ChessGame.TeamColor.BLACK);
                winTeamColor = "Black";
            }
            gameDAO.updateGame(gameID, chessGame);

            String username = userAuthToken.getUsername();
            ServerMessageNotify notify = new ServerMessageNotify(username + " has resigned. " + winTeamColor + " wins.\n");
            connMan.broadcast(gameID, "", json.toJson(notify));
        } catch (Exception e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getRemote().sendString(json.toJson(error));
        }
    }

    public static Gson createChessGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // This line should only be needed if your board class is using a Map to store chess pieces instead of a 2D array.
        gsonBuilder.enableComplexMapKeySerialization();

        gsonBuilder.registerTypeAdapter(ChessGame.class,
                (JsonDeserializer<ChessGame>) (el, type, ctx) -> ctx.deserialize(el, ChessGameImp.class));

        gsonBuilder.registerTypeAdapter(ChessBoard.class,
                (JsonDeserializer<ChessBoard>) (el, type, ctx) -> ctx.deserialize(el, ChessBoardImp.class));

        gsonBuilder.registerTypeAdapter(ChessPiece.class,
                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> ctx.deserialize(el, ChessPieceImp.class));

        gsonBuilder.registerTypeAdapter(ChessMove.class,
                (JsonDeserializer<ChessMove>) (el, type, ctx) -> ctx.deserialize(el, ChessMoveImp.class));

        gsonBuilder.registerTypeAdapter(ChessPosition.class,
                (JsonDeserializer<ChessPosition>) (el, type, ctx) -> ctx.deserialize(el, ChessPositionImp.class));

        gsonBuilder.registerTypeAdapter(PieceRuleset.class,
                (JsonDeserializer<PieceRuleset>) (el, type, ctx) -> {
                    PieceRuleset ruleset = null;
                    if (el.isJsonObject()) {
                        String pieceType = el.getAsJsonObject().get("type").getAsString();
                        switch (ChessPiece.PieceType.valueOf(pieceType)) {
                            case PAWN -> ruleset = ctx.deserialize(el, PawnRuleset.class);
                            case ROOK -> ruleset = ctx.deserialize(el, RookRuleset.class);
                            case KNIGHT -> ruleset = ctx.deserialize(el, KnightRuleset.class);
                            case BISHOP -> ruleset = ctx.deserialize(el, BishopRuleset.class);
                            case QUEEN -> ruleset = ctx.deserialize(el, QueenRuleset.class);
                            case KING -> ruleset = ctx.deserialize(el, KingRuleset.class);
                        }
                    }
                    return ruleset;
                });
        return gsonBuilder.create();
    }

}
