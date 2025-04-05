package ui;

import chess.*;
import chess.model.AuthData;
import chess.model.GameData;
import server.GameList;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Collection;

import static chess.ChessGame.TeamColor.*;
import static ui.ClientState.*;
import static ui.EscapeSequences.*;

public class ChessClient {

    private ClientState currentState = LOGGEDOUT;
    private final String serverURL;
    private final ServerFacade serverFacade;
    private AuthData clientAuthData = null;
    private GameData userSideGameData;
    private ChessGame.TeamColor userSideTeamColor;

    error make an eror here so you look
    // You need to make sure that the userSideGameData gets updated EVERY time new data is received or sent
    // You haven't yet implemented this part into everything you already wrote yet

    public ChessClient(String serverURL){

        this.serverURL = serverURL;
        serverFacade = new ServerFacade(serverURL);

    }


    public ClientState returnState(){

        return currentState;

    }


    public String determineTakenAction(String inputCommand) throws Exception{

        return determineTakenAction(inputCommand, null);

    }



    public String determineTakenAction(String inputCommand, String[] otherTokens) throws Exception{

        switch (inputCommand.toLowerCase()){

            case "help":

                return helpCommand(otherTokens);

            case "quit":

                return quitCommand(otherTokens);

            case "login":

                return loginCommand(otherTokens);

            case "register":

                return registerCommand(otherTokens);

            case "logout":

                return logoutCommand(otherTokens);

            case "create":

                return createCommand(otherTokens);

            case "join": // Implement launching the Websocket connection

                return joinCommand(otherTokens);

            case "observe": // Implement launching the Websocket connection

                return observeCommand(otherTokens);

            case "list":

                return listCommand(otherTokens);

            // You may want to make this actually read from the server later in case the local board
            // is not being effectively updated by the Websocket, actually.
            // Or just have the ChessClient update the board every time a new websocket board state is received lol
            case "redraw":

                if (currentState == INGAME){

                    return redrawCommand(otherTokens, userSideTeamColor);

                }

                else{

                    return redrawCommand(otherTokens);

                }

            // Make sure that this checks that the user is only able to move if it's thier turn FIRST.
            // Similarly, make sure that the user can't call this function unless they are actually in the game and NOT AN OBSERVER
            // After both of those, check to make sure that the right number of inputs are being read.
            case "make_move":

                return makeMoveCommand();

            // In theory, this is fully implemented. But there's a lot going on so something DEFINITELY could have broken
            case "highlight_moves":

                return highlightMovesCommand(otherTokens);

            case "leave":

                return leaveCommand();

            case "resign":

                return resignCommand();

        }

        return "Error: Unexpected input received, please try again. Type 'help' for a list of commands";

    }


    public String helpCommand(String[] otherTokens) throws Exception{

        if (otherTokens != null){

            throw new Exception("Error: 'help' doesn't accept any additional inputs. Please try again.");

        }

        return getHelpMenu();

    }


    public String quitCommand(String[] otherTokens) throws Exception{

        if (currentState != LOGGEDOUT){

            throw new Exception("Error: Please log out before attempting to quit the chess client.");

        }

        if (otherTokens != null){

            throw new Exception("Error: 'quit' doesn't accept any additional inputs. Please try again.");

        }

        return "Quitting the program";

    }


    public String loginCommand(String[] otherTokens) throws Exception{

        if (currentState != LOGGEDOUT){

            throw new Exception("Error: You are already logged in. Type 'help' for a list of commands.");

        }

        if (otherTokens.length != 2){

            throw new Exception("Error: 'login' only accepts exactly 2 inputs. Please try again.");

        }

        clientAuthData = serverFacade.loginUser(otherTokens);

        currentState = LOGGEDIN;

        return "Logged in the user " + clientAuthData.username() + ".";

    }


    public String registerCommand(String[] otherTokens) throws Exception{

        if (currentState != LOGGEDOUT){

            throw new Exception("Error: Already logged in, can't register a new user. Type 'help' for a list of commands.");

        }

        if (otherTokens.length != 3){

            throw new Exception("Error: 'register' only accepts exactly 3 inputs. Please try again.");

        }

        clientAuthData = serverFacade.registerUser(otherTokens);

        currentState = LOGGEDIN;

        return "Registered and logged in the new user " + clientAuthData.username() + ".";

    }


    public String logoutCommand(String[] otherTokens) throws Exception{

        if (otherTokens != null){

            throw new Exception("Error: 'logout' doesn't accept any additional inputs. Please try again.");

        }

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: You are already logged out. Type 'help' for a list of commands.");

        }

        if (currentState != LOGGEDIN){

            throw new Exception("Error: Please leave the game before attempting to log out. Type 'help' for a list of commands.");

        }

