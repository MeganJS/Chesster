package serverCode.webSocket;

import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;

public class ConnectionManager {
    HashMap<String, Connection> connections = new HashMap<>();

    public void addConnection(String authToken, Connection connection) {
        connections.put(authToken, connection);
        //TODO: add cases for if user already is in connections
    }

    public Connection getConnection(String authToken) {
        return connections.get(authToken);
    }

    public void removeConnection(String authToken) {
        connections.remove(authToken);
    }

    public void broadcast(int gameID, String userAuthToken, ServerMessage notification) throws IOException {
        for (Connection connection : connections.values()) {
            if (connection.getGameID() == gameID && !connection.getAuthToken().equals(userAuthToken)) {
                connection.send(notification);
            }
        }//TODO add clean up for old sessions
    }
}
