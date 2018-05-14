package com.gmail.vanyadubik.gitsearch.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface RepositoryDao {
    @Query("SELECT * FROM repository")
    Flowable<List<Repository>> getAll();

    @Query("SELECT COUNT(*) from repository")
    int count();

    @Query("SELECT * FROM repository WHERE name_filter = :textSearch")
    Flowable<List<Repository>> getByTextSearch(String textSearch);

    @Insert
    void insert(Repository... repositories);

    @Insert
    void insertList(List<Repository> repositories);

    @Query("DELETE FROM repository")
    void deleteAll();
}
