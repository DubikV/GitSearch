package com.gmail.vanyadubik.gitsearch.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface SearchTextDataDao {
    @Query("SELECT text FROM search_text_data  WHERE id = 0")
    Single<String> getLastText();

    @Query("SELECT COUNT(*) from search_text_data")
    int count();

    @Insert
    void insert(SearchTextData searchTextData);

    @Update
    void update(SearchTextData searchTextData);

    @Insert
    void insertList(List<SearchTextData> searchTextDataList);

    @Query("DELETE FROM search_text_data")
    void deleteAll();

    @Query("DELETE FROM search_text_data WHERE text = :text")
    void delete(String text);
}
