package serverCode.handlers;

import com.google.gson.Gson;
import serverCode.models.AuthToken;
import serverCode.models.User;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Map;

import static serverCode.services.UserAuthServices.login;

public class LoginHandler {

    public static Object handleLogin(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        var userMap = serializer.fromJson(req.body(), Map.class);
        try {
            User userToLog = createUser(userMap);
            AuthToken authToken = login(userToLog);
            res.status(200);
            res.body(serializer.toJson(authToken));
        } catch (Exception ex) {
            res.status(401);
            res.body(serializer.toJson(new ErrorDescription("Error: unauthorized")));
        }
        return res.body();
    }

    private static User createUser(Map userMap) throws IOException {
        String password = checkForString(userMap, "password");
        String username = checkForString(userMap, "username");
        return new User(username, password, null);
    }

    private static String checkForString(Map userMap, String key) throws IOException {
        if (userMap.containsKey(key)) {
            if (userMap.get(key) != null && !userMap.get(key).equals("")) {
                return (String) userMap.get(key);
            } else {
                throw new IOException("Error: unauthorized");
            }
        }
        throw new IOException("Error: unauthorized");
    }
}
