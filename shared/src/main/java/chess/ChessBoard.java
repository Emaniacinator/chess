package chess;

import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessPiece[][] currentBoard = new ChessPiece[8][8];

    public ChessBoard() {
        
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        currentBoard[position.getRow()][position.getColumn()] = piece;

    }


    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        /*  This code seemed redundant because if the area is null, the return will be null anyways.

        if (currentBoard[position.getRow()][position.getColumn()] == null){

            return null;

        }

        */

        return currentBoard[position.getRow()][position.getColumn()];

    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public String toString() {

        String currentBoardString = "";

        for (int i = 0; i < 8; i++){

            for (int j = 0; j < 8; j++){

                // Here we're going to get the piece at each location. Then we're going to
                // grab the visual representation of the variable and attach it to the string.

                ChessPosition positionToCheck = new ChessPosition(i+ 1, j + 1);

                if (getPiece(positionToCheck) != null){
                    currentBoardString += "|" + getPiece(positionToCheck).toString();
                }

                else{
                    currentBoardString += "| ";
                }

            }

            currentBoardString = currentBoardString + "|\n";

        }

        return currentBoardString;

    }


    @Override
    public boolean equals(Object obj) {

        if (obj.getClass() == this.getClass()){

            ChessBoard other = (ChessBoard) obj;

            return Objects.equals(other.toString(), this.toString());

        }

        return false;

    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        currentBoard = new ChessPiece[8][8];

        for(int i = 1; i < 9; i++){

            // This next bit is mad because I'm passing in the PAWN enum when it actually wants a complete
            // chess piece class. I'll need to pass in the actual pieces here in a moment.

            chess.ChessPieces.Pawn blackPawn = new chess.ChessPieces.Pawn(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN, false);
            chess.ChessPieces.Pawn whitePawn = new chess.ChessPieces.Pawn(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN, false);

            addPiece(new ChessPosition (7, i), blackPawn); // How does the position value work? I think this is how, but I'm not sure
            addPiece(new ChessPosition (2, i), whitePawn);

            switch (i){

                case 1: // In both of these cases, we want to create and place the rooks.
                case 8:
                    chess.ChessPieces.Rook blackRook = new chess.ChessPieces.Rook(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK, false);
                    chess.ChessPieces.Rook whiteRook = new chess.ChessPieces.Rook(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK, false);
                    addPiece(new ChessPosition (8, i), blackRook);
                    addPiece(new ChessPosition (1, i), whiteRook);
                    break;

                case 2: // In both of these cases, we want to create and place the knights
                case 7:
                    chess.ChessPieces.Knight blackKnight = new chess.ChessPieces.Knight(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                    chess.ChessPieces.Knight whiteKnight = new chess.ChessPieces.Knight(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                    addPiece(new ChessPosition(8, i), blackKnight);
                    addPiece(new ChessPosition(1, i), whiteKnight);
                    break;

                case 3: // In both of these cases, we want to create and place the bishops
                case 6:
                    chess.ChessPieces.Bishop blackBishop = new chess.ChessPieces.Bishop(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                    chess.ChessPieces.Bishop whiteBishop = new chess.ChessPieces.Bishop(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                    addPiece(new ChessPosition(8, i), blackBishop);
                    addPiece(new ChessPosition(1, i), whiteBishop);
                    break;

                case 4: // In this case, we want to create and place the queens
                    chess.ChessPieces.Queen blackQueen = new chess.ChessPieces.Queen(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                    chess.ChessPieces.Queen whiteQueen = new chess.ChessPieces.Queen(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                    addPiece(new ChessPosition(8, i), blackQueen);
                    addPiece(new ChessPosition(1, i), whiteQueen);
                    break;

                case 5: // In this case, we want to create and place the kings
                    chess.ChessPieces.King blackKing = new chess.ChessPieces.King(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING, false);
                    chess.ChessPieces.King whiteKing = new chess.ChessPieces.King(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING, false);
                    addPiece(new ChessPosition(8, i), blackKing);
                    addPiece(new ChessPosition(1, i), whiteKing);
                    break;

            }

        }

    }

}
