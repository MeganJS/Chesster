package models;

/**
 * This class represents an AuthToken for a session.
 */
public class AuthToken {
    /**
     * authToken is the string authentication token
     */
    private String authToken;
    /**
     * username is the username of the user whose session it is
     */
    private String username;

    /**
     * constructs an AuthToken object
     *
     * @param authorization
     * @param nameuser
     */
    public AuthToken(String authorization, String nameuser) {
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

    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (o == this) {
            return true;
        }
        AuthToken oAuth = (AuthToken) o;
        if (!this.username.equals(oAuth.username)) {
            return false;
        }
        if (!this.authToken.equals(oAuth.authToken)) {
            return false;
        }
        return true;
    }
}
