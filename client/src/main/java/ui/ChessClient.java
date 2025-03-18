package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.ChessGame.TeamColor.*;
import static ui.ClientState.*;
import static ui.EscapeSequences.*;

public class ChessClient {

    private ClientState currentState = LOGGEDOUT;

    public void determineMenuActions(){

        switch(currentState){

            case LOGGEDOUT:
                break;

            case LOGGEDIN:
                break;

            case GAMESELECT:
                break;

            case INGAME:
                break;

            case OBSERVINGGAME:
                break;

        }

    }


    public String getHelpMenu(){

        switch(currentState){

            case LOGGEDOUT:
                return "register <USERNAME> <PASSWORD> <EMAIL> - create an account\n" +
                        "login <USERNAME> <PASSWORD> - log registered user in\n" +
                        "quit - exit the program\n" +
                        "help - display a list of available commands";

            case LOGGEDIN:
                return "create <NAME> - create a new game\n" +
                        "join <ID> [WHITE|BLACK] - join a game as the specified team\n" +
                        "observe <ID> - watch a game in progress\n" +
                        "list - display a list of chess games\n" +
                        "logout - log out of the chess client\n" +
                        "help - display a list of available commands";

            case GAMESELECT:
                break;

            case INGAME:
                break;

            case OBSERVINGGAME:
                break;

        }

        return null;

    }


    public String displayBoard(ChessBoard displayedBoard, ChessGame.TeamColor displaySide){

        boolean colorSwitcher = true;
        int incrementer;
        int initialRow;
        if (displaySide == WHITE){

            // In this case, you start with black at the top, resulting in incrementing numerically downwards for the row.
            incrementer = -1;
            initialRow = 8;

        }

        else{

            // In this case, you start with white at the top, resulting in incrementing numerically upwards for the row.
            incrementer = 1;
            initialRow = 1;

        }

        String topAndBottom = firstAndLastRow(incrementer, initialRow);

        String entireBoard = topAndBottom + "\n";

        for (int i = initialRow; i >= 1 && i <= 8; i = i + incrementer){ // Increments through rows

            entireBoard = entireBoard + getCharacterSpacing((char)('0' + i));

            for (int j = initialRow; j >= 1 && j <= 8; j = j + incrementer){ // Increments through columns

                ChessPosition specificPiece = new ChessPosition (i, j);
                ChessPiece currentPiece = displayedBoard.getPiece(specificPiece);

                if (currentPiece != null){

                    entireBoard = entireBoard + getPieceIcon(currentPiece, colorSwitcher, currentPiece.getTeamColor());

                }

                else{

                    entireBoard = entireBoard + getPieceIcon(currentPiece, colorSwitcher, displaySide);

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
    public String firstAndLastRow(int incrementer, int initialRow){

        String returnRow = SET_BG_COLOR_LIGHT_GREY + EMPTY;

        for (int i = initialRow; i >= 1 && i <=8; i = i + incrementer){

            returnRow = returnRow + determineLetter(i);

        }

        returnRow = returnRow + EMPTY;

        return returnRow + RESET_BG_COLOR;

    }


    // Gets the proper background color, piece icon, and piece icon, and piece coloration for a specific space
    public String getPieceIcon(ChessPiece currentPiece, boolean colorSwitcher, ChessGame.TeamColor currentTeam){

        String backgroundColor;

        if (colorSwitcher == true){

            backgroundColor = SET_BG_COLOR_WHITE;

        }

        else{

            backgroundColor = SET_BG_COLOR_DARK_GREEN;

        }

        if (currentPiece == null){

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
    public String determineLetter(int input){

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


    // Get the proper spacing for a character to label rows and columns on the board.
    public String getCharacterSpacing(char input){

        return SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + PERIOD_SPACE + WEIRD_SPACE + input + WEIRD_SPACE + PERIOD_SPACE;

    }

}
