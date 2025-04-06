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

    // You will need to create a broadcast that returns a message for exception cases. It will only return
    // exceptions to the user who sent the bad request though. BUT MAKE THE CASE.

    // Note that the ServerMessage is just about the types of data that will be returned to the user.
    // Similarly, the UserGameCommand is just about the types of actions that the user can take.


    // NOTE: This class doesn't actually update anything on the server. It just sends out the needed messages
    // to the users. Don't try to have this actually make the move or anything, let the Server and ServerFacade
    // actually handle that part. Ehhhh.... maybe actually have it do the thing, actually? I already had to
    // integrate the DataAccessFramework shenanigans, so it might make sense.

    private final ConnectionManager connectionManager = new ConnectionManager();


//    @OnWebSocketError
//    public void onError(Throwable exception){
//
//        System.out.println("Thrown Error:\n" + exception.toString());
//
//        ServerMessage errorMessage = new ServerMessage(ERROR, exception.getMessage());
//
//        try{
//
//            connectionManager.broadcastMessageToSingleUser(mostRecentGameID, mostRecentUsername, errorMessage);
//
//        }
//
//        catch (Exception ex){
//
//            System.out.println("Error: Cannot broadcast message to user correctly. Please look at your code.");
//
//            System.out.println("Here is the error message:\n" + ex.toString());
//
//        }
//
//    }


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

                    connectForMissingFieldOutput(-99, mostRecentUsername, session);

                }

                break;

            case MAKE_MOVE:

                // Because of how they set up the tests, you'll probably need to run a check to see if the user matches
                // the color of piece that's trying to be moved here. Which is frustrating because I had a different
                // design in mind (and already being built) but they didn't explain this (or the tests) clearly so it's
                // not going to work anymore.

                GameData inputGameData = userCommand.getGameData();

                if (inputGameData != null){

                    makeMove(userCommand.getGameID(), userCommand.getAuthToken(), userCommand.getMoveToMake());

                }

                else {

                    ChessGame inputGame = dataAccess.getGameData(userCommand.getGameID()).game();

                    makeMove(userCommand.getGameID(), userCommand.getAuthToken(), userCommand.getMoveToMake());

                }

                break;

            case LEAVE:

                disconnectUser(userCommand.getGameID(), userCommand.getUsername(), session, userCommand.getTeamColor());

                break;

            case RESIGN:

                break;

            case CONNECT_OBSERVER:

                break;

        }

    }


    private void connectForMissingFieldOutput(int errorGameID, String username, Session session){

        connectionManager.addPlayer(errorGameID, username, session, null);

        String messageStringToUser = "Error: Did not input a valid gameID or had a bad authentication token. Please try again.";

        ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, messageStringToUser);

        try{

            connectionManager.broadcastMessageToSingleUser(-99, username, outputMessageToUser);

        }

        catch (Exception unknownError){

            System.out.println("How did you get here? Look at the connectForMissinFieldOutput in WebsocketHandler for troubleshooting.");

        }

        connectionManager.removeConnection(errorGameID, username);

    }


    // You need to program a LOAD_GAME case into the REPL or Client so that this displays correctly. Or else it just won't.
    private void connectPlayer(int gameID, String username, Session session, ChessGame.TeamColor userColor, ChessGame updatedGame) throws Exception{

        connectionManager.addPlayer(gameID, username, session, userColor);

        String messageStringToGame = String.format("%s has just joined the game on the %s team", username, userColor);

        ServerMessage outputMessageToGame = new ServerMessage(NOTIFICATION, messageStringToGame);

        String messageStringToUser = String.format("Successfully connected to game at the ID %s", gameID);

        // Note that when it does the load game message, it will display the message that I choose, update the client
        // side game board, and then draw the game board as a part of receiving a message with the load game type.
        ServerMessage outputMessageToUser = new ServerMessage(LOAD_GAME, updatedGame);

        ServerMessage outputMessageToUserTest = new ServerMessage(NOTIFICATION, messageStringToUser);

        connectionManager.broadcastMessageToGame(gameID, username, outputMessageToGame);

        connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);

        // connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUserTest);

    }


    // Is this where I should have it do a quick check of whether or not it's the user's turn?
    // Wait, actually I should just do that one client-side.

    // Also, you'll need to have the game grabbed, call the movePiece function inside of the game,
    // then update the game on the server's end, then send out the udpate to all the players and viewers.
    // You might need to implement a new function inside of the Server and ServerFacade to do this maybe?

    // Do I actually need to pass the session into this, or is that redundant? It feels useless right now, TBH.

    // Note that the actual game updating will happen through the serverFacade / Server that calls this, not actually through
    // the methods that I am writing right here.

    // Do I need to pass in an updated game in this manner? It seems like they want something really specific but did a
    // TERRIBLE job of explaining it and it's kind of pissing me off, honestly.
    private void makeMove(int gameID, String authToken, ChessMove moveToMake) throws Exception {

        AuthData userAuthData = dataAccess.getAuthData(authToken);

        String username = userAuthData.username();

        GameData initialGameData = dataAccess.getGameData(gameID);

        if (initialGameData.isOver() == true){

            System.out.println(username);

            String messageStringToUser = "Error: Tried to make a move in a game that is over. Type 'help' for a list of commands";

            ServerMessage outputMessageToUser = new ServerMessage(ERROR, true, messageStringToUser);

            connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);

            throw new Exception ("Error: This exception was already broadcast but I needed a way to break. Come up with something better maybe? lol");

        }

        ChessGame initialGame = initialGameData.game();

        initialGame.makeMove(moveToMake);

        GameData updatedGame;

        if (initialGame.isInCheckmate(WHITE) || initialGame.isInCheckmate(BLACK) || initialGame.isInStalemate(WHITE) || initialGame.isInStalemate(BLACK)){

            updatedGame = new GameData(gameID, initialGameData.whiteUsername(), initialGameData.blackUsername(), initialGameData.gameName(), initialGame, true);
            // Broadcast a winner at the end of this entire method. Maybe create a variable to determine if something came of this?

        }

        else{

            updatedGame = new GameData(gameID, initialGameData.whiteUsername(), initialGameData.blackUsername(), initialGameData.gameName(), initialGame, false);

        }

        dataAccess.updateGameData(gameID, updatedGame);

        String messageStringToGame = String.format("%s moved a piece from %s to %s", username, moveToMake.getStartPosition().toString(), moveToMake.getEndPosition().toString());

        ServerMessage outputBoardMessageToGame = new ServerMessage(LOAD_GAME, updatedGame.game());
        // You're probably going to have to redesign your Client Side stuff so that when the user calls draw board it reads
        // from the server. But we'll see how things actually go.

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
    private void disconnectUser(int gameID, String username, Session session, ChessGame.TeamColor userColor) throws Exception{

        connectionManager.removeConnection(gameID, username);

        String messageStringToGame;

        if (userColor != null){

            messageStringToGame = String.format("The player %s has disconnected from the game", username);

        }

        else{

            messageStringToGame = String.format("%s has stopped observing the game", username);

        }

        ServerMessage outputMessageToGame = new ServerMessage(NOTIFICATION, messageStringToGame);

        connectionManager.broadcastMessageToGame(gameID, username, outputMessageToGame);

    }


    // Handle the actual resignation inside of the ChessClient, as well as the confirmations.
    // Do I need to make it so that nobody else can join the game and make moves? Or is this fine?
    // WAIT, it should be fine because of the note above disconnectUser since the player won't
    // actually leave the player slot and thus cannot rejoin in that space after the game has ended.

    // You will need to have both players update to the OBSERVER state when this happens, so figure out
    // how to implement that in a way you like maybe?
    private void resign(){



    }

}
