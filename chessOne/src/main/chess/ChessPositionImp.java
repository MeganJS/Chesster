package chess;

public class ChessPositionImp implements ChessPosition{

    private int column = 0;
    private int row = 0;
    //add ChessPiece object?

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    @Override
    public int getColumn() {
        return column;
    }
}
