package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ChessClient;

import static ui.EscapeSequences.*;


public class ServerFacadeTests {

    private static Server server;

    private static ChessClient testClient = new ChessClient();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {

        System.out.println(testClient.displayBoard());
        Assertions.assertTrue(true);

        System.out.println();
        System.out.println(SET_BG_COLOR_LIGHT_GREY + EMPTY + RESET_BG_COLOR);
        System.out.println(SET_BG_COLOR_DARK_GREEN + WHITE_KING + RESET_BG_COLOR);
        System.out.println(testClient.getCharacterSpacing('a') + RESET_BG_COLOR);

    }



}
