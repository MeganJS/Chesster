package chess;

public class ChessPositionImp implements ChessPosition{

    private int column = 0;
    private int row = 0;
    //add ChessPiece object?
    private ChessPiece pieceOnSquare = null;

    public ChessPositionImp(int colInput, int rowInput) {
        column = colInput;
        row = rowInput;
    }
    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    @Override
    public int getColumn() {
        return column;
    }


    public void setColumn(int column) {
        this.column = column;
    }

    public ChessPiece getPieceOnSquare() {
        return pieceOnSquare;
    }

    public void setPieceOnSquare(ChessPiece pieceOnSquare) {
        this.pieceOnSquare = pieceOnSquare;
    }
}
