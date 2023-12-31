package chess;

import java.util.Collection;

public class ChessPieceImp implements ChessPiece {

    private PieceType type = null;
    private ChessGame.TeamColor color = null;
    private transient PieceRuleset moveRules;
    //add a position variable?

    public ChessPieceImp(PieceType typeInput, ChessGame.TeamColor colorInput) {
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

    public void setPieceType(PieceType type) {
        this.type = type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     *
     * @return Collection of valid moves
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.PAWN) {
            moveRules = new PawnRuleset();
            return moveRules.findValidMoves(myPosition, board);
        } else if (type == PieceType.KNIGHT) {
            moveRules = new KnightRuleset();
            return moveRules.findValidMoves(myPosition, board);
        } else if (type == PieceType.BISHOP) {
            moveRules = new BishopRuleset();
            return moveRules.findValidMoves(myPosition, board);
        } else if (type == PieceType.ROOK) {
            moveRules = new RookRuleset();
            return moveRules.findValidMoves(myPosition, board);
        } else if (type == PieceType.QUEEN) {
            moveRules = new QueenRuleset();
            return moveRules.findValidMoves(myPosition, board);
        } else if (type == PieceType.KING) {
            moveRules = new KingRuleset();
            return moveRules.findValidMoves(myPosition, board);
        }

        return null;
    }
    
}
