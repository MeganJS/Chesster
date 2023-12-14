import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import serverMessageClasses.ServerMessageError;
import serverMessageClasses.ServerMessageLoad;
import serverMessageClasses.ServerMessageNotify;

import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class ClientMessageHandler {
    ChessGame.TeamColor playerColor;

    public ClientMessageHandler(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public void loadGameBoard(ServerMessageLoad message) {
        ChessGame chessGame = createChessGson().fromJson(message.getChessGame(), ChessGameImp.class);
        System.out.println(makeGameBoardStr(chessGame));
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
        ArrayList<Character> pieceChars = getPieceCharsBlackTop(chessGame.getBoard());
        if (playerColor == ChessGame.TeamColor.BLACK) {
            blackTop = false;
            pieceChars = getPieceCharsWhiteTop(chessGame.getBoard());
        }

        char[] lettersBlackTop = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        char[] lettersWhiteTop = {'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'};


        //0-8, 8-16, 16-24, 24-32, 32-40, 40-48, 48-56, 56-64
        if (blackTop) {
            gameBoardStr.append(makeLettersStr(lettersBlackTop));
            for (int i = 8; i > 0; i--) {
                gameBoardStr.append(makeRowStr(i, pieceChars.subList((i - 1) * 8, i * 8), blackTop));
            }
            gameBoardStr.append(makeLettersStr(lettersBlackTop));
        } else {
            gameBoardStr.append(makeLettersStr(lettersWhiteTop));
            for (int i = 1; i < 9; i++) {
                gameBoardStr.append(makeRowStr(i, pieceChars.subList((i - 1) * 8, i * 8), blackTop));
            }
            gameBoardStr.append(makeLettersStr(lettersWhiteTop));
        }

        return gameBoardStr.toString();
    }


    private ArrayList<Character> getPieceCharsBlackTop(ChessBoard board) {
        ArrayList<Character> pieceChars = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = board.getPiece(new ChessPositionImp(j, i));
                pieceChars.add(pieceToChar(piece));
            }
        }
        return pieceChars;
    }

    private ArrayList<Character> getPieceCharsWhiteTop(ChessBoard board) {
        ArrayList<Character> pieceChars = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 8; j > 0; j--) {
                ChessPiece piece = board.getPiece(new ChessPositionImp(j, i));
                pieceChars.add(pieceToChar(piece));
            }
        }
        return pieceChars;
    }

    private char pieceToChar(ChessPiece piece) {
        if (piece == null) {
            return ' ';
        }
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return 'R';
            }
            return 'r';
        }
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return 'B';
            }
            return 'b';
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return 'N';
            }
            return 'n';
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return 'K';
            }
            return 'k';
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return 'Q';
            }
            return 'q';
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return 'P';
        }
        return 'p';
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

    private String makeRowStr(int i, List<Character> pieces, boolean blackTop) {
        StringBuilder rowStr = new StringBuilder();
        rowStr.append(SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY);
        rowStr.append(" " + i + " ");

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

    private String makeRowStartBlack(List<Character> pieces) {
        StringBuilder rowStrBlack = new StringBuilder();
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) > 'Z') {
                rowStrBlack.append("[38;5;22m");
            } else {
                rowStrBlack.append(SET_TEXT_COLOR_GREEN);
            }
            if (i % 2 == 0) {
                rowStrBlack.append(SET_BG_COLOR_BLACK + " " + pieces.get(i) + " ");
            } else {
                rowStrBlack.append(SET_BG_COLOR_WHITE + " " + pieces.get(i) + " ");
            }
        }
        return rowStrBlack.toString();
    }

    private String makeRowStartWhite(List<Character> pieces) {
        StringBuilder rowStrWhite = new StringBuilder();
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) > 'Z') {
                rowStrWhite.append("[38;5;22m");
            } else {
                rowStrWhite.append(SET_TEXT_COLOR_GREEN);
            }
            if (i % 2 == 0) {
                rowStrWhite.append(SET_BG_COLOR_WHITE + " " + pieces.get(i) + " ");
            } else {
                rowStrWhite.append(SET_BG_COLOR_BLACK + " " + pieces.get(i) + " ");
            }
        }
        return rowStrWhite.toString();
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
