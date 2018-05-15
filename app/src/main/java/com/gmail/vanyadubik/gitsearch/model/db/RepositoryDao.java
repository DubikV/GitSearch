package com.gmail.vanyadubik.gitsearch.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface RepositoryDao {
    @Query("SELECT * FROM repository")
    Flowable<List<Repository>> getAll();

    @Query("SELECT COUNT(*) from repository")
    int count();

    @Query("SELECT * FROM repository WHERE id = :id")
    Repository getById(int id);

    @Query("SELECT * FROM repository WHERE owner_id = :ownerId")
    Flowable<List<Repository>> getByOwnerId(int ownerId);

    @Insert
    void insert(Repository repositories);

    @Update
    void update(Repository repository);

    @Insert
    void insertList(List<Repository> repositories);

    @Query("DELETE FROM repository")
    void deleteAll();
}
