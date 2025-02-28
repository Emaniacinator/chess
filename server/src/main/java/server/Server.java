package server;

import chess.ChessGame;
import chess.model.GameData;
import chess.model.UserData;
import dataaccess.DataAccessException;
import jdk.jshell.spi.ExecutionControl;
import spark.*;
import com.google.gson.*;
import services.Service;
import chess.model.AuthData;

import java.util.Map;

public class Server {


    private final Service services = new Service();


    public Server(){



    }


    public int run(int desiredPort) {

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.post("/user", this::registerUserHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGameHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.delete("/db", this::clearAllDatabases);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    private void exceptionHandler(DataAccessException exception, Request request, Response response){

        int errorCode = exception.getErrorCode();
        String errorMessage = exception.getMessage();
        response.status(errorCode);
        response.body(new Gson().toJson(Map.of("error", errorCode, "message", errorMessage)));


    }


    public Object registerUserHandler(Request request, Response response) throws DataAccessException{

        UserData newUser = new Gson().fromJson(request.body(), UserData.class);

        AuthData resultingAuth = services.registerNewUser(newUser);

        return new Gson().toJson(resultingAuth);

    }


    public Object loginHandler(Request request, Response response) throws DataAccessException{

        UserData loginUserData = new Gson().fromJson(request.body(), UserData.class);

        AuthData resultingAuth = services.loginUser(loginUserData);

        return new Gson().toJson(resultingAuth);

    }


    public Object logoutHandler(Request request, Response response) throws DataAccessException{

        String authToken = request.headers("authorization"); // Uhhh... you might need to from Json this for it to work right. But only maybe

        services.logoutUser(authToken);

        return new Gson().toJson(null);

    }


    public Object listGameHandler(Request request, Response response) throws DataAccessException{

        String authToken = request.headers("authorization"); // Uhhh... you might need to from Json this for it to work right. But only maybe

        GameList allGames = new GameList(services.getAllGameData(authToken));

        return new Gson().toJson(allGames);

    }


    public Object createGameHandler(Request request, Response response) throws DataAccessException{

        String authToken = request.headers("authorization");

        CreateGameRequest requestedName = new Gson().fromJson(request.body(), CreateGameRequest.class); // Uhhh... you might need to from Json this for it to work right. But only maybe

        GameData newGame = services.createGame(authToken, requestedName.gameName());

        GameID idItem = new GameID(newGame.gameID());

        return new Gson().toJson(idItem);

    }


    public Object joinGameHandler(Request request, Response response) throws DataAccessException{

        String authToken = request.headers("authorization"); // Uhhh... you might need to from Json this for it to work right. But only maybe

        JoinGameRequest values = new Gson().fromJson(request.body(), JoinGameRequest.class);

        services.joinGame(authToken, values.playerColor(), values.gameID());

        return new Gson().toJson(null);

    }


    public Object clearAllDatabases(Request request, Response response){

        services.clearAllDatabases();

        return new JsonObject();

    }

}


record GameID(int gameID){



}


record GameList(GameData[] games){



}


record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID){



}


record CreateGameRequest(String gameName){



}