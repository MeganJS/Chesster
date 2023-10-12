package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.Game;

import java.util.Collection;

public class GameDAO {
    public static Game find(int gameID) throws DataAccessException {
        return null;
    }

    public static Collection<Game> findAll() throws DataAccessException{
        return null;
    }


    public static void insert(Game game) throws DataAccessException{

    }

    public static void addPlayer(String username, String color) throws DataAccessException{ //FIXME make an enum?

    }

    public static void remove(Game game) throws DataAccessException{

    }

    public static void clearAllGames() throws DataAccessException{

    }
}
