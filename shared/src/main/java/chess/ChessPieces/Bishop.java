package chess.ChessPieces;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

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

}

