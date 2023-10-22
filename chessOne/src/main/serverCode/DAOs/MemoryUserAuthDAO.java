package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.AuthToken;
import serverCode.models.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class MemoryUserAuthDAO implements UserAuthDAO {
    Collection<User> users = new HashSet<>();
    Collection<AuthToken> authTokens = new HashSet<>();

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
        throw new DataAccessException("This authToken does not exist.");
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {
        authTokens.remove(readAuthToken(authToken.getAuthToken()));
    }

    @Override
    public User createUser(User newUser) throws DataAccessException {
        for (User user : users) {
            if (user.equals(newUser)) {
                throw new DataAccessException("User already exists.");
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
        throw new DataAccessException("User does not exist.");
    }

    @Override
    public boolean clearAllUserAuthData() {
        users.clear();
        authTokens.clear();
        if (users.isEmpty() && authTokens.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}
