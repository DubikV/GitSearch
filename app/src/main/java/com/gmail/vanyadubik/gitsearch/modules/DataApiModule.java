package com.gmail.vanyadubik.gitsearch.modules;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.gmail.vanyadubik.gitsearch.model.db.DataBase;
import com.gmail.vanyadubik.gitsearch.model.db.OwnerDao;
import com.gmail.vanyadubik.gitsearch.model.db.RepositoryDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.gmail.vanyadubik.gitsearch.common.Consts.NAME_DATA_BASE_ROOM;

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
        return Room.databaseBuilder(application.getBaseContext(), DataBase.class, NAME_DATA_BASE_ROOM).build();
    }

    @Singleton
    @Provides
    public RepositoryDao provideRepositoryDao(DataBase dataBase){
        return dataBase.repositoryDao();
    }

    @Singleton
    @Provides
    public OwnerDao provideOwnerDao(DataBase dataBase){
        return dataBase.ownerDao();
    }
}
