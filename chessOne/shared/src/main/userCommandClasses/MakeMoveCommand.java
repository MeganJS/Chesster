package userCommandClasses;

import webSocketMessages.userCommands.UserGameCommand;

public class MakeMoveCommand extends UserGameCommand {
    public MakeMoveCommand(String authToken) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
    }
}
