package chess;

import java.util.ArrayList;
import java.util.Collection;
import chess.InvalidMoveException;
import jdk.jshell.spi.ExecutionControl;

import static chess.ChessPiece.PieceType.*;

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

        TeamColor currentTeamTurn = teamTurn;
        ChessPiece selectedPiece = currentBoard.getPiece(startPosition);
        TeamColor selectedPieceColor = selectedPiece.getTeamColor();
        Collection<ChessMove> initialMoveList = selectedPiece.pieceMoves(currentBoard, startPosition);
        Collection<ChessMove> finalMoveList = new ArrayList<>();

        for(ChessMove checkedMove: initialMoveList){

            // Call the move function for each checkedMove. If it results in the king in
            // check, have the makeMove function call an invalidMoveException. Any time
            // the exception is called, don't add it to the list. If no exception is
            // called, add it to the list. Then always undo the move by calling it backwards.

            ChessPosition checkedEndPosition = checkedMove.getEndPosition();
            ChessPiece.PieceType checkedPieceType = selectedPiece.getPieceType();
            ChessBoard backupBoard = new ChessBoard(currentBoard);

            try {

                makeMove(checkedMove, backupBoard);
                finalMoveList.add(checkedMove);
                teamTurn = selectedPieceColor;

            }

            catch (Exception InvalidMoveException){

                if (teamTurn != selectedPieceColor && InvalidMoveException.toString().equals("chess.InvalidMoveException: Attempted to move out of turn")){


                    try {

                        teamTurn = selectedPieceColor;
                        makeMove(checkedMove, backupBoard);
                        finalMoveList.add(checkedMove);
                        teamTurn = currentTeamTurn;

                    }

                    catch(Exception anotherInvalidMoveException){

                        teamTurn = currentTeamTurn;

                    }

                }

            }

        }

        return finalMoveList;

    }



    public void makeMove(ChessMove move) throws InvalidMoveException{

        makeMove(move, currentBoard);

    }

    /**
     * Makes a move in a chess game. Also undoes the move if it's invalid.
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move, ChessBoard board) throws InvalidMoveException {

        ChessPosition oldPosition = move.getStartPosition();
        ChessPosition newPosition = move.getEndPosition();
        ChessPiece currentPiece = board.getPiece(oldPosition);

        if(currentPiece == null){

            throw new InvalidMoveException("Attempted to move a piece that doesn't exist");

        }

        TeamColor currentPieceTeam = currentPiece.getTeamColor();

        if(teamTurn != currentPieceTeam){ // This might cause problems in phase 6, so implement it to happen later

            throw new InvalidMoveException("Attempted to move out of turn");

        }

        // Throw an exception if someone tries to make a move that isn't in the possible moves for the chess piece.

        Collection<ChessMove> possiblePieceMoves = currentPiece.pieceMoves(board, oldPosition);

        if (possiblePieceMoves.contains(move) != true){

            throw new InvalidMoveException("Attempted to make a move that is illegal");

        }

        if (move.getPromotionPiece() == null){

            board.addPiece(newPosition, currentPiece);
            board.addPiece(oldPosition, null);

            if(isInCheck(currentPieceTeam, board) == true){


                board.addPiece(newPosition, null);
                board.addPiece(oldPosition, currentPiece);
                throw new InvalidMoveException("Attempted to make a move that leaves the king in check");

            }

        }

        else{

            board.addPiece(newPosition, new ChessPiece(currentPieceTeam, move.getPromotionPiece(), true));
            board.addPiece(oldPosition, null);

            if(isInCheck(currentPieceTeam) == true) {

                board.addPiece(newPosition, null);
                board.addPiece(oldPosition, currentPiece);
                throw new InvalidMoveException("Attempted to make a move that leaves the king in check");

            }

        }

        if (teamTurn == TeamColor.WHITE){

            teamTurn = TeamColor.BLACK;

        }

        else {

            teamTurn = TeamColor.WHITE;

        }

        currentPiece.hasMovedUpdater();

    }


    public ChessPosition kingLocator(TeamColor teamColor) {

        return kingLocator(teamColor, currentBoard);

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to find the king for
     * @return the king's position on the board for the specified team
     */
    public ChessPosition kingLocator(TeamColor teamColor, ChessBoard board){

        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {

                ChessPosition checkedPosition = new ChessPosition(j + 1, i + 1);
                ChessPiece checkedPiece = board.getPiece(checkedPosition);

                if (checkedPiece != null && checkedPiece.type == KING){

                    if (checkedPiece.pieceColor == teamColor){

                        return checkedPosition;

                    }

                }

            }

        }

        return null;

    }


    public boolean isInCheck(TeamColor teamColor){

        return isInCheck(teamColor, currentBoard);

    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {

        ChessPosition kingToCheck = kingLocator(teamColor, board);

        for (int i = 0; i < 8; i++){

            for (int j = 0; j < 8; j++){

                ChessPosition checkedPosition = new ChessPosition(i + 1, j + 1);
                ChessPiece couldCauseCheck = board.getPiece(checkedPosition);

                if(couldCauseCheck != null && couldCauseCheck.pieceColor != teamColor){

                    Collection<ChessMove> possibleMoves = couldCauseCheck.pieceMoves(board, checkedPosition);

                    // NOTE: This won't work quite right with pawns due to promotions.

                    if (couldCauseCheck.getPieceType() == PAWN){

                        for (ChessMove currentCheckedMove : possibleMoves) {

                            if (currentCheckedMove.equals(new ChessMove(checkedPosition, kingToCheck, ROOK))){

                                return true;

                            }

                            if (currentCheckedMove.equals(new ChessMove(checkedPosition, kingToCheck, KNIGHT))){

                                return true;

                            }

                            if (currentCheckedMove.equals(new ChessMove(checkedPosition, kingToCheck, BISHOP))){

                                return true;

                            }

                            if (currentCheckedMove.equals(new ChessMove(checkedPosition, kingToCheck, QUEEN))){

                                return true;

                            }

                            if (currentCheckedMove.equals(new ChessMove(checkedPosition, kingToCheck, null))){

                                return true;

                            }

                        }

                    }

                    else{

                        ChessMove moveToCheck = new ChessMove(checkedPosition, kingToCheck, null);

                        for (ChessMove currentCheckedMove : possibleMoves) {

                            if (currentCheckedMove.equals(moveToCheck)){

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
    public boolean isInCheckmate(TeamColor teamColor) {

        Collection<ChessMove> validMoves = getAllValidTeamMoves(teamColor);

        if (isInCheck(teamColor) == true){

            if (validMoves.isEmpty() == true){

                return true;

            }

        }

        return false;

    }


    public Collection<ChessMove> getAllValidTeamMoves(TeamColor teamColor){

        return getAllValidTeamMoves(teamColor, currentBoard);

    }


    public Collection<ChessMove> getAllValidTeamMoves(TeamColor teamColor, ChessBoard board){

        Collection<ChessPosition> teamPieces = getAllTeamPieceLocations(teamColor, board);
        Collection<ChessMove> allValidMoves = new ArrayList<>();

        // For en passant and castling, add them to the possible moves list in the king and pawn classes

        for (ChessPosition currentCheckedPieceLocation: teamPieces){

            Collection<ChessMove> possibleValidMoves = validMoves(currentCheckedPieceLocation);
            allValidMoves.addAll(possibleValidMoves);

        }

        return allValidMoves;

    }


    public Collection<ChessPosition> getAllTeamPieceLocations(TeamColor teamColor){

        return getAllTeamPieceLocations(teamColor, currentBoard);

    }


    public Collection<ChessPosition> getAllTeamPieceLocations(TeamColor teamColor, ChessBoard board){

        Collection<ChessPosition> teamPieces = new ArrayList<>();

        for (int i = 0; i < 8; i++){

            for (int j = 0; j < 8; j++){

                ChessPosition temporaryPosition = new ChessPosition(i + 1, j + 1);

                if (board.getPiece(temporaryPosition) != null && board.getPiece(temporaryPosition).getTeamColor() == teamColor){

                    teamPieces.add(temporaryPosition);

                }

            }

        }

        return teamPieces;

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

            if (getAllValidTeamMoves(teamColor).isEmpty() == true){

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