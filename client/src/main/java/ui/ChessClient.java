package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import static chess.ChessGame.TeamColor.WHITE;
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


    public String displayBoard(/*ChessBoard displayedBoard, TeamColor displaySide*/){

        return SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + EMPTY + HELPER_SPACE + "A" + HELPER_SPACE + "B   C  D   E   F  G  H " + EMPTY + RESET_BG_COLOR + "\n" +
                SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_BG_COLOR_DARK_GREEN + WHITE_KING + SET_BG_COLOR_WHITE + WHITE_KNIGHT + SET_BG_COLOR_DARK_GREEN + EMPTY + SET_BG_COLOR_WHITE + EMPTY + SET_BG_COLOR_DARK_GREEN + EMPTY + SET_BG_COLOR_WHITE + EMPTY + SET_BG_COLOR_DARK_GREEN + EMPTY + SET_BG_COLOR_WHITE + EMPTY + SET_BG_COLOR_LIGHT_GREY + EMPTY + RESET_BG_COLOR;

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

        String pieceColor;

        if (currentTeam == WHITE){

            pieceColor = SET_TEXT_COLOR_MAGENTA;

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


    // Get the proper spacing for a character to label rows and columns on the board.
    public String getCharacterSpacing(Character input){

        return SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + PERIOD_SPACE + WEIRD_SPACE + input + WEIRD_SPACE + PERIOD_SPACE;

    }

}
