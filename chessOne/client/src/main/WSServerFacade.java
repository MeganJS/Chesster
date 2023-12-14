import chess.*;
import com.google.gson.*;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;
import userCommandClasses.MakeMoveCommand;
import userCommandClasses.ResignCommand;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSServerFacade extends Endpoint {
    private static Session session;
    private ClientMessageHandler cmHandler;

    public WSServerFacade(String urlString, ClientMessageHandler handler) {
        try {
            urlString = urlString.replace("http", "ws");
            URI wsURI = new URI(urlString + "connect");
            //add notification handler
            this.cmHandler = handler;
            WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();
            session = wsContainer.connectToServer(this, wsURI);

            session.addMessageHandler(new MessageHandler.Whole<String>() {
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
                            cmHandler.notifyUser(json.fromJson(message, ServerMessageNotify.class));
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

    public void joinPlayer(UserGameCommand command) {
        try {
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void joinObserver(UserGameCommand command) {
        try {
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String leaveGame(UserGameCommand command) {
        try {
            session.getBasicRemote().sendText(new Gson().toJson(command));
            session.close();
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public void sendMakeMove(MakeMoveCommand moveCommand) {
        try {
            session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public boolean sendResign(ResignCommand resignCmd) {
        try {
            session.getBasicRemote().sendText(new Gson().toJson(resignCmd));
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public String redrawBoard() {
        return cmHandler.redrawBoard();
    }

    public String highlightBoard(ChessPosition position) {
        return cmHandler.highlightBoard(position);
    }


}
