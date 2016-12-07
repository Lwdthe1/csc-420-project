package models;

import com.sun.org.apache.xml.internal.security.encryption.EncryptedType;

/**
 * Created by lwdthe1 on 12/6/16.
 */
public class CurrentUser {
    public static CurrentUser sharedInstance = new CurrentUser();
    private User user;

    private CurrentUser() {

    }

    public User getUser() {
        return user;
    }

    public Boolean getIsLoggedIn() {
        return user != null;
    }

    public RestCallResult attemptLogin(String username, String password) {
        //resultData should include the following
        RestCallResult resultData = new RestCallResult(false, "wrong password.");
        return resultData;
    }

    public String getId() {
        return getUsername();
    }

    public String getUsername() {
        return user != null ? user.getUsername() : "";
    }
}
