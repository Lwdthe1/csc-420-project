package models;

import org.json.JSONObject;
import utils.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class ChatMessage {
    private final String publicationId;
    private final String userId;
    private final String text;
    private String imageUrl;
    private String userName;
    private String contributorRole;
    private BufferedImage image;

    public ChatMessage(JSONObject payload) {
        this.publicationId = payload.getString("publicationId");
        this.userId = payload.getString("userId");
        this.text = payload.getString("text");
        this.userName = payload.getString("username");
        //this.contributorRole = payload.getString("contributorRole");
        this.imageUrl = payload.getString("userImageUrl");
    }

    public ChatMessage(String publicationId, String userId, String text) {
        this.publicationId = publicationId;
        this.userId = userId;
        this.text = text;
    }

    public static JSONObject createJSONPayload(String publicationId, String userId, String text) {
        JSONObject payload = new JSONObject();
        payload.put("publicationId", publicationId);
        payload.put("userId", userId);
        payload.put("text", text);
        return payload;
    }

    public String getPublicationId() {
        return publicationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public String getUserName() { return userName; }

    public String getContributorRole() { return contributorRole; }

    public BufferedImage getImage() {
        if(image == null) image = ImageUtils.loadImage(imageUrl);
        return image;
    }
}
