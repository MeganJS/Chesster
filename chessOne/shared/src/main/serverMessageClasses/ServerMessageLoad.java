package serverMessageClasses;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageLoad extends ServerMessage {

    String game;

    public ServerMessageLoad() {
        super(ServerMessageType.LOAD_GAME);
    }

    public ServerMessageLoad(String gameStr) {
        super(ServerMessageType.LOAD_GAME);
        game = gameStr;
    }

    public String getChessGame() {
        return game;
    }


}
