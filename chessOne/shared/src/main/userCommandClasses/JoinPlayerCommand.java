package userCommandClasses;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayerCommand extends UserGameCommand {

    public JoinPlayerCommand(String authToken, ChessGame.TeamColor color, int gameID) {
        super(authToken, color, gameID);
        this.commandType = CommandType.JOIN_PLAYER;
    }
}
