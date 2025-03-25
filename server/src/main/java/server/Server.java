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

        String authToken = request.headers("authorization");

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

        CreateGameRequest requestedName = new Gson().fromJson(request.body(), CreateGameRequest.class);

        GameData newGame = services.createGame(authToken, requestedName.gameName());

        GameID idItem = new GameID(newGame.gameID());

        return new Gson().toJson(idItem);

    }


    public Object joinGameHandler(Request request, Response response) throws DataAccessException{

        String authToken = request.headers("authorization");

        JoinGameRequest values = new Gson().fromJson(request.body(), JoinGameRequest.class);

        GameData joinedGame = services.joinGame(authToken, values.playerColor(), values.gameID());

        // You updated this in project 5, make sure it didn't accidentally break anything
        return new Gson().toJson(joinedGame);

    }


    // This was added in project 5, so make sure it didn't break anything
    public Object observeGameHandler(Request request, Response response) throws DataAccessException{

        String authToken = request.headers("authorization");

        GameID requestedGameID = new Gson().fromJson(request.body(), GameID.class);

        GameData observedGame = services.observeGame(authToken, requestedGameID.gameID());

        return new Gson().toJson(observedGame);

    }


    public Object clearAllDatabases(Request request, Response response) throws DataAccessException{

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