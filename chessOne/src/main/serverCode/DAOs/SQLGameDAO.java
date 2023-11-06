package serverCode.DAOs;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.Database;
import serverCode.models.Game;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {

    public static void databaseGameSetUp(Database database) throws DataAccessException {
        try {
            var dataConnection = database.getConnection();
            dataConnection.setCatalog("chessdata");
            //TODO make the game object serializable; this is just a placeholder until I figure that out
            //FIXME is gameName a NOT NULL field?
            var createGameStatement = """
                    CREATE TABLE IF NOT EXISTS games (
                        gameID INT NOT NULL AUTO_INCREMENT,
                        whiteUsername VARCHAR(100),
                        blackUsername VARCHAR(100),
                        observers VARCHAR(100),
                        gameName VARCHAR(100),
                        game VARCHAR(100) NOT NULL,
                        PRIMARY KEY (gameID)
                    )""";
            var createGameTable = dataConnection.prepareStatement(createGameStatement);
            createGameTable.executeUpdate();

            database.closeConnection(dataConnection);
        } catch (Exception ex) {
            throw new DataAccessException("Couldn't set up Game Table.");
        }
    }

    @Override
    public Game createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public Game readGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void claimGameSpot(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {

    }

    @Override
    public Collection<Game> readAllGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clearAllGames() throws DataAccessException {

    }
}
