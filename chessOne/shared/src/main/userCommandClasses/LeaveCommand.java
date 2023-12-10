package userCommandClasses;

import webSocketMessages.userCommands.UserGameCommand;

public class LeaveCommand extends UserGameCommand {
    public LeaveCommand(String authToken) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
    }
}
