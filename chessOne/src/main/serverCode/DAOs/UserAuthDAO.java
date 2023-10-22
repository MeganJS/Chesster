package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.AuthToken;
import serverCode.models.User;

import java.util.Collection;

/**
 * Data Access Object interface for User and AuthToken model objects. Can be implemented for memory or SQL.
 * Methods will be called by Service classes and use Model objects to fulfill tasks
 */
public interface UserAuthDAO {
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
     * Deletes all users and authTokens from memory/database
     *
     * @throws DataAccessException
     */
    boolean clearAllUserAuthData() throws DataAccessException;
    //could add later: delete and update user, for changing password and deleting account

}
