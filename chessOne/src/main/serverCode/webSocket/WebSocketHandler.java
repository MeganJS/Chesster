package serverCode.webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serverCode.DAOs.SQLGameDAO;
import serverCode.DAOs.SQLUserAuthDAO;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;
import userCommandClasses.JoinObserverCommand;
import userCommandClasses.JoinPlayerCommand;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Session;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connMan = new ConnectionManager();
    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();
    static SQLGameDAO gameDAO = new SQLGameDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        if (command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
            //call the join function
        } else if (command.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
            //call the join function
        } else {
            var conn = connMan.getConnection(command.getAuthString());
            if (conn != null) {
                switch (command.getCommandType()) {
                    case LEAVE:
                        //call the leave function
                    case MAKE_MOVE:
                        //call the make move function
                    case RESIGN:
                        //call the resign function
                }
            } else {
                //send an error that the user does not have a current connection
            }
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
    private void handleJoinGame(Session session, UserGameCommand command) throws IOException {
        ServerMessage returnMessage;
        Gson json = new Gson();
        int gameID;
        String playerColor;
        try {
            AuthToken userAuthToken = userAuthDAO.readAuthToken(command.getAuthString());
            //adding connection
            if (command.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
                JoinObserverCommand observerCommand = (JoinObserverCommand) command;
                gameID = observerCommand.getGameID();
                playerColor = observerCommand.getPlayerColor();
            } else {
                JoinPlayerCommand playerCommand = (JoinPlayerCommand) command;
                gameID = playerCommand.getGameID();
                playerColor = playerCommand.getPlayerColor();
            }
            connMan.addConnection(command.getAuthString(), new Connection(command.getAuthString(), session, gameID));
            loadGame(session, gameID);

            Game chessModel = gameDAO.readGame(gameID);
            String username = userAuthToken.getUsername();
            ServerMessageNotify notify = new ServerMessageNotify(username + " has joined " + chessModel.getGameName() + " as " + playerColor + ".\n");
            connMan.broadcast(gameID, userAuthToken.getAuthToken(), notify);

        } catch (DataAccessException | IOException e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getBasicRemote().sendText(json.toJson(error));
        }
    }

    private void loadGame(Session session, int gameID) throws IOException {
        Gson json = new Gson();
        try {
            Game chessModel = gameDAO.readGame(gameID);
            ChessGame chessGame = chessModel.getChessGame();
            ServerMessageLoad loadGame = new ServerMessageLoad(chessGame);
            session.getBasicRemote().sendText(json.toJson(loadGame));
        } catch (IOException | DataAccessException e) {
            ServerMessageError error = new ServerMessageError(e.getMessage());
            session.getBasicRemote().sendText(json.toJson(error));
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
    private void handleLeave() {

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
