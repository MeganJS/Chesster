package serverCode.handlers;

import com.google.gson.Gson;
import serverCode.models.User;
import spark.Request;
import spark.Response;

import java.util.Map;

public class LoginHandler {

    public static Object handleLogin(Request req, Response res) {
        var serializer = new Gson();
        var userMap = serializer.fromJson(req.body(), Map.class);
        String password = "";
        String username = "";
        if (userMap.containsKey("username")) {
            username = (String) userMap.get("username");
        }
        if (userMap.containsKey("password")) {
            password = (String) userMap.get("password");
        }
        User userToLog = new User(username, password, null);
        res.body(serializer.toJson(userToLog));
        res.status(200);
        return res.body();//turns out req.body() and res.body() are already Json objects!
    }
}
