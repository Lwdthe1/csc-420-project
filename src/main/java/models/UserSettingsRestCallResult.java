package models;

/**
 * Created by lwdthe1 on 12/6/16.
 */
public class UserSettingsRestCallResult {
    private Boolean success = false;
    private Boolean instantMessage = false;
    private Boolean statusRequest = false;

    public UserSettingsRestCallResult(){}

    public UserSettingsRestCallResult(Boolean instantMessage, Boolean statusRequest) {
        this.success = true;
        this.instantMessage = instantMessage;
        this.statusRequest = statusRequest;
    }

    public Boolean getSuccess() {
        return success;
    }
    public Boolean getInstantMessage() {
        return instantMessage;
    }
    public Boolean getStatusRequest() {
        return statusRequest;
    }

}
