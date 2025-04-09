package ui;

import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.Scanner;
import java.util.Arrays;

import static ui.ClientState.INGAME;
import static ui.ClientState.OBSERVINGGAME;
import static ui.EscapeSequences.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

public class Repl {


    private final ChessClient client;


    public Repl(String serverURL) throws Exception{

        client = new ChessClient(serverURL, this);

    }


    // This should be different and shouldn't call UserGameCommand at all
    // Will I need a separate ClientOnlyCommands class to make sure that all the needed features fit into ChessClient
    // with the weird line limitations?

    // Probably make it so that this calls a function in ChessClient to update the game when it gets a
    // websocket message that causes the game to be different.
    public void run(){

        System.out.println("Welcome to the chess client. Please sign in or type 'help' to get started.");

        Scanner readInput = new Scanner(System.in);
        String previousInput = "";

        while(!previousInput.equals("quit")){

            printPrettySideThing();
            String inputLine = readInput.nextLine();

            try{

                String[] inputTokens = inputLine.trim().split(" ");

                // if inputTokens.length is 0, print that no command was received and then print the help section.

                String toPrint;

                if (inputTokens.length == 1){

                    toPrint = client.determineTakenAction(inputTokens[0]);

                }

                else if (inputTokens.length > 1) {

                    toPrint = client.determineTakenAction(inputTokens[0], Arrays.copyOfRange(inputTokens, 1, inputTokens.length));

                }

                else{

                    toPrint = CommandHelper.getHelpMenu(client.returnState());

                }

                if (toPrint != null){

                    System.out.println(SET_TEXT_COLOR_GREEN + toPrint);

                }


                previousInput = inputTokens[0];

            }

            catch(Exception exception){

                System.out.println(SET_TEXT_COLOR_GREEN + exception.getMessage());

            }

        }

    }


    // This one might cause some quirks since it could do a weird thing when printing the board
    // Also print the pretty side thing first or not?

    // Probably have it do this differently depending on what *type* of message is received.
    // Create another switch statement for it here?

    public void printNotification(ServerMessage message){

        if (message.getServerMessageType() == LOAD_GAME){

            System.out.println(CommandHelper.displayBoard(message.getGame().getBoard(), client.returnUserColor()));

        }

        else if (message.getServerMessageType() == ERROR){

            System.out.println(SET_TEXT_COLOR_BLUE + "[" + message.getServerMessageType() + "] >>> " + message.getErrorMessage());

        }

        else{

            System.out.println(SET_TEXT_COLOR_BLUE + "[" + message.getServerMessageType() + "] >>> " + message.getMessage());

        }

    }


    public void printPrettySideThing(){

        System.out.println(SET_TEXT_COLOR_GREEN + "[" + client.returnState() + "] >>>");

    }

}
