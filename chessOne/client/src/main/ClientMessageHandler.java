import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;

import static ui.EscapeSequences.*;

public class ClientMessageHandler {
    ChessGame.TeamColor playerColor;

    public ClientMessageHandler(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public void loadGameBoard(ServerMessageLoad message) {
        System.out.println(message.getChessGame());
        ChessGame chessGame = createChessGson().fromJson(message.getChessGame(), ChessGameImp.class);
        System.out.println(chessGame.getTeamTurn());
    }

    public void notifyUser(ServerMessageNotify message) {
        System.out.println(message.getMessageText());
    }

    public void handleError(ServerMessageError message) {
        System.out.println(message.getMessageText());
    }


    private String makeGameBoardStr(ChessGame chessGame) {
        StringBuilder gameBoardStr = new StringBuilder();
        boolean blackTop = true;
        if (playerColor == ChessGame.TeamColor.BLACK) {
            blackTop = false;
        }
        ChessBoard board = chessGame.getBoard();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPositionImp(j, i));
                //TODO: turn this into a method
            }
        }

        char[] lettersBlackTop = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        char[] lettersWhiteTop = {'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'};

        char[] piecesBlackTop = {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'};
        char[] piecesWhiteTop = {'R', 'N', 'B', 'K', 'Q', 'B', 'N', 'R'};
        char[] pawns = {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'};
        char[] empty = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        gameBoardStr.append(makeLettersStr(lettersWhiteTop));
        for (int i = 1; i < 9; i++) {
            if (i == 1 || i == 8) {
                gameBoardStr.append(makeRowStr(i, piecesWhiteTop, blackTop));
            } else if (i == 2 || i == 7) {
                gameBoardStr.append(makeRowStr(i, pawns, blackTop));
            } else {
                gameBoardStr.append(makeRowStr(i, empty, blackTop));
            }
        }
        gameBoardStr.append(makeLettersStr(lettersWhiteTop));
        gameBoardStr.append("Game Board with Black on Top: \n");
        gameBoardStr.append(makeLettersStr(lettersBlackTop));
        blackTop = true;
        for (int i = 8; i > 0; i--) {
            if (i == 1 || i == 8) {
                gameBoardStr.append(makeRowStr(i, piecesBlackTop, blackTop));
            } else if (i == 2 || i == 7) {
                gameBoardStr.append(makeRowStr(i, pawns, blackTop));
            } else {
                gameBoardStr.append(makeRowStr(i, empty, blackTop));
            }
        }
        gameBoardStr.append(makeLettersStr(lettersBlackTop));

        return gameBoardStr.toString();
    }

    private String makeLettersStr(char[] letters) {
        StringBuilder letterStr = new StringBuilder();
        letterStr.append(SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY);
        letterStr.append("   ");
        for (char letter : letters) {
            letterStr.append(" " + letter + " ");
        }
        letterStr.append("   ");
        letterStr.append(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + RESET_TEXT_BOLD_FAINT + "\n");
        return letterStr.toString();
    }

    private String makeRowStr(int i, char[] pieces, boolean blackTop) {
        StringBuilder rowStr = new StringBuilder();
        rowStr.append(SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY);
        rowStr.append(" " + i + " ");
        if (i == 1 || i == 2) {
            rowStr.append(SET_TEXT_COLOR_GREEN);
        } else if (i == 8 || i == 7) {
            rowStr.append("[38;5;22m");
        }
        if (blackTop) {
            if (i % 2 == 1) {
                rowStr.append(makeRowStartBlack(pieces));
            } else {
                rowStr.append(makeRowStartWhite(pieces));
            }
        } else {
            if (i % 2 == 1) {
                rowStr.append(makeRowStartWhite(pieces));
            } else {
                rowStr.append(makeRowStartBlack(pieces));
            }
        }
        rowStr.append(SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY);
        rowStr.append(" " + i + " ");
        rowStr.append(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + RESET_TEXT_BOLD_FAINT + "\n");
        return rowStr.toString();
    }

    private String makeRowStartBlack(char[] pieces) {
        StringBuilder rowStrBlack = new StringBuilder();
        for (int i = 0; i < pieces.length; i++) {
            if (i % 2 == 0) {
                rowStrBlack.append(SET_BG_COLOR_BLACK + " " + pieces[i] + " ");
            } else {
                rowStrBlack.append(SET_BG_COLOR_WHITE + " " + pieces[i] + " ");
            }
        }
        return rowStrBlack.toString();
    }

    private String makeRowStartWhite(char[] pieces) {
        StringBuilder rowStrBlack = new StringBuilder();
        for (int i = 0; i < pieces.length; i++) {
            if (i % 2 == 0) {
                rowStrBlack.append(SET_BG_COLOR_WHITE + " " + pieces[i] + " ");
            } else {
                rowStrBlack.append(SET_BG_COLOR_BLACK + " " + pieces[i] + " ");
            }
        }
        return rowStrBlack.toString();
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
