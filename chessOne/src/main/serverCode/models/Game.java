package serverCode.models;

import chess.ChessGame;
import chess.ChessGameImp;

/**
 * This class represents a chess game for the database.
 */
public class Game {
    /**
     * gameID is a unique int identifier for this game
     * Cannot be null
     */
    private int gameID; //FIXME where and how will this be generated?
    /**
     * whiteUsername is the username of user who is the white player
     * Can be null
     */
    private String whiteUsername;
    /**
     * blackUsername is the username of user who is the black player
     * Can be null
     */
    private String blackUsername;
    /**
     * gameName is a String provided by the user when the game is created.
     * Does not need to be unique. Default value is an empty string.
     */
    private String gameName;
    /**
     * game object is the ChessGame itself
     */
    private ChessGame game = new ChessGameImp();

    Game(int IDforGame, String nameGame) {
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

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {//FIXME will I need a setter for game?
        return game;
    }

    public boolean equals(Object o) {
        return false;
    }
}
