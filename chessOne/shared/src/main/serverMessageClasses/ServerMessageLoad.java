package serverMessageClasses;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageLoad extends ServerMessage {

    public ServerMessageLoad() {
        super(ServerMessageType.LOAD_GAME);
    }

    public ServerMessageLoad(String gameStr) {
        super(ServerMessageType.LOAD_GAME, gameStr);
    }

/*
    public ChessGame getChessGame() {
        return chessGame;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

 */
}
