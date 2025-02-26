package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Rook extends ChessPiece {

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
    public String toString() {

        if (pieceColor == ChessGame.TeamColor.WHITE) {

            return "R";

        } else {

            return "r";

        }

    }


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        Collection<ChessMove> rightMoves = columnMoves(board, myPosition, 1);
        possibleMoves.addAll(rightMoves);

        Collection<ChessMove> leftMoves = columnMoves(board, myPosition, -1);
        possibleMoves.addAll(leftMoves);

        Collection<ChessMove> upMoves = rowMoves(board, myPosition, 1);
        possibleMoves.addAll(upMoves);

        Collection<ChessMove> downMoves = rowMoves(board, myPosition, -1);
        possibleMoves.addAll(downMoves);

        return possibleMoves;

    }

    public Collection<ChessMove> columnMoves(ChessBoard board, ChessPosition myPosition, int incrementer) {

        Collection<ChessMove> possibleMoves = new ArrayList();
        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        for (int i = currentCol + incrementer; i <= 8 && i >= 1; i = i + incrementer) {

            ChessPosition checkedPosition = new ChessPosition(currentRow, i);

            if (board.getPiece(checkedPosition) == null || board.getPiece(checkedPosition).getTeamColor() != pieceColor) {

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

                if (board.getPiece(checkedPosition) != null && board.getPiece(checkedPosition).getTeamColor() != pieceColor) {

                    break;

                }

            }

            else {

                break;

            }

        }

        return possibleMoves;

    }


    public Collection<ChessMove> rowMoves(ChessBoard board, ChessPosition myPosition, int incrementer){

        Collection<ChessMove> possibleMoves = new ArrayList();
        int currentCol = myPosition.getColumn() + 1;
        int currentRow = myPosition.getRow() + 1;

        for (int i = currentRow + incrementer; i <= 8 && i >= 1; i = i + incrementer) {

            ChessPosition checkedPosition = new ChessPosition(i, currentCol);

            if (board.getPiece(checkedPosition) == null || board.getPiece(checkedPosition).getTeamColor() != pieceColor) {

                possibleMoves.add(new ChessMove(myPosition, checkedPosition, null));

                if (board.getPiece(checkedPosition) != null && board.getPiece(checkedPosition).getTeamColor() != pieceColor) {

                    break;

                }

            }

            else {

                break;

            }

        }

        return possibleMoves;

    }

}

