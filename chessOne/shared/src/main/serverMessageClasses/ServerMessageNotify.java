package serverMessageClasses;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageNotify extends ServerMessage {
    String message;

    public ServerMessageNotify() {
        super(ServerMessageType.NOTIFICATION);
    }

    public ServerMessageNotify(String text) {
        super(ServerMessageType.NOTIFICATION);
        this.message = text;
    }

    public String getMessage() {
        return message;
    }
}
