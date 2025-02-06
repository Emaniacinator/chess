package chess;

import java.util.ArrayList;
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

        if(currentBoard.getPiece(oldPosition) == null){

            throw new InvalidMoveException("Attempted to move a piece that doesn't exist");

        }

        Collection<ChessMove> possibleMoves = validMoves(oldPosition);
        TeamColor currentPieceTeam = currentBoard.getPiece(oldPosition).getTeamColor();

        if(teamTurn != currentPieceTeam){

            throw new InvalidMoveException("Attempted to move out of turn");

        }

        if (possibleMoves.contains(move)){

            ChessPiece pieceType = currentBoard.getPiece(oldPosition);

            currentBoard.addPiece(oldPosition, null);

            if (move.getPromotionPiece() == null){

                if (pieceType.getPieceType() == KING){

                    currentBoard.addPiece(newPosition, pieceType);

                    // The following code mistakenly checks to see if the king is moving into check, not
                    // to see if the move is valid while the king is in check. (eg, gets the king out
                    // of check
                    if (isInCheck(currentPieceTeam) == true) {

                        currentBoard.addPiece(oldPosition, pieceType);
                        currentBoard.addPiece(newPosition, null);

                        throw new InvalidMoveException("Attempted to move king into check");

                    }

                }

                currentBoard.addPiece(newPosition, pieceType);

                if (isInCheck(currentPieceTeam) == true){ // This should throw an exception if the king is still in check after a move

                    currentBoard.addPiece(oldPosition, pieceType);
                    currentBoard.addPiece(newPosition, null);

                    throw new InvalidMoveException("Attempted to move king into check");

                }

                pieceType.hasMovedUpdater();

            }

            else{

                currentBoard.addPiece(newPosition, new ChessPiece(pieceType.getTeamColor(), move.getPromotionPiece(), true));

            }


        }

        else{

            throw new InvalidMoveException("Attempted to make an illegal move");

        }

        if (teamTurn == TeamColor.WHITE){

            teamTurn = TeamColor.BLACK;

        }

        else {

            teamTurn = TeamColor.WHITE;

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

                ChessPosition checkedPosition = new ChessPosition(j + 1, i + 1);
                ChessPiece checkedPiece = currentBoard.getPiece(checkedPosition);

                if (checkedPiece != null && checkedPiece.type == KING){

                    if (checkedPiece.pieceColor == teamColor){

                        return checkedPosition;

                    }

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

                if(couldCauseCheck != null && couldCauseCheck.pieceColor != teamColor){

                    Collection<ChessMove> possibleMoves = validMoves(checkedPosition);

                    // NOTE: This won't work quite right with pawns due to promotions.

                    ChessMove moveToCheck = new ChessMove(checkedPosition, kingToCheck, null);

                    for (ChessMove currentCheckedMove : possibleMoves) {

                        if (currentCheckedMove.equals(moveToCheck)){

                            return true;

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
    public boolean isInCheckmate(TeamColor teamColor) {

        if (isInCheck(teamColor)){

            if (getNonCheckKingMoves(teamColor).isEmpty() == true){

                return true;

            }

        }

        // Here are the things that might cause trouble:
        // Pieces that can move to block the king being in check
        // You could implement a function to find the piece creating the check
        // and then see if it prevents the king from being in check and see if that
        // works for this.
        //
        // Especially problematic will maybe be pawns?

        return false;

    }


    /**
     *
     * @param teamColor
     * @return nonCheckKingMoves, which is a collection of all the moves that a king
     * can take without being in check
     */
    public Collection<ChessMove> getNonCheckKingMoves(TeamColor teamColor){

        Collection<ChessMove> nonCheckMoves = new ArrayList<>();

        ChessPosition kingLocation = kingLocator(teamColor);
        ChessPiece currentKing = currentBoard.getPiece(kingLocation);
        Collection<ChessMove> possibleKingMoves = currentKing.pieceMoves(currentBoard, kingLocation);

        for (ChessMove currentMoveCheck : possibleKingMoves) {

            ChessPosition moveStart = currentMoveCheck.getStartPosition();
            ChessPosition moveEnd = currentMoveCheck.getEndPosition();


            try{

                makeMove(currentMoveCheck);
                nonCheckMoves.add(currentMoveCheck);
                makeMove(new ChessMove(moveEnd, moveStart, null)); // Undoes the move since we only want to check it, not make it

            }

            catch (chess.InvalidMoveException failedMove){

                // This just does nothing because we only want to create changes if the move is valid.

            }

        }

        return nonCheckMoves;

    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        if (isInCheck(teamColor) != true){

            if (getNonCheckKingMoves(teamColor).isEmpty() == false){

                return true;

            }

        }

        // The easiest way to do this might be to see if there are any other pieces
        // on the king's team and if they have any valid moves. Then, if they don't,
        // you can return false.

        return false;
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
