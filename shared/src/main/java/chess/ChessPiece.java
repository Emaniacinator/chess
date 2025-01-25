package chess;

import chess.ChessPieces.*;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    protected ChessGame.TeamColor pieceColor;
    protected ChessPiece.PieceType type;
    protected boolean hasMoved;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, boolean hasMoved) {

        this.pieceColor = pieceColor;
        this.type = type;
        this.hasMoved = hasMoved;

    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        this.pieceColor = pieceColor;
        this.type = type;
        this.hasMoved = false;

    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }


    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;

    }


    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;

    }


    // In the coding test, this might all just be better implemented as a set of helper functions instead of as
    // a bunch of individual inherited classes
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece grabbedPiece = board.getPiece(myPosition);
        PieceType currentPieceType = grabbedPiece.getPieceType();

        switch (currentPieceType) {

            case KING:
                King castToKing = new King(grabbedPiece.pieceColor, currentPieceType, myPosition, grabbedPiece.hasMoved);
                return castToKing.pieceMoves(board, myPosition);

            case QUEEN:
                Queen castToQueen = new Queen(grabbedPiece.pieceColor, currentPieceType, myPosition);
                return castToQueen.pieceMoves(board, myPosition);

            case BISHOP:
                Bishop castToBishop = new Bishop(grabbedPiece.pieceColor, currentPieceType, myPosition);
                return castToBishop.pieceMoves(board, myPosition);

            case KNIGHT:
                Knight castToKnight = new Knight(grabbedPiece.pieceColor, currentPieceType, myPosition);
                return castToKnight.pieceMoves(board, myPosition);

            case ROOK:
                Rook castToRook = new Rook(grabbedPiece.pieceColor, currentPieceType, myPosition, grabbedPiece.hasMoved);
                return castToRook.pieceMoves(board, myPosition);

            case PAWN:
                Pawn castToPawn = new Pawn(grabbedPiece.pieceColor, currentPieceType, myPosition, grabbedPiece.hasMoved);
                return castToPawn.pieceMoves(board, myPosition);
        }

        return null;

    }


    @Override
    public String toString() {

        switch(pieceColor){

            case BLACK:

                switch(type){

                    case KING:
                        return "k";

                    case QUEEN:
                        return "q";

                    case BISHOP:
                        return "b";

                    case KNIGHT:
                        return "n";

                    case ROOK:
                        return "r";

                    case PAWN:
                        return "p";

                }
                break;

            case WHITE:

                switch(type){

                    case KING:
                        return "K";

                    case QUEEN:
                        return "Q";

                    case BISHOP:
                        return "B";

                    case KNIGHT:
                        return "N";

                    case ROOK:
                        return "R";

                    case PAWN:
                        return "P";


                }
                break;

        }

        return null;

    }

}
