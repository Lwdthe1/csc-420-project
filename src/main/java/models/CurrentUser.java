package models;

import utils.WebService.RestCaller;

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

    public RestCallResult attemptLogin(String username, String password) {
        //resultData should include the following
        RestCallResult resultData = new RestCallResult(false, "wrong password.");
        return resultData;
    }

    public String getId() {
        return getUsername();
    }

    public String getUsername() {
        //TODO(andres) replace testUser0 with empty string after you implement login.
        return user != null ? user.getUsername() : "testUser0";
    }

    private void loadRequestsToContribute() {
        try {
            this.originalRequestsToContribute = (ArrayList<RequestToContribute>) RestCaller.sharedInstance.getCurrentUserRequests();
            for (RequestToContribute request: originalRequestsToContribute) {
                publicationRequestsMap.put(request.getPublicationId(), request);
            }
        } catch (Exception e) {
            //it's safe to ignore this.
        }
    }

    public ArrayList<RequestToContribute> getRequestsToContribute() {
        if (originalRequestsToContribute == null) {
            loadRequestsToContribute();
        }
        return originalRequestsToContribute;
    }

    public RequestToContribute getRequestToContributeByPubId(String publicationId) {
        return getRequestsToContributePubIdsMap().get(publicationId);
    }

    public HashMap<String, RequestToContribute> getRequestsToContributePubIdsMap() {
        //make sure we've loaded the requests from the server
        getRequestsToContribute();
        return publicationRequestsMap;
    }
}
