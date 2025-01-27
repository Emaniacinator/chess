package chess.chess.pieces;

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

        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        for (int i = currentCol + 1, j = currentRow + 1; i <= 8 && j <= 8 ; i++, j++){ // Something with i is wrong here

            ChessPosition checkedSpot = new ChessPosition(j, i);

            if(board.getPiece(checkedSpot) == null || board.getPiece(checkedSpot).getTeamColor() != this.getTeamColor()){

                ChessMove newMove = new ChessMove(myPosition, checkedSpot, null);
                possibleMoves.add(newMove);

                if (board.getPiece(checkedSpot) != null && board.getPiece(checkedSpot).getTeamColor() != this.getTeamColor()) {

                    break;

                }

            }

            else{

                break;

            }

        }

        for (int i = currentCol + 1, j = currentRow - 1; i <= 8 && j >= 1 ; i++, j--){ // Something with i is wrong here

            ChessPosition checkedSpot = new ChessPosition(j, i);

            if(board.getPiece(checkedSpot) == null || board.getPiece(checkedSpot).getTeamColor() != this.getTeamColor()){

                ChessMove newMove = new ChessMove(myPosition, checkedSpot, null);
                possibleMoves.add(newMove);

                if (board.getPiece(checkedSpot) != null && board.getPiece(checkedSpot).getTeamColor() != this.getTeamColor()) {

                    break;

                }

            }

            else{

                break;

            }

        }

        for (int i = currentCol - 1, j = currentRow + 1; i >= 1 && j <= 8 ; i--, j++){

            ChessPosition checkedSpot = new ChessPosition(j, i);

            if(board.getPiece(checkedSpot) == null || board.getPiece(checkedSpot).getTeamColor() != this.getTeamColor()){

                ChessMove newMove = new ChessMove(myPosition, checkedSpot, null);
                possibleMoves.add(newMove);

                if (board.getPiece(checkedSpot) != null && board.getPiece(checkedSpot).getTeamColor() != this.getTeamColor()) {

                    break;

                }

            }

            else{

                break;

            }

        }

        for (int i = currentCol - 1, j = currentRow - 1; i >= 1 && j >= 1 ; i--, j--){

            ChessPosition checkedSpot = new ChessPosition(j, i);

            if(board.getPiece(checkedSpot) == null || board.getPiece(checkedSpot).getTeamColor() != this.getTeamColor()){

                ChessMove newMove = new ChessMove(myPosition, checkedSpot, null);
                possibleMoves.add(newMove);

                if (board.getPiece(checkedSpot) != null && board.getPiece(checkedSpot).getTeamColor() != this.getTeamColor()) {

                    break;

                }

            }

            else{

                break;

            }

        }

        return possibleMoves;

    }

}

