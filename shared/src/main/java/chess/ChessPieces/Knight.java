package chess.ChessPieces;

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

        int currentCol = myPosition.getColumn();
        int currentRow = myPosition.getRow();

        if (currentRow < 6){

            if (currentCol < 7){

                ChessPosition topRight = new ChessPosition(currentRow + 2, currentCol + 1);

                if (board.getPiece(topRight) == null){

                    possibleMoves.add(new ChessMove(myPosition, topRight, null));

                }

            }

            if (currentCol > 0){

                ChessPosition topLeft = new ChessPosition(currentRow + 2, currentCol - 1);

                if (board.getPiece(topLeft) == null){

                    possibleMoves.add(new ChessMove(myPosition, topLeft, null));

                }

            }

        }

        if (currentCol < 6){

            if (currentRow < 7){

                ChessPosition rightTop = new ChessPosition(currentRow + 1, currentCol + 2);

                if (board.getPiece(rightTop) == null){

                    possibleMoves.add(new ChessMove(myPosition, rightTop, null));

                }

            }

            if (currentRow > 0){

                ChessPosition rightBottom = new ChessPosition(currentRow - 1, currentCol + 2);

                if (board.getPiece(rightBottom) == null){

                    possibleMoves.add(new ChessMove(myPosition, rightBottom, null));

                }

            }

        }

        if (currentRow > 1){

            if (currentCol < 7){

                ChessPosition bottomRight = new ChessPosition(currentRow - 2, currentCol + 1);

                if (board.getPiece(bottomRight) == null){

                    possibleMoves.add(new ChessMove(myPosition, bottomRight, null));

                }

            }

            if (currentCol > 0){

                ChessPosition bottomLeft = new ChessPosition(currentRow - 2, currentCol - 1);

                if (board.getPiece(bottomLeft) == null){

                    possibleMoves.add(new ChessMove(myPosition, bottomLeft, null));

                }

            }

        }

        if (currentCol > 1){

            if (currentRow < 7){

                ChessPosition leftTop = new ChessPosition(currentRow + 1, currentCol - 2);

                if (board.getPiece(leftTop) == null){

                    possibleMoves.add(new ChessMove(myPosition, leftTop, null));

                }

            }

            if (currentRow > 0){

                ChessPosition leftBottom = new ChessPosition(currentRow - 1, currentCol - 2);

                if (board.getPiece(leftBottom) == null){

                    possibleMoves.add(new ChessMove(myPosition, leftBottom, null));

                }

            }

        }

        return possibleMoves;

    }
}
