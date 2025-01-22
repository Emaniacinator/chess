package chess.ChessPieces;

import chess.ChessGame;
import chess.ChessPiece;

public class King extends ChessPiece {

    private boolean hasMoved;

    public King(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, boolean hasMoved) {
        super(pieceColor, type);
        this.hasMoved = hasMoved;

    }

    public void firstMoveUpdater(){ // Should I check if the movement is already true first, or just do this? Which is more efficient?

        hasMoved = true;

    }

    @Override
    public String toString(){

        if(pieceColor == ChessGame.TeamColor.WHITE){

            return "K";

        }

        else {

            return "k";

        }

    }

}
