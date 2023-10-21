package myTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.Test;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.models.User;

import static org.junit.jupiter.api.Assertions.*;

public class myDAOTests {

    MemoryUserAuthDAO userAuthDAO = new MemoryUserAuthDAO();

    @Test
    public void createNewUser() throws DataAccessException {
        User newUser = new User("jerry", "secretsss", "frogs");
        assertEquals(newUser, userAuthDAO.createUser(newUser));
    }

    @Test
    public void createExistingUser() {

    }

}
