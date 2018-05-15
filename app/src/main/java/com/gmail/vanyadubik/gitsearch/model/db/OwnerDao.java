package com.gmail.vanyadubik.gitsearch.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface OwnerDao {
    @Query("SELECT * FROM owner")
    Flowable<List<Owner>> getAll();

    @Query("SELECT COUNT(*) from owner")
    int count();

    @Query("SELECT * FROM owner WHERE id = :id")
    Flowable<Owner> getFlowableById(int id);

    @Query("SELECT * FROM owner WHERE id = :id")
    Owner getById(int id);

    @Insert
    void insert(Owner... owners);

    @Insert
    void insertList(List<Owner> owners);

    @Query("DELETE FROM Owner")
    void deleteAll();
}
