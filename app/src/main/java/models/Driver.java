package models;

/**
 * Created by vaibhav.rustogi on 2/20/17.
 */

public class Driver {
    private String responseCode;
    private Object errors;
    private Object responseMessage;
    private Object isValid;
    private User user;
    private Object vehicle;
    private App app;
    private Object accessToken;
    private String droidVersion;
    private int clientId;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public Object getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(Object responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Object getIsValid() {
        return isValid;
    }

    public void setIsValid(Object isValid) {
        this.isValid = isValid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Object getVehicle() {
        return vehicle;
    }

    public void setVehicle(Object vehicle) {
        this.vehicle = vehicle;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Object getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Object accessToken) {
        this.accessToken = accessToken;
    }

    public String getDroidVersion() {
        return droidVersion;
    }

    public void setDroidVersion(String droidVersion) {
        this.droidVersion = droidVersion;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
