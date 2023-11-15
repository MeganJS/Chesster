package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 */
public class ChessGameImp implements ChessGame{

    private TeamColor teamTurn = null;
    private ChessBoard gameBoard = new ChessBoardImp();
    /**
     * @return Which team's turn it is
     */
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     * @param team the team whose turn it is
     */
    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Gets a valid moves for a piece at the given location
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at startPosition
     */
    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> moveSet = piece.pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> validMoveSet = new HashSet<>();

        //I need to be able to completely undo the move
        ChessMove reverseMove = new ChessMoveImp();
        ChessPiece capturedPiece;
        for(ChessMove move : moveSet){
            reverseMove.setEndPosition(move.getStartPosition());
            reverseMove.setStartPosition(move.getEndPosition());
            reverseMove.setPromotionPiece(piece.getPieceType());
            capturedPiece = gameBoard.getPiece(move.getEndPosition());
            gameBoard.makeMove(move);
            if (!isInCheck(piece.getTeamColor())){
                validMoveSet.add(move);
            }
            gameBoard.makeMove(reverseMove);
            gameBoard.addPiece(move.getEndPosition(),capturedPiece);
        }
        return validMoveSet;
    }

    /**
     * Makes a move in a chess game
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //throw exception if move not in validMoves for that piece (call game function for it)
        //or if the piece's color doesn't match the current team's turn
        if(this.teamTurn != null){
            if(gameBoard.getPiece(move.getStartPosition()).getTeamColor() != this.teamTurn){
                throw new InvalidMoveException("The piece is not the same color as the team turn.");
            }
        }
        Collection<ChessMove> validMoveSet = validMoves(move.getStartPosition());
        if (!validMoveSet.contains(move)){
            throw new InvalidMoveException("This move is not in the valid moves for the piece.");
        }
        gameBoard.makeMove(move);
        if (teamTurn == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    @Override
    public boolean isInCheck(TeamColor teamColor) {
        //iterate through pieces
        ChessPosition curPosition = new ChessPositionImp(0,0);
        ChessPosition kingPosition = new ChessPositionImp(0,0);
        Collection<ChessMove> enemyMoves;
        for (int i = 1; i <= 8; i++){
            curPosition.setRow(i);
            for (int j = 1; j <= 8; j++){
                curPosition.setColumn(j);
                if(gameBoard.getPiece(curPosition) != null){
                    if (gameBoard.getPiece(curPosition).getPieceType() == ChessPiece.PieceType.KING){
                        if(gameBoard.getPiece(curPosition).getTeamColor() == teamColor){
                            kingPosition.setRow(i);
                            kingPosition.setColumn(j);
                        }
                    }
                }
            }
        }
        for (int i = 1; i <= 8; i++){
            curPosition.setRow(i);
            for (int j = 1; j <= 8; j++){
                curPosition.setColumn(j);
                if (gameBoard.getPiece(curPosition) != null){
                    if (gameBoard.getPiece(curPosition).getTeamColor() != teamColor){
                        enemyMoves = gameBoard.getPiece(curPosition).pieceMoves(gameBoard, curPosition);
                        for(ChessMove enemyMove : enemyMoves){
                            if (enemyMove.getEndPosition().equals(kingPosition)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        ChessPosition curPosition = new ChessPositionImp(0,0);
        Collection<ChessMove> allyMoves;
        for (int i = 1; i <= 8; i++){
            curPosition.setRow(i);
            for (int j = 1; j <= 8; j++){
                curPosition.setColumn(j);
                if(gameBoard.getPiece(curPosition) != null){
                    if(gameBoard.getPiece(curPosition).getTeamColor() == teamColor){
                        allyMoves = validMoves(curPosition);
                        if(!allyMoves.isEmpty()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        ChessPosition curPosition = new ChessPositionImp(0,0);
        Collection<ChessMove> allyMoves;
        for (int i = 1; i <= 8; i++){
            curPosition.setRow(i);
            for (int j = 1; j <= 8; j++){
                curPosition.setColumn(j);
                if(gameBoard.getPiece(curPosition) != null){
                    if(gameBoard.getPiece(curPosition).getTeamColor() == teamColor){
                        allyMoves = validMoves(curPosition);
                        if(!allyMoves.isEmpty()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     * @param board the new board to use
     */
    @Override
    public void setBoard(ChessBoard board) {
        if (board.getClass() == gameBoard.getClass()){
            gameBoard = board;
        }
    }

    /**
     * Gets the current chessboard
     * @return the chessboard
     */
    @Override
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
