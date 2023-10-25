package serverCode.services;

import dataAccess.DataAccessException;
import serverCode.DAOs.MemoryGameDAO;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.models.AuthToken;
import serverCode.models.Game;

import java.io.IOException;
import java.util.Collection;

/**
 * Service class for endpoints relating to Game objects
 */
public class GameServices {

    static MemoryUserAuthDAO userAuthDAO = new MemoryUserAuthDAO();
    static MemoryGameDAO gameDAO = new MemoryGameDAO();

    /**
     * Lists all current games. Will call DAO method readAllGames
     *
     * @param authToken of user making the request; needs to be verified
     * @return a collection of all game objects currently in database/memory
     */
    public static Collection<Game> listGames(AuthToken authToken) throws DataAccessException {
        userAuthDAO.readAuthToken(authToken.getAuthToken());
        return gameDAO.readAllGames();
    }

    /**
     * Creates a new game. Will call DAO method createGame
     *
     * @param authToken of user making the request; needs to be verified
     * @param gameName  of game to be created
     * @return game object that is created
     */
    public static Game createGame(AuthToken authToken, String gameName) throws DataAccessException {
        userAuthDAO.readAuthToken(authToken.getAuthToken());
        return gameDAO.createGame(gameName);
    }

    /**
     * Adds a user to an existing game as white player, black player, or observer
     *
     * @param authToken   of user making the request; needs to be verified
     * @param playerColor color of team the player wishes to join; if null, player is observer
     * @param gameID      of game to be joined
     * @throws IOException if color is already taken
     */
    public static void joinGame(AuthToken authToken, String playerColor, int gameID) throws IOException {
    }
}
