package myTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.models.AuthToken;
import serverCode.models.User;

import static org.junit.jupiter.api.Assertions.*;

public class myUserAuthDAOTests {

    MemoryUserAuthDAO userAuthDAO = new MemoryUserAuthDAO();

    @BeforeEach
    public void setup() throws DataAccessException {
        User existingUser = new User("froggos", "soggorf", "pond");
        userAuthDAO.createUser(existingUser);
    }


    @Test
    public void createNewUser() throws DataAccessException {
        User newUser = new User("jerry", "secretsss", "frogs");
        assertEquals(newUser, userAuthDAO.createUser(newUser));
    }

    @Test
    public void createExistingUser() throws DataAccessException {
        User existingUser = new User("froggos", "soggorf", "pond");
        assertThrows(DataAccessException.class, () -> userAuthDAO.createUser(existingUser));
    }

    @Test
    public void readUser() throws DataAccessException {
        User existingUser = new User("froggos", "soggorf", "pond");
        assertEquals(existingUser, userAuthDAO.readUser("froggos"));
    }

    @Test
    public void readFakeUser() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userAuthDAO.readUser("ohno"));
    }

    @Test
    public void createNewAuthToken() throws DataAccessException {
        AuthToken newAuthToken = userAuthDAO.createAuthToken("froggos");
        assertEquals("froggos", newAuthToken.getUsername());
        assertNotNull(newAuthToken.getAuthToken());
        System.out.println(newAuthToken.getAuthToken());
    }

    @Test
    public void createFakeAuthToken() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userAuthDAO.createAuthToken("ohno"));
    }

    @Test
    public void readAuthToken() throws DataAccessException {
        AuthToken newAuthToken = userAuthDAO.createAuthToken("froggos");
        assertEquals(newAuthToken, userAuthDAO.readAuthToken(newAuthToken.getAuthToken()));
    }

    @Test
    public void readFakeAuthToken() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userAuthDAO.readAuthToken("ohno"));
    }

    @Test
    public void deleteAuthToken() throws DataAccessException {
        AuthToken newAuthToken = userAuthDAO.createAuthToken("froggos");
        userAuthDAO.deleteAuthToken(newAuthToken);
        assertThrows(DataAccessException.class, () -> userAuthDAO.readAuthToken(newAuthToken.getAuthToken()));
    }

    @Test
    public void deleteAllUserAuth() throws DataAccessException {
        userAuthDAO.createAuthToken("froggos");
        userAuthDAO.createUser(new User("jerry", "secretsss", "frogs"));
        userAuthDAO.createAuthToken("jerry");
        assertTrue(userAuthDAO.clearAllUserAuthData());
    }
}
