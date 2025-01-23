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


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) { // Does this include capturing pieces? If so, this is wrong.

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        if (pieceColor == ChessGame.TeamColor.BLACK && myPosition.getColumn() == 0){

            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.ROOK));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.BISHOP));
            possibleMoves.add(new ChessMove(myPosition, myPosition, PieceType.QUEEN));

        } else if (pieceColor == ChessGame.TeamColor.WHITE && myPosition.getColumn() == 7) {

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
