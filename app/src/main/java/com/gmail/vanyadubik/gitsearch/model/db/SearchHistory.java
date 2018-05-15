package com.gmail.vanyadubik.gitsearch.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "search_history")
public class SearchHistory {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "date")
    protected Long date;

    public SearchHistory(String text, Long date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

