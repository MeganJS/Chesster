import userCommandClasses.JoinObserverCommand;
import userCommandClasses.JoinPlayerCommand;
import userCommandClasses.LeaveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import static java.lang.Integer.parseInt;

public class GameplayUI {

    private String serverURL;
    private WSServerFacade wsServerFacade;
    private String authToken;
    private String playerColor = "";
    private String strGameID;

    public GameplayUI(String serverURL, String userAuthToken, String gameID, String playerColor) {
        this.serverURL = serverURL;
        this.authToken = userAuthToken;
        this.strGameID = gameID;
        wsServerFacade = new WSServerFacade(serverURL);
        this.playerColor = playerColor;
    }

    public String gameplayCommand(String command) {
        try {
            switch (command) {
                case "help":
                    return helpInGame();
                case "quit":
                    return quitInGame();
                case "move":
                    return null;
                case "highlight":
                    return null;
                case "redraw":
                    return null;
                case "leave":
                    return leaveGame();
                case "resign":
                    return null;
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Sorry, that's not a valid action.";
    }

    public void joinGameMessage() {
        if (playerColor.contains("player")) {
            wsServerFacade.joinPlayer(new JoinPlayerCommand(authToken, parseInt(strGameID), playerColor));
        } else if (playerColor.contains("observer")) {
            wsServerFacade.joinObserver(new JoinObserverCommand(authToken, parseInt(strGameID), playerColor));
        }
    }

    private String helpInGame() {
        StringBuilder helpOutput = new StringBuilder();
        helpOutput.append("I'm glad you asked! Here are the actions available to you: \n");
        helpOutput.append("help - see a list of available actions\n");
        helpOutput.append("quit - exit application (if you are in a game, causes you to leave the game before exiting)\n");
        helpOutput.append("highlight <position> - highlights all legal moves of the piece at that position\n");
        helpOutput.append("redraw - redraws the chessboard\n");
        helpOutput.append("leave - stop playing or observing this game. Someone else may take your place\n");
        if (playerColor.contains("player")) {
            helpOutput.append("move (<start position>,<end position>) - make the specified move on the chessboard\n");
            helpOutput.append("resign - forfeit the game\n");
        }
        return helpOutput.toString();
    }

    private String quitInGame() {
        leaveGame();
        return "quit";
    }

    /**
     * sends LEAVE command to server
     *
     * @return confirmation message that you left the game
     */
    private String leaveGame() {
        wsServerFacade.leaveGame(new LeaveCommand(authToken, playerColor));

        return "You have successfully left game " + strGameID + ". \n";
    }


}
