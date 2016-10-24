package models;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lwdthe1 on 9/26/16.
 */
public class Publication {
    public static final PrettyTime p = new PrettyTime();
    private static BufferedImage superMeditorLogoImage;

    private String id;
    private String name;
    private String description;
    private String url;

    private String imageUrl;
    private String contributorRole;
    private String contributorUsername;
    private boolean belongsToCurrentUser;

    private String createDate;
    private String createdAgo;
    private String lastModified;

    private int pubIdTotalVisits;
    private int pubIdTotalVisitsByCurrentUser;
    private int pubIdTotalContributionRequests;

    private BufferedImage image;


    public Publication(JSONObject jsonPublication) {
        id = jsonPublication.getString("id");
        name = jsonPublication.getString("name");
        description = jsonPublication.getString("description");
        url = jsonPublication.getString("url");
        url.replace("c/200/200", "c/400/400");
        imageUrl = jsonPublication.getString("imageUrl");
        contributorRole = jsonPublication.getString("contributorRole");
        contributorUsername = jsonPublication.getString("contributorUsername");


        belongsToCurrentUser = jsonPublication.getBoolean("belongsToCurrentUser");
        pubIdTotalContributionRequests = jsonPublication.getInt("pubIdTotalContributionRequests");
        pubIdTotalVisits = jsonPublication.getInt("pubIdTotalVisits");
        pubIdTotalVisitsByCurrentUser = jsonPublication.getInt("pubIdTotalVisitsByCurrentUser");
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContributorRole() {
        return contributorRole;
    }

    public String getContributorUsername() {
        return contributorUsername;
    }

    public Boolean getBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public int getPubIdTotalVisits() {
        return pubIdTotalVisits;
    }

    public int getPubIdTotalVisitsByCurrentUser() {
        return pubIdTotalVisitsByCurrentUser;
    }

    public int getPubIdTotalContributionRequests() {
        return pubIdTotalContributionRequests;
    }

    public BufferedImage getImage() {
        if(image == null) image = loadImage(imageUrl);
        return image;
    }

    public static BufferedImage loadImage(String imageUrl) {
        BufferedImage image = null;
        try {
            String urls = imageUrl;
            URL url = new URL(urls);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "NING/1.0");
            InputStream inputStream = urlConnection.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            image = bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static BufferedImage getSuperMeditorLogoImage() {
        if(superMeditorLogoImage == null) superMeditorLogoImage = loadImage("http://supermeditor.com/img/logo.png");
        return superMeditorLogoImage;
    }

}
