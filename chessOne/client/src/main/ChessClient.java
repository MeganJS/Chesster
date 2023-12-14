import com.google.gson.Gson;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ChessClient {
    private String serverURL;
    private ChessServerFacade serverFacade = new ChessServerFacade();
    private String userAuthToken = null;
    private GameplayClient gameplayClient = null;

    public ChessClient(String url) {
        serverURL = url;
    }

    public String checkInput(String input) {
        String[] words = input.toLowerCase().split(" ");
        String command = words[0];
        if (gameplayClient != null) {
            if (command.equals("quit") || command.equals("leave")) {
                String retStr = gameplayClient.gameplayCommand(command, words);
                if (retStr.contains("quit") || retStr.contains("successfully left")) {
                    gameplayClient = null;
                }
                return retStr;
            }
            return gameplayClient.gameplayCommand(command, words);
        }
        try {
            switch (command) {
                case "quit":
                    return command;
                case "help":
                    return helpUser();
                case "register":
                    return registerUser(words);
                case "logout":
                    return logUserOut();
                case "login":
                    return logUserIn(words);
                case "new":
                    return makeNewGame(words);
                case "list":
                    return listGames();
                case "join", "observe":
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
        helpOutput.append("quit - exit application (if you are in a game, causes you to leave the game before exiting)\n");
        if (userAuthToken != null) {
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
            return "Hmm, something wasn't quite right there. Make sure to include a username, password, and email after the register command.";
        }
        var body = Map.of("username", words[1], "password", words[2], "email", words[3]);
        var jsonBody = new Gson().toJson(body);
        Map response = serverFacade.talkToServer(newURL, "POST", "", jsonBody);

        if ((int) response.get("statusCode") == 200) {

            userAuthToken = response.get("authToken").toString();
            return words[1] + " successfully registered. Welcome to chess!";
        } else if ((int) response.get("statusCode") == 400) {
            return "Hmm, something wasn't quite right with the input. Try again!";
        } else if ((int) response.get("statusCode") == 403) {
            return "Sorry, that username belongs to someone else already.";
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

            userAuthToken = null;
            return "Logout successful. Thanks for playing!";
        } else if ((int) response.get("statusCode") == 401) {
            return "Alas, you aren't authorized to make that request. Log in or register to start.";
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
            return "Hmm, something wasn't quite right there. Make sure to include the username and password after the login command.";
        }
        var body = Map.of("username", words[1], "password", words[2]);
        var jsonBody = new Gson().toJson(body);
        Map response = serverFacade.talkToServer(newURL, "POST", "", jsonBody);

        if ((int) response.get("statusCode") == 200) {

            userAuthToken = response.get("authToken").toString();
            return response.get("username") + " logged in. What would you like to do?";
        } else if ((int) response.get("statusCode") == 401) {
            return "Hmm, something wasn't quite right with the input. Try again!";
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
            return "'" + gameName + "' was successfully created. ID is " + toStrGameID((int) Math.round((Double) response.get("gameID"))) + ".";
        } else if ((int) response.get("statusCode") == 400) {
            return "The game needs a name to be created.";
        } else if ((int) response.get("statusCode") == 401) {
            return "Alas, you aren't authorized to make that request. Log in or register to start.";
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
            StringBuilder games = new StringBuilder();
            games.append("The current games are as follows:\n");
            games.append(printGameList(response.get("games")));
            return games.toString();
        } else if ((int) response.get("statusCode") == 401) {
            return "Alas, you aren't authorized to make that request. Log in or register to start.";
        } else if ((int) response.get("statusCode") == 500) {
            return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                    response.get("statusMessage").toString() + "\n";
        } else {
            return "We've got a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                    "\n Status Message: " + response.get("statusMessage").toString() + "\n";
        }
    }

    private String addUserToGame(String[] words) {
        try {
            String newURL = serverURL + "game";
            if (words.length < 2) {
                return "Hmm, something wasn't quite right. Make sure to include the game ID and color of choice after the command.";
            }
            var body = createJoinGameMap(words);
            String gameID = toStrGameID(parseInt(words[1]));
            String playerColor;
            if (words.length >= 3) {
                playerColor = words[2].toLowerCase() + " player";
            } else {
                playerColor = "observer";
            }
            var jsonBody = new Gson().toJson(body);
            Map response = serverFacade.talkToServer(newURL, "PUT", userAuthToken, jsonBody);


            if ((int) response.get("statusCode") == 200) {
                gameplayClient = new GameplayClient(serverURL, userAuthToken, gameID, playerColor);
                gameplayClient.joinGameMessage();
                return "Successfully joined game " + gameID + " as " + playerColor + ".";
            } else if ((int) response.get("statusCode") == 400) {
                return "Hmm, something wasn't quite right with the input. Try again!";
            } else if ((int) response.get("statusCode") == 401) {
                return "Alas, you aren't authorized to make that request. Log in or register to start.";
            } else if ((int) response.get("statusCode") == 403) {
                return "Unfortunately, " + playerColor + " for game " + gameID + " has already been taken.";
            } else if ((int) response.get("statusCode") == 500) {
                return "Looks like something went wrong serverside.\n Status Code: 500 \n Status Message: " +
                        response.get("statusMessage").toString() + "\n";
            } else {
                return "We've got a mystery on our hands.\n Status Code: " + response.get("statusCode").toString() +
                        "\n Status Message: " + response.get("statusMessage").toString() + "\n";
            }
        } catch (NumberFormatException ex) {
            return "Something was off about the game ID. Try again, or use the 'help' command to review actions.\n";
        }
    }


    private Map createJoinGameMap(String[] words) {
        var body = new HashMap();
        body.put("gameID", parseInt(words[1]));
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
        } else if (strGameID.length() == 2) {
            strGameID = "00" + gameID;
        } else if (strGameID.length() == 1) {
            strGameID = "000" + gameID;
        }
        return strGameID;
    }

    private String printGameList(Object gameListObj) {
        var gameList = (Collection) gameListObj;
        StringBuilder gameListStr = new StringBuilder();
        for (Object game : gameList) {
            gameListStr.append(printGame(game));
        }
        return gameListStr.toString();
    }

    private String printGame(Object game) {
        var json = new Gson();
        var jsonGame = json.toJson(game);
        var gameMap = new Gson().fromJson(jsonGame, Map.class);
        StringBuilder gameStr = new StringBuilder();
        gameStr.append("\u001b[38;5;0m\u001b[48;5;15m " + gameMap.get("gameName"));
        gameStr.append(" \u001b[38;5;160m\u001b[48;5;12m GameID: " + toStrGameID((int) Math.round((Double) gameMap.get("gameID"))));
        if (gameMap.containsKey("blackUsername")) {
            gameStr.append(" \u001b[38;5;15m\u001b[48;5;22m Black Player: " + gameMap.get("blackUsername"));
        }
        if (gameMap.containsKey("whiteUsername")) {
            gameStr.append(" \u001b[38;5;0m\u001b[48;5;46m White Player: " + gameMap.get("whiteUsername"));
        }
        gameStr.append("\u001b[38;5;15m \u001b[48;5;0m\n");
        return gameStr.toString();
    }


}
