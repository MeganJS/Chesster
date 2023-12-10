package serverCode.webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Session;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connMan = new ConnectionManager();

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
    private void handleJoinGame() {
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
