package serverCode.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import serverCode.models.Game;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Map;

import static serverCode.services.GameServices.createGame;

public class CreateGameHandler {
    public static Object handleCreateGame(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        String authString = req.headers("authorization");
        var gameMap = serializer.fromJson(req.body(), Map.class);
        try {
            String gameName = (String) gameMap.get("gameName");
            if (gameName == null || gameName.isEmpty()) {
                throw new IOException();
            }
            Game newGame = createGame(authString, gameName);
            gameMap.clear();
            gameMap.put("gameID", newGame.getGameID());
            res.body(serializer.toJson(gameMap));
            res.status(200);
            return res.body();
        } catch (IOException ex1) {
            res.status(400);
            res.body(serializer.toJson(new ErrorDescription("Error: bad request")));
            return res.body();
        } catch (DataAccessException ex2) {
            res.status(401);
            res.body(serializer.toJson(new ErrorDescription("Error: unauthorized")));
            return res.body();
        }
    }
}
