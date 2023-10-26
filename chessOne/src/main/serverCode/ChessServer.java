package serverCode;

import serverCode.handlers.LoginHandler;
import spark.Spark;
import spark.Request;
import spark.Response;

import static serverCode.handlers.LoginHandler.handleLogin;

public class ChessServer {
    public static void main(String[] args) {
        new ChessServer().run();
    }

    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");

        createRoutes();
        Spark.init();
    }

    private void createRoutes() {
        Spark.post("/session", LoginHandler::handleLogin);

    }

}
