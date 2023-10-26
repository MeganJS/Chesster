package serverCode.handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Map;

import static serverCode.services.UserAuthServices.logout;

public class LogoutHandler {

    public static Object handleLogout(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        String authString = req.headers("authorization");
        try {
            logout(authString);
            res.status(200);
            res.body(serializer.toJson(new Object()));
        } catch (Exception ex) {
            res.status(401);
            res.body(serializer.toJson(new ErrorDescription("Error: unauthorized")));
            return res.body();
        }
        return res.body();
    }
}
