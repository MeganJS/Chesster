package chess;

public class ChessMoveImp implements ChessMove{

    private ChessPosition startPosition = null;
    private ChessPosition endPosition = null;

    public ChessMoveImp(ChessPosition start, ChessPosition end){
        startPosition = start;
        endPosition = end;
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
}
