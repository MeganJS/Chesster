package serverCode.models;

public class User {
    private String username;
    private String password;
    private String email;

    User(String nameuser, String wordpass){
        this.username = nameuser;
        this.password = wordpass;
        this.email = null;
    }
    User(String nameuser, String wordpass, String emailAddress){
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
}
