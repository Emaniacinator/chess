package chess.ChessPieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends ChessPiece {

    private ChessPosition currentPosition;

    public Bishop (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessPosition currentPosition){
        super(pieceColor, type);
        this.currentPosition = currentPosition;
    }


    public ChessPosition getCurrentPosition(){

        return currentPosition;

    }


    @Override
    public String toString(){

        if(pieceColor == ChessGame.TeamColor.WHITE){

            return "B";

        }

        else {

            return "b";

        }

    }

    // It's worth noting that this might let the object exceed the edges of the chess board as currently implemented.
    // Limits may need to be created to prevent this from happening.
    // UPDATE: This one should be checking to make sure that it's only making moves that are possible on the board. Hopefully
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        int currentCol = myPosition.getColumn();
        int currentRow = myPosition.getRow();

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

