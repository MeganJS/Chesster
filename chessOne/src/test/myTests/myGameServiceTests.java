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
        logout(authToken);
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
        Game newGame = createGame(authToken, "frog's game");
        assertNotNull(newGame);
        assertEquals(newGame, gameDAO.readGame(newGame.getGameID()));
        gameDAO.clearAllGames();
        logout(authToken);
    }

    @Test
    public void createGameUnauthorized() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> createGame(new AuthToken("lies", "falsehoods"), "Gallery"));
    }

    @Test
    public void joinGameWhite() throws DataAccessException {
        
    }
}
