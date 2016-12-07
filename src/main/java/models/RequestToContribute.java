package models;

import org.json.JSONObject;

/**
 * Created by lwdthe1 on 12/7/16.
 */
public class RequestToContribute {
    private String userId, username, publicationId;
    private Boolean accepted, rejected, retracted;

    public RequestToContribute(JSONObject obj) {
        this.userId = obj.getString("userId");
        this.publicationId = obj.getString("publicationId");
        this.accepted = obj.has("status") && obj.getBoolean("status");
        this.rejected = obj.has("status") && !obj.getBoolean("status");
        this.retracted = obj.has("retracted") && obj.getBoolean("retracted");
    }

    public String getPublicationId() {
        return publicationId;
    }

    public Boolean getRetracted() {
        return retracted;
    }

    public Boolean wasAccepted() {
        return accepted && !rejected;
    }

    public Boolean wasRejected() {
        return rejected;
    }
}
