package userCommandClasses;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayerCommand extends UserGameCommand {
    int gameID;

    public JoinPlayerCommand(String authToken, int gameID, String color) {
        super(authToken, color);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

}
