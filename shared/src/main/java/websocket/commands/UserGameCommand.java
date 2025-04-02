package websocket.commands;

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


    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
    }


    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }


    public CommandType getCommandType() {

        return commandType;

    }


    public String getAuthToken() {

        return authToken;

    }


    public Integer getGameID() {

        return gameID;

    }


    public String determineUserAction(String inputCommand, String[] otherTokens, String gameState){

        switch (inputCommand.toLowerCase()){

            case "redraw":
                // Works in either case
                break;

            case "make_move":
                // Throw error if observing game
                break;

            case "highlight_moves":
                // Works in either case, regardless of team color or if is a player / observer
                break;

            case "leave":
                // Works in either case
                // Make sure that this updates the ChessClient's ClientState
                break;

            case "resign":
                // Throw error if observing game
                break;

            case "help":
                // Works in either case
                return getHelpCommandReturn(gameState);

        }

        return "Error: Unexpected input received, please try again. Type 'help' for a list of commands";

    }


    public String getHelpCommandReturn(String gameState){

        if (gameState.equals("in game")){

            return "redraw - redraws the chess board\n" + // This function will make the LOAD_GAME websocket (html? SQL?) call, then display the board
                    "make_move <START POSITION, END POSITION> - move a piece from the starting position to the ending position if the move is valid\n" +
                    "highlight_moves <PIECE POSITION> - Highlights all the legal spaces that a specific piece can move to\n" +
                    "leave - leaves the current chess game\n" +
                    "resign - forfeits the game, but does not leave the game\n" +
                    "help - display a list of available commands";

        }

        else{

            return "redraw - redraws the chess board\n" +
                    "highlight_moves <PIECE POSITION> - Highlights all the legal spaces that a specific piece can move to\n" +
                    "leave - stop observing the current chess game\n" +
                    "help - display a list of available commands";

        }

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
