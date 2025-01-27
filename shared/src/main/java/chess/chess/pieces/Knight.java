package chess.chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Knight extends ChessPiece {

    private ChessPosition currentPosition;

    public Knight(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessPosition currentPosition) {
        super(pieceColor, type);
        this.currentPosition = currentPosition;

    }


    public ChessPosition getCurrentPosition() {

        return currentPosition;

    }


    @Override
    public String toString(){

        if(pieceColor == ChessGame.TeamColor.WHITE){

            return "N";

        }

        else {

            return "n";

        }

    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        if (currentRow < 7){

            if (currentCol < 8){

                ChessPosition topRight = new ChessPosition(currentRow + 2, currentCol + 1);

                if (board.getPiece(topRight) == null || board.getPiece(topRight).getTeamColor() != this.getTeamColor()){

                    possibleMoves.add(new ChessMove(myPosition, topRight, null));

                }

            }

            if (currentCol > 1){

                ChessPosition topLeft = new ChessPosition(currentRow + 2, currentCol - 1);

                if (board.getPiece(topLeft) == null || board.getPiece(topLeft).getTeamColor() != this.getTeamColor()){

                    possibleMoves.add(new ChessMove(myPosition, topLeft, null));

                }

            }

        }

        if (currentCol < 7){

            if (currentRow < 8){

                ChessPosition rightTop = new ChessPosition(currentRow + 1, currentCol + 2);

                if (board.getPiece(rightTop) == null || board.getPiece(rightTop).getTeamColor() != this.getTeamColor()){

                    possibleMoves.add(new ChessMove(myPosition, rightTop, null));

                }

            }

            if (currentRow > 1){

                ChessPosition rightBottom = new ChessPosition(currentRow - 1, currentCol + 2);

                if (board.getPiece(rightBottom) == null || board.getPiece(rightBottom).getTeamColor() != this.getTeamColor()){

                    possibleMoves.add(new ChessMove(myPosition, rightBottom, null));

                }

            }

        }

        if (currentRow > 2){

            if (currentCol < 8){

                ChessPosition bottomRight = new ChessPosition(currentRow - 2, currentCol + 1);

                if (board.getPiece(bottomRight) == null || board.getPiece(bottomRight).getTeamColor() != this.getTeamColor()){

                    possibleMoves.add(new ChessMove(myPosition, bottomRight, null));

                }

            }

            if (currentCol > 1){

                ChessPosition bottomLeft = new ChessPosition(currentRow - 2, currentCol - 1);

                if (board.getPiece(bottomLeft) == null || board.getPiece(bottomLeft).getTeamColor() != this.getTeamColor()){

                    possibleMoves.add(new ChessMove(myPosition, bottomLeft, null));

                }

            }

        }

        if (currentCol > 2){

            if (currentRow < 8){

                ChessPosition leftTop = new ChessPosition(currentRow + 1, currentCol - 2);

                if (board.getPiece(leftTop) == null || board.getPiece(leftTop).getTeamColor() != this.getTeamColor()){

                    possibleMoves.add(new ChessMove(myPosition, leftTop, null));

                }

            }

            if (currentRow > 1){

                ChessPosition leftBottom = new ChessPosition(currentRow - 1, currentCol - 2);

                if (board.getPiece(leftBottom) == null || board.getPiece(leftBottom).getTeamColor() != this.getTeamColor()){

                    possibleMoves.add(new ChessMove(myPosition, leftBottom, null));

                }

            }

        }

        return possibleMoves;

    }
}
