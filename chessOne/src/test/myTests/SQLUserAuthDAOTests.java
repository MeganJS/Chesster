package myTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import serverCode.DAOs.SQLUserAuthDAO;
import models.AuthToken;
import models.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLUserAuthDAOTests {

    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();

    @BeforeEach
    public void setup() throws DataAccessException {
        User existingUser = new User("Heyden", "HelloAgain", "Sybil");
        userAuthDAO.createUser(existingUser);
    }


    @Test
    public void createNewUser() throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User newUser = new User("jerry", "secretsss", "frogs");
        User databaseUser = userAuthDAO.createUser(newUser);
        assertEquals(newUser.getUsername(), databaseUser.getUsername());
        assertTrue(encoder.matches("secretsss", databaseUser.getPassword()));
        assertEquals(newUser.getEmail(), databaseUser.getEmail());
    }

    @Test
    public void createExistingUser() throws DataAccessException {
        User existingUser = new User("froggos", "soggorf", "pond");
        userAuthDAO.createUser(existingUser);
        assertThrows(DataAccessException.class, () -> userAuthDAO.createUser(existingUser));
    }


    @Test
    public void readUser() throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User existingUser = new User("froggos", "soggorf", "pond");
        userAuthDAO.createUser(existingUser);
        User databaseUser = userAuthDAO.readUser("froggos");
        assertEquals(existingUser.getUsername(), databaseUser.getUsername());
        assertTrue(encoder.matches("soggorf", databaseUser.getPassword()));
        assertEquals(existingUser.getEmail(), databaseUser.getEmail());
    }


    @Test
    public void readFakeUser() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userAuthDAO.readUser("ohno"));
    }


    @Test
    public void createNewAuthToken() throws DataAccessException {
        AuthToken newAuthToken = userAuthDAO.createAuthToken("Heyden");
        assertEquals("Heyden", newAuthToken.getUsername());
        assertNotNull(newAuthToken.getAuthToken());
        System.out.println(newAuthToken.getAuthToken());
    }


    @Test
    public void createFakeAuthToken() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userAuthDAO.createAuthToken("ohno"));
    }


    @Test
    public void readAuthToken() throws DataAccessException {
        AuthToken newAuthToken = userAuthDAO.createAuthToken("Heyden");
        assertEquals(newAuthToken, userAuthDAO.readAuthToken(newAuthToken.getAuthToken()));
    }


    @Test
    public void readFakeAuthToken() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userAuthDAO.readAuthToken("ohno"));
    }


    @Test
    public void deleteAuthToken() throws DataAccessException {
        AuthToken newAuthToken = userAuthDAO.createAuthToken("Heyden");
        userAuthDAO.deleteAuthToken(newAuthToken);
        assertThrows(DataAccessException.class, () -> userAuthDAO.readAuthToken(newAuthToken.getAuthToken()));
    }

    @Test
    public void deleteFakeAuthToken() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userAuthDAO.readAuthToken("ohno"));
    }

    @Test
    public void deleteAllUserAuth() throws DataAccessException {
        AuthToken authToken = userAuthDAO.createAuthToken("Heyden");
        userAuthDAO.createUser(new User("Garry", "Blue", "macaroons"));
        userAuthDAO.clearAllUserAuthData();
        assertThrows(DataAccessException.class, () -> userAuthDAO.readUser("Garry"));
        assertThrows(DataAccessException.class, () -> userAuthDAO.readAuthToken(authToken.getAuthToken()));
    }


    @AfterEach
    public void clearDatabase() throws DataAccessException {
        userAuthDAO.clearAllUserAuthData();
    }

}
