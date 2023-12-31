package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenRuleset implements PieceRuleset {
    private Collection<ChessMove> validMoves = new HashSet<>();
    private ChessBoard gameBoard;
    private ChessPosition piecePosition;

    ChessPiece.PieceType type;

    public QueenRuleset() {
        this.type = ChessPiece.PieceType.QUEEN;
    }

    @Override
    public Collection<ChessMove> findValidMoves(ChessPosition position, ChessBoard board) {
        validMoves.clear();
        gameBoard = board;
        piecePosition = position;
        ChessPiece piece = gameBoard.getPiece(piecePosition);
        if (piece.getPieceType() != ChessPiece.PieceType.QUEEN) {
            System.out.println("This is not a queen.");
            return null;
        }
        generateMoves(piece.getTeamColor());

        return validMoves;
    }

    private void generateMoves(ChessGame.TeamColor color) {
        generateBishopMoves(color);
        generateRookMoves(color);
    }

    private void generateRookMoves(ChessGame.TeamColor color) {
        boolean notBlocked = true;
        int rowInt = piecePosition.getRow();
        int colInt = piecePosition.getColumn();
        ChessPosition newPos = new ChessPositionImp(colInt, rowInt);
        //up moves
        while (notBlocked) {
            rowInt += 1;
            //check if new position will be off board
            if (rowInt > 8) {
                break;
            }
            newPos.setRow(rowInt);
            notBlocked = canMove(newPos, color);
        }
        //left moves
        notBlocked = true;
        rowInt = piecePosition.getRow();
        colInt = piecePosition.getColumn();
        newPos.setRow(rowInt);
        while (notBlocked) {
            colInt -= 1;
            //check if new position will be off board
            if (colInt < 1) {
                break;
            }
            newPos.setColumn(colInt);
            notBlocked = canMove(newPos, color);
        }
        //down moves
        notBlocked = true;
        rowInt = piecePosition.getRow();
        colInt = piecePosition.getColumn();
        newPos.setColumn(colInt);
        while (notBlocked) {
            rowInt -= 1;
            //check if new position will be off board
            if (rowInt < 1) {
                break;
            }
            newPos.setRow(rowInt);
            notBlocked = canMove(newPos, color);
        }
        //right moves
        notBlocked = true;
        rowInt = piecePosition.getRow();
        colInt = piecePosition.getColumn();
        newPos.setRow(rowInt);
        while (notBlocked) {
            colInt += 1;
            //check if new position will be off board
            if (colInt > 8) {
                break;
            }
            newPos.setColumn(colInt);
            notBlocked = canMove(newPos, color);
        }
    }

    private void generateBishopMoves(ChessGame.TeamColor color) {
        boolean notBlocked = true;
        int rowInt = piecePosition.getRow();
        int colInt = piecePosition.getColumn();
        ChessPosition newPos = new ChessPositionImp(colInt, rowInt);
        //up-right moves
        while (notBlocked) {
            rowInt += 1;
            colInt += 1;
            //check if new position will be off board
            if (rowInt > 8 || colInt > 8) {
                break;
            }
            newPos.setRow(rowInt);
            newPos.setColumn(colInt);
            notBlocked = canMove(newPos, color);
        }
        //up-left moves
        notBlocked = true;
        rowInt = piecePosition.getRow();
        colInt = piecePosition.getColumn();
        while (notBlocked) {
            rowInt += 1;
            colInt -= 1;
            //check if new position will be off board
            if (rowInt > 8 || colInt < 1) {
                break;
            }
            newPos.setRow(rowInt);
            newPos.setColumn(colInt);
            notBlocked = canMove(newPos, color);
        }
        //down-left moves
        notBlocked = true;
        rowInt = piecePosition.getRow();
        colInt = piecePosition.getColumn();
        while (notBlocked) {
            rowInt -= 1;
            colInt -= 1;
            //check if new position will be off board
            if (rowInt < 1 || colInt < 1) {
                break;
            }
            newPos.setRow(rowInt);
            newPos.setColumn(colInt);
            notBlocked = canMove(newPos, color);
        }
        //down-right moves
        notBlocked = true;
        rowInt = piecePosition.getRow();
        colInt = piecePosition.getColumn();
        while (notBlocked) {
            rowInt -= 1;
            colInt += 1;
            //check if new position will be off board
            if (rowInt < 1 || colInt > 8) {
                break;
            }
            newPos.setRow(rowInt);
            newPos.setColumn(colInt);
            notBlocked = canMove(newPos, color);
        }
    }

    private boolean canMove(ChessPosition newPos, ChessGame.TeamColor color) {
        ChessPosition endPos = gameBoard.findPosOnBoard(newPos);
        if (gameBoard.getPiece(endPos) == null) {
            validMoves.add(new ChessMoveImp(piecePosition, endPos, null));
            return true;
        } else if (gameBoard.getPiece(endPos).getTeamColor() != color) {
            validMoves.add(new ChessMoveImp(piecePosition, endPos, null));
            return false;
        } else {
            return false;
        }
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }

    @Override
    public ChessPiece.PieceType getType() {
        return type;
    }
}
