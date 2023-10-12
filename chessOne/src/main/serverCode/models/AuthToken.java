package serverCode.models;

public class AuthToken {
    private String authToken;
    private String username;

    AuthToken(String authorization, String nameuser){
        this.authToken = authorization;
        this.username = nameuser;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
