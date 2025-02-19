package chess.model;

import chess.ChessGame;
import com.google.gson.*;

public record GameData (int gameID, String whiteUsername, String blackUsername , String gameName, ChessGame game){



}
