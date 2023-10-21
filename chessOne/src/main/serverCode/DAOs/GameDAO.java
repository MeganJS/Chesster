package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.Game;

import java.util.Collection;

/**
 * Data Access Object interface for Game model objects. Can be implemented for memory or SQL.
 * Methods will be called by Service classes and use Model objects to fulfill tasks
 */
public interface GameDAO {
    /**
     * Creates a new game in memory/database
     *
     * @param gameName of game to be created
     * @return game object that is created
     * @throws DataAccessException if gameName is faulty or game cannot be created
     */
    Game createGame(String gameName) throws DataAccessException;

    /**
     * Finds game in memory/database and replaces it with a newGame
     *
     * @param gameID  of game to be updated
     * @param newGame to replace current game of gameID
     * @throws DataAccessException if gameID is not in memory/database
     */
    void replaceGame(int gameID, Game newGame) throws DataAccessException;

    /**
     * Updates a game to add a player/observer
     *
     * @param gameID      The gameID of game to be updated
     * @param username    The username of user to be added
     * @param playerColor The color of the team the player will be;
     *                    if null, user will be added as an observer
     * @throws DataAccessException if game does not exist, if username is not a real user
     */
    void claimGameSpot(int gameID, String username, String playerColor) throws DataAccessException;

    /**
     * Returns all the games currently in memory/database
     *
     * @return a collection of all the games currently in memory/database
     * @throws DataAccessException
     */
    Collection<Game> readAllGames() throws DataAccessException;

    /**
     * Deletes all games from memory/database
     *
     * @throws DataAccessException
     */
    void clearAllGames() throws DataAccessException;
}
