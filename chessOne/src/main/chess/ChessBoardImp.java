package chess;

public class ChessBoardImp implements ChessBoard{
    /**
     * Adds a chess piece to the chessboard
     * @param position where to add the piece to
     * @param piece the piece to add
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {

    }

    /**
     * Gets a chess piece on the chessboard
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    @Override
    public void resetBoard() {

    }
}
