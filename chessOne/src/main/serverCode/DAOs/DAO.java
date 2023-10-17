package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.AuthToken;
import serverCode.models.Game;
import serverCode.models.User;

import java.util.Collection;

/**
 * Data Access Object interface which can be implemented for memory or SQL.
 * Methods will be called by Service classes and use Model objects to fulfill tasks
 */
public interface DAO {
    /**
     * Creates a new AuthToken for a user who is logging in/registering
     *
     * @param username username of user who is logging in/registering
     * @return new AuthToken object for this user's session
     * @throws DataAccessException if username is not in database, if user already has an authToken
     */
    AuthToken createAuthToken(String username) throws DataAccessException;

    /**
     * Finds the AuthToken associated with a user's session
     *
     * @param authorizationString of AuthToken being checked
     * @return AuthToken for user's session
     * @throws DataAccessException if authToken does not exist
     */
    AuthToken readAuthToken(String authorizationString) throws DataAccessException;

    /**
     * Deletes the specified authToken in memory/database, thus ending the user's session
     *
     * @param authToken to delete
     * @throws DataAccessException if authToken does not exist
     */
    void deleteAuthToken(AuthToken authToken) throws DataAccessException;

    /**
     * Creates a new user in memory/database
     *
     * @param newUser to be created in memory
     * @return the user that is created
     * @throws DataAccessException if user already exists
     */
    User createUser(User newUser) throws DataAccessException;

    /**
     * Finds a user in memory/database
     *
     * @param username of user being read
     * @return User object associated with username
     * @throws DataAccessException if username has no associated User
     */
    User readUser(String username) throws DataAccessException;

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
     * @throws DataAccessException if game does not exist, or if username is not a real user
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
     * Deletes all users, authTokens, and games from memory/database
     *
     * @throws DataAccessException
     */
    void clearAllData() throws DataAccessException;
    //could add later: delete and update user, for changing password and deleting account

}
