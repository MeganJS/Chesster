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
        gameBoard = board;
        piecePosition = position;
        ChessPiece piece = gameBoard.getPiece(piecePosition);
        if(piece.getPieceType() != ChessPiece.PieceType.PAWN){
            //TODO: throw an exception?
            System.out.println("This is not a pawn.");
            return null;
        }
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            moveWhite(piece.isHasMoved());
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            moveBlack(piece.isHasMoved());
        }
        return validMoves;
    }

    private void moveWhite(boolean hasMoved){
        //check if piece at end of board
        if (piecePosition.getRow() < 8){
            //space right ahead
            ChessPosition newPos = new ChessPositionImp(piecePosition.getColumn(), piecePosition.getRow()+1);
            canMove(newPos);
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
            if(!hasMoved){
                newPos.setRow(piecePosition.getRow()+2);
                newPos.setColumn(piecePosition.getColumn());
                canMove(newPos);
            }
        }

    }

    private void moveBlack(boolean hasMoved){
        //check if piece at end of board
        if (piecePosition.getRow() > 1){
            //space right ahead
            ChessPosition newPos = new ChessPositionImp(piecePosition.getColumn(), piecePosition.getRow()-1);
            canMove(newPos);
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
            if(!hasMoved){
                newPos.setRow(piecePosition.getRow()-2);
                newPos.setColumn(piecePosition.getColumn());
                canMove(newPos);
            }
        }

    }

    private void canMove(ChessPosition newPos){
        if(gameBoard.getPiece(newPos) == null){
            ChessMove move = new ChessMoveImp(piecePosition, newPos);
            //checks if pawn can promote
            if (newPos.getRow() == 8 || newPos.getRow() == 1){
                move.setCanPromote(true);
            }
            validMoves.add(move);
        }
    }
    private void canCapture(ChessPosition newPos, ChessGame.TeamColor enemyColor){
        if(gameBoard.getPiece(newPos) != null){
            if(gameBoard.getPiece(newPos).getTeamColor() == enemyColor) {
                ChessMove move = new ChessMoveImp(piecePosition, newPos);
                //checks if pawn can promote
                if (newPos.getRow() == 8 || newPos.getRow() == 1){
                    move.setCanPromote(true);
                }
                validMoves.add(move);
            }
        }
    }

    @Override
    public Collection<ChessMove> getValidMoves() {
        return validMoves;
    }
}
