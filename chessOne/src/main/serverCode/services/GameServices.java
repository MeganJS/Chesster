package serverCode.services;

import serverCode.models.AuthToken;
import serverCode.models.Game;

import java.util.Collection;

public class GameServices {

    public Collection<Game> listGames(AuthToken authToken) {
        return null;
    }

    public Game createGame(AuthToken authToken, String gameName) {
        return null;
    }

    public void joinGame(AuthToken authToken, String playerColor, int gameID) {
    }
}
