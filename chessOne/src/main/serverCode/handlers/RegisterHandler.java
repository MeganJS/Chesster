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
    public static Object handleRegister(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        var userMap = serializer.fromJson(req.body(), Map.class);
        try {
            User newUser = createUser(userMap);
            AuthToken authToken = register(newUser);
            res.status(200);
            res.body(serializer.toJson(authToken));
        } catch (DataAccessException ex1) {
            res.status(403);
            res.body(serializer.toJson(new ErrorDescription("Error: already taken")));
        } catch (IOException ex2) {
            res.status(400);
            res.body(serializer.toJson(new ErrorDescription(ex2.getMessage())));
        }
        return res.body();
    }

    private static User createUser(Map userMap) throws IOException {
        String password = checkForString(userMap, "password");
        String username = checkForString(userMap, "username");
        String email = checkForString(userMap, "email");
        return new User(username, password, email);
    }

    private static String checkForString(Map userMap, String key) throws IOException {
        if (userMap.containsKey(key)) {
            if (userMap.get(key) != null && !userMap.get(key).equals("")) {
                return (String) userMap.get(key);
            } else {
                throw new IOException("Error: bad request");
            }
        }
        throw new IOException("Error: bad request");
    }
}
