package client;

import chess.ChessBoard;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ChessClient;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;


public class ServerFacadeTests {

    private static Server server;

    private static ChessClient testClient = new ChessClient("http://localhost:8080");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {

        ChessGame starterGame = new ChessGame();

        System.out.println(starterGame.getBoard());

        System.out.println();

        System.out.println(testClient.displayBoard(starterGame.getBoard(), WHITE));

        System.out.println();

        System.out.println(starterGame.getBoard());

        System.out.println();

        System.out.println(testClient.displayBoard(starterGame.getBoard(), BLACK));

        Assertions.assertTrue(true);

    }



}
