package chess.ChessPieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Queen extends ChessPiece {

    private ChessPosition currentPosition;

    public Queen(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessPosition currentPosition){
        super(pieceColor, type);
        this.currentPosition = currentPosition;

    }


    public ChessPosition getCurrentPosition(){

        return currentPosition;

    }


    @Override
    public String toString(){

        if(pieceColor == ChessGame.TeamColor.WHITE){

            return "Q";

        }

        else {

            return "q";

        }

    }


    // This method is currently just the ones for the Rook and Bishop slammed together. Consider possible
    // better implementations in the future
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        int currentCol = myPosition.getColumn();
        int currentRow = myPosition.getRow();

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();


        // This part is the code from the Rook
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

        // This part is the code from the Bishop
        for (int i = currentCol + 1, j = currentRow + 1; i < 7 && j < 7 ; i++, j++){

            ChessPosition checkedSpot = new ChessPosition(i, j);

            if(board.getPiece(checkedSpot) == null){

                ChessMove newMove = new ChessMove(myPosition, checkedSpot, null);
                possibleMoves.add(newMove);

            }

            else{

                break;

            }

        }

        for (int i = currentCol + 1, j = currentRow - 1; i < 7 && j > 0 ; i++, j--){

            ChessPosition checkedSpot = new ChessPosition(i, j);

            if(board.getPiece(checkedSpot) == null){

                ChessMove newMove = new ChessMove(myPosition, checkedSpot, null);
                possibleMoves.add(newMove);

            }

            else{

                break;

            }

        }

        for (int i = currentCol - 1, j = currentRow + 1; i > 0 && j < 7 ; i--, j++){

            ChessPosition checkedSpot = new ChessPosition(i, j);

            if(board.getPiece(checkedSpot) == null){

                ChessMove newMove = new ChessMove(myPosition, checkedSpot, null);
                possibleMoves.add(newMove);

            }

            else{

                break;

            }

        }

        for (int i = currentCol - 1, j = currentRow - 1; i > 0 && j > 0 ; i--, j--){

            ChessPosition checkedSpot = new ChessPosition(i, j);

            if(board.getPiece(checkedSpot) == null){

                ChessMove newMove = new ChessMove(myPosition, checkedSpot, null);
                possibleMoves.add(newMove);

            }

            else{

                break;

            }

        }

        return possibleMoves;

    }
}

