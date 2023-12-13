package serverCode.services;

import dataAccess.Database;
import serverCode.DAOs.MemoryGameDAO;
import serverCode.DAOs.MemoryUserAuthDAO;
import serverCode.DAOs.SQLGameDAO;
import serverCode.DAOs.SQLUserAuthDAO;

/**
 * Service for clearing data from memory/database
 */
public class ClearService {

    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();
    static SQLGameDAO gameDAO = new SQLGameDAO();

    //This is just so I can clear the database whenever I need to
    //TODO remove this later
    public static void main(String[] args) {
        clearAllData();
    }

    /**
     * Clears all the data from memory/database.
     */
    public static void clearAllData() {
        userAuthDAO.clearAllUserAuthData();
        gameDAO.clearAllGames();
    }
}
