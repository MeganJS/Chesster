import chess.*;

import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class GameBoardDesign {
    ChessGame.TeamColor playerColor;
    ChessGame game;
    ChessPosition position = null;
    ArrayList<ArrayList<Integer>> highlightIndexes = null;

    public GameBoardDesign(ChessGame.TeamColor color, ChessGame chessGame) {
        this.playerColor = color;
        this.game = chessGame;
    }

    public void setPosition(ChessPosition position) {
        this.position = position;
    }

    public void setHighlightIndexes(ArrayList<ArrayList<Integer>> highlightIndexes) {
        this.highlightIndexes = highlightIndexes;
    }

    public String makeGameBoardStr() {
        StringBuilder gameBoardStr = new StringBuilder();
        boolean blackTop = true;
        ArrayList<Character> pieceChars = getPieceCharsBlackTop(game.getBoard());
        if (playerColor == ChessGame.TeamColor.BLACK) {
            blackTop = false;
            pieceChars = getPieceCharsWhiteTop(game.getBoard());
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
                rowStr.append(makeRowStartBlack(pieces, i));
            } else {
                rowStr.append(makeRowStartWhite(pieces, i));
            }
        } else {
            if (i % 2 == 1) {
                rowStr.append(makeRowStartWhite(pieces, i));
            } else {
                rowStr.append(makeRowStartBlack(pieces, i));
            }
        }
        rowStr.append(SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY);
        rowStr.append(" " + i + " ");
        rowStr.append(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + RESET_TEXT_BOLD_FAINT + "\n");
        return rowStr.toString();
    }

    private String makeRowStartBlack(List<Character> pieces, int row) {
        StringBuilder rowStrBlack = new StringBuilder();
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) > 'Z') {
                rowStrBlack.append("[38;5;22m");
            } else {
                rowStrBlack.append(SET_TEXT_COLOR_GREEN);
            }
            if (position != null && position.getRow() == row && (position.getColumn() - 1) == i) {
                rowStrBlack.append(SET_BG_COLOR_YELLOW);
            } else if (i % 2 == 0) {
                if (highlightIndexes != null && highlightIndexes.get(row - 1).contains(i + 1)) {
                    rowStrBlack.append(SET_BG_COLOR_MAGENTA);
                } else {
                    rowStrBlack.append(SET_BG_COLOR_BLACK);
                }
            } else {
                if (highlightIndexes != null && highlightIndexes.get(row - 1).contains(i + 1)) {
                    rowStrBlack.append(SET_BG_COLOR_BLUE);
                } else {
                    rowStrBlack.append(SET_BG_COLOR_WHITE);
                }
            }
            rowStrBlack.append(" " + pieces.get(i) + " ");
        }
        return rowStrBlack.toString();
    }

    private String makeRowStartWhite(List<Character> pieces, int row) {
        StringBuilder rowStrWhite = new StringBuilder();
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) > 'Z') {
                rowStrWhite.append("[38;5;22m");
            } else {
                rowStrWhite.append(SET_TEXT_COLOR_GREEN);
            }

            if (position != null && position.getRow() == row && (position.getColumn() - 1) == i) {
                rowStrWhite.append(SET_BG_COLOR_YELLOW);
            } else if (i % 2 == 0) {
                if (highlightIndexes != null && highlightIndexes.get(row - 1).contains(i + 1)) {
                    rowStrWhite.append(SET_BG_COLOR_BLUE);
                } else {
                    rowStrWhite.append(SET_BG_COLOR_WHITE);
                }
            } else {
                if (highlightIndexes != null && highlightIndexes.get(row - 1).contains(i + 1)) {
                    rowStrWhite.append(SET_BG_COLOR_MAGENTA);
                } else {
                    rowStrWhite.append(SET_BG_COLOR_BLACK);
                }
            }
            rowStrWhite.append(" " + pieces.get(i) + " ");
        }
        return rowStrWhite.toString();
    }
}
