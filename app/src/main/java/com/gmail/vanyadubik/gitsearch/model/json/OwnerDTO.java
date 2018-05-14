package com.gmail.vanyadubik.gitsearch.model.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OwnerDTO{

    @Expose
    String login;

    @Expose
    int id;

    @Expose
    @SerializedName("avatar_url")
    String avatar;

    @Expose
    @SerializedName("html_url")
    String url;

    @Expose
    String blog;

    @Expose
    String location;

    public OwnerDTO(String login, int id, String avatar, String url, String blog, String location) {
        this.login = login;
        this.id = id;
        this.avatar = avatar;
        this.url = url;
        this.blog = blog;
        this.location = location;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUrl() {
        return url;
    }

    public String getBlog() {
        return blog;
    }

    public String getLocation() {
        return location;
    }
}
