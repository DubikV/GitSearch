package com.gmail.vanyadubik.gitsearch.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "owner")
public class Owner {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "login")
    private String login;

    @ColumnInfo(name = "avatar_url")
    private String avatar;

    @ColumnInfo(name = "html_url")
    private String htmlUrl;

    @ColumnInfo(name = "blog")
    private String blog;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "name_filter")
    private String nameFilter;

    public Owner(int uid, int id, String login, String avatar,
                 String htmlUrl, String blog, String location, String nameFilter) {
        this.uid = uid;
        this.id = id;
        this.login = login;
        this.avatar = avatar;
        this.htmlUrl = htmlUrl;
        this.blog = blog;
        this.location = location;
        this.nameFilter = nameFilter;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }
}

