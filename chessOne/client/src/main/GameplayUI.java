public class GameplayUI {

    private String serverURL;
    private WSServerFacade serverFacade = new WSServerFacade(serverURL);
    private String userAuthToken;
    boolean isPlayer = false;
    boolean isObserver = false;
    int gameID;

    public GameplayUI(String serverURL, String userAuthToken) {
        this.serverURL = serverURL;
        this.userAuthToken = userAuthToken;
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

}
