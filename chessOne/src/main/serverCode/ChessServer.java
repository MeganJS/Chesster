package serverCode;

import dataAccess.Database;
import serverCode.handlers.*;
import spark.Spark;

public class ChessServer {

    public static Database database = new Database();

    public static void main(String[] args) {
        new ChessServer().run();
    }

    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");

        createRoutes();
        Spark.init();
    }

    private void createRoutes() {
        Spark.post("/session", LoginHandler::handleLogin);
        Spark.post("/user", RegisterHandler::handleRegister);
        Spark.delete("/session", LogoutHandler::handleLogout);
        Spark.delete("/db", ClearHandler::handleClear);
        Spark.post("/game", CreateGameHandler::handleCreateGame);
        Spark.get("/game", ListGamesHandler::handleListGames);
        Spark.put("/game", JoinGameHandler::handleJoinGame);

    }
    
}
