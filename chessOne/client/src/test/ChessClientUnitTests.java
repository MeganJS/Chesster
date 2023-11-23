import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ChessClientUnitTests {
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

    @Test
    public void helpSignedIn() {
        chessClient.checkInput("register frog frog frog");
        assertTrue(chessClient.checkInput("help").contains("observe"));
    }

    @Test
    public void helpSignedOut() {
        chessClient.checkInput("register frog frog frog");
        chessClient.checkInput("logout");
        assertTrue(chessClient.checkInput("help").contains("register"));
    }

    @Test
    public void loginRealUser() {
        chessClient.checkInput("register frog frog frog");
        chessClient.checkInput("logout");
        assertEquals("frog logged in. What would you like to do?\n", chessClient.checkInput("login frog frog"));
    }

    @Test
    public void loginFakeUser() {
        assertEquals("Hmm, something wasn't quite right with the input. Try again!\n", chessClient.checkInput("login fog fog"));
    }

    @Test
    public void makeNewGame() {
        assertEquals("Hmm, something wasn't quite right with the input. Try again!\n", chessClient.checkInput("login fog fog"));
    }

    @AfterEach
    public void clearDatabase() {
        ChessServerFacade serverFacade = new ChessServerFacade();
        serverFacade.talkToServer("http://localhost:8080/db", "DELETE", "", "");
    }

}
