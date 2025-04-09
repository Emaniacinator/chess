package ui;

import chess.*;
import chess.model.AuthData;
import chess.model.GameData;
import server.GameList;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

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
    private WebsocketFacade websocketFacade;
    private String[] arrayWithGameID;

    // Throw an error if make move is called when this IS NOT null. True = user resigned. False = opponent
    // resigned. When the error is thrown, say if the user won or not. If it is null, allow the move to
    // work as usual. Probably have the users enter observer mode after the opponent resigns to help this
    // to work. Make sure that the error says "you / your opponent has resigned and the game is over".
    private Boolean didUserResign = null;

    private Boolean didYouReadTheNoteBelowThis = false;
    // You need to make sure that the userSideGameData gets updated EVERY time new data is received or sent
    // You haven't yet implemented this part into everything you already wrote yet

    public ChessClient(String serverURL, Repl userRepl) throws Exception{

        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(serverURL);
        this.websocketFacade = new WebsocketFacade(serverURL, userRepl);

    }


    public ClientState returnState(){

        return currentState;

    }


    public String determineTakenAction(String inputCommand) throws Exception{

        return determineTakenAction(inputCommand, null);

    }


    public ChessGame.TeamColor returnUserColor(){

        return this.userSideTeamColor;

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

            case "join":

                joinCommand(otherTokens);

                break;

            case "observe":

                observeCommand(otherTokens);

                break;

            case "list":

                return listCommand(otherTokens);

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

                makeMoveCommand(otherTokens);

                break;


            // In theory, this is fully implemented. But there's a lot going on so something DEFINITELY could have broken
            case "highlight_moves":

                return highlightMovesCommand(otherTokens);

            // Not yet implemented fully

            // Make sure to set the current GameID to -99 AND to disconnect the user from the game AND to set the clientSideTeamColor to null
            case "leave":

                leaveCommand(otherTokens);

                break;

            // Not yet implemented fully
            case "resign":

                resignCommand(otherTokens);

                break;

            default:

                return "Error: Unexpected input received, please try again. Type 'help' for a list of commands";

        }

        return null;

    }


    public String helpCommand(String[] otherTokens) throws Exception{

        if (otherTokens != null && otherTokens.length != 0){

            throw new Exception("Error: 'help' doesn't accept any additional inputs. Please try again.");

        }

        return CommandHelper.getHelpMenu(currentState);

    }


    public String quitCommand(String[] otherTokens) throws Exception{

        if (currentState != LOGGEDOUT){

            throw new Exception("Error: Please log out before attempting to quit the chess client.");

        }

        if (otherTokens != null && otherTokens.length != 0){

            throw new Exception("Error: 'quit' doesn't accept any additional inputs. Please try again.");

        }

        return "Quitting the program";

    }


    public String loginCommand(String[] otherTokens) throws Exception{

        if (currentState != LOGGEDOUT){

            throw new Exception("Error: You are already logged in. Type 'help' for a list of commands.");

        }

        if (otherTokens == null || otherTokens.length != 2){

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

        if (otherTokens == null || otherTokens.length != 3){

            throw new Exception("Error: 'register' only accepts exactly 3 inputs. Please try again.");

        }

        clientAuthData = serverFacade.registerUser(otherTokens);

        currentState = LOGGEDIN;

        return "Registered and logged in the new user " + clientAuthData.username() + ".";

    }


    public String logoutCommand(String[] otherTokens) throws Exception{

        if (otherTokens != null && otherTokens.length != 0){

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

        if (otherTokens == null || otherTokens.length != 1){

            throw new Exception("Error: 'create' only accepts exactly 1 input. Please try again.");

        }

        serverFacade.createGame(otherTokens, clientAuthData);

        return "Created the game " + otherTokens[0] + ".";

    }


    // You made this not return a string anymore since the Server message should really do that instead.
    public void joinCommand(String[] otherTokens) throws Exception{

        int gameIDCheck;

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: Not logged in, can't join game. Type 'help' for a list of commands.");

        }

        if (currentState != LOGGEDIN){

            throw new Exception("Error: Can't join more than 1 game. Please leave current game before continuing.");

        }

        if (otherTokens == null || otherTokens.length != 2){

            throw new Exception("Error: 'join' only accepts exactly 2 inputs. Please try again.");

        }

        try{

            gameIDCheck = Integer.parseInt(otherTokens[0]);

            arrayWithGameID = otherTokens;

        }

        catch(Exception exception){

            throw new Exception("Error: input gameID value is not a valid number. Please try again.");

        }

        if (!otherTokens[1].toUpperCase().equals("WHITE") && !otherTokens[1].toUpperCase().equals("BLACK")){

            throw new Exception("Error: Can't join game without a valid team color. Please try again.");

        }

        userSideTeamColor = ChessGame.TeamColor.valueOf(otherTokens[1].toUpperCase());

        try{

            userSideGameData = serverFacade.joinGame(otherTokens, clientAuthData);

            websocketFacade.joinGame(clientAuthData.authToken(), gameIDCheck, userSideTeamColor, clientAuthData.username());

        }

        catch (Exception exception){

            userSideTeamColor = null;

            throw exception;

        }

        currentState = INGAME;

    }


    // You made this not return a string anymore since the Server message should really do that instead.
    public void observeCommand(String[] otherTokens) throws Exception{

        int observeGameIDCheck;

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: Not logged in, can't observe game. Type 'help' for a list of commands.");

        }

        if (currentState != LOGGEDIN){

            throw new Exception("Error: Can't join more than 1 game. Please leave current game before continuing.");

        }

        if (otherTokens == null || otherTokens.length != 1){

            throw new Exception("Error: 'observe' only accepts exactly 1 input. Please try again.");

        }

        try{

            observeGameIDCheck = Integer.parseInt(otherTokens[0]);

            arrayWithGameID = otherTokens;

        }

        catch(Exception exception){

            throw new Exception("Error: input gameID value is not a valid number. Please try again.");

        }

        websocketFacade.joinGame(clientAuthData.authToken(), observeGameIDCheck, userSideTeamColor, clientAuthData.username());

        userSideGameData = serverFacade.observeGame(otherTokens, clientAuthData);

        currentState = OBSERVINGGAME;

    }


    public String listCommand(String[] otherTokens) throws Exception{

        if (otherTokens != null && otherTokens.length != 0){

            throw new Exception("Error: 'list' does not accept any additional inputs. Please try again.");

        }

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

        if (currentState != OBSERVINGGAME && currentState != INGAME){

            return "Error: You must be in a game to redraw the board. Type 'help' for a list of commands.";

        }

        return redrawCommand(otherTokens, ChessGame.TeamColor.WHITE);

    }


    public String redrawCommand(String[] otherTokens, ChessGame.TeamColor teamColor) throws Exception{

        if (otherTokens != null && otherTokens.length != 0){

            return "Error: 'redraw' doesn't accept any additional inputs. Please try again.";

        }

        ChessBoard currentBoard = serverFacade.observeGame(arrayWithGameID, clientAuthData).game().getBoard();

        return CommandHelper.displayBoard(currentBoard, teamColor);

    }


    // This should be finished, but might have some errors.
    public void makeMoveCommand(String[] otherTokens) throws Exception{

        // Put all the error cases here

        // One for not being logged in

        if (currentState == LOGGEDOUT){

            throw new Exception ("Error: Please log in and join a game before attempting to highlight a piece's moves.");

        }

        // One for not in game / observing

        if (currentState != INGAME && currentState != OBSERVINGGAME){

            throw new Exception ("Error: You must be playing or observing a game to highlight a piece's moves. Type 'help' for a list of commands.");

        }

        // One for wrong number of tokens

        if (otherTokens == null || (otherTokens.length != 4 && otherTokens.length != 5)){

            throw new Exception ("Error: 'make_move' only accepts exactly 4 or 5 inputs. Please try again.");

        }

        ChessBoard currentBoard = serverFacade.observeGame(arrayWithGameID, clientAuthData).game().getBoard();

        int startColumnPosition = CommandHelper.makeColumnLetterANumber(otherTokens[0]);

        int startRowPosition = CommandHelper.makeRowPositionANumber(otherTokens[1]);

        int endColumnPosition = CommandHelper.makeColumnLetterANumber(otherTokens[2]);

        int endRowPosition = CommandHelper.makeRowPositionANumber(otherTokens[3]);

        ChessPiece.PieceType promotes = null;

        if (otherTokens.length == 5){

            promotes = CommandHelper.getPromotionInput(otherTokens[4]);

        }

        ChessPosition startPiecePosition = new ChessPosition(startRowPosition, startColumnPosition);

        ChessPosition endPiecePosition = new ChessPosition(endRowPosition, endColumnPosition);

        ChessPiece pieceToMove = currentBoard.getPiece(startPiecePosition);

        // If there is no piece there, return an error saying that there is no piece there.

        if (pieceToMove == null){

            throw new Exception("Error: No piece at the designated location. Please try again.");

        }

        ChessMove moveToMake = new ChessMove(startPiecePosition, endPiecePosition, promotes);

        websocketFacade.makeMove(clientAuthData.authToken(), Integer.parseInt(arrayWithGameID[0]), moveToMake);

    }

    // Okay, there's a lot here so it totally could have broken something :(
    public String highlightMovesCommand(String[] otherTokens) throws Exception{

        // Create an error for being logged out

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: Please log in and join a game before attempting to highlight a piece's moves.");

        }

        // Create an error for not being in game / observing a game

        if (currentState != INGAME && currentState != OBSERVINGGAME){

            throw new Exception("Error: You must be playing or observing a game to highlight a piece's moves. Type 'help' for a list of commands.");

        }

        // Create an error for the wrong number of inputs

        if (otherTokens == null || otherTokens.length != 2){

            throw new Exception ("Error: 'highlight_moves' only accepts exactly 2 inputs. Please try again.");

        }

        // turn the otherTokens into a ChessPosition

        int columnPosition = CommandHelper.makeColumnLetterANumber(otherTokens[0]);

        int rowPosition = CommandHelper.makeRowPositionANumber(otherTokens[1]);

        ChessPosition highlightedPiecePosition = new ChessPosition(rowPosition, columnPosition);

        // See if there is a piece there by making a ChessPiece item.

        // This might be a super flawed approach, but I also want to say that I *think* it should work so we're trying it.
        // Also, update JUST the game ID when a user joins a new game. Remove the local copy of the game.
        ChessBoard currentBoard = serverFacade.observeGame(arrayWithGameID, clientAuthData).game().getBoard();

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

        return CommandHelper.displayBoard(currentBoard, userSideTeamColor, highlightedPositions);

    }


    public void leaveCommand(String[] otherTokens) throws Exception{

        // Put exception cases here

        // Is logged out

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: You aren't logged in. Type 'help' for a list of commands.");

        }

        // Is not in a game or observing

        if (currentState != INGAME && currentState != OBSERVINGGAME){

            throw new Exception("Error: You are not in a game, so there is no game to leave. Type 'help' for a list of commands.");

        }

        // Has any inputs

        if (otherTokens != null && otherTokens.length != 0){

            throw new Exception("Error: 'leave' does not accept any additional inputs. Please try again.");

        }

        websocketFacade.leaveGame(clientAuthData.authToken(), Integer.parseInt(arrayWithGameID[0]));

        arrayWithGameID = new String[]{"-99"};

        userSideTeamColor = null;

        currentState = LOGGEDIN;

    }


    public void resignCommand(String[] otherTokens) throws Exception{

        // Put exception cases here

        // Is logged out

        if (currentState == LOGGEDOUT){

            throw new Exception("Error: You are currently logged out and cannot resign. Type 'help' for a list of commands.");

        }

        // Is not the player in a game

        if (currentState != INGAME){

            throw new Exception("Error: You cannot resign unless you are a player in a game. Type 'help' for a list of commands.");

        }

        // has any inputs

        if (otherTokens != null && otherTokens.length != 0){

            throw new Exception("Error: 'resign' does not accept any additional inputs. Please try again.");

        }

        // Remember the verification since we need to ask twice

        System.out.println(SET_TEXT_COLOR_GREEN + "Verify: You would like to resign the game. Please type 'true' or 'false'.");

        Scanner readInput = new Scanner(System.in);

        String inputLine = readInput.nextLine();

        // if resign, send command to websocket

        if(inputLine.toLowerCase().equals("true")){

            websocketFacade.resignFromGame(clientAuthData.authToken(), Integer.parseInt(arrayWithGameID[0]));

        }

        // if not resign, output message "chose not to resign"

        else if(inputLine.toLowerCase().equals("false")){

            System.out.println("You chose not to resign");

        }

        // if other input, display that they input an invalid response and the game has not been resigned

        else{

            System.out.println("This was an invalid response. You have not been resigned from the game.");

        }

    }

}
