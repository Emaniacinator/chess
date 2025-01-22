package chess;

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

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        currentBoard = new ChessPiece[8][8];

        for(int i = 0; i < 8; i++){

            // This next bit is mad because I'm passing in the PAWN enum when it actually wants a complete
            // chess piece class. I'll need to pass in the actual pieces here in a moment.

            ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

            addPiece(new ChessPosition (1, i), blackPawn); // How does the position value work? I think this is how, but I'm not sure
            addPiece(new ChessPosition (6, i), whitePawn);

            switch (i){

                case 0: // In both of these cases, we want to create and place the rooks.
                case 7:
                    ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                    ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                    addPiece(new ChessPosition (0, i), blackRook);
                    addPiece(new ChessPosition (7, i), whiteRook);
                    break;

                case 1: // In both of these cases, we want to create and place the knights
                case 6:
                    ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                    ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                    addPiece(new ChessPosition(0, i), blackKnight);
                    addPiece(new ChessPosition(7, i), whiteKnight);
                    break;

                case 2: // In both of these cases, we want to create and place the bishops
                case 5:
                    ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                    ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                    addPiece(new ChessPosition(0, i), blackBishop);
                    addPiece(new ChessPosition(7, i), whiteBishop);
                    break;

                case 3: // In this case, we want to create and place the queens
                    ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                    ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                    addPiece(new ChessPosition(0, i), blackQueen);
                    addPiece(new ChessPosition(7, i), whiteQueen);
                    break;

                case 4: // In this case, we want to create and place the kings
                    ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                    ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                    addPiece(new ChessPosition(0, i), blackKing);
                    addPiece(new ChessPosition(7, i), whiteKing);
                    break;

            }

        }

    }

}
