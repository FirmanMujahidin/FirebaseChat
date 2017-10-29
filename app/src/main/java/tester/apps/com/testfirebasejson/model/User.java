package tester.apps.com.testfirebasejson.model;

/**
 * Created by firma on 28-Oct-17.
 */

public class User {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String password;
    private String uid;
}
