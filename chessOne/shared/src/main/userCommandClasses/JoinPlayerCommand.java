package userCommandClasses;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayerCommand extends UserGameCommand {
    int gameID;
    String playerColor;

    public JoinPlayerCommand(String authToken, int gameID, String color) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
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
