package chess;

public class ChessBoardImp implements ChessBoard{
    //make 2d array of ChessPositions! it will be fun!
    private ChessPosition[][] positionsOnBoard;

    public ChessBoardImp(){
        positionsOnBoard = new ChessPositionImp[8][8];
        //i is rows, j is columns
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                positionsOnBoard[i][j] = new ChessPositionImp(j+1,i+1);
            }
        }
    }
    /**
     * Adds a chess piece to the chessboard
     * @param position where to add the piece to
     * @param piece the piece to add
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        ChessPosition boardPosition = findPosOnBoard(position);
        if (boardPosition.getPieceOnSquare() == null){
            boardPosition.setPieceOnSquare(piece);
            //this is a line I'm adding to see if it will make the tests work
            position.setPieceOnSquare(piece);
        }
        else{
            System.out.println("Cannot add; there is already a piece in that position.");
        }
    }

    /**
     * Gets a chess piece on the chessboard
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return findPosOnBoard(position).getPieceOnSquare();
    }

    public ChessPosition findPosOnBoard(ChessPosition position){
        return positionsOnBoard[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    @Override
    public void resetBoard() {
        //not sure if it's better to make new pieces every time or not
        //clear board
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                positionsOnBoard[i][j].setPieceOnSquare(null); // FIXME since we're resetting board, consider deleting old pieces. Could get cluttered if lots of resets happen
            }
        }

        //white pieces
        addPiece(positionsOnBoard[0][0],new ChessPieceImp(ChessPiece.PieceType.ROOK, ChessGame.TeamColor.WHITE));
        addPiece(positionsOnBoard[0][7],new ChessPieceImp(ChessPiece.PieceType.ROOK, ChessGame.TeamColor.WHITE));
        addPiece(positionsOnBoard[0][1],new ChessPieceImp(ChessPiece.PieceType.KNIGHT, ChessGame.TeamColor.WHITE));
        addPiece(positionsOnBoard[0][6],new ChessPieceImp(ChessPiece.PieceType.KNIGHT, ChessGame.TeamColor.WHITE));
        addPiece(positionsOnBoard[0][2],new ChessPieceImp(ChessPiece.PieceType.BISHOP, ChessGame.TeamColor.WHITE));
        addPiece(positionsOnBoard[0][5],new ChessPieceImp(ChessPiece.PieceType.BISHOP, ChessGame.TeamColor.WHITE));
        addPiece(positionsOnBoard[0][3],new ChessPieceImp(ChessPiece.PieceType.QUEEN, ChessGame.TeamColor.WHITE));
        addPiece(positionsOnBoard[0][4],new ChessPieceImp(ChessPiece.PieceType.KING, ChessGame.TeamColor.WHITE));
        for (int i = 0; i < 8; i++){
            addPiece(positionsOnBoard[1][i],new ChessPieceImp(ChessPiece.PieceType.PAWN, ChessGame.TeamColor.WHITE));
        }

        //black pieces
        addPiece(positionsOnBoard[7][0],new ChessPieceImp(ChessPiece.PieceType.ROOK, ChessGame.TeamColor.BLACK));
        addPiece(positionsOnBoard[7][7],new ChessPieceImp(ChessPiece.PieceType.ROOK, ChessGame.TeamColor.BLACK));
        addPiece(positionsOnBoard[7][1],new ChessPieceImp(ChessPiece.PieceType.KNIGHT, ChessGame.TeamColor.BLACK));
        addPiece(positionsOnBoard[7][6],new ChessPieceImp(ChessPiece.PieceType.KNIGHT, ChessGame.TeamColor.BLACK));
        addPiece(positionsOnBoard[7][2],new ChessPieceImp(ChessPiece.PieceType.BISHOP, ChessGame.TeamColor.BLACK));
        addPiece(positionsOnBoard[7][5],new ChessPieceImp(ChessPiece.PieceType.BISHOP, ChessGame.TeamColor.BLACK));
        addPiece(positionsOnBoard[7][3],new ChessPieceImp(ChessPiece.PieceType.QUEEN, ChessGame.TeamColor.BLACK));
        addPiece(positionsOnBoard[7][4],new ChessPieceImp(ChessPiece.PieceType.KING, ChessGame.TeamColor.BLACK));
        for (int i = 0; i < 8; i++){
            addPiece(positionsOnBoard[6][i],new ChessPieceImp(ChessPiece.PieceType.PAWN, ChessGame.TeamColor.BLACK));
        }
    }

    //FIXME add toString function (call position toString as well)
}
