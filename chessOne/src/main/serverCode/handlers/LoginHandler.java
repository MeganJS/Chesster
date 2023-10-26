package serverCode.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import serverCode.models.AuthToken;
import serverCode.models.User;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Map;

import static serverCode.services.UserAuthServices.login;

public class LoginHandler {

    public static Object handleLogin(Request req, Response res) {
        var serializer = new Gson();
        var userMap = serializer.fromJson(req.body(), Map.class);
        var returnedUser = createUser(userMap);
        if (returnedUser.getClass() == ErrorDescription.class) {
            res.status(401);
            res.body(serializer.toJson(returnedUser));
            return res.body();
        }
        User userToLog = (User) returnedUser;
        try {
            AuthToken authToken = login(userToLog);
            res.status(200);
            res.body(serializer.toJson(authToken));
            return res.body();
        } catch (Exception ex) {
            res.status(401);
            res.body(serializer.toJson(new ErrorDescription("Error: unauthorized")));
            return res.body();
        }
    }

    private static Object createUser(Map userMap) {
        try {
            String password = checkForString(userMap, "password");
            String username = checkForString(userMap, "username");
            return new User(username, password, null);
        } catch (IOException ex) {
            return new ErrorDescription("Error: unauthorized");
        }
    }

    private static String checkForString(Map userMap, String key) throws IOException {
        if (userMap.containsKey(key)) {
            if (userMap.get(key) != null && !userMap.get(key).equals("")) {
                return (String) userMap.get(key);
            } else {
                throw new IOException();
            }
        }
        throw new IOException();
    }
}
