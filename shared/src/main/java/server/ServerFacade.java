package server;

import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import com.google.gson.Gson;

public class ServerFacade {


    private final String serverUrl;


    public ServerFacade(String serverUrl){

        this.serverUrl = serverUrl;

    }


    // NOT FULLY IMPLEMENTED
    public AuthData loginUser(){

        return this.sendRequest("POST", "/session", null, AuthData.class);

    }


    // NOT FULLY IMPLEMENTED
    public AuthData registerUser(){

        return this.sendRequest("POST", "/user", null, AuthData.class);

    }


    // NOT FULLY IMPLEMENTED
    public void logoutUser(){

        this.sendRequest("DELETE", "/session", null, null);

    }


    // NOT FULLY IMPLEMENTED
    public void createGame(){

        this.sendRequest("POST", "/game", null, null);

    }



    // NOT FULLY IMPLEMENTED
    public GameData joinGame(){

        return this.sendRequest("PUT", "/game", null, GameData.class);

    }



    // NOT FULLY IMPLEMENTED
    public GameData observeGame(){

        return this.sendRequest("GET", "/game", null, GameData.class);

    }



    // NOT FULLY IMPLEMENTED
    public GameData[] listGames(){

        return null;

    }



    // THIS IS NOT YET IMPLEMENTED
    public <T> T sendRequest(String method, String path, Object something, Class<T> returnClass){

        return null;

    }


}
