package serverCode.DAOs;

import chess.*;
import com.google.gson.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import serverCode.models.AuthToken;
import serverCode.models.Game;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collection;

import static java.lang.Math.random;
import static serverCode.ChessServer.getDatabase;

public class SQLGameDAO implements GameDAO {

    public static void databaseGameSetUp(Database database) throws DataAccessException {
        try {
            var dataConnection = database.getConnection();
            dataConnection.setCatalog("chessdata");
            //TODO make the game object serializable; this is just a placeholder until I figure that out
            //FIXME is gameName a NOT NULL field?
            var createGameStatement = """
                    CREATE TABLE IF NOT EXISTS games (
                        gameID INT NOT NULL AUTO_INCREMENT,
                        whiteUsername VARCHAR(100),
                        blackUsername VARCHAR(100),
                        observers longtext,
                        gameName VARCHAR(100),
                        game longtext NOT NULL,
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
            preparedCreate.setString(1, String.valueOf(newGameID));
            preparedCreate.setString(2, gameName);
            preparedCreate.setString(3, new Gson().toJson(newGame.getChessGame()));

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
            preparedSearch.setString(1, String.valueOf(gameID));
            var result = preparedSearch.executeQuery();
            if (!result.isBeforeFirst()) {
                throw new DataAccessException("Error: bad request");
            }
            result.next();
            var jsonGame = result.getString("game");
            var builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
            builder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
            builder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
            Game foundGame = builder.create().fromJson(jsonGame, Game.class);
            //TODO: use TypeAdapters here to get the game object

            getDatabase().closeConnection(dataConnection);
            return foundGame;

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

    static class ChessGameAdapter implements JsonDeserializer<ChessGame> {

        @Override
        public ChessGame deserialize(JsonElement jsonEl, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return jdc.deserialize(jsonEl, ChessGameImp.class);
        }
    }

}
