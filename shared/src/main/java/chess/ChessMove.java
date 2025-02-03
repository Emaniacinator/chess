package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;


    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {

        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;

    }


    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {

        return startPosition;

    }


    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {

        return endPosition;

    }


    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {

        return promotionPiece;

    }


    @Override
    public String toString() {

        if (this.getPromotionPiece() == null){

            return "Starting Position: " + startPosition.toString() + "    Ending Position: " + endPosition.toString();

        }

        return "Promoted the piece to " + promotionPiece.toString();

    }


    @Override
    public boolean equals(Object o) {

        if (o.getClass() != ChessMove.class) {

            return false;

        }

        ChessMove move = (ChessMove) o;

        return move.toString().equals(this.toString());

    }


    @Override
    public int hashCode() {

        int promotionPieceHasher = 13;

        if (this.getPromotionPiece() != null) {

            promotionPieceHasher += this.getPromotionPiece().hashCode();

        }

        return startPosition.hashCode() * endPosition.hashCode() * promotionPieceHasher;

    }

}
