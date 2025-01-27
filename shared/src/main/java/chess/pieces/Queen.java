package chess.pieces;

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

        Bishop fakeBishop = new Bishop(pieceColor, type, currentPosition);
        Rook fakeRook = new Rook(pieceColor, type, currentPosition, true);

        possibleMoves.addAll(fakeBishop.pieceMoves(board, myPosition));
        possibleMoves.addAll(fakeRook.pieceMoves(board, myPosition));

        return possibleMoves;

    }
}

