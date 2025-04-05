package websocket;

import chess.ChessGame;
import chess.model.GameData;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

@WebSocket
public class WebsocketHandler {

    // Note that the ServerMessage is just about the types of data that will be returned to the user.
    // Similarly, the UserGameCommand is just about the types of actions that the user can take.

    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception{

        UserGameCommand userCommand = new Gson().fromJson(message, UserGameCommand.class);

        switch (userCommand.getCommandType()){

            case CONNECT:

                connectPlayer(userCommand.getGameID(), userCommand.getUsername(), session, userCommand.getTeamColor());

                break;

            case MAKE_MOVE:

                break;

            case LEAVE:

                break;

            case RESIGN:

                break;

            case CONNECT_OBSERVER:

                break;

        }

    }


    private void connectPlayer(int gameID, String username, Session session, ChessGame.TeamColor userColor) throws Exception{

        connectionManager.addPlayer(gameID, username, session, userColor);

        String messageStringToGame = String.format("%s has just joined the game one the %s team", username, userColor);

        ServerMessage outputMessageToGame = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, messageStringToGame);

        String messageStringToUser = String.format("Successfully connected to game at the ID %s", gameID);

        ServerMessage outputMessageToUser = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, messageStringToUser);

        connectionManager.broadcastMessageToGame(gameID, username, outputMessageToGame);

        connectionManager.broadcastMessageToSingleUser(gameID, username, outputMessageToUser);

    }


    // Is this where I should have it do a quick check of whether or not it's the user's turn?
    // Wait, actually I should just do that one client-side.

    // Also, you'll need to have the game grabbed, call the movePiece function inside of the game,
    // then update the game on the server's end, then send out the udpate to all the players and viewers.
    // You might need to implement a new function inside of the Server and ServerFacade to do this maybe?
    private void makeMove(int gameID, String username, Session session, GameData inputGame){



    }


}
