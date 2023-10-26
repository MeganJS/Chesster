package serverCode.services;

import chess.ChessGame;
import dataAccess.DataAccessException;
import serverCode.DAOs.MemoryGameDAO;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.models.AuthToken;
import serverCode.models.Game;

import java.io.IOException;
import java.util.Collection;

/**
 * Service class for endpoints relating to Game objects
 */
public class GameServices {

    static MemoryUserAuthDAO userAuthDAO = new MemoryUserAuthDAO();
    static MemoryGameDAO gameDAO = new MemoryGameDAO();

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
    //FIXME should I let players join both sides of a game?
    public static void joinGame(AuthToken authToken, String playerColor, int gameID) throws IOException, DataAccessException {
        userAuthDAO.readAuthToken(authToken.getAuthToken());
        Game gameToJoin = gameDAO.readGame(gameID);

        if (playerColor == null) {
            gameDAO.claimGameSpot(gameID, authToken.getUsername(), null);
            return;
        }

        String lowerColor = playerColor.toLowerCase();
        ChessGame.TeamColor teamColor;
        if (lowerColor.equals("white")) {
            teamColor = ChessGame.TeamColor.WHITE;
            if (gameToJoin.getWhiteUsername() == null) {
                gameDAO.claimGameSpot(gameID, authToken.getUsername(), teamColor);
            } else {
                throw new IOException("White is already taken.");
            }
        } else if (lowerColor.equals("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
            if (gameToJoin.getBlackUsername() == null) {
                gameDAO.claimGameSpot(gameID, authToken.getUsername(), teamColor);
            } else {
                throw new IOException("Black is already taken.");
            }
        } else {
            throw new IOException("Bad request.");
        }
    }
}
