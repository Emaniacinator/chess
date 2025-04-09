package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;
import chess.model.GameData;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    private final ChessGame.TeamColor commandForWhichSide;

    private final String commandFromWhatUser;

    private final GameData gameDataFromUser;

    private final ChessMove move;


    // This one is the generic, given constructor that was given
    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {

        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.commandForWhichSide = null;
        this.commandFromWhatUser = null;
        this.gameDataFromUser = null;
        this.move = null;

    }


    // This one is used to help produce the right information to display adding a user to a specific side of a game
    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor commandForWhichSide, String commandFromWhatUser) {

        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.commandForWhichSide = commandForWhichSide;
        this.commandFromWhatUser = commandFromWhatUser;
        this.gameDataFromUser = null;
        this.move = null;

    }


    // This one lets the game be passed into the server for an update and tracks what move is being made.
    public UserGameCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {

        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.commandForWhichSide = null;
        this.commandFromWhatUser = null;
        this.gameDataFromUser = null;
        this.move = move;

    }


    public enum CommandType {

        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN,
        CONNECT_OBSERVER

    }


    public CommandType getCommandType(){

        return commandType;

    }


    public String getAuthToken() {

        return authToken;

    }


    public Integer getGameID() {

        return gameID;

    }


    public String getUsername() {

        return commandFromWhatUser;

    }


    public ChessGame.TeamColor getTeamColor() {

        return commandForWhichSide;

    }


    public GameData getGameData() {

        return gameDataFromUser;

    }


    public ChessMove getMoveToMake() {

        return move;

    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {

            return true;

        }

        if (!(o instanceof UserGameCommand)) {

            return false;

        }

        UserGameCommand that = (UserGameCommand) o;

        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());

    }


    @Override
    public int hashCode() {

        return Objects.hash(getCommandType(), getAuthToken(), getGameID());

    }

}
