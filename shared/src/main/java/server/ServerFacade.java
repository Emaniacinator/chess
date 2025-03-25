package server;

import chess.ChessGame;
import chess.model.AuthData;
import chess.model.GameData;
import chess.model.UserData;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;

public class ServerFacade {


    private final String serverURL;


    public ServerFacade(String serverURL){

        this.serverURL = serverURL;

    }


    // Note that the next 7 functions all need to send the data that is described in the flowchart
    // you made in project 2 to the server for things to function as expected.

    // You want to return to the ChessClient a class item depending on the type of return data you
    // get, so you'll need a function to parse that data back into a class.


    public AuthData loginUser(String[] receivedTokens) throws Exception {

        UserData loginData = new UserData(receivedTokens[0], receivedTokens[1], null);

        // Make sure that passing in the loginData UserData is actually what's needed here
        return this.sendRequest("POST", "/session", loginData, AuthData.class, null);

    }


    public AuthData registerUser(String[] receivedTokens) throws Exception {

        UserData newUser = new UserData(receivedTokens[0], receivedTokens[1], receivedTokens[2]);

        // Make sure that passing in new UserData for user is actually what's needed here
        return this.sendRequest("POST", "/user", newUser, AuthData.class, null);

    }


    public void logoutUser(AuthData clientAuthData) throws Exception {

        // Does this need the %s to find what session to delete?
        // Also, this might be fine? Is the AuthData automatically included, or does it need to be removed?
        this.sendRequest("DELETE", "/session", null, null, clientAuthData);

    }



    public void createGame(String[] receivedTokens, AuthData clientAuthData) throws Exception {

        // using receivedTokens[0] as the input might not work, be aware of this possiblity
        CreateGameRequest wrapper = new CreateGameRequest(receivedTokens[0]);
        this.sendRequest("POST", "/game", wrapper, null, clientAuthData);

    }


    public GameData joinGame(String[] receivedTokens, AuthData clientAuthData) throws Exception {

        // passing in the recievedTokens might format this wrong for the server, we'll have to find out.

        ChessGame.TeamColor fillerColor;

        if (receivedTokens[1].toUpperCase().equals("WHITE")){

            fillerColor = ChessGame.TeamColor.WHITE;

        }

        else{

            fillerColor = ChessGame.TeamColor.BLACK;

        }
        JoinGameRequest wrapper = new JoinGameRequest(fillerColor, Integer.parseInt(receivedTokens[0]));

        return this.sendRequest("PUT", "/game", wrapper, GameData.class, clientAuthData);

    }


    // NOT FULLY IMPLEMENTED
    //
    // In other parts of this program, you may need to add a user check to make sure that the observer
    // can't hijack the players' turns hahaha
    //
    // You may also need to add a new command to the Server file to get this to work as expected, or
    // maybe at least parse a list of returned games for the desired one.
    public GameData observeGame(String[] receivedTokens, AuthData clientAuthData) throws Exception {

        // passing in the recievedTokens might format this wrong for the server, we'll have to find out.

        GameData selectedGame = null;

        boolean foundGame = false;

        // It might not like how I've converted this object, but we'll find out
        GameList gameList = this.sendRequest("GET", "/game", null, GameList.class, clientAuthData);

        for (GameData currentData : gameList.games()){

            if (currentData.gameID() == Integer.parseInt(receivedTokens[0])){

                selectedGame = currentData;
                foundGame = true;

            }

        }

        if (selectedGame == null || foundGame != true){

            throw new Exception("Error: The gameID you requested doesn't exist in the database. Please try again.");

        }

        return selectedGame;

    }


    // NOT FULLY IMPLEMENTED
    // Actually maybe have this return a string since it's just info and doesn't actually need to display any specific game
    public GameList listGames(AuthData clientAuthData) throws Exception{

        // It might not like how I've converted this object, but we'll find out
        return this.sendRequest("GET", "/game", null, GameList.class, clientAuthData);

    }


    // THIS IS NOT YET IMPLEMENTED
    public <T> T sendRequest(String method, String path, Object neededDataForRequest, Class<T> returnClass, AuthData clientAuthData) throws Exception{

        try{

            URL databaseURL = (new URI(serverURL + path)).toURL();

            HttpURLConnection http = (HttpURLConnection) databaseURL.openConnection();

            http.setRequestMethod(method);

            http.setDoOutput(true);

            if (clientAuthData != null) {

                http.setRequestProperty("authorization", clientAuthData.authToken());

            }

            formatRequest(neededDataForRequest, http);

            http.connect();

            checkConnectionIsValid(http);

            return translateReturnForClient(http, returnClass);

        }

        // Should this throw a DataAccessException instead? And if so how do I make that happen
        // as I don't want to relocate the file, but it doesn't like me just importing it either.
        catch(Exception exception){

            throw new Exception(exception.getMessage());

        }

    }


    public void formatRequest(Object request, HttpURLConnection http) throws Exception {

        if (request != null){

            http.addRequestProperty("Content-Type", "application/json"); // You might need to adjust this later?

            String requestDataAsString = new Gson().toJson(request);

            try (OutputStream bodyOfRequest = http.getOutputStream()){

                bodyOfRequest.write(requestDataAsString.getBytes());

            }

        }

    }


    private void checkConnectionIsValid(HttpURLConnection http) throws Exception{

        var status = http.getResponseCode();

        if (status != 200){

            try(InputStream responseError = http.getErrorStream()){

                if (responseError != null){

                    InputStreamReader readInput = new InputStreamReader(responseError);

                    HashMap mappedError = new Gson().fromJson(readInput, HashMap.class);

                    String retrievedErrorMessage = mappedError.get("message").toString();

                    throw new Exception(retrievedErrorMessage);

                }

            }

            throw new Exception("Unexpected Error: " + status);

        }

    }


    private <T> T translateReturnForClient(HttpURLConnection http, Class<T> responseClass) throws Exception{

        T response = null;

        // Check that it's performed all the actions, if not it shouldn't return anything
        if (http.getContentLength() < 0){

            try (InputStream responseStreamBody = http.getInputStream()){

                InputStreamReader streamReader = new InputStreamReader(responseStreamBody);

                if (responseClass != null){

                    response = new Gson().fromJson(streamReader, responseClass);

                }

            }

        }

        return response;

    }

}
