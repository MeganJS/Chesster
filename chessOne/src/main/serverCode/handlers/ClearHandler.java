package serverCode.handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import static serverCode.services.ClearService.clearAllData;

public class ClearHandler {
    public static Object handleClear(Request req, Response res) {
        clearAllData();
        res.type("application/json");
        res.status(200);
        res.body(new Gson().toJson(new Object()));
        return res.body();
    }
}
