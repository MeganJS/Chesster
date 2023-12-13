public class ClientMain2 {
    public static void main(String[] args) {
        String serverURL = "http://localhost:8080/";
        if (args.length == 1) {
            serverURL = args[0];
        }
        new Repl(serverURL).run();
    }
}
