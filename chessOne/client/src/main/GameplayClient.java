import chess.*;
import userCommandClasses.*;

import static java.lang.Integer.parseInt;

public class GameplayClient {

    private WSServerFacade wsServerFacade;
    private String authToken;
    private ChessGame.TeamColor teamColor;
    private int gameID;

    public GameplayClient(String serverURL, String userAuthToken, String strGameID, String playerColor) {
        this.authToken = userAuthToken;
        this.gameID = parseInt(strGameID);
        if (playerColor.contains("black")) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else if (playerColor.contains("white")) {
            teamColor = ChessGame.TeamColor.WHITE;
        } else if (playerColor.contains("observer")) {
            teamColor = null;
        }
        wsServerFacade = new WSServerFacade(serverURL, new ClientMessageHandler(teamColor));
    }

    public String gameplayCommand(String command, String[] words) {
        try {
            switch (command) {
                case "help":
                    return helpInGame();
                case "quit":
                    return quitInGame();
                case "move":
                    return makeMove(words);
                case "highlight":
                    return highlightGame(words);
                case "redraw":
                    return redrawGame();
                case "leave":
                    return leaveGame();
                case "resign":
                    return resignGame();
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Sorry, that's not a valid action.";
    }

    public void joinGameMessage() {
        if (teamColor != null) {
            wsServerFacade.joinPlayer(new JoinPlayerCommand(authToken, teamColor, gameID));
        } else {
            wsServerFacade.joinObserver(new JoinObserverCommand(authToken, null, gameID));
        }
    }

    private String helpInGame() {
        StringBuilder helpOutput = new StringBuilder();
        helpOutput.append("I'm glad you asked! Here are the actions available to you: \n");
        helpOutput.append("help - see a list of available actions\n");
        helpOutput.append("quit - exit application (if you are in a game, causes you to leave the game before exiting)\n");
        helpOutput.append("highlight <position> - highlights all legal moves of the piece at that position\n");
        helpOutput.append("redraw - redraws the chessboard\n");
        helpOutput.append("leave - stop playing or observing this game. Someone else may take your place\n");
        if (teamColor != null) {
            helpOutput.append("move <start position> <end position> <promotion piece type (if applicable)> - make the specified move on the chessboard\n");
            helpOutput.append("resign - forfeit the game\n");
        }
        return helpOutput.toString();
    }

    private String quitInGame() {
        System.out.println(leaveGame());
        return "quit";
    }

    /***
     *
     * @return the board
     */
    private String redrawGame() {
        return wsServerFacade.redrawBoard();
    }

    /***
     * checks to see if position is valid
     * calls the wsServerFacade highlight method which calls the cmHandler highlight method
     * @return the highlighted chess board
     */
    private String highlightGame(String[] words) {
        if (words.length < 2) {
            return "Make sure to include the position after the command. Ex: highlight b8";
        }
        if (!isValidPosition(words[1])) {
            return "Make sure to include the position after the command. Ex: highlight b8";
        }
        int col = words[1].charAt(0) - 96;
        int row = words[1].charAt(1) - '0';
        return wsServerFacade.highlightBoard(new ChessPositionImp(col, row));
    }

    /**
     * sends LEAVE command to server
     *
     * @return confirmation message that you left the game
     */
    private String leaveGame() {
        String retStr = wsServerFacade.leaveGame(new LeaveCommand(authToken, teamColor, gameID));
        if (retStr.equals("success")) {
            return "You have successfully left game " + gameID + ".";
        } else {
            return retStr;
        }
    }

    private String makeMove(String[] words) {
        try {
            String badInputMessage = "Make sure to include the start and end position after the command." +
                    "If you intend to promote the piece, also add the type you wish to promote to.\n" +
                    "Examples:\n" +
                    "move A7 A6\n" +
                    "move f7 f6 rook\n";
            if (words.length < 3) {
                return badInputMessage;
            }
            String start = words[1];
            String end = words[2];
            if (!(isValidPosition(start) && isValidPosition(end))) {
                return badInputMessage;
            }
            String promotionPiece = null;
            if (words.length >= 4) {
                promotionPiece = words[3];
            }
            ChessMove move = createChessMove(start, end, promotionPiece);
            wsServerFacade.sendMakeMove(new MakeMoveCommand(authToken, gameID, move));
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String resignGame() {
        boolean success = wsServerFacade.sendResign(new ResignCommand(authToken, teamColor, gameID));
        if (success) {
            return "Resignation complete.";
        } else {
            return "We encountered an error, and you may not have resigned as expected.\n";
        }
    }

    private boolean isValidPosition(String position) {
        if (position.length() != 2) {
            return false;
        }
        if (position.charAt(0) < 'a' || position.charAt(0) > 'h') {
            return false;
        }
        if (position.charAt(1) < '0' || position.charAt(1) > '8') {
            return false;
        }
        return true;
    }

    private ChessMove createChessMove(String start, String end, String promotion) throws Exception {
        //the ASCII for the char 'a' is 97
        int startCol = start.charAt(0) - 96;
        int startRow = start.charAt(1) - '0';
        int endCol = end.charAt(0) - 96;
        int endRow = end.charAt(1) - '0';
        ChessPiece.PieceType promotionType = null;
        if (promotion != null) {
            promotionType = findPieceType(promotion);
        }
        return new ChessMoveImp(new ChessPositionImp(startCol, startRow), new ChessPositionImp(endCol, endRow), promotionType);
    }

    private ChessPiece.PieceType findPieceType(String promotion) throws Exception {
        if (promotion.toLowerCase().equals("rook")) {
            return ChessPiece.PieceType.ROOK;
        }
        if (promotion.toLowerCase().equals("bishop")) {
            return ChessPiece.PieceType.BISHOP;
        }
        if (promotion.toLowerCase().equals("knight")) {
            return ChessPiece.PieceType.KNIGHT;
        }
        if (promotion.toLowerCase().equals("queen")) {
            return ChessPiece.PieceType.QUEEN;
        }
        throw new Exception("Promotion piece type unrecognized.");
    }


}
