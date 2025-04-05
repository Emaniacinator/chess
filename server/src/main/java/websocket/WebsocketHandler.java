package websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.model.GameData;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

@WebSocket
public class WebsocketHandler {

    // You probably need to get the serverURL as a parameter for this so that it can pull data from the server as needed.

    private final String serverURL;

    public WebsocketHandler(String serverURL){

        this.serverURL = serverURL;

    }

    make another error appear for remembering things :(.
    // You will need to create a broadcast that returns a message for exception cases. It will only return
    // exceptions to the user who sent the bad request though. BUT MAKE THE CASE.

    // Note that the ServerMessage is just about the types of data that will be returned to the user.
    // Similarly, the UserGameCommand is just about the types of actions that the user can take.


    // NOTE: This class doesn't actually update anything on the server. It just sends out the needed messages
    // to the users. Don't try to have this actually make the move or anything, let the Server and ServerFacade
    // actually handle that part.

    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception{

        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);

        switch (userCommand.getCommandType()){

            case CONNECT:

                connectPlayer(userCommand.getGameID(), userCommand.getUsername(), session, userCommand.getTeamColor());

                break;

            case MAKE_MOVE:

                makeMove(userCommand.getGameID(), userCommand.getUsername(), session, userCommand.getMoveToMake());

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


    Make another error show up here
    // You need to program a LOAD_GAME case into the REPL or Client so that this displays correctly. Or else it just won't.
    private void connectPlayer(int gameID, String username, Session session, ChessGame.TeamColor userColor) throws Exception{

        connectionManager.addPlayer(gameID, username, session, userColor);

        String messageStringToGame = String.format("%s has just joined the game one the %s team", username, userColor);

        ServerMessage outputMessageToGame = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, messageStringToGame);

        String messageStringToUser = String.format("Successfully connected to game at the ID %s", gameID);

        // Note that when it does the load game message, it will display the message that I choose, update the client
        // side game board, and then draw the game board as a part of receiving a message with the load game type.
        ServerMessage outputMessageToUser = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, messageStringToUser);

        connectionManager.broadcastMessageToGame(gameID, username, outputMessageToGame);

        connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);

    }


    // Is this where I should have it do a quick check of whether or not it's the user's turn?
    // Wait, actually I should just do that one client-side.

    // Also, you'll need to have the game grabbed, call the movePiece function inside of the game,
    // then update the game on the server's end, then send out the udpate to all the players and viewers.
    // You might need to implement a new function inside of the Server and ServerFacade to do this maybe?

    make an error here because its not yet fully implemented!

    // Do I actually need to pass the session into this, or is that redundant? It feels useless right now, TBH.

    // Note that the actual game updating will happen through the serverFacade / Server that calls this, not actually through
    // the methods that I am writing right here.
    private void makeMove(int gameID, String username, Session session, ChessMove moveToMake) throws Exception {

        String messageStringToGame = String.format("%s moved a piece from %s to %s", username, moveToMake.getStartPosition().toString(), moveToMake.getEndPosition().toString());

        ServerMessage outputMessageToGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, messageStringToGame);

        connectionManager.broadcastMessageToGame(gameID, username, outputMessageToGame);

        connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToGame);

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

        ServerMessage outputMessageToGame = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, messageStringToGame);

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
