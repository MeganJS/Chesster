package serverCode.services;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.Database;
import serverCode.DAOs.SQLGameDAO;
import serverCode.DAOs.SQLUserAuthDAO;
import models.AuthToken;
import models.Game;

import java.io.IOException;
import java.util.Collection;

/**
 * Service class for endpoints relating to Game objects
 */
public class GameServices {

    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();
    static SQLGameDAO gameDAO = new SQLGameDAO();

    /**
     * Lists all current games. Will call DAO method readAllGames
     *
     * @param authToken of user making the request; needs to be verified
     * @return a collection of all game objects currently in database/memory
     */
    public static Collection<Game> listGames(String authToken) throws DataAccessException {
        userAuthDAO.readAuthToken(authToken);
        return gameDAO.readAllGames();
    }

    /**
     * Creates a new game. Will call DAO method createGame
     *
     * @param authToken of user making the request; needs to be verified
     * @param gameName  of game to be created
     * @return game object that is created
     */
    public static Game createGame(String authToken, String gameName) throws DataAccessException {
        userAuthDAO.readAuthToken(authToken);
        return gameDAO.createGame(gameName);
    }

    /**
     * Adds a user to an existing game as white player, black player, or observer
     *
     * @param authToken   of user making the request; needs to be verified
     * @param playerColor color of team the player wishes to join; if null, player is observer
     * @param gameID      of game to be joined
     * @throws IOException if color is already taken
     */
    public static void joinGame(String authToken, String playerColor, int gameID) throws IOException, DataAccessException {
        AuthToken userAuthToken = userAuthDAO.readAuthToken(authToken);
        Game gameToJoin = gameDAO.readGame(gameID);

        if (playerColor == null || playerColor.isEmpty()) {
            gameDAO.claimGameSpot(gameID, userAuthToken.getUsername(), null);
        } else {
            String lowerColor = playerColor.toLowerCase();
            gameDAO.claimGameSpot(gameID, userAuthToken.getUsername(), findColor(lowerColor, gameToJoin));
        }


    }

    private static ChessGame.TeamColor findColor(String colorString, Game gameToJoin) throws IOException {
        if (colorString.equals("white")) {
            if (gameToJoin.getWhiteUsername() == null) {
                return ChessGame.TeamColor.WHITE;
            } else {
                throw new IOException("Error: already taken");
            }
        }
        if (colorString.equals("black")) {
            if (gameToJoin.getBlackUsername() == null) {
                return ChessGame.TeamColor.BLACK;
            } else {
                throw new IOException("Error: already taken");
            }
        }
        throw new IOException("Error: bad request");
    }
}
