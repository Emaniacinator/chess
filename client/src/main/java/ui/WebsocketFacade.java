package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

import static websocket.commands.UserGameCommand.CommandType.*;

public class WebsocketFacade extends Endpoint {

    Session session;

    public WebsocketFacade(String url, Repl outputManagement) throws Exception{

        try{

            url = url.replace("http", "ws");
            URI websocketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, websocketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>(){

                @Override
                public void onMessage(String inputMessage){

                    ServerMessage message = new Gson().fromJson(inputMessage, ServerMessage.class);
                    outputManagement.printNotification(message);

                }

            });

        }

        catch(Exception weirdException){

            throw new Exception("I'll be honest, I don't know how we got here but it's in the WebsocketFacade initializer.");

        }

    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){


    }


    // REMEMBER TO INCLUDE CALLING THIS IN THE CHESS CLIENT OR WEIRD STUFF WILL HAPPEN
    public void joinGame(String authToken, int gameID, ChessGame.TeamColor playerColor, String username) throws Exception{

            UserGameCommand inputCommand = new UserGameCommand(CONNECT, authToken, gameID, playerColor, username);
            this.session.getBasicRemote().sendText(new Gson().toJson(inputCommand));

    }


    public void makeMove(String authToken, int gameID, ChessMove moveToMake) throws Exception{

        UserGameCommand inputCommand = new UserGameCommand(MAKE_MOVE, authToken, gameID, moveToMake);
        this.session.getBasicRemote().sendText(new Gson().toJson(inputCommand));

    }


    public void leaveGame() throws Exception{



    }


    public void resignFromGame() throws Exception{



    }


}
