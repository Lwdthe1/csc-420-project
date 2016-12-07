package utils.WebService;

import models.CurrentUser;
import models.Publication;
import models.RequestToContribute;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by lwdthe1 on 3/11/2016.
 */
public class RestCaller
{
    public static RestCaller sharedInstance = new RestCaller();
    private final String TAG = "SuperMeditorRestCaller";
    private final String REST_API_URL = "http://lcontacts.herokuapp.com/api/i/";

    //prevent others from instantiating
    private RestCaller() {

    }

    /**
     * gets all the publications
     *
     * @return String array containing all the Pids
     * @throws URISyntaxException
     * @throws HttpException
     * @throws IOException
     */
    public List<Publication> getPublications() throws URISyntaxException, HttpException, IOException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + "pubs?deliminatePubs=false";

        HttpGet httpGet = new HttpGet(restUri);

        HttpResponse response = httpClient.execute(httpGet);

        String resultJson = EntityUtils.toString(response.getEntity());
        List<Publication> publications = new ArrayList<>();
        JSONObject resultJsonObject = new JSONObject(resultJson);

        if (resultJsonObject.has("advertisedPubs")) {
            JSONArray jsonPublications = resultJsonObject.getJSONArray("advertisedPubs");
            for (int i = 0; i < jsonPublications.length(); i++)
            {
                publications.add(new Publication(jsonPublications.getJSONObject(i)));
            }
        }
        else {
            System.out.println("returned json did not contain publications JSON: " + resultJsonObject.toString());
        }

        return publications;
    }

    public List<RequestToContribute> getCurrentUserRequests() throws URISyntaxException, HttpException, IOException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + format("fake/user/%s/requests", CurrentUser.sharedInstance.getId());

        HttpGet httpGet = new HttpGet(restUri);

        HttpResponse response = httpClient.execute(httpGet);

        String resultJson = EntityUtils.toString(response.getEntity());
        List<RequestToContribute> contributeRequests = new ArrayList<>();
        JSONObject resultJsonObject = new JSONObject(resultJson);

        if (resultJsonObject.has("requests")) {
            JSONArray jsonRequests = resultJsonObject.getJSONArray("requests");
            for (int i = 0; i < jsonRequests.length(); i++)
            {
                contributeRequests.add(new RequestToContribute(jsonRequests.getJSONObject(i)));
            }
        } else {
            System.out.println("returned json did not contain contributeRequests JSON: " + resultJsonObject.toString());
        }

        return contributeRequests;
    }


    public Boolean checkUserFollowsPublicationById(String publicationId, String userId) throws URISyntaxException, IOException, HttpException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + format("fake/user/%s/follows/%s",userId, publicationId);

        HttpGet httpGet = new HttpGet(restUri);

        HttpResponse response = httpClient.execute(httpGet);
        String resultJson = EntityUtils.toString(response.getEntity());
        return resultJson == "true";
    }

    public Boolean requestToContributeToPublicationById(String publicationId, String userId) throws URISyntaxException, IOException, HttpException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + format("fake/user/%s/pub/%s/contribute", userId, publicationId);

        HttpPost httpPost = new HttpPost(restUri);

        HttpResponse response = httpClient.execute(httpPost);
        EntityUtils.toString(response.getEntity());
        return true;
    }

    public Boolean retractRequestToContributeToPublicationById(String publicationId, String userId) throws URISyntaxException, IOException, HttpException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + format("fake/pubs/contribute/%s/%s", publicationId, userId);

        HttpDelete httpDelete = new HttpDelete(restUri);

        HttpResponse response = httpClient.execute(httpDelete);
        //if null, the server ended the request successfully.
        return response.getEntity() == null;
    }
}
