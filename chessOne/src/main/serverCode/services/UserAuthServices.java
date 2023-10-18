package serverCode.services;

import serverCode.models.AuthToken;
import serverCode.models.User;

/**
 * Service class for all endpoints relating to users/authTokens (login, logout, register).
 */
public class UserAuthServices {
    /**
     * Creates a session for an existing user. Will use DAO methods like readUser anc createAuthToken.
     *
     * @param userToLog is the model object representing the user who is logging in
     * @return the User model object of the user once their session has been created
     */
    public static User login(User userToLog) {
        return null;
    }

    /**
     * Ends a user's session. Will call DAO method deleteAuthToken
     *
     * @param authToken the authToken of the session to be logged out of
     */
    public static void logout(AuthToken authToken) {
    }

    /**
     * Registers a new user. Will call DAO method createUser
     *
     * @param userToRegister the model object representing the user who is registering
     * @return the model object representing the user once they have registered
     */
    public static User register(User userToRegister) {
        return null;
    }
}
