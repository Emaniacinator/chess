package ui;

import java.util.Scanner;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class Repl {

    private final ChessClient client;

    public Repl(){

        client = new ChessClient();

    }

    public void run(){

        System.out.println("Welcome to the chess client. Please sign in or type 'help' to get started.");

        Scanner readInput = new Scanner(System.in);
        String previousInput = "";

        while(previousInput != "quit"){

            printPrettySideThing();
            String inputLine = readInput.nextLine();

            try{

                String inputTokens[] = inputLine.toLowerCase().split(" ");

                // if inputTokens.length is 0, print that no command was received and then print the help section.

                if (inputTokens.length == 1){

                    client.determineTakenAction(inputTokens[0]);

                }

                else if (inputTokens.length > 1) {

                    client.determineTakenAction(inputTokens[0], Arrays.copyOfRange(inputTokens, 1, inputTokens.length));

                }


                previousInput = inputTokens[0];

            }

            catch(Exception exception){



            }

        }

    }

    public void printPrettySideThing(){

        System.out.println(SET_TEXT_COLOR_GREEN + "[" + client.returnState() + "] >>>");

    }

}
