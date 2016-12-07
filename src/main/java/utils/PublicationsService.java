package utils;

import models.Publication;
import utils.WebService.RestCaller;

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
            Boolean success = RestCaller.sharedInstance.requestToContributeToPublicationById(publicationId, userId);
            getById(publicationId).setCurrentUserRequested(success);
            return success;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean retractRequestToContributeById(String publicationId, String userId) {
        try {
            Boolean success = RestCaller.sharedInstance.retractRequestToContributeToPublicationById(publicationId, userId);
            getById(publicationId).setCurrentUserRetractedRequested(success);
            return success;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean checkUserIsContributor() {
        return false;
    }
}
