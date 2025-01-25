package chess.ChessPieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Rook extends ChessPiece {

    private boolean hasMoved = false;
    private ChessPosition currentPosition;

    public Rook(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessPosition currentPosition, boolean hasMoved) {
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

            return "R";

        }

        else {

            return "r";

        }

    }


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        int currentCol = myPosition.getColumn();
        int currentRow = myPosition.getRow();

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        for (int i = currentCol + 1; i < 7; i++){

            ChessPosition checkedPosition = new ChessPosition(currentRow, i);

            if (board.getPiece(checkedPosition) == null){

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

            }

            else{

                break;

            }

        }

        for (int i = currentCol - 1; i > 0; i--){

            ChessPosition checkedPosition = new ChessPosition(currentRow, i);

            if (board.getPiece(checkedPosition) == null){

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

            }

            else{

                break;

            }

        }

        for (int j = currentRow + 1; j < 7; j++){

            ChessPosition checkedPosition = new ChessPosition(j, currentCol);

            if (board.getPiece(checkedPosition) == null){

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

            }

            else{

                break;

            }

        }

        for (int j = currentRow - 1; j > 0; j--){

            ChessPosition checkedPosition = new ChessPosition(j, currentCol);

            if (board.getPiece(checkedPosition) == null){

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

            }

            else{

                break;

            }

        }

        return possibleMoves;

    }
}

