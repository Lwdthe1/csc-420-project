package utils.WebService;

import models.ChatMessage;
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

        //String resultJson = EntityUtils.toString(response.getEntity());
        String resultJson = "{\"advertisedPubs\":[{\"_id\":\"5719fb0bd2c040110082b59a\",\"id\":\"eb297ea1161a\",\"name\":\"Prototyping: From UX to Front End\",\"description\":\"All things prototyping: Responsive Design, Wireframing, Front-end Development, Accessibility and beyond\",\"url\":\"https://medium.com/front-end-dev\",\"imageUrl\":\"https://cdn-images-1.medium.com/fit/c/200/200/1*T6MSo0PeE-WvKv6PM8wgNQ.png\",\"contributorId\":\"#hidden#\",\"contributorRole\":\"editor\",\"contributorEmail\":\"#hidden#\",\"contributorUsername\":\"graeme_fulton\",\"createDate\":\"2016-04-22T10:20:59.878Z\",\"lastModified\":\"2016-04-23T22:55:00.169Z\",\"pubIdTotalContributionRequests\":9,\"currentUserIsContributor\":false,\"belongsToCurrentUser\":false,\"pubIdTotalVisits\":4,\"pubIdTotalVisitsByCurrentUser\":1}]}";
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

    public List<ChatMessage> getChatMessages(String publicationId) throws URISyntaxException, HttpException, IOException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + "chat/" + "39d9b4950757" + "/VanhishikhaB";

        HttpGet httpGet = new HttpGet(restUri);
        HttpResponse response = httpClient.execute(httpGet);

        String resultJson = EntityUtils.toString(response.getEntity());
        List<ChatMessage> chatMessages = new ArrayList<>();
        JSONObject resultJsonObject = new JSONObject(resultJson);
        if (resultJsonObject.has("messages")) {
            JSONArray jsonChatMessages = resultJsonObject.getJSONArray("messages");
            System.out.println(jsonChatMessages);
            for (int i = 0; i < jsonChatMessages.length(); i++) {
                chatMessages.add(new ChatMessage(jsonChatMessages.getJSONObject(i)));
            }
        } else {
            System.out.println("returned json did not contain publications JSON: " + resultJsonObject.toString());
        }
        System.out.println(chatMessages.size());
        return chatMessages;
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

    public Boolean requestToContributeToPublicationById(String publicationId) throws URISyntaxException, IOException, HttpException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + format("fake/user/%s/pub/%s/contribute", CurrentUser.sharedInstance.getId(), publicationId);

        HttpPost httpPost = new HttpPost(restUri);

        HttpResponse response = httpClient.execute(httpPost);
        EntityUtils.toString(response.getEntity());
        return true;
    }

    public Boolean retractRequestToContributeToPublicationById(String publicationId) throws URISyntaxException, IOException, HttpException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + format("fake/pubs/contribute/%s/%s", publicationId, CurrentUser.sharedInstance.getId());

        HttpDelete httpDelete = new HttpDelete(restUri);

        HttpResponse response = httpClient.execute(httpDelete);
        //if null, the server ended the request successfully.
        return response.getEntity() == null;
    }
}
