package serverCode.webSocket;

import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    HashMap<String, Connection> connections = new HashMap<>();

    public void addConnection(String authToken, Connection connection) throws IllegalAccessException {
        if (connections.containsKey(authToken) && connections.get(authToken).session.isOpen()) {
            throw new IllegalAccessException("User already has an open session. No connection will be added.");
        }
        connections.put(authToken, connection);
    }

    public Connection getConnection(String authToken) {
        return connections.get(authToken);
    }

    public void removeConnection(String authToken) {
        connections.remove(authToken);
    }

    public void broadcast(int gameID, String userAuthToken, String notification) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        for (Connection connection : connections.values()) {
            if (!connection.session.isOpen()) {
                removeList.add(connection);
            }
            if (connection.getGameID() == gameID && !connection.getAuthToken().equals(userAuthToken)) {
                connection.send(notification);
            }
        }

        for (Connection connection : removeList) {
            removeConnection(connection.authToken);
        }
    }
}
