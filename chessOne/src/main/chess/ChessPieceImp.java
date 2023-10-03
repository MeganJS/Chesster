package chess;

import java.util.Collection;

public class ChessPieceImp implements ChessPiece{

    private PieceType type = null;
    private ChessGame.TeamColor color = null;
    private boolean hasMoved = false;
    //add a position variable?

    public ChessPieceImp(PieceType typeInput, ChessGame.TeamColor colorInput){
        type = typeInput;
        color = colorInput;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    @Override
    public PieceType getPieceType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     * @return Collection of valid moves
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
