package models;

import org.apache.http.HttpException;
import utils.WebService.RestCaller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lwdthe1 on 12/6/16.
 */
public class CurrentUser {
    public static CurrentUser sharedInstance = new CurrentUser();
    private User user;
    private ArrayList<RequestToContribute> originalRequestsToContribute;
    private HashMap<String, RequestToContribute> publicationRequestsMap = new HashMap<>();

    private CurrentUser() {

    }

    public User getUser() {
        return user;
    }

    public Boolean getIsLoggedIn() {
        return user != null;
    }

    public UserRestCallResult attemptLogin(String username, String password) {
        //resultData should include the following
        try {
            UserRestCallResult result =  RestCaller.sharedInstance.loginUser(username,password);
            if(result.getSuccess()){
                this.user = result.getUser();
            }
            return result;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return getUsername();
    }

    public String getUsername() {
        return user != null ? user.getUsername() : "aramosfa";
    }

    public void logout(){
        this.user = null;
    }

    public void loadRequestsToContribute() {
        try {
            this.originalRequestsToContribute = (ArrayList<RequestToContribute>) RestCaller.sharedInstance.getCurrentUserRequests();
            for (RequestToContribute request: originalRequestsToContribute) {
                publicationRequestsMap.put(request.getPublicationId(), request);
            }
        } catch (Exception e) {
            //it's safe to ignore this.
        }
    }

    public RequestToContribute getRequestToContributeByPubId(String publicationId) {
        return publicationRequestsMap.get(publicationId);
    }
}
