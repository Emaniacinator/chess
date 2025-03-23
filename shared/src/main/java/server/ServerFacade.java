package server;

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


    // NOT FULLY IMPLEMENTED
    public AuthData loginUser() throws Exception {

        return this.sendRequest("POST", "/session", null, AuthData.class);

    }


    // NOT FULLY IMPLEMENTED
    public AuthData registerUser() throws Exception {

        return this.sendRequest("POST", "/user", null, AuthData.class);

    }


    // NOT FULLY IMPLEMENTED
    public void logoutUser() throws Exception {

        // Does this need the %s to find what session to delete?
        this.sendRequest("DELETE", "/session", null, null);

    }


    // NOT FULLY IMPLEMENTED
    public void createGame() throws Exception {

        this.sendRequest("POST", "/game", null, null);

    }


    // NOT FULLY IMPLEMENTED
    public GameData joinGame() throws Exception {

        return this.sendRequest("PUT", "/game", null, GameData.class);

    }


    // NOT FULLY IMPLEMENTED
    //
    // In other parts of this program, you may need to add a user check to make sure that the observer
    // can't hijack the players' turns hahaha
    //
    // You may also need to add a new command to the Server file to get this to work as expected, or
    // maybe at least parse a list of returned games for the desired one.
    public GameData observeGame() throws Exception {

        return this.sendRequest("GET", "/game", null, GameData.class);

    }


    // NOT FULLY IMPLEMENTED
    public GameData[] listGames(){

        return null;

    }


    // THIS IS NOT YET IMPLEMENTED
    public <T> T sendRequest(String method, String path, Object neededDataForRequest , Class<T> returnClass) throws Exception {

        try{

            URL databaseURL = (new URI(serverURL + path)).toURL();

            HttpURLConnection http = (HttpURLConnection) databaseURL.openConnection();

            http.setRequestMethod(method);

            http.setDoOutput(true);

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
