package ui;


// maybe use this class to try and help manage the number of lines in ChessClient?

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;

import static chess.ChessGame.TeamColor.*;
import static ui.ClientState.*;
import static ui.EscapeSequences.*;

public class CommandHelper {


    public static int makeRowPositionANumber(String rowPosition) throws Exception{

        try{

            int positionAsNumber = Integer.parseInt(rowPosition);

            if (positionAsNumber > 8 || positionAsNumber < 1){

                throw new Exception("Error: Did not input a valid number for the desired piece's row");

            }

            return positionAsNumber;

        }

        catch (Exception exception){

            throw new Exception("Error: Did not input a valid number for the desired piece's row");

        }

    }


    public static int makeColumnLetterANumber(String letter) throws Exception{

        switch(letter.toLowerCase()){

            case "a":

                return 1;

            case "b":

                return 2;

            case "c":

                return 3;

            case "d":

                return 4;

            case "e":

                return 5;

            case "f":

                return 6;

            case "g":

                return 7;

            case "h":

                return 8;

            default:

                throw new Exception("Error: Did not input a valid letter for the desired piece's column");

        }

    }


    public static ChessPiece.PieceType getPromotionInput(String inputToken) throws Exception{

        switch (inputToken.toLowerCase()){

            case "rook":

                return ChessPiece.PieceType.ROOK;

            case "bishop":

                return ChessPiece.PieceType.BISHOP;

            case "knight":

                return ChessPiece.PieceType.KNIGHT;

            case "queen":

                return ChessPiece.PieceType.QUEEN;

            default:

                throw new Exception("Error: Input an invalid promotion piece type. Please try again.");

        }

    }


    public static String getHelpMenu(ClientState currentState){

        switch(currentState){

            case LOGGEDOUT:
                return  "register <USERNAME> <PASSWORD> <EMAIL> - create an account\n" +
                        "login <USERNAME> <PASSWORD> - log registered user in\n" +
                        "quit - exit the program\n" +
                        "help - display a list of available commands";

            case LOGGEDIN:
                return  "create <NAME> - create a new game\n" +
                        "join <gameID> [WHITE|BLACK] - join a game as the specified team\n" +
                        "observe <gameID> - watch a game in progress\n" +
                        "list - display a list of chess games\n" +
                        "logout - log out of the chess client\n" +
                        "help - display a list of available commands";

            case INGAME:

                return  "redraw - redraws the chess board\n" +
                        "make_move <START POSITION> <END POSITION> <PROMOTION PIECE?> - " +
                        "move a piece from the starting position to the ending position if the move is valid\n" +
                        "highlight_moves <PIECE POSITION> - Highlights all the legal spaces that a specific piece can move to\n" +
                        "leave - leaves the current chess game\n" +
                        "resign - forfeits the game, but does not leave the game\n" +
                        "help - display a list of available commands\n\n" +
                        "Please note that for any piece position, you will need to format it as follows: <LETTER> <NUMBER>\n" +
                        "For example:  'make_move A 2 A 3' will move the piece at A, 2 to the space at A, 3.";

            case OBSERVINGGAME:

                return  "redraw - redraws the chess board\n" +
                        "highlight_moves <PIECE POSITION> - Highlights all the legal spaces that a specific piece can move to\n" +
                        "leave - stop observing the current chess game\n" +
                        "help - display a list of available commands";

            default:

                return "Error: There was an error calling the help command";

        }

    }


    // It's very possible that this ends up throwing a null exception error right here due to null not being empty
    public static String displayBoard(ChessBoard displayedBoard, ChessGame.TeamColor displaySide){

        if (displaySide == null){

            displaySide = BLACK;

        }

        return displayBoard(displayedBoard, displaySide, null);

    }


    public static String displayBoard(ChessBoard displayedBoard, ChessGame.TeamColor displaySide, ArrayList<ChessPosition> locationsToHighlight){

        boolean colorSwitcher = true;
        int incrementer;
        int initialRow;
        int initialColumn;

        if (displaySide == WHITE){

            // In this case, you start with black at the top, resulting in incrementing numerically downwards for the row.
            incrementer = -1;
            initialRow = 8;
            initialColumn = 1;

        }

        else{

            // In this case, you start with white at the top, resulting in incrementing numerically upwards for the row.
            incrementer = 1;
            initialRow = 1;
            initialColumn = 8;

        }

        String topAndBottom = firstAndLastRow(incrementer, initialRow);

        String entireBoard = topAndBottom + "\n";

        for (int i = initialRow; i >= 1 && i <= 8; i = i + incrementer){ // Increments through rows

            entireBoard = entireBoard + getCharacterSpacing((char)('0' + i));

            for (int j = initialColumn; j >= 1 && j <= 8; j = j - incrementer){ // Increments through columns

                ChessPosition specificPiece = new ChessPosition (i, j);
                ChessPiece currentPiece = displayedBoard.getPiece(specificPiece);

                if (currentPiece != null){


                    if (locationsToHighlight != null && locationsToHighlight.isEmpty() == false
                            && locationsToHighlight.contains(specificPiece)){

                        entireBoard = entireBoard + getPieceIcon(currentPiece, colorSwitcher, currentPiece.getTeamColor(), true);

                    }

                    else{

                        entireBoard = entireBoard + getPieceIcon(currentPiece, colorSwitcher, currentPiece.getTeamColor(), false);

                    }

                }

                else{

                    if (locationsToHighlight != null && locationsToHighlight.isEmpty() == false
                            && locationsToHighlight.contains(specificPiece)){

                        entireBoard = entireBoard + getPieceIcon(null, colorSwitcher, displaySide, true);

                    }

                    else{

                        entireBoard = entireBoard + getPieceIcon(null, colorSwitcher, displaySide, false);

                    }

                }

                if (colorSwitcher == true){
                    colorSwitcher = false;
                }

                else{
                    colorSwitcher = true;
                }

            }

            entireBoard = entireBoard + getCharacterSpacing((char)('0' + i)) + RESET_BG_COLOR + "\n";

            if (colorSwitcher == true){
                colorSwitcher = false;
            }

            else{
                colorSwitcher = true;
            }

        }

        entireBoard = entireBoard + topAndBottom;
        return entireBoard;

    }


