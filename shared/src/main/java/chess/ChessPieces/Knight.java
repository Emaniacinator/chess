package chess.ChessPieces;

import chess.ChessGame;
import chess.ChessPiece;

public class Knight extends ChessPiece {

    public Knight(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        super(pieceColor, type);

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
