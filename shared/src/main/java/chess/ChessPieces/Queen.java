package chess.ChessPieces;

import chess.ChessGame;
import chess.ChessPiece;

public class Queen extends ChessPiece {

    public Queen(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        super(pieceColor, type);

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

}

