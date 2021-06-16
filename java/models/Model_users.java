package models;

public class Model_users {
    String name,email,search,phone,uid,cover,image,onlineStatus;

    public Model_users() {


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Model_users(String name, String email, String search, String phone, String uid, String cover, String image, String onlineStatus) {
        this.name = name;
        this.email = email;
        this.search = search;
        this.phone = phone;
        this.uid = uid;
        this.cover = cover;
        this.image = image;
        this.onlineStatus = onlineStatus;
    }
}
