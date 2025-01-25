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

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        // This section is copied from the Bishop
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

        //This section is copied from the Rook
        for (int i = currentCol + 1; i <= 8; i++){

            ChessPosition checkedPosition = new ChessPosition(currentRow, i);

            if (board.getPiece(checkedPosition) == null || board.getPiece(checkedPosition).getTeamColor() != pieceColor){

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

                if(board.getPiece(checkedPosition) != null){

                    break;

                }

            }

            else{

                break;

            }

        }

        for (int i = currentCol - 1; i >= 1; i--){

            ChessPosition checkedPosition = new ChessPosition(currentRow, i);

            if (board.getPiece(checkedPosition) == null || board.getPiece(checkedPosition).getTeamColor() != pieceColor){

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

                if(board.getPiece(checkedPosition) != null){

                    break;

                }

            }

            else{

                break;

            }

        }

        for (int j = currentRow + 1; j <= 8; j++){

            ChessPosition checkedPosition = new ChessPosition(j, currentCol);

            if (board.getPiece(checkedPosition) == null || board.getPiece(checkedPosition).getTeamColor() != pieceColor){

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

                if(board.getPiece(checkedPosition) != null){

                    break;

                }

            }

            else{

                break;

            }

        }

        for (int j = currentRow - 1; j >= 1; j--){

            ChessPosition checkedPosition = new ChessPosition(j, currentCol);

            if (board.getPiece(checkedPosition) == null || board.getPiece(checkedPosition).getTeamColor() != pieceColor){

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

                if(board.getPiece(checkedPosition) != null){

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

