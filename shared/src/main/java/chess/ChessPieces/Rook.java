package chess.ChessPieces;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class Rook extends ChessPiece {

    private boolean hasMoved = false;
    private ChessPosition currentPosition;

    public Rook(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessPosition currentPosition, boolean hasMoved) {
        super(pieceColor, type);
        this.currentPosition = currentPosition;
        this.hasMoved = hasMoved;

    }


    public void firstMoveUpdater(){ // Should I check if the movement is already true first, or just do this? Which is more efficient?

        hasMoved = true;

    }


    public ChessPosition getCurrentPosition() {

        return currentPosition;

    }


    @Override
    public String toString(){

        if(pieceColor == ChessGame.TeamColor.WHITE){

            return "R";

        }

        else {

            return "r";

        }

    }

}

