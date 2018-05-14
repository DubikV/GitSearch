package com.gmail.vanyadubik.gitsearch.model.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultItemDTO {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String full_name;

    @Expose
    @SerializedName("private")
    private boolean priv;

    @Expose
    @SerializedName("html_url")
    private String url;

    @Expose
    private String description;

    @Expose
    @SerializedName("created_at")
    private String dateCreate;

    @Expose
    @SerializedName("updated_at")
    private String dateUpdate;

    @Expose
    private int size;

    @Expose
    private int watchers;

    @Expose
    private double score;

    @Expose
    @SerializedName("default_branch")
    private String defBranch;

    @Expose
    @SerializedName("owner")
    private ResultOwnerDTO owner;

    public ResultItemDTO(int id, String name, String full_name, boolean priv, String url,
                         String description, String dateCreate, String dateUpdate,
                         int size, int watchers, double score, String defBranch, ResultOwnerDTO owner) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.priv = priv;
        this.url = url;
        this.description = description;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.size = size;
        this.watchers = watchers;
        this.score = score;
        this.defBranch = defBranch;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFull_name() {
        return full_name;
    }

    public boolean isPriv() {
        return priv;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public int getSize() {
        return size;
    }

    public int getWatchers() {
        return watchers;
    }

    public double getScore() {
        return score;
    }

    public String getDefBranch() {
        return defBranch;
    }

    public ResultOwnerDTO getOwner() {
        return owner;
    }
}
