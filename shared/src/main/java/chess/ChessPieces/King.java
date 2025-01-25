package chess.ChessPieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class King extends ChessPiece {

    private boolean hasMoved;
    private ChessPosition currentPosition;

    public King(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessPosition currentPosition, boolean hasMoved) {
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
    public String toString(){

        if(pieceColor == ChessGame.TeamColor.WHITE){

            return "K";

        }

        else {

            return "k";

        }

    }

    // Castling is not yet implemented
    // Probably there's a prettier way to do this with switch statements. Investigate further
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList();

        int currentCol = currentPosition.getColumn();
        int currentRow = currentPosition.getRow();

        if (currentCol < 7){

            ChessPosition right = new ChessPosition(currentCol + 1, currentRow);

            if(board.getPiece(right) == null){

                possibleMoves.add(new ChessMove(myPosition, right, null));

            }

        }

        if (currentCol > 0) {

            ChessPosition left = new ChessPosition(currentCol - 1, currentRow);

            if(board.getPiece(left) == null){

                possibleMoves.add(new ChessMove(myPosition, left, null));

            }

        }

        if (currentRow > 0) {

            ChessPosition down = new ChessPosition(currentCol, currentRow - 1);

            if(board.getPiece(down) == null){

                possibleMoves.add(new ChessMove(myPosition, down, null));

            }

        }

        if (currentRow < 7) {

            ChessPosition up = new ChessPosition(currentCol, currentRow + 1);

            if(board.getPiece(up) == null){

                possibleMoves.add(new ChessMove(myPosition, up, null));

            }

        }

        if (currentCol < 7 && currentRow < 7) {

            ChessPosition diagonalRightUp = new ChessPosition(currentCol + 1, currentRow + 1);

            if(board.getPiece(diagonalRightUp) == null){

                possibleMoves.add(new ChessMove(myPosition, diagonalRightUp, null));

            }

        }

        if (currentCol < 7 && currentRow > 0) {

            ChessPosition diagonalRightDown = new ChessPosition(currentCol + 1, currentRow - 1);

            if(board.getPiece(diagonalRightDown) == null){

                possibleMoves.add(new ChessMove(myPosition, diagonalRightDown, null));

            }

        }

        if (currentCol > 0 && currentRow < 7) {

            ChessPosition diagonalLeftUp = new ChessPosition(currentCol - 1, currentRow + 1);

            if(board.getPiece(diagonalLeftUp) == null){

                possibleMoves.add(new ChessMove(myPosition, diagonalLeftUp, null));

            }

        }

        if (currentCol > 0 && currentRow > 0) {

            ChessPosition diagonalLeftDown = new ChessPosition(currentCol - 1, currentRow - 1);

            if(board.getPiece(diagonalLeftDown) == null){

                possibleMoves.add(new ChessMove(myPosition, diagonalLeftDown, null));

            }

        }

        return possibleMoves;

    }
}
