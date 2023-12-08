package serverMessageClasses;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageLoad extends ServerMessage {
    public ServerMessageLoad() {
        super(ServerMessageType.LOAD_GAME);
    }
}
