package models;

/**
 * Created by lwdthe1 on 12/6/16.
 */
public class RestCallResult {
    private Boolean success = false;
    private String errorMessage = "";

    public RestCallResult(Boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public Boolean getSuccess() {
        return success && errorMessage.isEmpty();
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
