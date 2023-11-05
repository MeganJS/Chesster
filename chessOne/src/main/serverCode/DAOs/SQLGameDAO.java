package serverCode.DAOs;

import chess.ChessGame;
import dataAccess.DataAccessException;
import serverCode.models.Game;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public Game createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public Game readGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void claimGameSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {

    }

    @Override
    public Collection<Game> readAllGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clearAllGames() throws DataAccessException {

    }
}
