package serverCode.DAOs;

import dataAccess.DataAccessException;
import serverCode.models.User;

public class UserDAO {

    public static User find(String username) throws DataAccessException {
        return null;
    }


    public static void insert(User user) throws DataAccessException{

    }


    public static void clearAllUsers() throws DataAccessException{

    }
    /*
    Things I could add:
        - ability to change password
        - ability to change email address
        - ability to delete user
     */
}
