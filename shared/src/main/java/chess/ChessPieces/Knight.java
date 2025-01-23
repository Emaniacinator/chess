package chess.ChessPieces;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

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

}
