package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PawnRuleset implements PieceRuleset{

    private Collection<ChessMove> validMoves = new HashSet<>();
    private ChessBoard gameBoard;
    private ChessPosition piecePosition;

    @Override
    public Collection<ChessMove> findValidMoves(ChessPosition position, ChessBoard board) {
        validMoves.clear();
        gameBoard = board;
        piecePosition = position;
        ChessPiece piece = gameBoard.getPiece(piecePosition);
        if(piece.getPieceType() != ChessPiece.PieceType.PAWN){
            //TODO: throw an exception?
            System.out.println("This is not a pawn.");
            return null;
        }
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            moveWhite();
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            moveBlack();
        }
        return validMoves;
    }

    private void moveWhite(){
        //check if piece at end of board
        if (piecePosition.getRow() < 8){
            //space right ahead
            ChessPosition newPos = new ChessPositionImp(piecePosition.getColumn(), piecePosition.getRow()+1);
            boolean unblocked = canMove(newPos);
            //diagonal spaces
            if (piecePosition.getColumn() > 1){
                newPos.setRow(piecePosition.getRow()+1);
                newPos.setColumn(piecePosition.getColumn()-1);
                canCapture(newPos, ChessGame.TeamColor.BLACK);
            }
            if (piecePosition.getColumn() < 8){
                newPos.setRow(piecePosition.getRow()+1);
                newPos.setColumn(piecePosition.getColumn()+1);
                canCapture(newPos, ChessGame.TeamColor.BLACK);
            }
            //for a pawn moving for the first time
            if(piecePosition.getRow() == 2 && unblocked){
                newPos.setRow(piecePosition.getRow()+2);
                newPos.setColumn(piecePosition.getColumn());
                canMove(newPos);
            }
        }

    }

    private void moveBlack(){
        //check if piece at end of board
        if (piecePosition.getRow() > 1){
            //space right ahead
            ChessPosition newPos = new ChessPositionImp(piecePosition.getColumn(), piecePosition.getRow()-1);
            boolean unblocked = canMove(newPos);
            //diagonal spaces
            if (piecePosition.getColumn() > 1){
                newPos.setRow(piecePosition.getRow()-1);
                newPos.setColumn(piecePosition.getColumn()-1);
                canCapture(newPos, ChessGame.TeamColor.WHITE);
            }
            if (piecePosition.getColumn() < 8){
                newPos.setRow(piecePosition.getRow()-1);
                newPos.setColumn(piecePosition.getColumn()+1);
                canCapture(newPos, ChessGame.TeamColor.WHITE);
            }
            //for a pawn moving for the first time
            if(piecePosition.getRow() == 7 && unblocked){
                newPos.setRow(piecePosition.getRow()-2);
                newPos.setColumn(piecePosition.getColumn());
                canMove(newPos);
            }
        }

    }

    private boolean canMove(ChessPosition newPos){
        ChessPosition startPos = gameBoard.findPosOnBoard(newPos);
        if(gameBoard.getPiece(startPos) == null){
            //checks if pawn can promote
            if (startPos.getRow() == 8 || startPos.getRow() == 1){
                promotionMoves(startPos);
            }
            else{
                validMoves.add(new ChessMoveImp(piecePosition, startPos, null));
            }
            return true;
        }
        else{
            return false;
        }
    }
    private void canCapture(ChessPosition newPos, ChessGame.TeamColor enemyColor){
        ChessPosition startPos = gameBoard.findPosOnBoard(newPos);
        if(gameBoard.getPiece(startPos) != null){
            if(gameBoard.getPiece(startPos).getTeamColor() == enemyColor) {
                //checks if pawn can promote
                if (startPos.getRow() == 8 || startPos.getRow() == 1){
                    promotionMoves(startPos);
                }
                else{
                    validMoves.add(new ChessMoveImp(piecePosition, startPos, null));
                }
            }
        }
    }
    private void promotionMoves(ChessPosition startPos){
        validMoves.add(new ChessMoveImp(piecePosition, startPos, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMoveImp(piecePosition, startPos, ChessPiece.PieceType.KNIGHT));
        validMoves.add(new ChessMoveImp(piecePosition, startPos, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMoveImp(piecePosition, startPos, ChessPiece.PieceType.BISHOP));
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}
