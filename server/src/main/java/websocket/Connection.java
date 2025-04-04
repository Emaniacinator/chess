package websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

public record Connection(String username, Session session, ChessGame.TeamColor userColor){

    public Connection(String username, Session session){

        this(username, session, null);

    }

}
