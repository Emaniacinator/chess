package chess.model;

import chess.ChessGame;
import com.google.gson.*;

public record GameData (int gameID, String whiteUsername, String blackUsername , String gameName, ChessGame game, Boolean isOver){


    public GameData setWhiteUsername(String newWhiteUsername){

        return new GameData(gameID, newWhiteUsername, blackUsername, gameName, game, isOver);

    }


    public GameData setBlackUsername(String newBlackUsername){

        return new GameData(gameID, whiteUsername, newBlackUsername, gameName, game, isOver);

    }


    public GameData updateToGameOver(){

        return new GameData(gameID, whiteUsername, blackUsername, gameName, game, true);

    }

}
