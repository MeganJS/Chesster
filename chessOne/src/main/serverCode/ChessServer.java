package serverCode;

import dataAccess.DataAccessException;
import dataAccess.Database;
import serverCode.handlers.*;
import spark.Spark;

import java.sql.SQLException;

public class ChessServer {

    public static Database database = new Database();

    public static void main(String[] args) throws SQLException, DataAccessException {
        new ChessServer().run();
    }

    private void run() throws SQLException, DataAccessException {
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

    private void databaseSetUp() throws DataAccessException, SQLException {
        var conn = database.getConnection();
        var createDB = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chessdata");
        createDB.executeUpdate();
        conn.setCatalog("chessdata");
        //TODO create tables; possibly call functions in DAO classes?
    }
}
