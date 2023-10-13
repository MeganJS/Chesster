package serverCode.DAOs;

import serverCode.models.AuthToken;
import serverCode.models.Game;
import serverCode.models.User;

import java.util.Collection;

public interface DAO {
    AuthToken createAuthToken(String username);

    AuthToken readAuthToken(String username);

    void deleteAuthToken(AuthToken authToken); //FIXME will I also need username? needs to be deleted in associated user as well

    User createUser(User newUser);

    User readUser(String username);

    Game createGame(String gameName);

    void updateGame(int gameID);

    Collection<Game> readAllGames();

    void clearAllData();
    //could add later: delete and update user, for changing password and deleting account

}
