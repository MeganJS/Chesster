import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

public class ChessClient {
    private boolean isSignedIn = false;
    private String serverURL;
    private ChessServerFacade serverFacade = new ChessServerFacade();

    public ChessClient(String url) {
        serverURL = url;
    }

    public String checkInput(String input) {
        String[] words = input.toLowerCase().split(" ");
        String command = words[0];
        try {
            if (command.equals("quit")) {
                return command;
            }
            if (command.equals("help")) {
                return helpUser();
            }
            if (command.equals("register")) {
                return registerUser(words);
            }

        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Sorry, that's not a valid action.";
    }

    private String helpUser() {
        StringBuilder helpOutput = new StringBuilder();
        helpOutput.append("I'm so glad you asked! Here are the actions available to you: \n");
        helpOutput.append("help - see a list of available actions\n");
        helpOutput.append("quit - exit application\n");
        if (isSignedIn) {
            helpOutput.append("logout - logout of your current session\n");
            helpOutput.append("new <NAME> - create a new game of the specified name\n");
            helpOutput.append("list - list all current games\n");
            helpOutput.append("join <ID> <WHITE/BLACK/empty> - join a game of the specified ID as white or black player. Leave last field empty to join as observer\n");
            helpOutput.append("observe <ID> - observe a game of the specified ID\n");
        } else {
            helpOutput.append("login <USERNAME> <PASSWORD> - login to play chess\n");
            helpOutput.append("register <USERNAME> <PASSWORD> <EMAIL> - register a new account to play chess\n");
        }
        return helpOutput.toString();
    }

    private String registerUser(String[] words) {
        try {
            StringBuilder newURL = new StringBuilder();
            newURL.append(serverURL);
            newURL.append("user");
            var body = Map.of("username", words[1], "password", words[2], "email", words[3]);
            var jsonBody = new Gson().toJson(body);

            Map response = serverFacade.talkToServer(newURL.toString(), "POST", "", jsonBody);
            if ((int) response.get("statusCode") == 200) {
                return words[1] + " successfully registered. Welcome to chess!\n";
            } else if ((int) response.get("statusCode") == 400) {
                return "Hmm, something wasn't quite right with the input. Try again!\n";
            } else if ((int) response.get("statusCode") == 403) {
                return "Sorry, that username belongs to someone else already.\n";
            } else if ((int) response.get("statusCode") == 500) {
                return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                        response.get("statusMessage").toString() + "\n";
            } else {
                return "We have a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                        "\n Status Message: " + response.get("statusMessage").toString() + "\n";
            }

        } catch (Exception ex) {
            return ex.getMessage();
        }
    }


}
