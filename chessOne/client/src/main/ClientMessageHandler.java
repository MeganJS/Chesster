import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;

public class ClientMessageHandler {

    public void loadGameBoard(ServerMessageLoad message) {
        System.out.println(message.getMessageText());
    }

    public void notify(ServerMessageNotify message) {
        System.out.println(message.getMessageText());
    }

    public void handleError(ServerMessageError message) {
        System.out.println(message.getMessageText());
    }

}
