package chess.pieces;

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


    /* public void firstMoveUpdater(){ // Should I check if the movement is already true first, or just do this? Which is more efficient?

        hasMoved = true;

    } */ // This could be helpful later if you decide to go for bonus points or refine this program


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

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        if (currentCol < 8){
            ChessPosition right = new ChessPosition(currentRow, currentCol + 1);
            if(board.getPiece(right) == null || board.getPiece(right).getTeamColor() != pieceColor){
                possibleMoves.add(new ChessMove(myPosition, right, null));
            }
        }

        if (currentCol > 1) {
            ChessPosition left = new ChessPosition(currentRow, currentCol - 1);
            if(board.getPiece(left) == null || board.getPiece(left).getTeamColor() != pieceColor){
                possibleMoves.add(new ChessMove(myPosition, left, null));
            }
        }

        if (currentRow > 1) {
            ChessPosition down = new ChessPosition(currentRow - 1, currentCol);
            if(board.getPiece(down) == null || board.getPiece(down).getTeamColor() != pieceColor){
                possibleMoves.add(new ChessMove(myPosition, down, null));
            }
        }

        if (currentRow < 8) {
            ChessPosition up = new ChessPosition(currentRow + 1, currentCol);
            if(board.getPiece(up) == null || board.getPiece(up).getTeamColor() != pieceColor){
                possibleMoves.add(new ChessMove(myPosition, up, null));
            }
        }

        if (currentCol < 8 && currentRow < 8) {
            ChessPosition diagonalRightUp = new ChessPosition(currentRow + 1, currentCol + 1);
            if(board.getPiece(diagonalRightUp) == null || board.getPiece(diagonalRightUp).getTeamColor() != pieceColor){
                possibleMoves.add(new ChessMove(myPosition, diagonalRightUp, null));
            }
        }

        if (currentCol < 8 && currentRow > 1) {
            ChessPosition diagonalRightDown = new ChessPosition(currentRow - 1, currentCol + 1);
            if(board.getPiece(diagonalRightDown) == null || board.getPiece(diagonalRightDown).getTeamColor() != pieceColor){
                possibleMoves.add(new ChessMove(myPosition, diagonalRightDown, null));
            }
        }

        if (currentCol > 1 && currentRow < 8) {
            ChessPosition diagonalLeftUp = new ChessPosition(currentRow + 1, currentCol - 1);
            if(board.getPiece(diagonalLeftUp) == null || board.getPiece(diagonalLeftUp).getTeamColor() != pieceColor){
                possibleMoves.add(new ChessMove(myPosition, diagonalLeftUp, null));
            }
        }

        if (currentCol > 1 && currentRow > 1) {
            ChessPosition diagonalLeftDown = new ChessPosition(currentRow - 1, currentCol - 1);
            if(board.getPiece(diagonalLeftDown) == null || board.getPiece(diagonalLeftDown).getTeamColor() != pieceColor){
                possibleMoves.add(new ChessMove(myPosition, diagonalLeftDown, null));
            }
        }

        return possibleMoves;

    }


    // This implementation might cause a few problems due to weird classes conflicting.
    @Override
    public boolean equals (Object o){

        if (o.getClass() == this.getClass()){

            King other = (King) o;
            return super.equals(o) && other.getCurrentPosition().equals(currentPosition);

        }

        return false;

    }

}
