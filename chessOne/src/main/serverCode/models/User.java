package serverCode.models;

/**
 * This class models a User object for the database/memory.
 */
public class User {
    /**
     * username is the String represetation of the user's username. Needs to be unique.
     * Cannot be null.
     */
    private String username;
    /**
     * password is the String representation of the user's password. Does not need to be unique.
     * Cannot be null.
     */
    private String password;
    /**
     * email is String representation of the user's email. Needs to be unique.
     * Can be null.
     */
    private String email;

    /**
     * Constructs a user object
     *
     * @param nameuser
     * @param wordpass
     * @param emailAddress
     */
    User(String nameuser, String wordpass, String emailAddress) {
        this.username = nameuser;
        this.password = wordpass;
        this.email = emailAddress;
    }

    public String getUsername() { //FIXME have getters return copies?
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean equals(Object o) {
        return false;
    }
}
