package serverCode.DAOs;

import dataAccess.DataAccessException;
import dataAccess.Database;
import serverCode.models.AuthToken;
import serverCode.models.User;

import java.sql.SQLException;

import static serverCode.ChessServer.getDatabase;

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
        try {
            var dataConnection = getDatabase().getConnection();
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
            var searchStatement = "SELECT * FROM users WHERE username = ?";
            var preparedSearch = dataConnection.prepareStatement(searchStatement);
            preparedSearch.setString(1, username);
            var result = preparedSearch.executeQuery();
            while (result.next()) {
                if (result.getString("username") == null) {
                    throw new DataAccessException("User does not exist");
                }
                String name = result.getString("username");
                String password = result.getString("password");
                String email = result.getString("email");
                getDatabase().closeConnection(dataConnection);
                return new User(name, password, email);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error: database");
        }
        return null;
    }

    @Override
    public void clearAllUserAuthData() throws DataAccessException {
        try {
            var dataConnection = getDatabase().getConnection();
            var clearUserStatement = "TRUNCATE TABLE users";
            var preparedClearUser = dataConnection.prepareStatement(clearUserStatement);
            preparedClearUser.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error: database");
        }
    }
}
