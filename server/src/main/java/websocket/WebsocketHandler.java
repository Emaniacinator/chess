package websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WebsocketHandler {

    // Note that the ServerMessage is just about the types of data that will be returned to the user.
    // Similarly, the UserGameCommand is just about the types of actions that the user can take.

    private final ConnectionManager connectionManager = new ConnectionManager();

}
