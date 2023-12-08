import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;
import webSocketMessages.serverMessages.ServerMessage;

import javax.management.Notification;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSServerFacade extends Endpoint {
    private Session session;
    private ClientMessageHandler cmHandler = new ClientMessageHandler();

    public WSServerFacade(String urlString) {
        try {
            urlString = urlString.replace("http", "ws");
            URI wsURI = new URI(urlString + "connect");
            //add notification handler

            WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();
            this.session = wsContainer.connectToServer(this, wsURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson json = new Gson();
                    ServerMessage serverMessage = json.fromJson(message, ServerMessage.class);
                    //use notification handler to handle message
                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME:
                            cmHandler.loadGameBoard(json.fromJson(message, ServerMessageLoad.class));
                            break;
                        case ERROR:
                            cmHandler.handleError(json.fromJson(message, ServerMessageError.class));
                            break;
                        case NOTIFICATION:
                            cmHandler.notify(json.fromJson(message, ServerMessageNotify.class));
                    }
                }
            });

        } catch (URISyntaxException | IOException | DeploymentException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


}
