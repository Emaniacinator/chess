package websocket.messages;

import chess.ChessGame;
import chess.model.GameData;
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

    ChessGame game;

    String message;

    Boolean isError;

    String errorMessage;


    public enum ServerMessageType {

        LOAD_GAME,
        ERROR,
        NOTIFICATION

    }


    public ServerMessage(ServerMessageType type) {

        this.serverMessageType = type;
        this.message = null;
        this.game = null;
        this.isError = false;
        this.errorMessage = null;

    }


    public ServerMessage(ServerMessageType type, String message) {

        this.serverMessageType = type;
        this.message = message;
        this.game = null;
        this.isError = false;
        this.errorMessage = null;

    }


    public ServerMessage(ServerMessageType type, ChessGame game){

        this.serverMessageType = type;
        this.message = null;
        this.game = game;
        this.isError = false;
        this.errorMessage = null;

    }


    public ServerMessage(ServerMessageType type, Boolean isError, String errorMessage){

        this.serverMessageType = type;
        this.message = null;
        this.game = null;
        this.isError = isError;
        this.errorMessage = errorMessage;

    }


    public ServerMessage(ServerMessageType type, String message, ChessGame game){

        this.serverMessageType = type;
        this.message = message;
        this.game = game;
        this.isError = false;
        this.errorMessage = null;

    }


    public String getMessage(){

        return this.message;

    }


    public String getErrorMessage(){

        return this.errorMessage;

    }


    public ServerMessageType getServerMessageType() {

        return this.serverMessageType;

    }


    public ChessGame getGame(){

        return this.game;

    }


    // We'll see how this looks. I might not like it and as a result might have to change it.
    public String toString(){

        if (this.serverMessageType == ServerMessageType.LOAD_GAME){

            if (message == null){

                record LoadGameNotification(ServerMessageType serverMessageType, ChessGame game){

                }

                LoadGameNotification returnNotification = new LoadGameNotification(this.serverMessageType, this.game);

                return new Gson().toJson(returnNotification);

            }

            else{

                record LoadGameWithMessage(ServerMessageType serverMessageType, String message, ChessGame game){


                }

                LoadGameWithMessage returnNotificationWithMessage = new LoadGameWithMessage(this.serverMessageType, this.message, this.game);

                return new Gson().toJson(returnNotificationWithMessage);

            }

        }

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
