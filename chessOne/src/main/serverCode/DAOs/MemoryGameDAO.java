package serverCode.DAOs;

import chess.ChessGame;
import dataAccess.DataAccessException;
import models.Game;

import java.util.Collection;
import java.util.HashSet;

import static java.lang.Math.random;

public class MemoryGameDAO implements GameDAO {

    public static Collection<Game> games = new HashSet<>();

    @Override
    public Game createGame(String gameName) {
        int newGameID = generateGameID();
        Game newGame = new Game(newGameID, gameName);
        games.add(newGame);
        return newGame;
    }

    @Override
    public Game readGame(int gameID) throws DataAccessException {
        for (Game game : games) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Error: bad request");
    }


    @Override
    public void claimGameSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        Game gameInQuestion = readGame(gameID);
        if (color == ChessGame.TeamColor.WHITE) {
            gameInQuestion.setWhiteUsername(username);
        } else if (color == ChessGame.TeamColor.BLACK) {
            gameInQuestion.setBlackUsername(username);
        } else if (color == null) {
            gameInQuestion.addObserver(username);
        }
    }

    @Override
    public Collection<Game> readAllGames() {
        return games;
    }

    @Override
    public void clearAllGames() {
        games.clear();
    }

    private int generateGameID() {
        int newGameID = (int) (random() * 10000);
        for (Game game : games) {
            if (game.getGameID() == newGameID) {
                generateGameID();
            }
        }
        return newGameID;
    }

}
