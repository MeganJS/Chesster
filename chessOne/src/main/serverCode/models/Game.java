package serverCode.models;

import chess.ChessGame;
import chess.ChessGameImp;

import java.util.Collection;
import java.util.HashSet;

/**
 * This class represents a chess game for the database.
 */
public class Game {
    /**
     * gameID is a unique int identifier for this game
     * Cannot be null
     */
    private int gameID;
    /**
     * whiteUsername is the username of user who is the white player
     * Can be null
     */
    private String whiteUsername = null;
    /**
     * blackUsername is the username of user who is the black player
     * Can be null
     */
    private String blackUsername = null;
    /**
     * observers is the collection of usernames of the users who are observing the game
     * Can be empty
     */
    private Collection<String> observers = new HashSet<>();
    /**
     * gameName is a String provided by the user when the game is created.
     * Does not need to be unique. Default value is an empty string.
     */
    private String gameName;
    /**
     * game object is the ChessGame itself
     */
    private ChessGame game = new ChessGameImp();

    /**
     * constructs a game object
     *
     * @param IDforGame
     * @param nameGame
     */
    public Game(int IDforGame, String nameGame) {
        this.gameID = IDforGame;
        this.gameName = nameGame;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public Collection<String> getObservers() {
        return observers;
    }

    public void addObserver(String username) {
        observers.add(username);
    }

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Game oGame = (Game) o;
        if (!oGame.gameName.equals(this.gameName)) {
            return false;
        }
        if (oGame.gameID != this.gameID) {
            return false;
        }
        if (oGame.whiteUsername == null && this.whiteUsername != null) {
            return false;
        }
        if (oGame.whiteUsername != null && this.whiteUsername == null) {
            return false;
        }
        if (oGame.whiteUsername != null && this.whiteUsername != null) {
            if (!oGame.whiteUsername.equals(this.whiteUsername)) {
                return false;
            }
        }
        if (oGame.blackUsername == null && this.blackUsername != null) {
            return false;
        }
        if (oGame.blackUsername != null && this.blackUsername == null) {
            return false;
        }
        if (oGame.blackUsername != null && this.blackUsername != null) {
            if (!oGame.blackUsername.equals(this.blackUsername)) {
                return false;
            }
        }
        if (!this.observers.equals(oGame.observers)) {
            return false;
        }
        return true;
    }
}
