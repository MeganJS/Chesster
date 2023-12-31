package serverCode.DAOs;

import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class MemoryUserAuthDAO implements UserAuthDAO {
    public static Collection<User> users = new HashSet<>();
    public static Collection<AuthToken> authTokens = new HashSet<>();

    @Override
    public AuthToken createAuthToken(String username) throws DataAccessException {
        readUser(username);
        AuthToken newAuthToken = new AuthToken(UUID.randomUUID().toString(), username);
        authTokens.add(newAuthToken);
        return newAuthToken;
    }

    @Override
    public AuthToken readAuthToken(String authString) throws DataAccessException {
        for (AuthToken token : authTokens) {
            if (token.getAuthToken().equals(authString)) {
                return token;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {
        authTokens.remove(readAuthToken(authToken.getAuthToken()));
    }

    @Override
    public User createUser(User newUser) throws DataAccessException {
        for (User user : users) {
            if (user.getUsername().equals(newUser.getUsername())) {
                throw new DataAccessException("Error: already taken");
            }
        }
        User userToAdd = new User(newUser);
        users.add(userToAdd);
        return userToAdd;
    }

    @Override
    public User readUser(String username) throws DataAccessException {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("user does not exist");
    }

    @Override
    public void clearAllUserAuthData() {
        users.clear();
        authTokens.clear();
    }

}
