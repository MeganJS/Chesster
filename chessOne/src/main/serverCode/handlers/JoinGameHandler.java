package serverCode.handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Map;

import static serverCode.services.GameServices.joinGame;

public class JoinGameHandler {

    public static Object handleJoinGame(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        String authString = req.headers("authorization");
        var gameMap = serializer.fromJson(req.body(), Map.class);
        try {
            String playerColor = (String) gameMap.get("playerColor");
            int gameID = (int) gameMap.get("gameID");
            joinGame(authString, playerColor, gameID);
            res.status(200);
            res.body(serializer.toJson(new Object()));
            return res.body();
        } catch (Exception ex) {
            return (serializer.toJson(createError(res, ex)));
        }
    }

    private static ErrorDescription createError(Response res, Exception ex) {
        if (ex.getMessage().contains("bad")) {
            res.status(400);
        } else if (ex.getMessage().contains("taken")) {
            res.status(401);
        } else if (ex.getMessage().contains("unauthorized")) {
            res.status(403);
        }
        return new ErrorDescription(ex.getMessage());
    }
}
