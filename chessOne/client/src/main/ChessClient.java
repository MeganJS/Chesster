public class ChessClient {
    private boolean isSignedIn = false;

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


}
