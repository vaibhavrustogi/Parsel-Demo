package models;

/**
 * Created by vaibhav.rustogi on 2/20/17.
 */

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private int userType;
    private String email;
    private String phone;
    private String locality;
    private Object password;
    private int online;
    private int login;
    private int sockOnline;
    private Object filterParam;
    private Object sortBy;
    private Object uniqueParam;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getSockOnline() {
        return sockOnline;
    }

    public void setSockOnline(int sockOnline) {
        this.sockOnline = sockOnline;
    }

    public Object getFilterParam() {
        return filterParam;
    }

    public void setFilterParam(Object filterParam) {
        this.filterParam = filterParam;
    }

    public Object getSortBy() {
        return sortBy;
    }

    public void setSortBy(Object sortBy) {
        this.sortBy = sortBy;
    }

    public Object getUniqueParam() {
        return uniqueParam;
    }

    public void setUniqueParam(Object uniqueParam) {
        this.uniqueParam = uniqueParam;
    }
}
