package serverCode.services;

import dataAccess.DataAccessException;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.DAOs.SQLUserAuthDAO;
import serverCode.models.AuthToken;
import serverCode.models.User;

import java.io.IOException;

/**
 * Service class for all endpoints relating to users/authTokens (login, logout, register).
 */
public class UserAuthServices {

    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();

    /**
     * Creates a session for an existing user. Will use DAO methods like readUser anc createAuthToken.
     *
     * @param userToLog is the model object representing the user who is logging in
     * @return the authToken model object for the user's session
     */
    public static AuthToken login(User userToLog) throws DataAccessException, IOException {
        User userInMemory = userAuthDAO.readUser(userToLog.getUsername());
        if (!userToLog.getPassword().equals(userInMemory.getPassword())) {
            throw new IOException("Error: unauthorized");
        }
        return userAuthDAO.createAuthToken(userToLog.getUsername());
    }

    /**
     * Ends a user's session. Will call DAO method deleteAuthToken
     *
     * @param authString the string of the authToken of the session to be logged out of
     */
    public static void logout(String authString) throws DataAccessException {
        userAuthDAO.deleteAuthToken(userAuthDAO.readAuthToken(authString));
    }

    /**
     * Registers a new user. Will call DAO method createUser
     *
     * @param userToRegister the model object representing the user who is registering
     * @return the model object representing the user once they have registered
     */
    public static AuthToken register(User userToRegister) throws DataAccessException {
        User createdUser = userAuthDAO.createUser(userToRegister);
        return userAuthDAO.createAuthToken(createdUser.getUsername());
    }
}
