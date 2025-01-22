package chess.ChessPieces;

import chess.ChessGame;
import chess.ChessPiece;

public class Bishop extends ChessPiece {

    public Bishop (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        super(pieceColor, type);

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

}

