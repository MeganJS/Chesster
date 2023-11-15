package serverCode.DAOs;

import chess.*;
import com.google.gson.*;
import dataAccess.DataAccessException;
import models.Game;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;


import static java.lang.Math.random;
import static serverCode.ChessServer.getDatabase;

public class SQLGameDAO implements GameDAO {

    /*
    public static void databaseGameSetUp(Database database) throws DataAccessException {
        try {
            var dataConnection = database.getConnection();
            dataConnection.setCatalog("chessdata");
            var createGameStatement = """
                    CREATE TABLE IF NOT EXISTS games (
                        gameID INT NOT NULL,
                        whiteUsername VARCHAR(100),
                        blackUsername VARCHAR(100),
                        observers TEXT,
                        gameName VARCHAR(100) NOT NULL,
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

     */

    @Override
    public Game createGame(String gameName) throws DataAccessException {
        try {
            if (gameName == null) {
                throw new DataAccessException("Error: bad request");
            }
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var createStatement = "INSERT INTO games (gameID, gameName, game) VALUES (?, ?, ?)";
            var preparedCreate = dataConnection.prepareStatement(createStatement);
            int newGameID = generateGameID();
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
            dataConnection.setCatalog("chessdata");
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
            HashSet<String> observers = new Gson().fromJson(result.getString("observers"), HashSet.class);
            if (observers == null) {
                observers = new HashSet<>();
            }
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

    public void updateGame(int gameID, ChessGame newChessGame) throws DataAccessException {
        try {
            readGame(gameID);
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            if (newChessGame == null) {
                throw new DataAccessException("Error: bad request");
            }
            var jsonChessGame = new Gson().toJson(newChessGame);
            var updateStatement = "UPDATE games SET game=? WHERE gameID = ?";
            var preparedUpdate = dataConnection.prepareStatement(updateStatement);
            preparedUpdate.setString(1, jsonChessGame);
            preparedUpdate.setInt(2, gameID);
            preparedUpdate.executeUpdate();
            getDatabase().closeConnection(dataConnection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void claimGameSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try {
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            Game gameToClaim = readGame(gameID);
            if (color == ChessGame.TeamColor.WHITE) {
                var updateStatement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
                var preparedUpdate = dataConnection.prepareStatement(updateStatement);
                preparedUpdate.setString(1, username);
                preparedUpdate.setInt(2, gameID);
                preparedUpdate.executeUpdate();
            } else if (color == ChessGame.TeamColor.BLACK) {
                var updateStatement = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
                var preparedUpdate = dataConnection.prepareStatement(updateStatement);
                preparedUpdate.setString(1, username);
                preparedUpdate.setInt(2, gameID);
                preparedUpdate.executeUpdate();
            } else if (color == null) {
                gameToClaim.addObserver(username);
                var updateStatement = "UPDATE games SET observers = ? WHERE gameID = ?";
                var preparedUpdate = dataConnection.prepareStatement(updateStatement);
                preparedUpdate.setString(1, new Gson().toJson(gameToClaim.getObservers()));
                preparedUpdate.setInt(2, gameID);
                preparedUpdate.executeUpdate();
            } else {
                throw new DataAccessException("Error: bad request");
            }

            getDatabase().closeConnection(dataConnection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Game> readAllGames() throws DataAccessException {
        try {
            Collection<Game> allGames = new HashSet<>();
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var searchStatement = "SELECT * FROM games";
            var preparedSearch = dataConnection.prepareStatement(searchStatement);
            var result = preparedSearch.executeQuery();
            while (result.next()) {
                int gameID = result.getInt("gameID");
                allGames.add(readGame(gameID));
            }
            getDatabase().closeConnection(dataConnection);
            return allGames;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAllGames() {
        try {
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var clearGameStatement = "TRUNCATE TABLE games";
            var preparedClearGame = dataConnection.prepareStatement(clearGameStatement);
            preparedClearGame.executeUpdate();
            getDatabase().closeConnection(dataConnection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int generateGameID() {
        int newGameID = (int) (random() * 10000);
        try {
            readGame(newGameID);
            return generateGameID();
        } catch (DataAccessException ex) {
            return newGameID;
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
