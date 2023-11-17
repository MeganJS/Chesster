package myTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import serverCode.DAOs.SQLGameDAO;
import serverCode.DAOs.SQLUserAuthDAO;
import models.AuthToken;
import models.Game;
import models.User;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static serverCode.services.ClearService.clearAllData;
import static serverCode.services.GameServices.*;
import static serverCode.services.UserAuthServices.*;

public class myGameServiceTests {

    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();
    static SQLGameDAO gameDAO = new SQLGameDAO();

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

        AuthToken authToken = login(new User("frogs", "secretssss", "nope"));
        assertEquals(games, listGames(authToken.getAuthToken()));
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void listGamesUnauthorized() throws DataAccessException {
        Collection<Game> games = new HashSet<>();
        games.add(gameDAO.createGame("frog's game"));
        games.add(gameDAO.createGame("Gallery"));
        games.add(gameDAO.createGame("napstablook"));
        assertThrows(DataAccessException.class, () -> listGames(new AuthToken("lies", "falsehoods").getAuthToken()));
        gameDAO.clearAllGames();
    }

    @Test
    public void createGameTest() throws DataAccessException, IOException {
        AuthToken authToken = login(new User("frogs", "secretssss", "nope"));
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
        AuthToken authToken = login(new User("frogs", "secretssss", "nope"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        joinGame(authToken.getAuthToken(), "white", newGame.getGameID());
        assertEquals("frogs", gameDAO.readGame(newGame.getGameID()).getWhiteUsername());
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void joinGameBlack() throws DataAccessException, IOException {
        AuthToken authToken = login(new User("frogs", "secretssss", "nope"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        joinGame(authToken.getAuthToken(), "blaCK", newGame.getGameID());
        assertEquals("frogs", gameDAO.readGame(newGame.getGameID()).getBlackUsername());
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void joinGameObserver() throws DataAccessException, IOException {
        AuthToken authToken = login(new User("frogs", "secretssss", "nope"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        joinGame(authToken.getAuthToken(), null, newGame.getGameID());
        assertTrue(gameDAO.readGame(newGame.getGameID()).getObservers().contains("frogs"));
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void joinGameAlreadyTaken() throws DataAccessException, IOException {
        AuthToken authToken1 = login(new User("frogs", "secretssss", "nope"));
        AuthToken authToken2 = login(new User("ghostie", "ohno", "emails"));
        Game newGame = createGame(authToken1.getAuthToken(), "frog's game");
        joinGame(authToken1.getAuthToken(), "White", newGame.getGameID());
        assertThrows(IOException.class, () -> joinGame(authToken2.getAuthToken(), "White", newGame.getGameID()));
        gameDAO.clearAllGames();
        logout(authToken1.getAuthToken());
        logout(authToken2.getAuthToken());
    }

    @Test
    public void joinGameBadRequest() throws DataAccessException, IOException {
        AuthToken authToken = login(new User("frogs", "secretssss", "nope"));
        Game newGame = createGame(authToken.getAuthToken(), "frog's game");
        assertThrows(IOException.class, () -> joinGame(authToken.getAuthToken(), "mischief", newGame.getGameID()));
        gameDAO.clearAllGames();
        logout(authToken.getAuthToken());
    }

    @Test
    public void clearAllDataTest() throws DataAccessException, IOException {
        AuthToken authToken1 = login(new User("frogs", "secretssss", "nope"));
        AuthToken authToken2 = login(new User("Garry", "Blue", "artschool"));
        AuthToken authToken3 = login(new User("ghostie", "ohno", "emails"));
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

    @AfterAll
    public static void takeDown() {
        clearAllData();
    }
}
