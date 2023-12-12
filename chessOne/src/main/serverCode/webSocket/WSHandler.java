package serverCode.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.springframework.security.core.userdetails.User;
import serverCode.DAOs.SQLGameDAO;
import serverCode.DAOs.SQLUserAuthDAO;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;
import userCommandClasses.JoinObserverCommand;
import userCommandClasses.JoinPlayerCommand;
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
                        case MAKE_MOVE:
                            //call the make move function
                        case RESIGN:
                            //call the resign function
                    }
                } else {
                    //send an error that the user does not have a current connection
                }
            }
        } catch (IOException e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(error));
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
    private void handleJoinGame(Session session, String message) throws IOException {
        Gson json = new Gson();
        int gameID;
        String playerColor;
        String authString;
        try {
            UserGameCommand command = json.fromJson(message, UserGameCommand.class);
            authString = command.getAuthString();
            playerColor = command.getPlayerColor();
            AuthToken userAuthToken = userAuthDAO.readAuthToken(authString);
            //adding connection
            gameID = findGameID(command.getCommandType(), message);
            /*
            if (command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
                JoinObserverCommand observerCmd = json.fromJson(message, JoinObserverCommand.class);
                gameID = observerCmd.getGameID();
            } else {
                JoinPlayerCommand playerCmd = json.fromJson(message, JoinPlayerCommand.class);
                gameID = playerCmd.getGameID();
            }
             */
            connMan.addConnection(authString, new Connection(authString, session, gameID));
            loadGame(session, gameID);

            Game chessModel = gameDAO.readGame(gameID);
            String username = userAuthToken.getUsername();
            ServerMessageNotify notify = new ServerMessageNotify(username + " has joined " + chessModel.getGameName() + " as " + playerColor + ".\n");
            connMan.broadcast(gameID, userAuthToken.getAuthToken(), notify);

        } catch (DataAccessException | IOException | IllegalAccessException e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getRemote().sendString(json.toJson(error));
        }
    }

    private int findGameID(UserGameCommand.CommandType type, String message) {
        Gson json = new Gson();
        if (type == UserGameCommand.CommandType.JOIN_OBSERVER) {
            JoinObserverCommand observerCmd = json.fromJson(message, JoinObserverCommand.class);
            return observerCmd.getGameID();
        } else {
            JoinPlayerCommand playerCmd = json.fromJson(message, JoinPlayerCommand.class);
            return playerCmd.getGameID();
        }
    }

    private void loadGame(Session session, int gameID) throws IOException {
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

    /***
     * this will:
     *  make the move on the board
     *  sends a LOAD message to all in game clients
     *  sends a NOTIFY message to all in game clients
     *  if move results in Stalemate or Checkmate, ends the game
     */
    private void handleMakeMove() {

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
            //TODO test this!!
            gameDAO.claimGameSpot(gameID, null, findTeamColor(command.getPlayerColor()));

            Game chessModel = gameDAO.readGame(gameID);
            String username = userAuthToken.getUsername();
            ServerMessageNotify notify = new ServerMessageNotify(username + " has left the game " + chessModel.getGameName() + ".\n");
            connMan.broadcast(gameID, userAuthToken.getAuthToken(), notify);
            connMan.removeConnection(authString);
        } catch (Exception e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getRemote().sendString(json.toJson(error));
        }
    }

    private ChessGame.TeamColor findTeamColor(String playerColor) throws Exception {
        if (playerColor.contains("black")) {
            return ChessGame.TeamColor.BLACK;
        } else if (playerColor.contains("white")) {
            return ChessGame.TeamColor.WHITE;
        } else if (playerColor.contains("observer")) {
            return null;
        }
        throw new Exception("Player color unrecognized.");
    }

    /***
     * this will:
     *  end the game (how?)
     *  TODO make a win screen! for fun?
     *  notify all clients in game that this player resigned and thus the other player won
     */
    private void handleResign() {

    }


}
