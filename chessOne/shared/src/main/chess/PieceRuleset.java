package chess;

import java.util.Collection;

public interface PieceRuleset {

    Collection<ChessMove> findValidMoves(ChessPosition position, ChessBoard board);

    Collection<ChessMove> getValidMoves();

    ChessPiece.PieceType getType();
}