        serverFacade.logoutUser(clientAuthData);

        String loggedOutUser = clientAuthData.username();

        currentState = LOGGEDOUT;

        clientAuthData = null;

        return "Logged out the user " + loggedOutUser + ".";

    }


    public String createCommand(String[] otherTokens) throws Exception{

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: Not logged in, can't create game. Type 'help' for a list of commands.");

        }

        if (currentState != LOGGEDIN){

            throw new Exception("Error: Please leave the game before making a new one. Type 'help' for a list of commands.");

        }

        if (otherTokens.length != 1){

            throw new Exception("Error: 'create' only accepts exactly 1 input. Please try again.");

        }

        serverFacade.createGame(otherTokens, clientAuthData);

        return "Created the game " + otherTokens[0] + ".";

    }


    // Implement launching the Websocket connection
    public String joinCommand(String[] otherTokens) throws Exception{

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: Not logged in, can't join game. Type 'help' for a list of commands.");

        }

        if (currentState != LOGGEDIN){

            throw new Exception("Error: Can't join more than 1 game. Please leave current game before continuing.");

        }

        if (otherTokens.length != 2){

            throw new Exception("Error: 'join' only accepts exactly 2 inputs. Please try again.");

        }

        try{

            Integer.parseInt(otherTokens[0]);

        }

        catch(Exception exception){

            throw new Exception("Error: input gameID value is not a valid number. Please try again.");

        }

        if (!otherTokens[1].toUpperCase().equals("WHITE") && !otherTokens[1].toUpperCase().equals("BLACK")){

            throw new Exception("Error: Can't join game without a valid team color. Please try again.");

        }

        userSideGameData = serverFacade.joinGame(otherTokens, clientAuthData);

        userSideTeamColor = ChessGame.TeamColor.valueOf(otherTokens[1].toUpperCase());

        currentState = INGAME;

        return displayBoard(userSideGameData.game().getBoard(), userSideTeamColor);

    }


    // Implement launching the Websocket connection
    public String observeCommand(String[] otherTokens) throws Exception{

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: Not logged in, can't observe game. Type 'help' for a list of commands.");

        }

        if (currentState != LOGGEDIN){

            throw new Exception("Error: Can't join more than 1 game. Please leave current game before continuing.");

        }

        if (otherTokens.length != 1){

            throw new Exception("Error: 'observe' only accepts exactly 1 input. Please try again.");

        }

        try{

            Integer.parseInt(otherTokens[0]);

        }

        catch(Exception exception){

            throw new Exception("Error: input gameID value is not a valid number. Please try again.");

        }

        userSideGameData = serverFacade.observeGame(otherTokens, clientAuthData);

        currentState = OBSERVINGGAME;

        return displayBoard(userSideGameData.game().getBoard(), ChessGame.TeamColor.WHITE);

    }


    public String listCommand(String[] otherTokens) throws Exception{

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: Not logged in, can't list games. Type 'help' for a list of commands.");

        }

        if (currentState != LOGGEDIN){

            throw new Exception("Error: Can't list games while in a game. Please leave current game before continuing.");

        }

        GameList returnedList = serverFacade.listGames(clientAuthData);

        String listOfGames = "Here is a list of the games. They will be formatted as follows:\n" +
                "Game ID - Game Name - White Player - Black Player \n\n";

        for (GameData currentGame : returnedList.games()){

            listOfGames = listOfGames + currentGame.gameID() + " - " + currentGame.gameName() + " - " +
                    currentGame.whiteUsername() + " - " + currentGame.blackUsername() + "\n";

        }

        return listOfGames;

    }


    public String redrawCommand(String[] otherTokens) throws Exception{

        // Create an error for the wrong number of inputs

        if (currentState == LOGGEDOUT){

            return "Error: Please log in and join a game before attempting to redraw the board. Type 'help' for a list of commands.";

        }

        if (currentState != OBSERVINGGAME){

            return "Error: You must be in a game to redraw the board. Type 'help' for a list of commands.";

        }

        return redrawCommand(otherTokens, ChessGame.TeamColor.WHITE);

    }


    public String redrawCommand(String[] otherTokens, ChessGame.TeamColor teamColor) throws Exception{

        // Create an error for the wrong number of inputs

        return displayBoard(userSideGameData.game().getBoard(), teamColor);

    }


    // Put the makeMovesCommand here later instead of at the bottom so that you don't go crazy and your
    // organization keeps working as expected.

    // Okay, there's a lot here so it totally could have broken something :(
    public String highlightMovesCommand(String[] otherTokens) throws Exception{

        // Create an error for the wrong number of inputs

        if (otherTokens.length != 2){

            throw new Exception ("Error: 'highlight_moves' only accepts exactly 2 inputs. Please try again.");

        }

        // turn the otherTokens into a ChessPosition

        int columnPosition = makeColumnLetterANumber(otherTokens[0]);

        int rowPosition = makeRowPositionANumber(otherTokens[1]);

        ChessPosition highlightedPiecePosition = new ChessPosition(rowPosition, columnPosition);

        // See if there is a piece there by making a ChessPiece item.

        ChessBoard currentBoard = userSideGameData.game().getBoard();

        if (currentBoard == null){

            throw new Exception("Error: How the heck did you get here? Anyways, there is no chess board *shrug emoji*");

        }

        ChessPiece highlightedPiece = currentBoard.getPiece(highlightedPiecePosition);

        // If there is no piece there, return an error saying that there is no piece there.

        if (highlightedPiece == null){

            throw new Exception("Error: No piece at the designated location. Please try again.");

        }

        // Have the ChessPiece return all possible moves that it can make.

        Collection<ChessMove> possiblePieceMoves = highlightedPiece.pieceMoves(currentBoard, highlightedPiecePosition);

        // If there are no possible moves, just return a note that says "No possible moves for that piece"

        if (possiblePieceMoves.isEmpty()){

            return "There are no possible moves for that piece.";

        }

        // Then convert the collection into an array of ending ChessPositions for simplicity here

        ChessPosition[] highlightedPositions = new ChessPosition[possiblePieceMoves.size() + 1];

        highlightedPositions[0] = highlightedPiecePosition;

        int positionHelper = 1;

        for (ChessMove convertToPosition : possiblePieceMoves){

            highlightedPositions[positionHelper] = convertToPosition.getEndPosition();
            positionHelper++;

        }

        // Otherwise pass them into the displayBoard method as an override and have it the background color be shades of red.
        // instead of the usual color if it matches one of the spaces in the list of possible moves for the ChessPiece.

        return displayBoard(currentBoard, userSideTeamColor, highlightedPositions);

    }


    private int makeRowPositionANumber(String rowPosition) throws Exception{

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


    private int makeColumnLetterANumber(String letter) throws Exception{

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


    public String getHelpMenu(){

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

                return  "redraw - redraws the chess board\n" + // This function will make the LOAD_GAME websocket (html? SQL?) call, then display the board
                        "make_move <START POSITION> <END POSITION> - move a piece from the starting position to the ending position if the move is valid\n" +
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

        }

        return null;

    }


    // It's very possible that this ends up throwing a null exception error right here due to null not being empty, depending on how the isEmpty function works
    public String displayBoard(ChessBoard displayedBoard, ChessGame.TeamColor displaySide){

        return displayBoard(displayedBoard, displaySide, null);

    }


    public String displayBoard(ChessBoard displayedBoard, ChessGame.TeamColor displaySide, ChessPosition[] locationsToHighlight){

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

                    if (Arrays.asList(locationsToHighlight).isEmpty() == false && Arrays.asList(locationsToHighlight).contains(specificPiece)){

                        entireBoard = entireBoard + getPieceIcon(currentPiece, colorSwitcher, currentPiece.getTeamColor(), true);

                    }

                    else{

                        entireBoard = entireBoard + getPieceIcon(currentPiece, colorSwitcher, currentPiece.getTeamColor(), false);

                    }

                }

                // This might be where the weird board spacing is coming in
                else{

                    if (Arrays.asList(locationsToHighlight).isEmpty() == false && Arrays.asList(locationsToHighlight).contains(specificPiece)){

                        entireBoard = entireBoard + getPieceIcon(currentPiece, colorSwitcher, displaySide, true);

                    }

                    else{

                        entireBoard = entireBoard + getPieceIcon(currentPiece, colorSwitcher, displaySide, false);

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
    public String firstAndLastRow(int incrementer, int initialRow){

        String returnRow = SET_BG_COLOR_LIGHT_GREY + EMPTY;

        for (int i = initialRow; i >= 1 && i <=8; i = i + incrementer){

            returnRow = returnRow + determineLetter(i);

        }

        returnRow = returnRow + EMPTY;

        return returnRow + RESET_BG_COLOR + RESET_TEXT_COLOR;

    }


    // Gets the proper background color, piece icon, and piece icon, and piece coloration for a specific space
    public String getPieceIcon(ChessPiece currentPiece, boolean colorSwitcher, ChessGame.TeamColor currentTeam, boolean isHighlighted){

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

            // This is where it's returning the space that's too small, I suspect
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


    public String getCharacterSpacing(char input){

        return SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + PERIOD_SPACE + WEIRD_SPACE + input + WEIRD_SPACE + PERIOD_SPACE;

    }

}
