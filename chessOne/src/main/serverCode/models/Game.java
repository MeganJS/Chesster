package serverCode.models;

import chess.ChessGame;
import chess.ChessGameImp;

public class Game {
    private int gameID;
    private String whiteUsername; //FIXME do I want this to be a string or an actual User?
    private String blackUsername;
    private String gameName;
    private ChessGame game = new ChessGameImp();

    Game(int IDforGame, String nameGame){
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
}
