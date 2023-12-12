import chess.*;
import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

public class WSServerFacade extends Endpoint {
    private static Session session;
    //private ClientMessageHandler cmHandler = new ClientMessageHandler();

    public WSServerFacade(String urlString) {
        try {
            urlString = urlString.replace("http", "ws");
            URI wsURI = new URI(urlString + "connect");
            //add notification handler

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
                            /*
                            var builder = new GsonBuilder();
                            builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
                            builder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
                            builder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
                            builder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
                            ServerMessageLoad loadMessage = builder.create().fromJson(message, ServerMessageLoad.class);
                            */
                            loadGameBoard(json.fromJson(message, ServerMessageLoad.class));
                            break;
                        case ERROR:
                            handleError(json.fromJson(message, ServerMessageError.class));
                            break;
                        case NOTIFICATION:
                            notifyUser(json.fromJson(message, ServerMessageNotify.class));
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


    private void loadGameBoard(ServerMessageLoad message) {
        System.out.println(message.getMessageText());
    }

    private void notifyUser(ServerMessageNotify message) {
        System.out.println(message.getMessageText());
    }

    private void handleError(ServerMessageError message) {
        System.out.println(message.getMessageText());
    }






    /*
    static class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
        @Override
        public ChessPiece deserialize(JsonElement jsonEl, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return jdc.deserialize(jsonEl, ChessPieceImp.class);
        }
    }

    static class ChessBoardAdapter implements JsonDeserializer<ChessBoard> {
        @Override
        public ChessBoard deserialize(JsonElement jsonEl, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return jdc.deserialize(jsonEl, ChessBoardImp.class);
        }
    }

    static class ChessPositionAdapter implements JsonDeserializer<ChessPosition> {
        @Override
        public ChessPosition deserialize(JsonElement jsonEl, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return jdc.deserialize(jsonEl, ChessPositionImp.class);
        }
    }

    static class ChessGameAdapter implements JsonDeserializer<ChessGame> {
        @Override
        public ChessGame deserialize(JsonElement jsonEl, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return jdc.deserialize(jsonEl, ChessGameImp.class);
        }
    }

     */

}
