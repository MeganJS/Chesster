package userCommandClasses;

import webSocketMessages.userCommands.UserGameCommand;

public class LeaveCommand extends UserGameCommand {
    public LeaveCommand(String authToken, String color) {
        super(authToken, color);
        this.commandType = CommandType.LEAVE;
    }
}
