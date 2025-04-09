package websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.model.AuthData;
import chess.model.GameData;
import com.google.gson.Gson;
import dataaccess.DataAccessFramework;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import services.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebsocketHandler {


    private final DataAccessFramework dataAccess;

    private int mostRecentGameID = -99;

    private String mostRecentUsername;


    public WebsocketHandler(Service services){

        this.dataAccess = services.getDataAccess();

    }


    public Boolean didYouReadTheNoteRightBelowThis = false;
    // You may not need to broadcast the user's own actions back to them since you *can*
    // just return a string to their REPL as an output (UNLESS IT'S A LOAD GAME NOTIFICATION,
    // SINCE THAT AFFECTS MORE VARIABLES SUCH AS THE USER SIDE GAME DATA). Alternatively, the
    // client side stuff can return null or "" when the server sends out a notification from
    // the methods that are used.

    // Note that the ServerMessage is just about the types of data that will be returned to the user.
    // Similarly, the UserGameCommand is just about the types of actions that the user can take.

    private final ConnectionManager connectionManager = new ConnectionManager();


    @OnWebSocketError
    public void onError(Throwable exception){

        System.out.println("Thrown Error:\n" + exception.toString());

        ServerMessage errorMessage = new ServerMessage(ERROR, exception.getMessage());

        try{

            connectionManager.broadcastMessageToSingleUser(mostRecentGameID, mostRecentUsername, errorMessage);

        }

        catch (Exception ex){

            System.out.println("Error: Cannot broadcast message to user correctly. Please look at your code.");

            System.out.println("Here is the error message:\n" + ex.toString());

        }

    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception{

        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);

        switch (userCommand.getCommandType()){

            case CONNECT:

                ChessGame testGame = null;

                try{

                    mostRecentUsername = dataAccess.getAuthData(userCommand.getAuthToken()).username();

                    if (userCommand.getGameID() != null){

                        mostRecentGameID = userCommand.getGameID();

                    }

                    else{

                        throw new Exception("Error: Did not input a game id to connect to. Please try again");

                    }

                    testGame = dataAccess.getGameData(userCommand.getGameID()).game();

                    String userUsername = dataAccess.getAuthData(userCommand.getAuthToken()).username();

                    connectPlayer(mostRecentGameID, mostRecentUsername, session, userCommand.getTeamColor(), testGame);

                }

                catch(Exception missingField){

                    connectForMissingFieldOutput(mostRecentUsername, session);

                }

                break;

            case MAKE_MOVE:

                // Because of how they set up the tests, you'll probably need to run a check to see if the user matches
                // the color of piece that's trying to be moved here. Which is frustrating because I had a different
                // design in mind (and already being built) but they didn't explain this (or the tests) clearly so it's
                // not going to work anymore.

                GameData inputGameData = userCommand.getGameData();

                if (inputGameData != null){

                    makeMove(userCommand.getGameID(), userCommand.getAuthToken(), userCommand.getMoveToMake(), session);

                }

                else {

                    ChessGame inputGame = dataAccess.getGameData(userCommand.getGameID()).game();

                    makeMove(userCommand.getGameID(), userCommand.getAuthToken(), userCommand.getMoveToMake(), session);

                }

                break;

            case LEAVE:

                disconnectUser(userCommand.getGameID(), userCommand.getAuthToken(), session, userCommand.getTeamColor());

                break;

            case RESIGN:

                resign(userCommand.getGameID(), userCommand.getAuthToken(), session);

                break;

            case CONNECT_OBSERVER:

                break;

        }

    }


    private void connectForMissingFieldOutput(String username, Session session){

        connectionManager.addPlayer(-99, username, session, null);

        String messageStringToUser = "Error: Did not input a valid gameID or had a bad authentication token. Please try again.";

        ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, messageStringToUser);

        try{

            connectionManager.broadcastMessageToSingleUser(-99, username, outputMessageToUser);

        }

        catch (Exception unknownError){

            System.out.println("How did you get here? Look at the connectForMissingFieldOutput in WebsocketHandler for troubleshooting.");

        }

        connectionManager.removeConnection(-99, username);

    }


    // You need to program a LOAD_GAME case into the REPL or Client so that this displays correctly. Or else it just won't.
    private void connectPlayer(int gameID, String username, Session session, ChessGame.TeamColor userColor, ChessGame updatedGame) throws Exception{

        connectionManager.addPlayer(gameID, username, session, userColor);

        String messageStringToGame;

        if (userColor != null){

            messageStringToGame = String.format("%s has just joined the game on the %s team", username, userColor);

        }

        else {

            messageStringToGame = String.format("%s has just joined the game as an observer", username);

        }



        ServerMessage outputMessageToGame = new ServerMessage(NOTIFICATION, messageStringToGame);

        // Note that when it does the load game message, it will display the message that I choose, update the client
        // side game board, and then draw the game board as a part of receiving a message with the load game type.
        ServerMessage outputMessageToUser = new ServerMessage(LOAD_GAME, updatedGame);

        connectionManager.broadcastMessageToGame(gameID, username, outputMessageToGame);

        connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);

    }


    private void makeMove(int gameID, String authToken, ChessMove moveToMake, Session session) throws Exception {

        AuthData userAuthData;

        try{
            userAuthData = dataAccess.getAuthData(authToken);
        }

        catch (Exception invalidAuth){
            connectForMissingFieldOutput(authToken, session);
            return;
        }

        String username = userAuthData.username();
        GameData initialGameData = dataAccess.getGameData(gameID);

        if (initialGameData.isOver() == true){

            System.out.println(username);
            String messageStringToUser = "Error: Tried to make a move in a game that is over. Type 'help' for a list of commands";
            ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, messageStringToUser);
            connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);
            return;

        }

        ChessGame initialGame = initialGameData.game();
        ChessGame.TeamColor currentTurn = initialGame.getTeamTurn();

        if (!Objects.equals(initialGameData.whiteUsername(), username) && !Objects.equals(initialGameData.blackUsername(), username)){

            String wrongTurnObserver = "Error: You are an observer and cannot make a move";
            ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, wrongTurnObserver);
            connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);
            return;

        }

        else if (currentTurn == WHITE){

            if(!Objects.equals(initialGameData.whiteUsername(), username)){
                String wrongTurnBlack = "Error: Tried to make a move for your opponent. Please wait your turn. You are the BLACK team.";
                ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, wrongTurnBlack);
                connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);
                return;

            }

        }

        else{

            if(!Objects.equals(initialGameData.blackUsername(), username)){

                String wrongTurnWhite = "Error: Tried to make a move for your opponent. Please wait your turn. You are the WHITE team.";
                ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, wrongTurnWhite);
                connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);
                return;

            }

        }

        try{

            initialGame.makeMove(moveToMake);

        }

        catch(Exception badMove){

            String invalidMove = "That is not a valid move, please try again.";
            ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, invalidMove);
            connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);
            return;

        }

        GameData updatedGame;

        if (initialGame.isInCheckmate(WHITE) || initialGame.isInCheckmate(BLACK) ||
                initialGame.isInStalemate(WHITE) || initialGame.isInStalemate(BLACK)){

            updatedGame = new GameData(gameID, initialGameData.whiteUsername(), initialGameData.blackUsername(),
                    initialGameData.gameName(), initialGame, true);

        }

        else{

            updatedGame = new GameData(gameID, initialGameData.whiteUsername(), initialGameData.blackUsername(),
                    initialGameData.gameName(), initialGame, false);

        }

        dataAccess.updateGameData(gameID, updatedGame);

        String messageStringToGame = String.format("%s moved a piece from %s to %s",
                username, moveToMake.getStartPosition().toString(), moveToMake.getEndPosition().toString());

        ServerMessage outputBoardMessageToGame = new ServerMessage(LOAD_GAME, updatedGame.game());
        ServerMessage outputMoveUpdateMessageToGame = new ServerMessage(NOTIFICATION, messageStringToGame);

        connectionManager.broadcastMessageToGame(gameID, username, outputMoveUpdateMessageToGame);
        connectionManager.broadcastMessageToGame(gameID, username, outputBoardMessageToGame);
        connectionManager.broadcastMessageToSingleUser(gameID, username, outputBoardMessageToGame);

    }


    // The leave game mesesage for the user who is leaving will just happen
    // through the client and their REPL, rather than being broadcast to them.

    // Note that for updating the player in the actual game, it will use the same approach as
    // described in the makeMove method. NOTE: The player only needs to be updated to be null
    // if they were in the INGAME state when this was called.

    // Once again, do I actually need to pass the session into this?
    private void disconnectUser(int gameID, String authToken, Session session, ChessGame.TeamColor userColor) throws Exception{

        GameData disconnectFrom = dataAccess.getGameData(gameID);

        String username = dataAccess.getAuthData(authToken).username();

        String messageStringToGame;

        if (Objects.equals(disconnectFrom.whiteUsername(), username) || Objects.equals(disconnectFrom.blackUsername(), username)){

            GameData updatedWithDisconnect;

            if (Objects.equals(disconnectFrom.whiteUsername(), username)){

                updatedWithDisconnect = new GameData(gameID, null, disconnectFrom.blackUsername(),
                        disconnectFrom.gameName(), disconnectFrom.game(), disconnectFrom.isOver());

            }

            else{

                updatedWithDisconnect = new GameData(gameID, disconnectFrom.whiteUsername(), null,
                        disconnectFrom.gameName(), disconnectFrom.game(), disconnectFrom.isOver());

            }

            dataAccess.updateGameData(gameID, updatedWithDisconnect);

            messageStringToGame = String.format("The player %s has disconnected from the game", username);

            ServerMessage outputMessageToGame = new ServerMessage(NOTIFICATION, messageStringToGame);

            connectionManager.broadcastMessageToGame(gameID, username, outputMessageToGame);

            connectionManager.removeConnection(gameID, username);

            System.out.println("Player disconnected");


        }

        else{

            String messageStringToObserver = String.format("%s has stopped observing the game", username);

            ServerMessage outputMessageToObserver = new ServerMessage(NOTIFICATION, messageStringToObserver);

            connectionManager.broadcastMessageToGame(gameID, username, outputMessageToObserver);

            connectionManager.removeConnection(gameID, username);

            System.out.println("Observer disconnected");


        }

    }


    // Handle the actual resignation inside of the ChessClient, as well as the confirmations.
    // Do I need to make it so that nobody else can join the game and make moves? Or is this fine?
    // WAIT, it should be fine because of the note above disconnectUser since the player won't
    // actually leave the player slot and thus cannot rejoin in that space after the game has ended.

    // You will need to have both players update to the OBSERVER state when this happens, so figure out
    // how to implement that in a way you like maybe?
    private void resign(int gameID, String authToken, Session session) throws Exception{

        AuthData userAuthData;

        try{

            userAuthData = dataAccess.getAuthData(authToken);

        }

        catch (Exception invalidAuth){

            connectForMissingFieldOutput(authToken, session);

            return;

        }

        String resignerUsername = userAuthData.username();

        GameData gameDataToUpdate = dataAccess.getGameData(gameID);

        if (!Objects.equals(gameDataToUpdate.whiteUsername(), resignerUsername) &&
                !Objects.equals(gameDataToUpdate.blackUsername(), resignerUsername)){

            String cantResignObserver = "Error: You are an observer and cannot resign";

            ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, cantResignObserver);

            connectionManager.broadcastMessageToSingleUser(gameID, resignerUsername, outputMessageToUser);

            System.out.println("This should have blocked the observer from making the game end through resignation");

            return;

        }

        if (gameDataToUpdate.isOver() == true){

            String gameAlreadyResigned = "Error: Your opponent has already resigned. Cannot resign.";

            ServerMessage duplicateResign = new ServerMessage(ERROR, true, gameAlreadyResigned);

            connectionManager.broadcastMessageToSingleUser(gameID, resignerUsername, duplicateResign);

            System.out.println("This should only print to the user trying to resign");

        }

        else{

            GameData finalGamestate = gameDataToUpdate.updateToGameOver();

            dataAccess.updateGameData(gameID, finalGamestate);

            String resignMessageToGame = String.format("%s has resigned", resignerUsername);

            ServerMessage resign = new ServerMessage(NOTIFICATION, resignMessageToGame);

            connectionManager.broadcastMessageToSingleUser(gameID, resignerUsername, resign);

            connectionManager.broadcastMessageToGame(gameID, resignerUsername, resign);

            System.out.println("This should have broadcasted to everyone");

        }

    }

}
