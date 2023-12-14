package userCommandClasses;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMoveCommand extends UserGameCommand {
    ChessMove move;

    public MakeMoveCommand(String authToken) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
    }

    public MakeMoveCommand(String authToken, int gameID, ChessMove move) {
        super(authToken, gameID);
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public ChessMove getMove() {
        return move;
    }
}
