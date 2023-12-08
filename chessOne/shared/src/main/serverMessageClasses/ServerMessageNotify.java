package serverMessageClasses;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageNotify extends ServerMessage {
    public ServerMessageNotify() {
        super(ServerMessageType.NOTIFICATION);
    }
}
