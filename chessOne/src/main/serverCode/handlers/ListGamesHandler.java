package serverCode.handlers;

import com.google.gson.Gson;
import serverCode.models.Game;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static serverCode.services.GameServices.listGames;

public class ListGamesHandler {
    public static Object handleListGames(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        String authString = req.headers("authorization");
        try {
            Collection<Game> games = listGames(authString);
            res.status(200);
            res.body(serializer.toJson(createResponse(games)));
            return res.body();
        } catch (Exception ex) {
            res.status(401);
            res.body(serializer.toJson(new ErrorDescription("Error: unauthorized")));
            return res.body();
        }
    }

    private static Map createResponse(Collection<Game> games) {
        Map responseGameMap = new HashMap<>();
        responseGameMap.put("games", createResponseList(games));
        return responseGameMap;
    }

    private static Collection<ResponseGame> createResponseList(Collection<Game> games) {
        Collection<ResponseGame> responseGames = new HashSet<>();
        for (Game game : games) {
            responseGames.add(new ResponseGame(game));
        }
        return responseGames;
    }

    private static class ResponseGame {
        int gameID;
        String whiteUsername;
        String blackUsername;
        String gameName;

        ResponseGame(Game game) {
            this.gameID = game.getGameID();
            this.whiteUsername = game.getWhiteUsername();
            this.blackUsername = game.getBlackUsername();
            this.gameName = game.getGameName();
        }

    }
}
