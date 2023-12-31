package myTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import serverCode.DAOs.SQLUserAuthDAO;
import models.AuthToken;
import models.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static serverCode.services.ClearService.clearAllData;
import static serverCode.services.UserAuthServices.*;

public class myUserAuthServiceTests {

    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();

    @BeforeAll
    public static void setUp() throws DataAccessException {
        userAuthDAO.createUser(new User("frogs", "secretssss", "nope"));
    }

    @Test
    public void loginServiceTest() throws DataAccessException, IOException {
        User existingUser = new User("frogs", "secretssss", "nope");
        AuthToken authToken = login(existingUser);
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
        User existingUser = new User("frogs", "secretssss", "nope");
        AuthToken authToken = login(existingUser);
        logout(authToken.getAuthToken());
        assertThrows(DataAccessException.class, () -> logout(authToken.getAuthToken()));
    }

    @Test
    public void logoutFakeAuthToken() {
        assertThrows(DataAccessException.class, () -> logout(new AuthToken("stuff", "nonsense").getAuthToken()));
    }

    @Test
    public void registerUserCreated() throws DataAccessException, IOException {
        register(new User("Garry", "Blue", "artschool"));
        assertNotNull(userAuthDAO.readUser("Garry"));
    }

    @Test
    public void registerAuthCreated() throws DataAccessException, IOException {
        User newUser = new User("Jerry", "Glue", "archschool");
        AuthToken authToken = register(newUser);
        assertNotNull(authToken);
        assertEquals(authToken, userAuthDAO.readAuthToken(authToken.getAuthToken()));
    }

    @Test
    public void registerExistingUser() throws DataAccessException, IOException {
        assertThrows(DataAccessException.class, () -> register(new User("frogs", "ohno", "emails")));
    }

    @AfterAll
    public static void takeDown() {
        clearAllData();
    }

}
