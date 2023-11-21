import java.util.Scanner;

public class Repl {
    private ChessClient client;

    public Repl(String serverURL) {
        client = new ChessClient();
    }

    public void run() {
        System.out.println("You've reached Chesster, the chess application of your dreams[citation needed]. Congratulations!");
        System.out.println("Type \"help\" to view available actions.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String userInput = scanner.nextLine();
            result = client.checkInput(userInput);
            if (result.equals("quit")) {
                System.out.println("Till next time!");
            } else {
                System.out.println(result);
            }
        }

    }
}
