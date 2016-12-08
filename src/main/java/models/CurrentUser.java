package models;

import utils.PublicationsService;
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
        return user != null ? user.getUsername() : "LincolnWDaniel";
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

    public void addRequestToContribute(RequestToContribute requestToContribute) {
        Publication publication = PublicationsService.sharedInstance.getById(requestToContribute.getPublicationId());
        publication.setCurrentUserRequested(true);
        originalRequestsToContribute.add(requestToContribute);
        publicationRequestsMap.put(requestToContribute.getPublicationId(), requestToContribute);
    }

    public void removeRequestToContribute(String publicationId) {
        int i = 0;
        for (RequestToContribute request: originalRequestsToContribute) {
            if (request.getPublicationId() == publicationId) {
                originalRequestsToContribute.remove(i);
                i++;
            }
        }
        publicationRequestsMap.remove(publicationId);
        Publication publication = PublicationsService.sharedInstance.getById(publicationId);
        publication.setCurrentUserRequested(false);
        publication.setCurrentUserRetractedRequested(true);
    }

    public boolean hasRequestsToContribute() {
        return originalRequestsToContribute != null &&  !originalRequestsToContribute.isEmpty();
    }
}
