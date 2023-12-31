package serverCode.DAOs;

import chess.*;
import com.google.gson.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import models.Game;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;


import static java.lang.Math.random;


public class SQLGameDAO implements GameDAO {

    /*

                    CREATE TABLE IF NOT EXISTS games (
                        gameID INT NOT NULL,
                        whiteUsername VARCHAR(100),
                        blackUsername VARCHAR(100),
                        observers TEXT,
                        gameName VARCHAR(100) NOT NULL,
                        game TEXT NOT NULL,
                        PRIMARY KEY (gameID)
                    )
     */
    static Database database = new Database();

    @Override
    public Game createGame(String gameName) throws DataAccessException {
        try {
            if (gameName == null) {
                throw new DataAccessException("Error: bad request");
            }
            var dataConnection = database.getConnection();
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

            database.closeConnection(dataConnection);
            return readGame(newGameID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game readGame(int gameID) throws DataAccessException {
        try {
            var dataConnection = database.getConnection();
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
            Gson chessDeserializer = createChessGson();

            ChessGame chessGame = chessDeserializer.fromJson(jsonChess, ChessGameImp.class);

            database.closeConnection(dataConnection);
            return new Game(gameID, whiteUser, blackUser, observers, gameName, chessGame);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGame(int gameID, ChessGame newChessGame) throws DataAccessException {
        try {
            readGame(gameID);
            var dataConnection = database.getConnection();
            dataConnection.setCatalog("chessdata");
            if (newChessGame == null) {
                throw new DataAccessException("Error: bad request");
            }
            var updateStatement = "UPDATE games SET game = ? WHERE gameID = ?";
            var preparedUpdate = dataConnection.prepareStatement(updateStatement);
            String jsonChessGame = new Gson().toJson(newChessGame);
            preparedUpdate.setString(1, jsonChessGame);
            preparedUpdate.setInt(2, gameID);
            preparedUpdate.executeUpdate();
            database.closeConnection(dataConnection);

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void claimGameSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try {
            var dataConnection = database.getConnection();
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

            database.closeConnection(dataConnection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Collection<Game> readAllGames() throws DataAccessException {
        try {
            Collection<Game> allGames = new HashSet<>();
            var dataConnection = database.getConnection();
            dataConnection.setCatalog("chessdata");
            var searchStatement = "SELECT * FROM games";
            var preparedSearch = dataConnection.prepareStatement(searchStatement);
            var result = preparedSearch.executeQuery();
            while (result.next()) {
                int gameID = result.getInt("gameID");
                allGames.add(readGame(gameID));
            }
            database.closeConnection(dataConnection);
            return allGames;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAllGames() {
        try {
            var dataConnection = database.getConnection();
            dataConnection.setCatalog("chessdata");
            var clearGameStatement = "TRUNCATE TABLE games";
            var preparedClearGame = dataConnection.prepareStatement(clearGameStatement);
            preparedClearGame.executeUpdate();
            database.closeConnection(dataConnection);
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

    public static Gson createChessGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // This line should only be needed if your board class is using a Map to store chess pieces instead of a 2D array.
        gsonBuilder.enableComplexMapKeySerialization();

        gsonBuilder.registerTypeAdapter(ChessGame.class,
                (JsonDeserializer<ChessGame>) (el, type, ctx) -> ctx.deserialize(el, ChessGameImp.class));

        gsonBuilder.registerTypeAdapter(ChessBoard.class,
                (JsonDeserializer<ChessBoard>) (el, type, ctx) -> ctx.deserialize(el, ChessBoardImp.class));

        gsonBuilder.registerTypeAdapter(ChessPiece.class,
                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> ctx.deserialize(el, ChessPieceImp.class));

        gsonBuilder.registerTypeAdapter(ChessMove.class,
                (JsonDeserializer<ChessMove>) (el, type, ctx) -> ctx.deserialize(el, ChessMoveImp.class));

        gsonBuilder.registerTypeAdapter(ChessPosition.class,
                (JsonDeserializer<ChessPosition>) (el, type, ctx) -> ctx.deserialize(el, ChessPositionImp.class));

        gsonBuilder.registerTypeAdapter(PieceRuleset.class,
                (JsonDeserializer<PieceRuleset>) (el, type, ctx) -> {
                    PieceRuleset ruleset = null;
                    if (el.isJsonObject()) {
                        String pieceType = el.getAsJsonObject().get("type").getAsString();
                        switch (ChessPiece.PieceType.valueOf(pieceType)) {
                            case PAWN -> ruleset = ctx.deserialize(el, PawnRuleset.class);
                            case ROOK -> ruleset = ctx.deserialize(el, RookRuleset.class);
                            case KNIGHT -> ruleset = ctx.deserialize(el, KnightRuleset.class);
                            case BISHOP -> ruleset = ctx.deserialize(el, BishopRuleset.class);
                            case QUEEN -> ruleset = ctx.deserialize(el, QueenRuleset.class);
                            case KING -> ruleset = ctx.deserialize(el, KingRuleset.class);
                        }
                    }
                    return ruleset;
                });
        return gsonBuilder.create();
    }

}
