package myTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import serverCode.DAOs.MemoryGameDAO;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.models.Game;
import serverCode.models.User;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class myGameDAOTests {
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryUserAuthDAO userAuthDAO = new MemoryUserAuthDAO();

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
        userAuthDAO.createUser(new User("frienchd fries", "secretestSauce", "nunyabeezwax"));
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", ChessGame.TeamColor.WHITE);
        Game sameGame = new Game(newGame.getGameID(), "frogs");
        sameGame.setWhiteUsername("frienchd fries");
        assertEquals(sameGame, newGame);
    }

    @Test
    public void claimGameSpotBlack() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        userAuthDAO.createUser(new User("frienchd fries", "secretestSauce", "nunyabeezwax"));
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", ChessGame.TeamColor.BLACK);
        Game sameGame = new Game(newGame.getGameID(), "frogs");
        sameGame.setBlackUsername("frienchd fries");
        assertEquals(sameGame, newGame);
    }

    @Test
    public void claimGameSpotObserver() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        userAuthDAO.createUser(new User("frienchd fries", "secretestSauce", "nunyabeezwax"));
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", null);
        Game sameGame = new Game(newGame.getGameID(), "frogs");
        sameGame.addObserver("frienchd fries");
        assertEquals(sameGame, newGame);
    }

    @Test
    public void claimTakenColorBlack() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        userAuthDAO.createUser(new User("frienchd fries", "secretestSauce", "nunyabeezwax"));
        userAuthDAO.createUser(new User("froggos", "cute!", "plsStopAsking"));
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", ChessGame.TeamColor.BLACK);
        assertThrows(DataAccessException.class, () -> gameDAO.claimGameSpot(newGame.getGameID(), "froggos", ChessGame.TeamColor.BLACK));
    }

    @Test
    public void claimTakenColorWhite() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        userAuthDAO.createUser(new User("frienchd fries", "secretestSauce", "nunyabeezwax"));
        userAuthDAO.createUser(new User("froggos", "cute!", "plsStopAsking"));
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", ChessGame.TeamColor.WHITE);
        assertThrows(DataAccessException.class, () -> gameDAO.claimGameSpot(newGame.getGameID(), "froggos", ChessGame.TeamColor.WHITE));
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

}
