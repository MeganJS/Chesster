package userCommandClasses;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinObserverCommand extends UserGameCommand {

    public JoinObserverCommand(String authToken, ChessGame.TeamColor color, int gameID) {
        super(authToken, color, gameID);
        this.commandType = CommandType.JOIN_OBSERVER;
    }

}
