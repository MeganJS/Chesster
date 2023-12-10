public class GameplayUI {

    private String serverURL;
    private WSServerFacade wsServerFacade;
    private String userAuthToken;
    private boolean isPlayer = false;
    private boolean isObserver = false;
    private String strGameID;

    public GameplayUI(String serverURL, String userAuthToken, String gameID, String playerColor) {
        this.serverURL = serverURL;
        this.userAuthToken = userAuthToken;
        this.strGameID = gameID;
        wsServerFacade = new WSServerFacade(serverURL);
        if (playerColor.contains("player")) {
            isPlayer = true;
        } else if (playerColor.contains("observer")) {
            isObserver = true;
        }
    }

    public String gameplayCommand(String command) {
        try {
            switch (command) {
                case "help":
                    return helpInGame();
                case "quit":
                    return null;
                case "move":
                    return null;
                case "highlight":
                    return null;
                case "redraw":
                    return null;
                case "leave":
                    return null;
                case "resign":
                    return null;
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "Sorry, that's not a valid action.";
    }

    public void joinGameMessage() {
        if (isPlayer) {
            wsServerFacade.joinPlayer();
        } else if (isObserver) {
            wsServerFacade.joinObserver();
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
        if (isPlayer) {
            helpOutput.append("move (<start position>,<end position>) - make the specified move on the chessboard\n");
            helpOutput.append("resign - forfeit the game\n");
        }
        return helpOutput.toString();
    }

    private String quitInGame() {
        leaveGame();
        return "quit";
    }

    private String leaveGame() {
        return null;
    }


}
