import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ChessClientUnitTests {
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
        chessClient.checkInput("register frog frog frog");
        assertTrue(chessClient.checkInput("new frog's game").contains("successfully created"));
    }

    @Test
    public void makeGameLongName() {
        chessClient.checkInput("register frog frog frog");
        String result = chessClient.checkInput("new Well, we've been lucky devils both, and there's no need" +
                " of pledge or oath to bind our lovely friendship fast; by firmer stuff, close bound enough.");
        System.out.printf(result);
        assertTrue(result.contains("successfully created"));
    }

    @Test
    public void makeNewGameUnauthorized() {
        assertEquals("Alas, you aren't authorized to make that request. Log in or register to start.\n", chessClient.checkInput("new gamesters"));
    }

    @Test
    public void makeGameNoName() {
        assertEquals("The game needs a name to be created.\n", chessClient.checkInput("new"));
    }

    @Test
    public void listNoGames() {
        chessClient.checkInput("register frog frog frog");
        assertEquals("The current games are as follows:\n[]\n", chessClient.checkInput("list"));
    }

    @Test
    public void listTwoGames() {
        chessClient.checkInput("register frog frog frog");
        chessClient.checkInput("new frog's game");
        chessClient.checkInput("new ghostchess");
        String result = chessClient.checkInput("list");
        assertTrue(result.contains("ghostchess") && result.contains("frog's game"));
    }

    @Test
    public void listGamesUnauthorized() {
        assertEquals("Alas, you aren't authorized to make that request. Log in or register to start.\n", chessClient.checkInput("list"));
    }

    @Test
    public void joinGameBlack() {
        chessClient.checkInput("register frog frog frog");
        String result = chessClient.checkInput("new frog's game");
        String gameID = result.substring(46, 50);
        System.out.println(gameID);
        assertEquals("Successfully joined game " + gameID + " as black team.\n", chessClient.checkInput("join " + gameID + " BLACK"));

    }


    @Test
    public void joinGameWhite() {
        chessClient.checkInput("register frog frog frog");
        String result = chessClient.checkInput("new frog's game");
        String gameID = result.substring(46, 50);
        System.out.println(gameID);
        assertEquals("Successfully joined game " + gameID + " as white team.\n", chessClient.checkInput("join " + gameID + " WHITE"));

    }

    @Test
    public void joinGameObserver() {
        chessClient.checkInput("register frog frog frog");
        String result = chessClient.checkInput("new frog's game");
        String gameID = result.substring(46, 50);
        System.out.println(gameID);
        assertEquals("Successfully joined game " + gameID + " as observer.\n", chessClient.checkInput("join " + gameID));

    }

    //TODO add case for if color is entered before ID


    @Test
    public void joinGameInvalidGameID() {

    }

    @Test
    public void joinGameSpotTaken() {

    }

    @Test
    public void joinGameObserveCommand() {

    }


    @AfterEach
    public void clearDatabase() {
        ChessServerFacade serverFacade = new ChessServerFacade();
        serverFacade.talkToServer("http://localhost:8080/db", "DELETE", "", "");
    }

}
