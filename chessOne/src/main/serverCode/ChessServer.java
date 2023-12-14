package serverCode;

import dataAccess.Database;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serverCode.handlers.*;
import serverCode.webSocket.WSHandler;
import spark.Spark;

@WebSocket
public class ChessServer {

    public static Database database = new Database();

    private static final WSHandler wsHandler = new WSHandler();

    public static void main(String[] args) {
        //new ChessServer().run();
        Spark.port(8080);
        Spark.webSocket("/connect", WSHandler.class);
        Spark.externalStaticFileLocation("web");

        createRoutes();
        Spark.init();
    }

    /*
    private void run() {
        Spark.port(8080);
        Spark.webSocket("/connect", webSocketHandler);
        Spark.externalStaticFileLocation("web");

        createRoutes();
        Spark.init();
    }
     */

    private static void createRoutes() {
        Spark.post("/session", LoginHandler::handleLogin);
        Spark.post("/user", RegisterHandler::handleRegister);
        Spark.delete("/session", LogoutHandler::handleLogout);
        Spark.delete("/db", ClearHandler::handleClear);
        Spark.post("/game", CreateGameHandler::handleCreateGame);
        Spark.get("/game", ListGamesHandler::handleListGames);
        Spark.put("/game", JoinGameHandler::handleJoinGame);
    }

}
