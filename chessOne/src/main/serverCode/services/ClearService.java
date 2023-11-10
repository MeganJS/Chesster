package serverCode.services;

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

    /**
     * Clears all the data from memory/database.
     */
    public static void clearAllData() {
        userAuthDAO.clearAllUserAuthData();
        gameDAO.clearAllGames();
    }
}
