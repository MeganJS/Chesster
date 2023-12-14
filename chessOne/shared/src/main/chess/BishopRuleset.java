package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopRuleset implements PieceRuleset {
    private Collection<ChessMove> validMoves = new HashSet<>();
    private ChessBoard gameBoard;
    private ChessPosition piecePosition;

    ChessPiece.PieceType type;

    public BishopRuleset() {
        this.type = ChessPiece.PieceType.BISHOP;
    }

    @Override
    public Collection<ChessMove> findValidMoves(ChessPosition position, ChessBoard board) {
        validMoves.clear();
        gameBoard = board;
        piecePosition = position;
        ChessPiece piece = gameBoard.getPiece(piecePosition);
        if (piece.getPieceType() != ChessPiece.PieceType.BISHOP) {
            //TODO: throw an exception?
            System.out.println("This is not a bishop.");
            return null;
        }
        generateMoves(piece.getTeamColor());

        return validMoves;
    }

    private void generateMoves(ChessGame.TeamColor color) {
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
