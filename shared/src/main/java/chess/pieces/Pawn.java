package chess.pieces;

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
    // Note that white starts at the bottom and black starts at the top
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        int currentRow = currentPosition.getRow() + 1;
        int currentCol = currentPosition.getColumn() + 1;
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor();

        if (teamColor == ChessGame.TeamColor.WHITE) {

            if (currentRow + 1 == 8) {

                ChessPosition whitePromotionStraight = new ChessPosition(currentRow + 1, currentCol);

                if (board.getPiece(whitePromotionStraight) == null) {

                    possibleMoves.add(new ChessMove(myPosition, whitePromotionStraight, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, whitePromotionStraight, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, whitePromotionStraight, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, whitePromotionStraight, PieceType.QUEEN));

                }

                if (currentCol - 1 > 1) { // Something about this is registering when there's nothing to capture

                    ChessPosition whitePromotionCaptureLeft = new ChessPosition(currentRow + 1, currentCol - 1);

                    if (board.getPiece(whitePromotionCaptureLeft) != null && board.getPiece(whitePromotionCaptureLeft).getTeamColor() != pieceColor) {

                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureLeft, PieceType.ROOK));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureLeft, PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureLeft, PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureLeft, PieceType.QUEEN));

                    }

                }

                if (currentCol + 1 < 8) { // Something about this is registering when there's nothing to capture

                    ChessPosition whitePromotionCaptureRight = new ChessPosition(currentRow + 1, currentCol + 1);

                    if(board.getPiece(whitePromotionCaptureRight) != null && board.getPiece(whitePromotionCaptureRight).getTeamColor() != pieceColor){

                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureRight, PieceType.ROOK));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureRight, PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureRight, PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, whitePromotionCaptureRight, PieceType.QUEEN));

                    }

                }

            }

            ChessPosition stepForward = new ChessPosition(currentRow + 1, currentCol);

            if (board.getPiece(stepForward) == null && currentRow + 1 != 8) {

                possibleMoves.add(new ChessMove(myPosition, stepForward, null));

                if (currentRow == 2) {

                    ChessPosition doubleMove = new ChessPosition(currentRow + 2, currentCol);

                    if (board.getPiece(doubleMove) == null) {

                        possibleMoves.add(new ChessMove(myPosition, doubleMove, null));

                    }

                }

            }

            if (currentCol - 1 > 1 && currentRow + 1 != 8) {

                ChessPosition whiteCaptureLeft = new ChessPosition(currentRow + 1, currentCol - 1);

                if (board.getPiece(whiteCaptureLeft) != null && board.getPiece(whiteCaptureLeft).getTeamColor() != pieceColor) {

                    possibleMoves.add(new ChessMove(myPosition, whiteCaptureLeft, null));

                }

            }

            if (currentCol + 1 < 8 && currentRow + 1 != 8) {

                ChessPosition whiteCaptureRight = new ChessPosition(currentRow + 1, currentCol + 1);

                if (board.getPiece(whiteCaptureRight) != null && board.getPiece(whiteCaptureRight).getTeamColor() != pieceColor) {

                    possibleMoves.add(new ChessMove(myPosition, whiteCaptureRight, null));

                }

            }

        }


        if (teamColor == ChessGame.TeamColor.BLACK) {

            if (currentRow - 1 == 1) {

                ChessPosition blackPromotionStraight = new ChessPosition(currentRow - 1, currentCol);

                if (board.getPiece(blackPromotionStraight) == null) {

                    possibleMoves.add(new ChessMove(myPosition, blackPromotionStraight, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, blackPromotionStraight, PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, blackPromotionStraight, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, blackPromotionStraight, PieceType.QUEEN));

                }

                if (currentCol - 1 >= 1) {

                    ChessPosition blackPromotionCaptureLeft = new ChessPosition(currentRow - 1, currentCol - 1);

                    if (board.getPiece(blackPromotionCaptureLeft) != null && board.getPiece(blackPromotionCaptureLeft).getTeamColor() != pieceColor) {

                        possibleMoves.add(new ChessMove(myPosition, blackPromotionCaptureLeft, PieceType.ROOK));
                        possibleMoves.add(new ChessMove(myPosition, blackPromotionCaptureLeft, PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, blackPromotionCaptureLeft, PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, blackPromotionCaptureLeft, PieceType.QUEEN));

                    }

                }

                if (currentCol + 1 <= 8) {

                    ChessPosition blackPromotionCaptureRight = new ChessPosition(currentRow - 1, currentCol + 1);

                    if(board.getPiece(blackPromotionCaptureRight) != null && board.getPiece(blackPromotionCaptureRight).getTeamColor() != pieceColor){

                        possibleMoves.add(new ChessMove(myPosition, blackPromotionCaptureRight, PieceType.ROOK));
                        possibleMoves.add(new ChessMove(myPosition, blackPromotionCaptureRight, PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, blackPromotionCaptureRight, PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, blackPromotionCaptureRight, PieceType.QUEEN));

                    }

                }

            }

            ChessPosition blackStepForward = new ChessPosition(currentRow - 1, currentCol);

            if (board.getPiece(blackStepForward) == null && currentRow - 1 != 1) {

                possibleMoves.add(new ChessMove(myPosition, blackStepForward, null));

                if (currentRow == 7) {

                    ChessPosition blackDoubleMove = new ChessPosition(currentRow - 2, currentCol);

                    if (board.getPiece(blackDoubleMove) == null) {

                        possibleMoves.add(new ChessMove(myPosition, blackDoubleMove, null));

                    }

                }

            }

            if (currentCol - 1 > 1 && currentRow - 1 != 1) {

                ChessPosition blackCaptureLeft = new ChessPosition(currentRow - 1, currentCol - 1);

                if (board.getPiece(blackCaptureLeft) != null && board.getPiece(blackCaptureLeft).getTeamColor() != pieceColor) {

                    possibleMoves.add(new ChessMove(myPosition, blackCaptureLeft, null));

                }

            }

            if (currentCol + 1 < 8 && currentRow - 1 != 1) {

                ChessPosition blackCaptureRight = new ChessPosition(currentRow - 1, currentCol + 1);

                if (board.getPiece(blackCaptureRight) != null && board.getPiece(blackCaptureRight).getTeamColor() != pieceColor) {

                    possibleMoves.add(new ChessMove(myPosition, blackCaptureRight, null));

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

}
