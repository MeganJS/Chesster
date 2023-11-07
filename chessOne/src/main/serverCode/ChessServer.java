package serverCode;

import dataAccess.Database;
import serverCode.handlers.*;
import spark.Spark;


import static serverCode.DAOs.SQLGameDAO.databaseGameSetUp;
import static serverCode.DAOs.SQLUserAuthDAO.databaseUserAuthSetUp;

public class ChessServer {

    public static Database database = new Database();

    public static void main(String[] args) {
        new ChessServer().run();
    }

    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");

        createRoutes();
        databaseSetUp();
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

    public static Database getDatabase() {
        return database;
    }

    private void databaseSetUp() {
        try {
            var dataConnection = database.getConnection();
            var createDB = dataConnection.prepareStatement("CREATE DATABASE IF NOT EXISTS chessdata");
            createDB.executeUpdate();
            dataConnection.setCatalog("chessdata");
            databaseGameSetUp(database);
            databaseUserAuthSetUp(database);
            database.closeConnection(dataConnection);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
