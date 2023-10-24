package serverCode.DAOs;

import chess.ChessGame;
import dataAccess.DataAccessException;
import serverCode.models.Game;

import java.util.Collection;
import java.util.HashSet;

import static java.lang.Math.random;

public class MemoryGameDAO implements GameDAO {

    Collection<Game> games = new HashSet<>();

    @Override
    public Game createGame(String gameName) throws DataAccessException {
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
        throw new DataAccessException("Game not found.");
    }


    @Override
    public void replaceGame(int gameID, Game newGame) throws DataAccessException {

    }

    @Override
    public void claimGameSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        //Note: This does not check if user is real or authorized. Service will have to do that.
        Game gameInQuestion = readGame(gameID);
        if (color == ChessGame.TeamColor.WHITE) {
            if (gameInQuestion.getWhiteUsername() == null) {
                gameInQuestion.setWhiteUsername(username);
                return;
            } else {
                throw new DataAccessException("White team already taken.");
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            if (gameInQuestion.getBlackUsername() == null) {
                gameInQuestion.setBlackUsername(username);
                return;
            } else {
                throw new DataAccessException("Black team already taken.");
            }
        }
        if (color == null) {
            gameInQuestion.addObserver(username);
            return;
        }
        throw new DataAccessException("Bad request.");
    }

    @Override
    public Collection<Game> readAllGames() throws DataAccessException {
        return games;
    }

    @Override
    public void clearAllGames() throws DataAccessException {
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
