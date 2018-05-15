package com.gmail.vanyadubik.gitsearch.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface SearchHistoryDao {
    @Query("SELECT text FROM search_history ORDER BY date")
    Flowable<List<String>> getAll();

    @Query("SELECT text FROM search_history  ORDER BY date DESC LIMIT 1")
    Flowable<String> getLastText();

    @Query("SELECT COUNT(*) from search_history")
    int count();

    @Insert
    void insert(SearchHistory searchHistories);

    @Insert
    void insertList(List<SearchHistory> searchHistories);

    @Query("DELETE FROM search_history")
    void deleteAll();

    @Query("DELETE FROM search_history WHERE text = :text")
    void delete(String text);
}
