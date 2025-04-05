package websocket.messages;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {

    ServerMessageType serverMessageType;

    String messageString;


    public enum ServerMessageType {

        LOAD_GAME,
        ERROR,
        NOTIFICATION

    }


    public ServerMessage(ServerMessageType type) {

        this.serverMessageType = type;
        this.messageString = null;

    }


    public ServerMessage(ServerMessageType type, String messageString) {

        this.serverMessageType = type;
        this.messageString = messageString;

    }


    public ServerMessageType getServerMessageType() {

        return this.serverMessageType;

    }


    // We'll see how this looks. I might not like it and as a result might have to change it.
    public String toString(){

        return new Gson().toJson(this);

    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {

            return true;

        }

        if (!(o instanceof ServerMessage)) {

            return false;

        }

        ServerMessage that = (ServerMessage) o;

        return getServerMessageType() == that.getServerMessageType();

    }


    @Override
    public int hashCode() {

        return Objects.hash(getServerMessageType());

    }

}
