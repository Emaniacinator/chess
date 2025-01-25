package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;

    public ChessPosition(int row, int col) {

        this.row = row;
        this.col = col;

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        
        return row - 1;

    }

    /**
     * @return which column this position is in
     * 1 codes for the left column.
     */
    public int getColumn() {

        return col - 1;

    }

    @Override
    public String toString() {

        char columnPosition = ' ';
        int rowPosition = this.getRow() + 1;

        switch(this.getColumn()){

            case 0:
                columnPosition = 'A';
                break;

            case 1:
                columnPosition = 'B';
                break;

            case 2:
                columnPosition = 'C';
                break;

            case 3:
                columnPosition = 'D';
                break;

            case 4:
                columnPosition = 'E';
                break;

            case 5:
                columnPosition = 'F';
                break;

            case 6:
                columnPosition = 'G';
                break;

            case 7:
                columnPosition = 'H';
                break;

        }

        return columnPosition + ", " + rowPosition;

    }

    @Override
    public boolean equals(Object obj) {

        if (obj.getClass() != ChessPosition.class) {

            return false;

        }

        ChessPosition other = (ChessPosition) obj;

        return this.getRow() == other.getRow() && this.getColumn() == other.getColumn();

    }

    @Override
    public int hashCode() {

        return this.toString().hashCode() * (this.getRow() + 1) * this.getColumn();

    }

}
