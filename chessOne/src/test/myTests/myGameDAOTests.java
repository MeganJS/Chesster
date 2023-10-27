package myTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import serverCode.DAOs.MemoryGameDAO;
import serverCode.models.Game;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class myGameDAOTests {
    static MemoryGameDAO gameDAO = new MemoryGameDAO();

    @Test
    public void createGame() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        assertEquals(newGame.getGameName(), "frogs");
    }

    @Test
    public void readGame() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        assertEquals(gameDAO.readGame(newGame.getGameID()), newGame);
    }

    @Test
    public void readFakeGame() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameDAO.readGame(5));
    }

    @Test
    public void claimGameSpotWhite() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", ChessGame.TeamColor.WHITE);
        Game sameGame = new Game(newGame.getGameID(), "frogs");
        sameGame.setWhiteUsername("frienchd fries");
        assertEquals(sameGame, newGame);
    }

    @Test
    public void claimGameSpotBlack() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", ChessGame.TeamColor.BLACK);
        Game sameGame = new Game(newGame.getGameID(), "frogs");
        sameGame.setBlackUsername("frienchd fries");
        assertEquals(sameGame, newGame);
    }

    @Test
    public void claimGameSpotObserver() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", null);
        Game sameGame = new Game(newGame.getGameID(), "frogs");
        sameGame.addObserver("frienchd fries");
        assertEquals(sameGame, newGame);
    }

    @Test
    public void claimFakeGameSpot() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameDAO.claimGameSpot(5817, "frienchd fires", ChessGame.TeamColor.BLACK));
    }

    @Test
    public void readAllGames() throws DataAccessException {
        Collection<Game> testGameSet = new HashSet<>();
        testGameSet.add(gameDAO.createGame("frogs"));
        testGameSet.add(gameDAO.createGame("serendipity"));
        testGameSet.add(gameDAO.createGame("ChessTime!!"));

        assertEquals(testGameSet, gameDAO.readAllGames());
    }

    @Test
    public void deleteAllGames() throws DataAccessException {
        Collection<Game> testGameSet = new HashSet<>();
        testGameSet.add(gameDAO.createGame("frogs"));
        testGameSet.add(gameDAO.createGame("serendipity"));
        testGameSet.add(gameDAO.createGame("ChessTime!!"));
        gameDAO.clearAllGames();
        assertTrue(gameDAO.readAllGames().isEmpty());
    }

    @AfterAll
    public static void takeDown() {
        gameDAO.clearAllGames();
    }

}
