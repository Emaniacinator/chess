package chess.ChessPieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends ChessPiece {

    private boolean hasMoved;
    private ChessPosition currentPosition;

    public Pawn(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessPosition currentPosition, boolean hasMoved) {
        super(pieceColor, type);
        this.currentPosition = currentPosition;
        this.hasMoved = hasMoved;

    }


    public void firstMoveUpdater(){ // Should I check if the movement is already true first, or just do this? Which is more efficient?

        hasMoved = true;

    }


    public ChessPosition getCurrentPosition() {

        return currentPosition;

    }


    // En Passant capturing not yet implemented
    // It's worth noting that this might let the object exceed the edges of the chess board as currently implemented.
    // Limits may need to be created to prevent this from happening.
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) { // Note that white starts at the bottom and black starts at the top

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        int currentRow = currentPosition.getRow() + 1;
        int currentCol = currentPosition.getColumn() + 1;
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor();

        if (teamColor == ChessGame.TeamColor.WHITE){

            if (currentRow + 1 == 8){

                ChessPosition whitePromotionStraight = new ChessPosition(currentRow + 1, currentCol);

                if (board.getPiece(whitePromotionStraight) == null){

                    possibleMoves.add(new ChessMove(myPosition, whitePromotionStraight, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, whitePromotionStraight, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, whitePromotionStraight, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, whitePromotionStraight, PieceType.QUEEN));

                }

                if (currentCol - 1 > 1) { // Something about this is registering when there's nothing to capture

                    ChessPosition whitePromotionCaptureLeft = new ChessPosition(currentRow + 1, currentCol - 1);

                    if (board.getPiece(whitePromotionCaptureLeft) != null && board.getPiece(whitePromotionCaptureLeft).getTeamColor() != pieceColor){

                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureLeft, PieceType.ROOK));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureLeft, PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureLeft, PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureLeft, PieceType.QUEEN));

                    }

                }

                if (currentCol + 1 < 8){ // Something about this is registering when there's nothing to capture

                    ChessPosition whitePromotionCaptureRight = new ChessPosition(currentRow + 1, currentCol + 1);

                    if (board.getPiece(whitePromotionCaptureRight) != null && board.getPiece(whitePromotionCaptureRight).getTeamColor() != pieceColor){

                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureRight, PieceType.ROOK));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureRight, PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureRight, PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureRight, PieceType.QUEEN));

                    }

                }

            }

            ChessPosition stepForward = new ChessPosition(currentRow + 1, currentCol);

            if (board.getPiece(stepForward) == null && currentRow + 1 != 8){

                possibleMoves.add(new ChessMove(myPosition, stepForward, null));

                if (currentRow == 2){

                    ChessPosition doubleMove = new ChessPosition(currentRow + 2, currentCol);

                    if (board.getPiece(doubleMove) == null){

                        possibleMoves.add(new ChessMove(myPosition, doubleMove, null));

                    }

                }

            }

            if (currentCol - 1 > 1 && currentRow + 1 != 8){

                ChessPosition whiteCaptureLeft = new ChessPosition(currentRow + 1, currentCol - 1);

                if (board.getPiece(whiteCaptureLeft) != null && board.getPiece(whiteCaptureLeft).getTeamColor() != pieceColor){

                    possibleMoves.add(new ChessMove(myPosition, whiteCaptureLeft, null));

                }

            }

            if (currentCol + 1 < 8 && currentRow + 1 != 8){

                ChessPosition whiteCaptureRight = new ChessPosition(currentRow + 1, currentCol + 1);

                if (board.getPiece(whiteCaptureRight) != null && board.getPiece(whiteCaptureRight).getTeamColor() != pieceColor){

                    possibleMoves.add(new ChessMove(myPosition, whiteCaptureRight, null));

                }

            }

        }

        return possibleMoves;

        /*if (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7){

            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.ROOK));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.BISHOP));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.QUEEN));

        } else if (pieceColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7) {

            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.ROOK));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.BISHOP));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.QUEEN));

        } else {

            ChessPosition oneSpaceMove = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            if (board.getPiece(oneSpaceMove) == null) {

                possibleMoves.add(new ChessMove(myPosition, oneSpaceMove, null));

                if (hasMoved == false) {

                    ChessPosition twoSpaceMove = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 2);
                    if (board.getPiece(twoSpaceMove) == null) {

                        possibleMoves.add(new ChessMove(myPosition, twoSpaceMove, null));

                    }

                }

            }

        }

        return possibleMoves;*/
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

}
