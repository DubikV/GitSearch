package com.gmail.vanyadubik.gitsearch.app;


import android.app.Application;

import com.gmail.vanyadubik.gitsearch.component.DIComponent;
import com.gmail.vanyadubik.gitsearch.component.DaggerDIComponent;
import com.gmail.vanyadubik.gitsearch.modules.ActivityUtilsApiModule;
import com.gmail.vanyadubik.gitsearch.modules.DataApiModule;
import com.gmail.vanyadubik.gitsearch.modules.ErrorUtilsApiModule;
import com.gmail.vanyadubik.gitsearch.modules.NetworkUtilsApiModule;
import com.gmail.vanyadubik.gitsearch.modules.ServiceApiModule;

public class GSApplication extends Application {

    DIComponent diComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        diComponent = DaggerDIComponent.builder()
                .dataApiModule(new DataApiModule(this))
                .serviceApiModule(new ServiceApiModule(this))
                .activityUtilsApiModule(new ActivityUtilsApiModule())
                .networkUtilsApiModule(new NetworkUtilsApiModule(this))
                .errorUtilsApiModule(new ErrorUtilsApiModule(this))
                .build();

    }

    public DIComponent getComponent() {
        return diComponent;
    }

}
