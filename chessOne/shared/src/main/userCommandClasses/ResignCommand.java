package userCommandClasses;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class ResignCommand extends UserGameCommand {

    public ResignCommand(String authToken) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
    }

    public ResignCommand(String authToken, ChessGame.TeamColor playerColor, int gameID) {
        super(authToken, playerColor, gameID);
        this.commandType = CommandType.RESIGN;
    }
}
