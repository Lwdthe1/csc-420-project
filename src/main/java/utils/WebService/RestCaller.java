package utils.WebService;

import models.Publication;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwdthe1 on 3/11/2016.
 */
public class RestCaller
{
    private static final String TAG = "SuperMeditorRestCaller";
    private static final String REST_API_URL = "http://supermeditor.com/api/i";

    /**
     * gets all the publications
     *
     * @return String array containing all the Pids
     * @throws URISyntaxException
     * @throws HttpException
     * @throws IOException
     */
    public static List<Publication> getPublications() throws URISyntaxException, HttpException, IOException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + "/pubs";

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


    public static Boolean checkUserFollowsPublicationById(String publicationId, String userId) throws URISyntaxException, IOException, HttpException {
        // Create a new HttpClient and Get Sequence number
        HttpClient httpClient = new DefaultHttpClient();
        String restUri = REST_API_URL + "/fake/user/:userId/follows/:publicationId";

        HttpGet httpGet = new HttpGet(restUri);

        HttpResponse response = httpClient.execute(httpGet);
        String resultJson = EntityUtils.toString(response.getEntity());
        return resultJson == "true";
    }
}
