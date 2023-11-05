package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.AuthToken;
import serverCode.models.User;

public class SQLUserAuthDAO implements UserAuthDAO {
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
        return null;
    }

    @Override
    public User readUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearAllUserAuthData() throws DataAccessException {

    }
}
