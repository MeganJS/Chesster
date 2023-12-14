package serverMessageClasses;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerMessageError extends ServerMessage {
    String errorMessage;

    public ServerMessageError() {
        super(ServerMessageType.ERROR);
    }

    public ServerMessageError(String text) {
        super(ServerMessageType.ERROR);
        this.errorMessage = text;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}
