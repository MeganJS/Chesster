package serverCode.services;

import serverCode.DAOs.MemoryGameDAO;
import serverCode.DAOs.MemoryUserAuthDAO;

/**
 * Service for clearing data from memory/database
 */
public class ClearService {

    static MemoryUserAuthDAO userAuthDAO = new MemoryUserAuthDAO();
    static MemoryGameDAO gameDAO = new MemoryGameDAO();

    /**
     * Clears all the data from memory/database.
     */
    public static void clearAllData() {
        userAuthDAO.clearAllUserAuthData();
        gameDAO.clearAllGames();
    }
}
