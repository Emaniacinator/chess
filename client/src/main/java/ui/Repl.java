package ui;

import websocket.commands.UserGameCommand;

import java.util.Scanner;
import java.util.Arrays;

import static ui.ClientState.INGAME;
import static ui.ClientState.OBSERVINGGAME;
import static ui.EscapeSequences.*;

public class Repl {


    private final ChessClient client;


    public void makeErrorLineAppearOnPurposeSoYouRememberThisComment(){};
    // OH! The repl should feed the info to the ChessClient, which is then supposed to determine the command and the state.
    // That will then send a request to the ServerFacade? This will then make an instance of UserGameCommand to run game
    // commands through the websocket? While a separate instance of the websocket stays open to return and siplay updates?


    public Repl(String serverURL){

        client = new ChessClient(serverURL);

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

                ClientState currentState = client.returnState();

                if (currentState == INGAME || currentState == OBSERVINGGAME){

                    // if inputTokens.length is 0, print that no command was received and then print the help section.

                    String passedStateValue;

                    if (currentState == INGAME){

                        passedStateValue = "in game";

                    }

                    else{

                        passedStateValue = "observing game";

                    }

                }

                else{

                    // if inputTokens.length is 0, print that no command was received and then print the help section.

                    if (inputTokens.length == 1){

                        System.out.println(client.determineTakenAction(inputTokens[0]));

                    }

                    else if (inputTokens.length > 1) {

                        System.out.println(client.determineTakenAction(inputTokens[0], Arrays.copyOfRange(inputTokens, 1, inputTokens.length)));

                    }

                }

                previousInput = inputTokens[0];

            }

            catch(Exception exception){

                System.out.println(exception.getMessage());

            }

        }

    }


    public void printPrettySideThing(){

        System.out.println(SET_TEXT_COLOR_GREEN + "[" + client.returnState() + "] >>>");

    }

}
