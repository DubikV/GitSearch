package com.gmail.vanyadubik.gitsearch.component;

import com.gmail.vanyadubik.gitsearch.activity.ReposActivity;
import com.gmail.vanyadubik.gitsearch.activity.SearchActivity;
import com.gmail.vanyadubik.gitsearch.modules.ActivityUtilsApiModule;
import com.gmail.vanyadubik.gitsearch.modules.DataApiModule;
import com.gmail.vanyadubik.gitsearch.modules.ErrorUtilsApiModule;
import com.gmail.vanyadubik.gitsearch.modules.NetworkUtilsApiModule;
import com.gmail.vanyadubik.gitsearch.modules.ServiceApiModule;
import com.gmail.vanyadubik.gitsearch.service.sync.SyncIntentService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataApiModule.class,
        ServiceApiModule.class, ActivityUtilsApiModule.class,
        NetworkUtilsApiModule.class, ErrorUtilsApiModule.class})
public interface DIComponent {
    void inject(SyncIntentService syncIntentService);
    void inject(SearchActivity searchActivity);
    void inject(ReposActivity reposActivity);
}
