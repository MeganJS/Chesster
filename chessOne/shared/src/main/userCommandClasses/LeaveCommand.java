package userCommandClasses;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class LeaveCommand extends UserGameCommand {
    public LeaveCommand(String authToken, ChessGame.TeamColor color, int gameID) {
        super(authToken, color, gameID);
        this.commandType = CommandType.LEAVE;
    }
}
