package serverCode.DAOs;

import dataAccess.DataAccessException;
import dataAccess.Database;
import serverCode.models.AuthToken;
import serverCode.models.User;

import java.sql.SQLException;
import java.util.UUID;

import static serverCode.ChessServer.getDatabase;

public class SQLUserAuthDAO implements UserAuthDAO {

    /*
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
                        authToken VARCHAR(36) NOT NULL,
                        username CHAR(100) NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";
            var createAuthTable = dataConnection.prepareStatement(createAuthStatement);
            createAuthTable.executeUpdate();

            database.closeConnection(dataConnection);
        } catch (Exception ex) {
            throw new DataAccessException("Couldn't set up User/Auth Tables.");
        }
    }

     */

    @Override
    public AuthToken createAuthToken(String username) throws DataAccessException {
        try {
            readUser(username);
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var createStatement = "INSERT INTO authTokens (authToken, username) VALUES (?, ?)";
            var preparedCreate = dataConnection.prepareStatement(createStatement);
            String authString = UUID.randomUUID().toString();
            preparedCreate.setString(1, authString);
            preparedCreate.setString(2, username);
            preparedCreate.executeUpdate();

            getDatabase().closeConnection(dataConnection);
            return readAuthToken(authString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthToken readAuthToken(String authorizationString) throws DataAccessException {
        try {
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var searchStatement = "SELECT * FROM authTokens WHERE authToken = ?";
            var preparedSearch = dataConnection.prepareStatement(searchStatement);
            preparedSearch.setString(1, authorizationString);
            var result = preparedSearch.executeQuery();
            if (!result.isBeforeFirst()) {
                throw new DataAccessException("Error: unauthorized");
            }
            result.next();
            String authString = result.getString("authToken");
            String username = result.getString("username");
            getDatabase().closeConnection(dataConnection);
            return new AuthToken(authString, username);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {
        try {
            readAuthToken(authToken.getAuthToken());
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var deleteStatement = "DELETE FROM authTokens WHERE authToken = ?";
            var preparedDelete = dataConnection.prepareStatement(deleteStatement);
            preparedDelete.setString(1, authToken.getAuthToken());
            preparedDelete.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User createUser(User newUser) throws DataAccessException {
        try {
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var searchStatement = "SELECT * FROM users WHERE username = ?";
            var preparedSearch = dataConnection.prepareStatement(searchStatement);
            preparedSearch.setString(1, newUser.getUsername());
            var result = preparedSearch.executeQuery();
            if (result.next()) {
                throw new DataAccessException("Error: already taken");
            }

            var createStatement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            var preparedCreate = dataConnection.prepareStatement(createStatement);
            preparedCreate.setString(1, newUser.getUsername());
            preparedCreate.setString(2, newUser.getPassword());
            preparedCreate.setString(3, newUser.getEmail());
            preparedCreate.executeUpdate();

            getDatabase().closeConnection(dataConnection);
            return readUser(newUser.getUsername());
        } catch (SQLException ex) {
            throw new DataAccessException("Error: database");
        }
    }

    @Override
    public User readUser(String username) throws DataAccessException {
        try {
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var searchStatement = "SELECT * FROM users WHERE username = ?";
            var preparedSearch = dataConnection.prepareStatement(searchStatement);
            preparedSearch.setString(1, username);
            var result = preparedSearch.executeQuery();
            if (!result.isBeforeFirst()) {
                throw new DataAccessException("user does not exist");
            }
            result.next();
            String name = result.getString("username");
            String password = result.getString("password");
            String email = result.getString("email");
            getDatabase().closeConnection(dataConnection);
            return new User(name, password, email);

        } catch (SQLException e) {
            throw new DataAccessException("Error: database");
        }
    }

    @Override
    public void clearAllUserAuthData() throws DataAccessException {
        try {
            var dataConnection = getDatabase().getConnection();
            dataConnection.setCatalog("chessdata");
            var clearUserStatement = "TRUNCATE TABLE users";
            var preparedClearUser = dataConnection.prepareStatement(clearUserStatement);
            preparedClearUser.executeUpdate();

            var clearAuthStatement = "TRUNCATE TABLE authTokens";
            var preparedClearAuth = dataConnection.prepareStatement(clearAuthStatement);
            preparedClearAuth.executeUpdate();
            getDatabase().closeConnection(dataConnection);

        } catch (SQLException e) {
            throw new DataAccessException("Error: database");
        }
    }
}
