package myTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import serverCode.DAOs.MemoryGameDAO;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.models.AuthToken;
import serverCode.models.Game;
import serverCode.models.User;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static serverCode.services.ClearService.clearAllData;
import static serverCode.services.GameServices.*;
import static serverCode.services.UserAuthServices.*;

public class myGameServiceTests {

    static MemoryUserAuthDAO userAuthDAO = new MemoryUserAuthDAO();
    static MemoryGameDAO gameDAO = new MemoryGameDAO();

    @BeforeAll
    public static void setUp() throws DataAccessException {
        userAuthDAO.createUser(new User("frogs", "secretssss", "nope"));
        userAuthDAO.createUser(new User("Garry", "Blue", "artschool"));
        userAuthDAO.createUser(new User("ghostie", "ohno", "emails"));

    }

    @Test
    public void listGamesTest() throws DataAccessException, IOException {
        Collection<Game> games = new HashSet<>();
        games.add(gameDAO.createGame("frog's game"));
        games.add(gameDAO.createGame("Gallery"));
        games.add(gameDAO.createGame("napstablook"));
        AuthToken authToken = login(userAuthDAO.readUser("frogs"));
        assertEquals(games, listGames(authToken));
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void listGamesUnauthorized() throws DataAccessException {
        Collection<Game> games = new HashSet<>();
        games.add(gameDAO.createGame("frog's game"));
        games.add(gameDAO.createGame("Gallery"));
        games.add(gameDAO.createGame("napstablook"));
        assertThrows(DataAccessException.class, () -> listGames(new AuthToken("lies", "falsehoods")));
        gameDAO.clearAllGames();
    }

    @Test
    public void createGameTest() throws DataAccessException, IOException {
        AuthToken authToken = login(userAuthDAO.readUser("frogs"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        assertNotNull(newGame);
        assertEquals(newGame, gameDAO.readGame(newGame.getGameID()));
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void createGameUnauthorized() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> createGame(new AuthToken("lies", "falsehoods").getAuthToken(), "Gallery"));
    }

    @Test
    public void joinGameWhite() throws DataAccessException, IOException {
        AuthToken authToken = login(userAuthDAO.readUser("frogs"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        joinGame(authToken, "white", newGame.getGameID());
        assertEquals("frogs", newGame.getWhiteUsername());
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void joinGameBlack() throws DataAccessException, IOException {
        AuthToken authToken = login(userAuthDAO.readUser("frogs"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        joinGame(authToken, "blaCK", newGame.getGameID());
        assertEquals("frogs", newGame.getBlackUsername());
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void joinGameObserver() throws DataAccessException, IOException {
        AuthToken authToken = login(userAuthDAO.readUser("frogs"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        joinGame(authToken, null, newGame.getGameID());
        assertTrue(newGame.getObservers().contains("frogs"));
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void joinGameAlreadyTaken() throws DataAccessException, IOException {
        AuthToken authToken1 = login(userAuthDAO.readUser("frogs"));
        AuthToken authToken2 = login(userAuthDAO.readUser("ghostie"));
        Game newGame = createGame(authToken1.getAuthToken(), "frog's game");
        joinGame(authToken1, "White", newGame.getGameID());
        assertThrows(IOException.class, () -> joinGame(authToken2, "White", newGame.getGameID()));
        gameDAO.clearAllGames();
        logout(authToken1.getAuthToken());
        logout(authToken2.getAuthToken());
    }

    @Test
    public void joinGameBadRequest() throws DataAccessException, IOException {
        AuthToken authToken = login(userAuthDAO.readUser("frogs"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        assertThrows(IOException.class, () -> joinGame(authToken, "mischief", newGame.getGameID()));
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void clearAllDataTest() throws DataAccessException, IOException {
        AuthToken authToken1 = login(userAuthDAO.readUser("frogs"));
        AuthToken authToken2 = login(userAuthDAO.readUser("Garry"));
        AuthToken authToken3 = login(userAuthDAO.readUser("ghostie"));
        createGame(authToken1.getAuthToken(), "frog's game");
        createGame(authToken2.getAuthToken(), "Gallery");
        createGame(authToken3.getAuthToken(), "robotsRcool");
        clearAllData();
        assertTrue(gameDAO.readAllGames().isEmpty());
        assertThrows(DataAccessException.class, () -> userAuthDAO.readAuthToken(authToken3.getAuthToken()));
        assertThrows(DataAccessException.class, () -> userAuthDAO.readUser("frogs"));

        //putting things back the way they were
        userAuthDAO.createUser(new User("frogs", "secretssss", "nope"));
        userAuthDAO.createUser(new User("Garry", "Blue", "artschool"));
        userAuthDAO.createUser(new User("ghostie", "ohno", "emails"));
    }
}
