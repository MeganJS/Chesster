package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingRuleset implements PieceRuleset{
    private Collection<ChessMove> validMoves = new HashSet<>();
    private ChessBoard gameBoard;
    private ChessPosition piecePosition;
    @Override
    public Collection<ChessMove> findValidMoves(ChessPosition position, ChessBoard board) {
        validMoves.clear();
        gameBoard = board;
        piecePosition = position;
        ChessPiece piece = gameBoard.getPiece(piecePosition);
        if(piece.getPieceType() != ChessPiece.PieceType.KING){
            //TODO: throw an exception?
            System.out.println("This is not a king.");
            return null;
        }
        generateMoves(piece.getTeamColor());

        return validMoves;
    }

    private void generateMoves(ChessGame.TeamColor color){
        ChessPosition newPos = new ChessPositionImp(piecePosition.getColumn(), piecePosition.getRow());
        if (piecePosition.getRow() < 8){
            //up
            newPos.setRow(piecePosition.getRow() + 1);
            canMove(newPos, color);
            //up-right
            if(piecePosition.getColumn() < 8){
                newPos.setColumn(piecePosition.getColumn() + 1);
                canMove(newPos, color);
            }
            //up-left
            if(piecePosition.getColumn() > 1){
                newPos.setColumn(piecePosition.getColumn() - 1);
                canMove(newPos, color);
            }
        }

        if (piecePosition.getRow() > 1){
            //down
            newPos.setRow(piecePosition.getRow() - 1);
            newPos.setColumn(piecePosition.getColumn());
            canMove(newPos, color);
            //down-right
            if(piecePosition.getColumn() < 8){
                newPos.setColumn(piecePosition.getColumn() + 1);
                canMove(newPos, color);
            }
            //down-left
            if(piecePosition.getColumn() > 1){
                newPos.setColumn(piecePosition.getColumn() - 1);
                canMove(newPos, color);
            }
        }
        //left
        if (piecePosition.getColumn() > 1){
            newPos.setRow(piecePosition.getRow());
            newPos.setColumn(piecePosition.getColumn() - 1);
            canMove(newPos, color);
        }
        //right
        if (piecePosition.getColumn() < 8){
            newPos.setRow(piecePosition.getRow());
            newPos.setColumn(piecePosition.getColumn() + 1);
            canMove(newPos, color);
        }
    }

    private void canMove(ChessPosition newPos, ChessGame.TeamColor color){
        ChessPosition endPos = gameBoard.findPosOnBoard(newPos);
        if(gameBoard.getPiece(endPos) == null){
            validMoves.add(new ChessMoveImp(piecePosition, endPos, null));
        }
        else if(gameBoard.getPiece(endPos).getTeamColor() != color){
            validMoves.add(new ChessMoveImp(piecePosition, endPos, null));
        }
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return null;
    }
}
