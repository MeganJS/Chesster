import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;

import java.util.ArrayList;
import java.util.Collection;


public class ClientMessageHandler {
    ChessGame.TeamColor playerColor;
    ChessGame game;

    GameBoardDesign gameBoardDesign;

    public ClientMessageHandler(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public void loadGameBoard(ServerMessageLoad message) {
        this.game = createChessGson().fromJson(message.getChessGame(), ChessGameImp.class);
        this.gameBoardDesign = new GameBoardDesign(playerColor, game);
        System.out.println(gameBoardDesign.makeGameBoardStr());
    }

    public void notifyUser(ServerMessageNotify message) {
        System.out.println(message.getMessage());
    }

    public void handleError(ServerMessageError message) {
        System.out.println(message.getErrorMessage());
    }

    public String redrawBoard() {
        gameBoardDesign.setHighlightIndexes(null);
        gameBoardDesign.setPosition(null);
        return gameBoardDesign.makeGameBoardStr();
    }

    //highlight the position in yellow
    //highlight black squares and white squares to move to in two different shades - maybe red?
    //SET_BG_COLOR_BLUE and SET_BG_COLOR_MAGENTA
    public String highlightBoard(ChessPosition position) {

        Collection<ChessMove> moves = game.validMoves(position);
        if (moves == null) {
            return "There is no piece at that position.";
        }
        ArrayList<ArrayList<Integer>> highlightSquares = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            highlightSquares.add(row);
        }
        if (playerColor == ChessGame.TeamColor.BLACK) {
            for (ChessMove move : moves) {
                //1 -> 8, 8 -> 1
                int reverseIndex = 9 - move.getEndPosition().getColumn();
                highlightSquares.get(move.getEndPosition().getRow() - 1).add(reverseIndex);
            }
            position = new ChessPositionImp(9 - position.getColumn(), position.getRow());
        } else {
            for (ChessMove move : moves) {
                highlightSquares.get(move.getEndPosition().getRow() - 1).add(move.getEndPosition().getColumn());
            }
        }
        System.out.println(highlightSquares);

        gameBoardDesign.setPosition(position);
        gameBoardDesign.setHighlightIndexes(highlightSquares);

        return gameBoardDesign.makeGameBoardStr();
    }


    public static Gson createChessGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // This line should only be needed if your board class is using a Map to store chess pieces instead of a 2D array.
        gsonBuilder.enableComplexMapKeySerialization();

        gsonBuilder.registerTypeAdapter(ChessGame.class,
                (JsonDeserializer<ChessGame>) (el, type, ctx) -> ctx.deserialize(el, ChessGameImp.class));

        gsonBuilder.registerTypeAdapter(ChessBoard.class,
                (JsonDeserializer<ChessBoard>) (el, type, ctx) -> ctx.deserialize(el, ChessBoardImp.class));

        gsonBuilder.registerTypeAdapter(ChessPiece.class,
                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> ctx.deserialize(el, ChessPieceImp.class));

        gsonBuilder.registerTypeAdapter(ChessMove.class,
                (JsonDeserializer<ChessMove>) (el, type, ctx) -> ctx.deserialize(el, ChessMoveImp.class));

        gsonBuilder.registerTypeAdapter(ChessPosition.class,
                (JsonDeserializer<ChessPosition>) (el, type, ctx) -> ctx.deserialize(el, ChessPositionImp.class));

        gsonBuilder.registerTypeAdapter(PieceRuleset.class,
                (JsonDeserializer<PieceRuleset>) (el, type, ctx) -> {
                    PieceRuleset ruleset = null;
                    if (el.isJsonObject()) {
                        String pieceType = el.getAsJsonObject().get("type").getAsString();
                        switch (ChessPiece.PieceType.valueOf(pieceType)) {
                            case PAWN -> ruleset = ctx.deserialize(el, PawnRuleset.class);
                            case ROOK -> ruleset = ctx.deserialize(el, RookRuleset.class);
                            case KNIGHT -> ruleset = ctx.deserialize(el, KnightRuleset.class);
                            case BISHOP -> ruleset = ctx.deserialize(el, BishopRuleset.class);
                            case QUEEN -> ruleset = ctx.deserialize(el, QueenRuleset.class);
                            case KING -> ruleset = ctx.deserialize(el, KingRuleset.class);
                        }
                    }
                    return ruleset;
                });
        return gsonBuilder.create();
    }
}
