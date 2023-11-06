package serverCode.DAOs;

import dataAccess.DataAccessException;
import dataAccess.Database;
import serverCode.models.AuthToken;
import serverCode.models.User;

public class SQLUserAuthDAO implements UserAuthDAO {

    public static void databaseUserAuthSetUp(Database database) throws DataAccessException {
        try {
            var dataConnection = database.getConnection();
            dataConnection.setCatalog("chessdata");
            var createUsersStatement = """
                    CREATE TABLE IF NOT EXISTS users (
                        username VARCHAR(100) NOT NULL,
                        password VARCHAR(100) NOT NULL,
                        email VARCHAR(100),
                        PRIMARY KEY (username)
                    )""";
            var createUsersTable = dataConnection.prepareStatement(createUsersStatement);
            createUsersTable.executeUpdate();
            var createAuthStatement = """
                    CREATE TABLE IF NOT EXISTS authTokens (
                        authToken VARCHAR(100) NOT NULL,
                        username CHAR(36) NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";
            var createAuthTable = dataConnection.prepareStatement(createAuthStatement);
            createAuthTable.executeUpdate();

            database.closeConnection(dataConnection);
        } catch (Exception ex) {
            throw new DataAccessException("Couldn't set up User/Auth Tables.");
        }
    }

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
