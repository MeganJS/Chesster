package serverCode.webSocket;

import com.google.gson.Gson;
import models.AuthToken;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;


import java.io.IOException;

public class Connection {
    String authToken;
    Session session;
    int gameID;

    public Connection(String authToken, Session session, int gameID) {
        this.authToken = authToken;
        this.session = session;
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public void send(ServerMessage message) throws IOException {
        this.session.getRemote().sendString(new Gson().toJson(message));
    }
}
