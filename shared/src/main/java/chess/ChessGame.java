package chess;

import java.util.Collection;
import chess.InvalidMoveException;

import static chess.ChessPiece.PieceType.KING;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard currentBoard;

    /**
     * Initializes a brand new game
     */
    public ChessGame() {

        teamTurn = TeamColor.WHITE;
        currentBoard = new ChessBoard();
        currentBoard.resetBoard();

    }


    /**
     * Initializes a game using a previous board
     */
    public ChessGame(TeamColor teamTurn, ChessBoard currentBoard){

        this.teamTurn = teamTurn;
        this.currentBoard = currentBoard;

    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return teamTurn;

    }


    /**
     * Sets which team's turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        teamTurn = team;

    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece selectedPiece = currentBoard.getPiece(startPosition);

        return selectedPiece.pieceMoves(currentBoard, startPosition);

    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPosition oldPosition = move.getStartPosition();
        ChessPosition newPosition = move.getEndPosition();

        Collection<ChessMove> possibleMoves = validMoves(oldPosition);

        if (teamTurn != currentBoard.getPiece(oldPosition).getTeamColor()){

            throw new InvalidMoveException("Attempted to move out of turn");

        }

        if (possibleMoves.contains(move)){

            ChessPiece pieceType = currentBoard.getPiece(oldPosition);
            pieceType.hasMovedUpdater();

            currentBoard.addPiece(oldPosition, null);

            if (move.getPromotionPiece() == null){

                currentBoard.addPiece(newPosition, pieceType);

            }

            else{

                currentBoard.addPiece(newPosition, new ChessPiece(pieceType.getTeamColor(), move.getPromotionPiece(), true));

            }


        }

        else{

            throw new InvalidMoveException("Attempted to make an illegal move");

        }

    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to find the king for
     * @return the king's position on the board for the specified team
     */
    public ChessPosition kingLocator(TeamColor teamColor){

        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {

                ChessPosition checkedPosition = new ChessPosition(i + 1, j + 1);
                ChessPiece checkedPiece = currentBoard.getPiece(checkedPosition);

                if (checkedPiece != null && checkedPiece.type == KING){

                    return checkedPosition;

                }

            }

        }

        return null;

    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition kingToCheck = kingLocator(teamColor);

        for (int i = 0; i < 8; i++){

            for (int j = 0; j < 8; j++){

                ChessPosition checkedPosition = new ChessPosition(i + 1, j + 1);
                ChessPiece couldCauseCheck = currentBoard.getPiece(checkedPosition);

                /*
                 *
                 * Time for pseudo code lol. Basically, pull each piece and then check to see if it's possible
                 * moves include the tile that the king is in. Note that there is a need to check for weird
                 * pawn shenanigans though :( Or maybe not? It might already be built into the pawn function.
                 *
                 */

                if(couldCauseCheck != null && couldCauseCheck.pieceColor != teamColor){

                    Collection<ChessMove> possibleMoves = validMoves(checkedPosition);

                    // NOTE: This won't work quite right with pawns due to promotions.

                    ChessMove moveToCheck = new ChessMove(checkedPosition, kingToCheck, null);

                    if (possibleMoves.contains(moveToCheck)){

                        return true;

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
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        currentBoard = board;

    }


    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return currentBoard;

    }
}
