package models;

import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpException;
import utils.PublicationsService;
import utils.WebService.RestCaller;
import viewControllers.interfaces.AuthEvent;
import viewControllers.interfaces.AuthListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lwdthe1 on 12/6/16.
 */
public class CurrentUser {
    public static CurrentUser sharedInstance = new CurrentUser();
    private User user;
    private ArrayList<RequestToContribute> originalRequestsToContribute;
    private HashMap<String, RequestToContribute> publicationRequestsMap = new HashMap<>();

    private ConcurrentHashMap<AuthEvent, LinkedList<AuthListener>> eventListenersMap = new ConcurrentHashMap<>();

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
                notifyAuthListeners(AuthEvent.LOGGED_IN);
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

    private void notifyAuthListeners(AuthEvent event) {
        LinkedList<AuthListener> eventListeners = eventListenersMap.get(event);
        if (eventListeners != null) {
            eventListeners = (LinkedList<AuthListener>) eventListeners.clone();
            for (AuthListener listener: eventListeners) {
                System.out.println("notifiying listeners");
                listener.onEvent(event);
            }
        }
    }

    public String getId() {
        return getUsername();
    }

    public String getUsername() {
        return user != null ? user.getUsername() : "";
    }

    public void logout(){
        this.user = null;
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

    public void listen(AuthEvent event, AuthListener listener) {
        eventListenersMap.putIfAbsent(event, new LinkedList<AuthListener>());
        eventListenersMap.get(event).add(listener);
    }
}
