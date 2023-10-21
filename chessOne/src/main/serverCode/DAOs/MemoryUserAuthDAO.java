package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.AuthToken;
import serverCode.models.User;

import java.util.Collection;
import java.util.HashSet;

public class MemoryUserAuthDAO implements UserAuthDAO {
    Collection<User> users = new HashSet<>();
    Collection<AuthToken> authTokens = new HashSet<>();

    @Override
    public AuthToken createAuthToken(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthToken readAuthToken(String authorizationString) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {

    }

    @Override
    public User createUser(User newUser) throws DataAccessException {
        if (users.contains(newUser)) {
            throw new DataAccessException("User already exists.");
        }
        User userToAdd = new User(newUser);
        users.add(userToAdd);
        return userToAdd;
    }

    @Override
    public User readUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearAllUserAuthData() throws DataAccessException {

    }
}
