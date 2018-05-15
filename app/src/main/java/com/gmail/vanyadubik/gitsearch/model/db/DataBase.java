package com.gmail.vanyadubik.gitsearch.model.db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Repository.class, Owner.class, SearchHistory.class},
        version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
        public abstract RepositoryDao repositoryDao();
        public abstract OwnerDao ownerDao();
        public abstract SearchHistoryDao searchHistoryDao();

        @Override
        protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
            return null;
        }

        @Override
        protected InvalidationTracker createInvalidationTracker() {
            return null;
        }
}
