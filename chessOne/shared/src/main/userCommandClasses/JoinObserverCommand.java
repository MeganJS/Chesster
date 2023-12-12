package userCommandClasses;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinObserverCommand extends UserGameCommand {
    int gameID;

    public JoinObserverCommand(String authToken, int gameID, String color) {
        super(authToken, color);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

}
