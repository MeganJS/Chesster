package userCommandClasses;

import webSocketMessages.userCommands.UserGameCommand;

public class ResignCommand extends UserGameCommand {

    public ResignCommand(String authToken) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
    }
}
