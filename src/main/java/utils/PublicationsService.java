package utils;

import models.ChatMessage;
import models.Publication;
import org.apache.http.HttpException;
import utils.WebService.RestCaller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class PublicationsService {
    public final static PublicationsService sharedInstance = new PublicationsService();

    HashMap<String, Publication> publicationIdsMap = new HashMap<>();
    private ArrayList<Publication> publications;

    //prevent others from instantiating
    private PublicationsService() {

    }

    public void loadAll() {
        fetchPublications();
    }

    private void fetchPublications() {
        try {
            publications = (ArrayList<Publication>) RestCaller.sharedInstance.getPublications();
            for (Publication publication: publications) {
                publicationIdsMap.put(publication.getId(), publication);
            }
        } catch (Exception e) {
            publications = new ArrayList<>();
        }
    }

    /**
     * This will block to fetch publications from server if you dont call loadAll() first.
     * @return
     */
    public ArrayList<Publication> getAll() {
        if (publications == null || publications.isEmpty()) {
            fetchPublications();
        }
        return publications;
    }

    public Publication getById(String id) {
        return publicationIdsMap.get(id);
    }

    public Boolean checkUserFollowsById(String publicationId, String userId) {
        try {
            return RestCaller.sharedInstance.checkUserFollowsPublicationById(publicationId, userId);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean requestToContributeById(String publicationId, String userId) {
        try {
            return RestCaller.sharedInstance.requestToContributeToPublicationById(publicationId, userId);
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<ChatMessage> getChatMessages(String publicationId) {
        try {
            return (ArrayList<ChatMessage>) RestCaller.sharedInstance.getChatMessages(publicationId);
            //return (ArrayList<ChatMessage>) RestCaller.sharedInstance.getChatMessages();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
