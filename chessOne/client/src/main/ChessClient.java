import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

public class ChessClient {
    private boolean isSignedIn = false;
    private String serverURL;

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

    private String registerUser(String[] words) throws IOException, URISyntaxException {
        StringBuilder newURL = new StringBuilder();
        newURL.append(serverURL);
        newURL.append("user");
        URI uri = new URI(newURL.toString());
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        try {
            var body = Map.of("username", words[1], "password", words[2], "email", words[3]);
            var jsonBody = new Gson().toJson(body);
            var outputStream = http.getOutputStream();
            outputStream.write(jsonBody.getBytes());
        } catch (Exception ex) {
            return ex.getMessage();
        }
        http.connect();
        try {
            InputStream inputStream = http.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            return new Gson().fromJson(reader, Map.class).toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }


}
