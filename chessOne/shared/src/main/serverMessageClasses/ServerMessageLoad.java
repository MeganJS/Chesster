package serverMessageClasses;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageLoad extends ServerMessage {
    ChessGame chessGame;

    public ServerMessageLoad() {
        super(ServerMessageType.LOAD_GAME);
    }

    public ServerMessageLoad(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.chessGame = game;
    }


    public ChessGame getChessGame() {
        return chessGame;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }
}
