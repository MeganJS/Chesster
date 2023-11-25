import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ChessClient {
    private boolean isSignedIn = false;
    private String serverURL;
    private ChessServerFacade serverFacade = new ChessServerFacade();
    private String userAuthToken = null;

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
            if (command.equals("logout")) {
                return logUserOut();
            }
            if (command.equals("login")) {
                return logUserIn(words);
            }
            if (command.equals("new")) {
                return makeNewGame(words);
            }
            if (command.equals("list")) {
                return listGames();
            }
            if (command.equals("join") || command.equals("observe")) {
                return addUserToGame(words);
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Sorry, that's not a valid action.";
    }

    private String helpUser() {
        StringBuilder helpOutput = new StringBuilder();
        helpOutput.append("I'm glad you asked! Here are the actions available to you: \n");
        helpOutput.append("help - see a list of available actions\n");
        helpOutput.append("quit - exit application\n");
        if (isSignedIn) {
            helpOutput.append("logout - logout of your current session\n");
            helpOutput.append("new <NAME> - create a new game of the specified name\n");
            helpOutput.append("list - list all current games\n");
            helpOutput.append("join <ID> <WHITE/BLACK/empty> - join a game of the specified ID as white or black player. Leave out the last field to join as observer\n");
            helpOutput.append("observe <ID> - observe a game of the specified ID\n");
        } else {
            helpOutput.append("login <USERNAME> <PASSWORD> - login to play chess\n");
            helpOutput.append("register <USERNAME> <PASSWORD> <EMAIL> - register a new account to play chess\n");
        }
        return helpOutput.toString();
    }

    private String registerUser(String[] words) {
        String newURL = serverURL + "user";
        if (words.length < 4) {
            return "Hmm, something wasn't quite right there. Make sure to include a username, password, and email after the register command.\n";
        }
        var body = Map.of("username", words[1], "password", words[2], "email", words[3]);
        var jsonBody = new Gson().toJson(body);
        Map response = serverFacade.talkToServer(newURL, "POST", "", jsonBody);

        if ((int) response.get("statusCode") == 200) {
            isSignedIn = true;
            userAuthToken = response.get("authToken").toString();
            return words[1] + " successfully registered. Welcome to chess!\n";
        } else if ((int) response.get("statusCode") == 400) {
            return "Hmm, something wasn't quite right with the input. Try again!\n";
        } else if ((int) response.get("statusCode") == 403) {
            return "Sorry, that username belongs to someone else already.\n";
        } else if ((int) response.get("statusCode") == 500) {
            return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                    response.get("statusMessage").toString() + "\n";
        } else {
            return "We've got a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                    "\n Status Message: " + response.get("statusMessage").toString() + "\n";
        }

    }


    private String logUserOut() {
        String newURL = serverURL + "session";
        Map response = serverFacade.talkToServer(newURL, "DELETE", userAuthToken, "");

        if ((int) response.get("statusCode") == 200) {
            isSignedIn = false;
            userAuthToken = null;
            return "Logout successful. Thanks for playing!\n";
        } else if ((int) response.get("statusCode") == 401) {
            return "Alas, you aren't authorized to make that request. Log in or register to start.\n";
        } else if ((int) response.get("statusCode") == 500) {
            return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                    response.get("statusMessage").toString() + "\n";
        } else {
            return "We've got a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                    "\n Status Message: " + response.get("statusMessage").toString() + "\n";
        }
    }

    private String logUserIn(String[] words) {
        String newURL = serverURL + "session";
        if (words.length < 3) {
            return "Hmm, something wasn't quite right there. Make sure to include the username and password after the login command.\n";
        }
        var body = Map.of("username", words[1], "password", words[2]);
        var jsonBody = new Gson().toJson(body);
        Map response = serverFacade.talkToServer(newURL, "POST", "", jsonBody);

        if ((int) response.get("statusCode") == 200) {
            isSignedIn = true;
            userAuthToken = response.get("authToken").toString();
            return response.get("username") + " logged in. What would you like to do?\n";
        } else if ((int) response.get("statusCode") == 401) {
            return "Hmm, something wasn't quite right with the input. Try again!\n";
        } else if ((int) response.get("statusCode") == 500) {
            return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                    response.get("statusMessage").toString() + "\n";
        } else {
            return "We've got a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                    "\n Status Message: " + response.get("statusMessage").toString() + "\n";
        }
    }

    private String makeNewGame(String[] words) {
        String newURL = serverURL + "game";
        String gameName = makeGameName(words);
        var body = Map.of("gameName", gameName);
        var jsonBody = new Gson().toJson(body);

        Map response = serverFacade.talkToServer(newURL, "POST", userAuthToken, jsonBody);
        if ((int) response.get("statusCode") == 200) {
            return "'" + gameName + "' was successfully created. ID is " + toStrGameID((int) Math.round((Double) response.get("gameID"))) + ".\n";
        } else if ((int) response.get("statusCode") == 400) {
            return "The game needs a name to be created.\n";
        } else if ((int) response.get("statusCode") == 401) {
            return "Alas, you aren't authorized to make that request. Log in or register to start.\n";
        } else if ((int) response.get("statusCode") == 500) {
            return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                    response.get("statusMessage").toString() + "\n";
        } else {
            return "We've got a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                    "\n Status Message: " + response.get("statusMessage").toString() + "\n";
        }
    }

    private String makeGameName(String[] words) {
        if (words.length <= 1) {
            return "";
        }
        StringBuilder gameName = new StringBuilder();
        gameName.append(words[1]);
        for (int i = 2; i < words.length; i++) {
            gameName.append(" ");
            gameName.append(words[i]);
        }
        if (gameName.length() > 100) {
            gameName.setLength(100);
        }
        return gameName.toString();
    }

    private String listGames() {
        String newURL = serverURL + "game";
        Map response = serverFacade.talkToServer(newURL, "GET", userAuthToken, "");

        if ((int) response.get("statusCode") == 200) {
            return "The current games are as follows:\n" +
                    response.get("games").toString() +
                    "\n";
        } else if ((int) response.get("statusCode") == 401) {
            return "Alas, you aren't authorized to make that request. Log in or register to start.\n";
        } else if ((int) response.get("statusCode") == 500) {
            return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                    response.get("statusMessage").toString() + "\n";
        } else {
            return "We've got a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                    "\n Status Message: " + response.get("statusMessage").toString() + "\n";
        }
    }
    //TODO add helper functions to clean things up
    //specifically for the URL and the error response codes

    private String addUserToGame(String[] words) {
        String newURL = serverURL + "game";
        if (words.length < 2) {
            return "Hmm, something wasn't quite right. Make sure to include the game ID and color of choice after the command.\n";
        }
        var body = createJoinGameMap(words); //TODO add case if the color is put before ID
        String gameID = toStrGameID(parseInt(words[1]));// TODO add try-catch block for invalid int
        String playerColor;
        if (words.length >= 3) {
            playerColor = words[2].toLowerCase() + " team";
        } else {
            playerColor = "observer";
        }
        var jsonBody = new Gson().toJson(body);
        Map response = serverFacade.talkToServer(newURL, "PUT", userAuthToken, jsonBody);


        if ((int) response.get("statusCode") == 200) {
            return "Successfully joined game " + gameID + " as " + playerColor + ".\n";
        } else if ((int) response.get("statusCode") == 400) {
            return "Hmm, something wasn't quite right with the input. Try again!\n";
        } else if ((int) response.get("statusCode") == 401) {
            return "Alas, you aren't authorized to make that request. Log in or register to start.\n";
        } else if ((int) response.get("statusCode") == 403) {
            return "Unfortunately, " + playerColor + " for game " + gameID + " has already been taken.\n";
        } else if ((int) response.get("statusCode") == 500) {
            return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                    response.get("statusMessage").toString() + "\n";
        } else {
            return "We've got a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                    "\n Status Message: " + response.get("statusMessage").toString() + "\n";
        }

    }

    private Map createJoinGameMap(String[] words) {
        var body = new HashMap(); //TODO add case if the color is put before ID
        body.put("gameID", parseInt(words[1])); // TODO add try-catch block for invalid int
        if (words.length >= 3) {
            body.put("playerColor", words[2]);
        } else {
            body.put("playerColor", "");
        }
        return body;
    }

    private String toStrGameID(int gameID) {
        String strGameID = Integer.toString(gameID);
        if (strGameID.length() == 3) {
            strGameID = "0" + gameID;
        }
        return strGameID;
    }

}
