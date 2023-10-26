package serverCode.handlers;

import dataAccess.DataAccessException;
import serverCode.models.AuthToken;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import serverCode.models.User;

import java.io.IOException;
import java.util.Map;

import static serverCode.services.UserAuthServices.register;

public class RegisterHandler {
    public static Object handleLogin(Request req, Response res) {
        var serializer = new Gson();
        var userMap = serializer.fromJson(req.body(), Map.class);
        var returnedUser = createUser(userMap);
        if (returnedUser.getClass() == ErrorDescription.class) {
            res.status(400);
            res.body(serializer.toJson(returnedUser));
            return res.body();
        }
        User newUser = (User) returnedUser;
        try {
            AuthToken authToken = register(newUser);
            res.status(200);
            res.body(serializer.toJson(authToken));
            return res.body();
        } catch (DataAccessException ex) {
            res.status(403);
            res.body(serializer.toJson(new ErrorDescription("Error: already taken")));
            return res.body();
        }
    }

    private static Object createUser(Map userMap) {
        try {
            String password = checkForString(userMap, "password");
            String username = checkForString(userMap, "username");
            String email = checkForString(userMap, "email");
            return new User(username, password, email);
        } catch (IOException ex) {
            return new ErrorDescription("Error: bad request");
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
