package myTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.models.AuthToken;
import serverCode.models.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static serverCode.services.UserAuthServices.*;

public class myServiceTests {

    static MemoryUserAuthDAO userAuthDAO = new MemoryUserAuthDAO();

    @BeforeAll
    public static void setUp() throws DataAccessException {
        userAuthDAO.createUser(new User("frogs", "secretssss", "nope"));
    }

    @Test
    public void loginServiceTest() throws DataAccessException, IOException {
        AuthToken authToken = login(userAuthDAO.readUser("frogs"));
        assertNotNull(authToken);
        assertEquals(authToken, userAuthDAO.readAuthToken(authToken.getAuthToken()));
    }

    @Test
    public void loginBadPassword() throws DataAccessException, IOException {
        User falseUser = new User("frogs", "ohno", "emails");
        assertThrows(IOException.class, () -> login(falseUser));
    }

    @Test
    public void loginFakeUser() throws DataAccessException, IOException {
        User falseUser = new User("ghostie", "ohno", "emails");
        assertThrows(DataAccessException.class, () -> login(falseUser));
    }

    @Test
    public void logoutServiceTest() throws DataAccessException, IOException {
        AuthToken authToken = login(userAuthDAO.readUser("frogs"));
        logout(authToken);
        assertThrows(DataAccessException.class, () -> logout(authToken));
    }

    @Test
    public void registerUserCreated() throws DataAccessException, IOException {
        User newUser = new User("Garry", "Blue", "artschool");
        register(newUser);
        assertEquals(newUser, userAuthDAO.readUser("Garry"));
    }

    @Test
    public void registerAuthCreated() throws DataAccessException, IOException {
        User newUser = new User("Garry", "Blue", "artschool");
        AuthToken authToken = register(newUser);
        assertNotNull(authToken);
        assertEquals(authToken, userAuthDAO.readAuthToken(authToken.getAuthToken()));

    }
}
