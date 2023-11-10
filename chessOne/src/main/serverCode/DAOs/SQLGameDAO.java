package serverCode.DAOs;

import chess.*;
import com.google.gson.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import serverCode.models.Game;

import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import static com.mysql.cj.MysqlType.LONGTEXT;
import static java.lang.Math.random;
import static serverCode.ChessServer.getDatabase;

public class SQLGameDAO implements GameDAO {

    public static void databaseGameSetUp(Database database) throws DataAccessException {
        try {
            var dataConnection = database.getConnection();
            dataConnection.setCatalog("chessdata");
            //FIXME move this to a script so it doesn't run every time?
            var createGameStatement = """
                    CREATE TABLE IF NOT EXISTS games (
                        gameID INT NOT NULL AUTO_INCREMENT,
                        whiteUsername VARCHAR(100),
                        blackUsername VARCHAR(100),
                        observers TEXT,
                        gameName VARCHAR(100),
                        game TEXT NOT NULL,
                        PRIMARY KEY (gameID)
                    )""";
            var createGameTable = dataConnection.prepareStatement(createGameStatement);
            createGameTable.executeUpdate();

            database.closeConnection(dataConnection);
        } catch (Exception ex) {
            throw new DataAccessException("Couldn't set up Game Table.");
        }
    }

    @Override
    public Game createGame(String gameName) throws DataAccessException {
        //TODO check if listgames is empty, if so create a random number for the id
        try {
            var dataConnection = getDatabase().getConnection();
            var createStatement = "INSERT INTO games (gameID, gameName, game) VALUES (?, ?, ?)";
            var preparedCreate = dataConnection.prepareStatement(createStatement);
            int newGameID = (int) (random() * 10000);
            Game newGame = new Game(newGameID, gameName);
            var chessGame = new Gson().toJson(newGame.getChessGame());
            preparedCreate.setInt(1, newGameID);
            preparedCreate.setString(2, gameName);
            preparedCreate.setObject(3, chessGame);
            preparedCreate.executeUpdate();

            getDatabase().closeConnection(dataConnection);
            return readGame(newGameID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game readGame(int gameID) throws DataAccessException {
        try {
            var dataConnection = getDatabase().getConnection();
            var searchStatement = "SELECT * FROM games WHERE gameID = ?";
            var preparedSearch = dataConnection.prepareStatement(searchStatement);
            preparedSearch.setInt(1, gameID);
            var result = preparedSearch.executeQuery();
            if (!result.isBeforeFirst()) {
                throw new DataAccessException("Error: bad request");
            }
            result.next();
            String whiteUser = result.getString("whiteUsername");
            String blackUser = result.getString("blackUsername");
            Collection<String> observers = new Gson().fromJson(result.getString("observers"), Collection.class);
            String gameName = result.getString("gameName");
            var jsonChess = result.getString("game");
            var builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
            builder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
            builder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
            ChessGame chessGame = builder.create().fromJson(jsonChess, ChessGameImp.class);

            getDatabase().closeConnection(dataConnection);
            return new Game(gameID, whiteUser, blackUser, observers, gameName, chessGame);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO add update game function
    @Override
    public void claimGameSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {

    }

    @Override
    public Collection<Game> readAllGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clearAllGames() throws DataAccessException {
        try {
            var dataConnection = getDatabase().getConnection();
            var clearGameStatement = "TRUNCATE TABLE games";
            var preparedClearGame = dataConnection.prepareStatement(clearGameStatement);
            preparedClearGame.executeUpdate();
            getDatabase().closeConnection(dataConnection);

        } catch (SQLException e) {
            throw new DataAccessException("Error: database");
        }
    }

    static class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {

        @Override
        public ChessPiece deserialize(JsonElement jsonEl, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return jdc.deserialize(jsonEl, ChessPieceImp.class);
        }
    }

    static class ChessBoardAdapter implements JsonDeserializer<ChessBoard> {

        @Override
        public ChessBoard deserialize(JsonElement jsonEl, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return jdc.deserialize(jsonEl, ChessBoardImp.class);
        }
    }

    static class ChessPositionAdapter implements JsonDeserializer<ChessPosition> {

        @Override
        public ChessPosition deserialize(JsonElement jsonEl, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return jdc.deserialize(jsonEl, ChessPositionImp.class);
        }
    }

}
