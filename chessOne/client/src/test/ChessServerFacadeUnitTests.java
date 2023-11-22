import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChessServerFacadeUnitTests {
    //TODO: write tests for each method in ChessClient.java
    private ChessClient chessClient = new ChessClient("http://localhost:8080/");

    @Test
    public void registerNewUser() {
        assertEquals(chessClient.checkInput("register frog frog frog"), "frog successfully registered. Welcome to chess!\n");
    }

    @Test
    public void registerUsedUsername() {
        chessClient.checkInput("register frog frog frog");
        assertEquals(chessClient.checkInput("register frog frog frog"), "Sorry, that username belongs to someone else already.\n");
    }


    @AfterEach
    public void clearDatabase() {
        ChessServerFacade serverFacade = new ChessServerFacade();
        serverFacade.talkToServer("http://localhost:8080/db", "DELETE", "", "");
    }

}
