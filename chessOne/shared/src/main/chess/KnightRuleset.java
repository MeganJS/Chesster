package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightRuleset implements PieceRuleset{


    private Collection<ChessMove> validMoves = new HashSet<>();
    private ChessBoard gameBoard;
    private ChessPosition piecePosition;

    @Override
    public Collection<ChessMove> findValidMoves(ChessPosition position, ChessBoard board) {
        validMoves.clear();
        gameBoard = board;
        piecePosition = position;
        ChessPiece piece = gameBoard.getPiece(piecePosition);
        if(piece.getPieceType() != ChessPiece.PieceType.KNIGHT){
            //TODO: throw an exception?
            System.out.println("This is not a knight.");
            return null;
        }
        generateMoves(piece.getTeamColor());

        return validMoves;
    }

    private void generateMoves(ChessGame.TeamColor color){
        ChessPosition newPos = new ChessPositionImp(piecePosition.getColumn(), piecePosition.getRow());
        //right moves
        if (piecePosition.getColumn() < 7){
            //up-right moves
            if(piecePosition.getRow() < 7){
                newPos.setRow(piecePosition.getRow()+2);
                newPos.setColumn(piecePosition.getColumn()+1);
                canMove(newPos, color);
                newPos.setRow(piecePosition.getRow()+1);
                newPos.setColumn(piecePosition.getColumn()+2);
                canMove(newPos, color);
            }
            //down-right moves
            if(piecePosition.getRow() > 2){
                newPos.setRow(piecePosition.getRow()-2);
                newPos.setColumn(piecePosition.getColumn()+1);
                canMove(newPos, color);
                newPos.setRow(piecePosition.getRow()-1);
                newPos.setColumn(piecePosition.getColumn()+2);
                canMove(newPos, color);
            }
        }
        //left moves
        if (piecePosition.getColumn() > 2){
            //up-left moves
            if(piecePosition.getRow() < 7){
                newPos.setRow(piecePosition.getRow()+2);
                newPos.setColumn(piecePosition.getColumn()-1);
                canMove(newPos, color);
                newPos.setRow(piecePosition.getRow()+1);
                newPos.setColumn(piecePosition.getColumn()-2);
                canMove(newPos, color);
            }
            //down-left moves
            if(piecePosition.getRow() > 2){
                newPos.setRow(piecePosition.getRow()-2);
                newPos.setColumn(piecePosition.getColumn()-1);
                canMove(newPos, color);
                newPos.setRow(piecePosition.getRow()-1);
                newPos.setColumn(piecePosition.getColumn()-2);
                canMove(newPos, color);
            }
        }
    }

    private void canMove(ChessPosition newPos, ChessGame.TeamColor color){
        ChessPosition endPos = gameBoard.findPosOnBoard(newPos);
        if(gameBoard.getPiece(endPos) == null || gameBoard.getPiece(endPos).getTeamColor() != color){
            validMoves.add(new ChessMoveImp(piecePosition, endPos, null));
        }
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}
