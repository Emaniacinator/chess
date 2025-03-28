package ui;

import java.util.Scanner;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class Repl {

    private final ChessClient client;

    public Repl(String serverURL){

        client = new ChessClient(serverURL);

    }

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

                if (inputTokens.length == 1){

                    System.out.println(client.determineTakenAction(inputTokens[0]));

                }

                else if (inputTokens.length > 1) {

                    System.out.println(client.determineTakenAction(inputTokens[0], Arrays.copyOfRange(inputTokens, 1, inputTokens.length)));

                }

                // for some reason it won't exit the 'while' loop here
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
