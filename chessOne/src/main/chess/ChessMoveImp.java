package chess;

import java.util.Objects;

public class ChessMoveImp implements ChessMove{

    private ChessPosition startPosition = null;
    private ChessPosition endPosition = null;
    private ChessPiece.PieceType promotionPiece = null;


    public ChessMoveImp(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotion){
        startPosition = start;
        endPosition = end;
        promotionPiece = promotion;
    }
    /**
     * @return ChessPosition of starting location
     */
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this chess move
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return null;
    }


    //hashcode function
    public int hashCode(){
        return Objects.hash(startPosition,endPosition,promotionPiece);
    }


    public boolean equals(Object o){
        if(o.getClass() != this.getClass()){
            return false;
        } //FIXME will this work with interfaces? if they come from the same interface but different classes...
        if(o == this){
            return true;
        }
        //casting time!
        ChessMoveImp oMove = (ChessMoveImp) o;
        if(!this.startPosition.equals(oMove.startPosition)){
            return false;
        }
        if(!this.endPosition.equals(oMove.endPosition)){
            return false;
        }
        if(oMove.promotionPiece != this.promotionPiece){
            return false;
        }
        return true;
    }
}
