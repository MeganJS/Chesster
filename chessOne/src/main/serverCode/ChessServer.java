package serverCode;

import spark.Spark;
import spark.Request;
import spark.Response;

public class ChessServer {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");

    }

    private static void createRoutes() {

    }

}
