package userCommandClasses;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinObserverCommand extends UserGameCommand {
    int gameID;
    String playerColor;

    public JoinObserverCommand(String authToken, int gameID, String color) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
        this.playerColor = color;
    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }
}