    // Returns the labels for the top and bottom rows of the chess board
    public static String firstAndLastRow(int incrementer, int initialRow){

        String returnRow = SET_BG_COLOR_LIGHT_GREY + EMPTY;

        for (int i = initialRow; i >= 1 && i <=8; i = i + incrementer){

            returnRow = returnRow + determineLetter(i);

        }

        returnRow = returnRow + EMPTY;

        return returnRow + RESET_BG_COLOR + RESET_TEXT_COLOR;

    }


    // Gets the proper background color, piece icon, and piece icon, and piece coloration for a specific space
    public static String getPieceIcon(ChessPiece currentPiece, boolean colorSwitcher, ChessGame.TeamColor currentTeam, boolean isHighlighted){

        String backgroundColor;

        if (colorSwitcher == true){

            if (isHighlighted == false){
                backgroundColor = SET_BG_COLOR_WHITE;
            }
            else{
                backgroundColor = SET_BG_COLOR_LIGHT_RED;
            }

        }

        else{

            if (isHighlighted == false){
                backgroundColor = SET_BG_COLOR_DARK_GREEN;
            }
            else{
                backgroundColor = SET_BG_COLOR_DARK_RED;
            }

        }

        if (currentPiece == null){
            // This is where it's returning the space that's too big, I suspect
            return backgroundColor + EMPTY;
        }

        String pieceColor;

        if (currentTeam == WHITE){
            pieceColor = SET_TEXT_COLOR_BLUE;
        }
        else{
            pieceColor = SET_TEXT_COLOR_BLACK;
        }

        switch(currentPiece.getPieceType()) {

            case KING:

                if (currentTeam == WHITE){
                    return backgroundColor + pieceColor + WHITE_KING;
                }
                else{
                    return backgroundColor + pieceColor + BLACK_KING;
                }

            case QUEEN:

                if (currentTeam == WHITE){
                    return backgroundColor + pieceColor + WHITE_QUEEN;
                }
                else{
                    return backgroundColor + pieceColor + BLACK_QUEEN;
                }

            case BISHOP:

                if (currentTeam == WHITE){
                    return backgroundColor + pieceColor + WHITE_BISHOP;
                }
                else{
                    return backgroundColor + pieceColor + BLACK_BISHOP;
                }

            case KNIGHT:

                if (currentTeam == WHITE){
                    return backgroundColor + pieceColor + WHITE_KNIGHT;
                }
                else{
                    return backgroundColor + pieceColor + BLACK_KNIGHT;
                }

            case ROOK:

                if (currentTeam == WHITE){
                    return backgroundColor + pieceColor + WHITE_ROOK;
                }
                else{
                    return backgroundColor + pieceColor + BLACK_ROOK;
                }

            case PAWN:

                if (currentTeam == WHITE){
                    return backgroundColor + pieceColor + WHITE_PAWN;
                }
                else{
                    return backgroundColor + pieceColor + BLACK_PAWN;
                }

        }
        return null;
    }


    // Takes a number and turns it into the corresponding column label
    public static String determineLetter(int input){

        char letter;

        switch(input){

            case 8:

                letter = 'A';

                break;

            case 7:

                letter = 'B';

                break;

            case 6:

                letter = 'C';

                break;

            case 5:

                letter = 'D';

                break;

            case 4:

                letter = 'E';

                break;

            case 3:

                letter = 'F';

                break;

            case 2:

                letter = 'G';

                break;

            case 1:

                letter = 'H';

                break;

            default:

                return EMPTY;

        }

        return getCharacterSpacing(letter);

    }


    public static String getCharacterSpacing(char input){

        return SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + PERIOD_SPACE + WEIRD_SPACE + input + WEIRD_SPACE + PERIOD_SPACE;

    }

}
