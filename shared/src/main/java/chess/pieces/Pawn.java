package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends ChessPiece {

    private ChessPosition currentPosition;
    private boolean justMadeFirstMove;

    public Pawn(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessPosition currentPosition, boolean hasMoved) {

        super(pieceColor, type);
        this.currentPosition = currentPosition;
        this.hasMoved = hasMoved;
        this.justMadeFirstMove = false; // Could maybe cause problems?

    }


    public ChessPosition getCurrentPosition() {

        return currentPosition;

    }


    // En Passant capturing not yet implemented
    // Note that white starts at the bottom and black starts at the top
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        int currentRow = currentPosition.getRow() + 1;
        int currentCol = currentPosition.getColumn() + 1;
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor();
        int promotionRow;
        int movementDirection;

        if (teamColor == ChessGame.TeamColor.WHITE){
            promotionRow = 8;
            movementDirection = 1;
        }
        else{
            promotionRow = 1;
            movementDirection = -1;
        }

        if (currentRow + movementDirection == promotionRow) {

            ChessPosition promotionStraight = new ChessPosition(currentRow + movementDirection, currentCol);

            if (board.getPiece(promotionStraight) == null) {
                possibleMoves.add(new ChessMove(myPosition, promotionStraight, PieceType.ROOK));
                possibleMoves.add(new ChessMove(myPosition, promotionStraight, PieceType.KNIGHT));
                possibleMoves.add(new ChessMove(myPosition, promotionStraight, PieceType.BISHOP));
                possibleMoves.add(new ChessMove(myPosition, promotionStraight, PieceType.QUEEN));
            }

            if (currentCol - 1 >= 1) {
                ChessPosition promotionCaptureLeft = new ChessPosition(currentRow + movementDirection, currentCol - 1);
                if (board.getPiece(promotionCaptureLeft) != null && board.getPiece(promotionCaptureLeft).getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, promotionCaptureLeft, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, promotionCaptureLeft, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, promotionCaptureLeft, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, promotionCaptureLeft, PieceType.QUEEN));
                }
            }

            if (currentCol + 1 <= 8) {
                ChessPosition promotionCaptureRight = new ChessPosition(currentRow + movementDirection, currentCol + 1);
                if(board.getPiece(promotionCaptureRight) != null && board.getPiece(promotionCaptureRight).getTeamColor() != pieceColor){
                    possibleMoves.add(new ChessMove(myPosition, promotionCaptureRight, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, promotionCaptureRight, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, promotionCaptureRight, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, promotionCaptureRight, PieceType.QUEEN));
                }
            }
        }

        else {

            ChessPosition stepForward = new ChessPosition(currentRow + movementDirection, currentCol);

            if (board.getPiece(stepForward) == null && currentRow + movementDirection != promotionRow) {

                possibleMoves.add(new ChessMove(myPosition, stepForward, null));

                if ((currentRow == 2 && hasMoved == false) || (currentRow == 7 && hasMoved == false)) {

                    ChessPosition doubleMove = new ChessPosition(currentRow + (movementDirection * 2), currentCol);

                    if (board.getPiece(doubleMove) == null) {

                        possibleMoves.add(new ChessMove(myPosition, doubleMove, null));

                    }

                }

            }

            if (currentCol - 1 > 1 && currentRow + movementDirection != promotionRow) {

                ChessPosition captureLeft = new ChessPosition(currentRow + movementDirection, currentCol - 1);

                if (board.getPiece(captureLeft) != null && board.getPiece(captureLeft).getTeamColor() != pieceColor) {

                    possibleMoves.add(new ChessMove(myPosition, captureLeft, null));

                }

            }

            if (currentCol + 1 < 8 && currentRow + movementDirection != promotionRow) {

                ChessPosition captureRight = new ChessPosition(currentRow + movementDirection, currentCol + 1);

                if (board.getPiece(captureRight) != null && board.getPiece(captureRight).getTeamColor() != pieceColor) {

                    possibleMoves.add(new ChessMove(myPosition, captureRight, null));

                }
            }
        }

        return possibleMoves;

    }

    @Override
    public String toString(){

        if(pieceColor == ChessGame.TeamColor.WHITE){

            return "P";

        }

        else {

            return "p";

        }

    }


    @Override
    public void hasMovedUpdater() {

        if (this.hasMoved == false){

            justMadeFirstMove = true;

            // This will need to also see if any other moves have been taken and become false if they have.

        }

        else{

            justMadeFirstMove = false;

        }

        super.hasMovedUpdater();

    }

}
