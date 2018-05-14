package com.gmail.vanyadubik.gitsearch.model.json;

import com.google.gson.annotations.Expose;

public class ResultOwnerDTO {

    @Expose
    String login;

    @Expose
    int id;

    @Expose
    String url;

    public ResultOwnerDTO(String login, int id, String url) {
        this.login = login;
        this.id = id;
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
