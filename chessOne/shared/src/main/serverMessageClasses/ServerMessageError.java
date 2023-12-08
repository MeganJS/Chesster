package serverMessageClasses;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageError extends ServerMessage {
    public ServerMessageError() {
        super(ServerMessageType.ERROR);
    }

}
