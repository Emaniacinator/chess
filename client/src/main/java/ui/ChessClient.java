package ui;

import chess.ChessBoard;
import chess.ChessPiece;

import static ui.ClientState.*;

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


    public String displayBoard(ChessBoard displayedBoard){

        return null;

    }


    public String getPieceIcon(ChessPiece currentPiece){

        switch(currentPiece.getPieceType()) {

            case KING:
                break;

            case QUEEN:
                break;

            case BISHOP:
                break;

            case KNIGHT:
                break;

            case ROOK:
                break;

            case PAWN:
                break;

        }

        return null;

    }

}
