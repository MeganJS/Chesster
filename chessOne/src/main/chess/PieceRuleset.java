package chess;

import java.util.Collection;

public interface PieceRuleset {

    public Collection<ChessMove> findValidMoves(ChessPosition position, ChessBoard board);

    public Collection<ChessMove> getValidMoves();
}
