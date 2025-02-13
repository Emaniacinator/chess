package chess.pieces;

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


    /* public void firstMoveUpdater(){ // Should I check if the movement is already true first, or just do this? Which is more efficient?

        hasMoved = true;

    } */ // This could be helpful later if you decide to go for bonus points or refine this program


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

        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

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

