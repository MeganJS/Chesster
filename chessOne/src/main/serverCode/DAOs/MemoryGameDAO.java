package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.Game;

import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    @Override
    public Game createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public void replaceGame(int gameID, Game newGame) throws DataAccessException {

    }

    @Override
    public void claimGameSpot(int gameID, String username, String playerColor) throws DataAccessException {

    }

    @Override
    public Collection<Game> readAllGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clearAllGames() throws DataAccessException {

    }
}
