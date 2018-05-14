package com.gmail.vanyadubik.gitsearch.modules;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.gmail.vanyadubik.gitsearch.model.db.DataBase;
import com.gmail.vanyadubik.gitsearch.model.db.RepositoryDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataApiModule {

    private Application application;

    public DataApiModule(Application application) {
        this.application = application;
    }

    @Singleton @Provides
    public Application provideContext(){
        return application;
    }

    @Provides
    @Singleton
    public DataBase getDataRepository() {
        return Room.databaseBuilder(application.getBaseContext(), DataBase.class, "reposearch-db").build();
    }

    @Singleton
    @Provides
    public RepositoryDao provideRepositoryDao(DataBase dataBase){
        return dataBase.repositoryDao();
    }
}
