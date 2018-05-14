package com.gmail.vanyadubik.gitsearch.model.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DownloadResponse {

    @Expose
    @SerializedName("total_count")
    private int totalCount;
    @Expose
    @SerializedName("incomplete_results")
    private boolean incompleteResults;

    @Expose
    private List<ResultItemDTO> items;

    public DownloadResponse(int totalCount, boolean incompleteResults,
                             List<ResultItemDTO> items) {
        this.totalCount = totalCount;
        this.incompleteResults = incompleteResults;
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public List<ResultItemDTO> getItems() {
        return items;
    }
}
