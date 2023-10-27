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

    public User(String nameuser, String wordpass, String emailAddress) {
        this.username = nameuser;
        this.password = wordpass;
        this.email = emailAddress;
    }

    public User(User copy) {
        this.username = new String(copy.username);
        this.password = new String(copy.password);
        this.email = new String(copy.email);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
    
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        if (o == this) {
            return true;
        }
        User oUser = (User) o;
        if (!oUser.getUsername().equals(this.username)) {
            return false;
        }
        if (!oUser.getPassword().equals(this.password)) {
            return false;
        }
        if (!oUser.getEmail().equals(this.email)) {
            return false;
        }
        return true;
    }
}
