package websocket;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

public class ConnectionManager {

    // Is a hashmap of connections that tracks the ID of the game it's connected to. The Connection class
    // stores the username and the associated session. Maybe track the connected player's team too
    // to help with the make move function

    public record ConnectionArray(ArrayList<Connection> allConnections){

    }


    public final ConcurrentHashMap<Integer, ConnectionArray> activeConnections = new ConcurrentHashMap<>();


    // I feel like the way I did this could break things instead of fix them when several people join the same game,
    // but I guess I'll just have to find out.
    public void addPlayer(Integer gameID, String username, Session session, ChessGame.TeamColor userColor){

        if (activeConnections.containsKey(gameID)){

            ConnectionArray updatedArray = activeConnections.get(gameID);

            Connection newConnection = new Connection(username, session, userColor);

            updatedArray.allConnections.add(newConnection);

        }

        else{

            Connection newConnection = new Connection(username, session, userColor);

            ArrayList<Connection> newArrayList = new ArrayList<>();

            newArrayList.add(newConnection);

            activeConnections.put(gameID, new ConnectionArray(newArrayList));

        }

    }


    // Do I need to throw an exception here if the user isn't already connected? Or is it okay to assume
    // that because I build and break all of the connections that won't happen?
    public void removeConnection(Integer gameID, String username){

        ConnectionArray updatedArray = activeConnections.get(gameID);

        for (Connection checkedConnection : updatedArray.allConnections){

            if (Objects.equals(checkedConnection.username(), username)){

                updatedArray.allConnections.remove(checkedConnection);

                break;

            }

        }

        if (updatedArray.allConnections.isEmpty()){

            activeConnections.remove(gameID);

        }

    }


    // If the same user is connected more than once, this may send duplicate messages. You need to be aware of that.
    // It's worth noting that in the case of a load game call, this may not work because it is sending all the messages
    // exclusively as a string. Unless I'm mistaken, which I could be.
    public void broadcastMessageToGame(Integer gameID, String senderUsername, ServerMessage messageToSend) throws Exception{

        ConnectionArray gameToBroadcastTo = activeConnections.get(gameID);

        for (Connection userToSendTo : gameToBroadcastTo.allConnections){

            if (userToSendTo.session().isOpen()){

                if (!Objects.equals(userToSendTo.username(), senderUsername)){

                    userToSendTo.session().getRemote().sendString(messageToSend.toString());

                }

            }

            // If their connection isn't open, this will now close it. Maybe program this to send another message though?
            // Also maybe implement something such that if this happens it removes their authData from the active authData
            // set to prevent shennanigans?
            else {

                removeConnection(gameID, userToSendTo.username());

            }

        }

    }


    // This might be a flawed approach. Note that the senderUsername also represents who the message is being sent to.
    // This will also have most of if not all the issues that broadcastMessageToGame has, if it has any.
    public void broadcastMessageToSingleUser(Integer gameID, String senderUsername, ServerMessage messageToSend) throws Exception{

        ConnectionArray gameToBroadcastTo = activeConnections.get(gameID);

        for (Connection userToSendTo : gameToBroadcastTo.allConnections){

            if (userToSendTo.session().isOpen()){

                if (Objects.equals(userToSendTo.username(), senderUsername)){

                    userToSendTo.session().getRemote().sendString(messageToSend.toString());

                    break;

                }

            }

            // If their connection isn't open, this will now close it. Maybe program this to send another message though?
            // Also maybe implement something such that if this happens it removes their authData from the active authData
            // set to prevent shennanigans?
            else {

                removeConnection(gameID, userToSendTo.username());

            }

        }

    }

}
