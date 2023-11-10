package myTests;

import chess.ChessGame;
import chess.ChessGameImp;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import serverCode.DAOs.SQLGameDAO;
import serverCode.models.Game;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {
    static SQLGameDAO gameDAO = new SQLGameDAO();

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
    public void updateGame() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        newGame.getChessGame().setTeamTurn(ChessGame.TeamColor.BLACK);
        System.out.println(newGame.getChessGame().getTeamTurn());
        gameDAO.updateGame(newGame.getGameID(), newGame.getChessGame());
        assertEquals(ChessGame.TeamColor.BLACK, gameDAO.readGame(newGame.getGameID()).getChessGame().getTeamTurn());
    }

    @Test
    public void updateFakeGame() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(5, new ChessGameImp()));
    }


    @Test
    public void claimGameSpotWhite() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", ChessGame.TeamColor.WHITE);
        newGame.setWhiteUsername("frienchd fries");
        assertEquals(newGame, gameDAO.readGame(newGame.getGameID()));
    }


    @Test
    public void claimGameSpotBlack() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", ChessGame.TeamColor.BLACK);
        newGame.setBlackUsername("frienchd fries");
        assertEquals(newGame, gameDAO.readGame(newGame.getGameID()));
    }


    @Test
    public void claimGameSpotObserver() throws DataAccessException {
        Game newGame = gameDAO.createGame("frogs");
        gameDAO.claimGameSpot(newGame.getGameID(), "frienchd fries", null);
        newGame.addObserver("frienchd fries");
        assertEquals(newGame, gameDAO.readGame(newGame.getGameID()));
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
    public void readNoGames() throws DataAccessException {
        assertTrue(gameDAO.readAllGames().isEmpty());
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


    @AfterEach
    public void takeDown() throws DataAccessException {
        gameDAO.clearAllGames();
    }

}
