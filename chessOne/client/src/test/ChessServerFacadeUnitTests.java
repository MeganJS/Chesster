import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChessServerFacadeUnitTests {
    //TODO: write tests for each method in ChessClient.java
    private final ChessClient chessClient = new ChessClient("http://localhost:8080/");

    @Test
    public void registerNewUser() {
        assertEquals(chessClient.checkInput("register frog frog frog"), "frog successfully registered. Welcome to chess!\n");
    }

    @Test
    public void registerUsedUsername() {
        chessClient.checkInput("register frog frog frog");
        assertEquals(chessClient.checkInput("register frog frog frog"), "Sorry, that username belongs to someone else already.\n");
    }

    @Test
    public void logoutUser() {
        chessClient.checkInput("register frog frog frog");
        assertEquals(chessClient.checkInput("logout"), "Logout successful. Thanks for playing!\n");
    }

    @Test
    public void logoutTwice() {
        chessClient.checkInput("register frog frog frog");
        chessClient.checkInput("logout");
        assertEquals("Alas, you aren't authorized to make that request. Log in or register to start.\n", chessClient.checkInput("logout"));
    }

    //TODO: write tests to make sure "help" gives correct output for signed in vs signed out


    @AfterEach
    public void clearDatabase() {
        ChessServerFacade serverFacade = new ChessServerFacade();
        serverFacade.talkToServer("http://localhost:8080/db", "DELETE", "", "");
    }

}
