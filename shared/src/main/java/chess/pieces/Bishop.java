package chess.pieces;

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


    public Collection<ChessMove> moveHelper(ChessBoard board, ChessPosition myPosition, int iChange, int jChange){

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        for (int i = currentCol + iChange, j = currentRow + jChange; i <= 8 && j <= 8 && i >= 1 && j >= 1; i = i + iChange, j = j + jChange){

            ChessPosition checkedMovement = new ChessPosition(j, i);

            if(board.getPiece(checkedMovement) == null || board.getPiece(checkedMovement).getTeamColor() != this.getTeamColor()){

                ChessMove newMove = new ChessMove(myPosition, checkedMovement, null);
                possibleMoves.add(newMove);

                if (board.getPiece(checkedMovement) != null && board.getPiece(checkedMovement).getTeamColor() != this.getTeamColor()) {

                    break;

                }

            }

            else{

                break;

            }

        }

        return possibleMoves;

    }


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        Collection<ChessMove> upRightMoves = moveHelper(board, myPosition, 1, 1);
        possibleMoves.addAll(upRightMoves);

        Collection<ChessMove> upLeftMoves = moveHelper(board, myPosition, 1, -1);
        possibleMoves.addAll(upLeftMoves);

        Collection<ChessMove> downRightMoves = moveHelper(board, myPosition, -1, 1);
        possibleMoves.addAll(downRightMoves);

        Collection<ChessMove> downLeftMoves = moveHelper(board, myPosition, -1, -1);
        possibleMoves.addAll(downLeftMoves);

        return possibleMoves;

    }

}

