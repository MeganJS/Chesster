package serverCode.services;

import serverCode.DAOs.SQLGameDAO;
import serverCode.DAOs.SQLUserAuthDAO;

/**
 * Service for clearing data from memory/database
 */
public class ClearService {

    static SQLUserAuthDAO userAuthDAO = new SQLUserAuthDAO();
    static SQLGameDAO gameDAO = new SQLGameDAO();

    //This is just so I can clear the database whenever I need to
    /*
    public static void main(String[] args) {
        clearAllData();
    }
     */

    /**
     * Clears all the data from memory/database.
     */
    public static void clearAllData() {
        userAuthDAO.clearAllUserAuthData();
        gameDAO.clearAllGames();
    }
}
