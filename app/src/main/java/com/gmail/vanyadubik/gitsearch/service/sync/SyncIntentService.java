package com.gmail.vanyadubik.gitsearch.service.sync;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.gmail.vanyadubik.gitsearch.R;
import com.gmail.vanyadubik.gitsearch.app.GSApplication;
import com.gmail.vanyadubik.gitsearch.model.APIError;
import com.gmail.vanyadubik.gitsearch.model.db.DataBase;
import com.gmail.vanyadubik.gitsearch.model.db.Owner;
import com.gmail.vanyadubik.gitsearch.model.db.Repository;
import com.gmail.vanyadubik.gitsearch.model.db.SearchHistory;
import com.gmail.vanyadubik.gitsearch.model.db.SearchTextData;
import com.gmail.vanyadubik.gitsearch.model.json.DownloadResponse;
import com.gmail.vanyadubik.gitsearch.model.json.OwnerDTO;
import com.gmail.vanyadubik.gitsearch.model.json.ResultItemDTO;
import com.gmail.vanyadubik.gitsearch.utils.ErrorUtils;
import com.gmail.vanyadubik.gitsearch.utils.NetworkUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;

import static com.gmail.vanyadubik.gitsearch.common.Consts.STATUS_ERROR_SYNC;
import static com.gmail.vanyadubik.gitsearch.common.Consts.STATUS_FINISHED_SYNC;
import static com.gmail.vanyadubik.gitsearch.common.Consts.STATUS_STARTED_SYNC;
import static com.gmail.vanyadubik.gitsearch.utils.Json2DbModelConverter.convertOwner;
import static com.gmail.vanyadubik.gitsearch.utils.Json2DbModelConverter.convertRepository;


public class SyncIntentService extends IntentService {
    public static final String SYNC_RECEIVER = "sync_receiver";
    public static final String SYNC_FILTER_SEARCH = "sync_filter_search";

    @Inject
    DataBase dataBase;
    @Inject
    NetworkUtils networkUtils;
    @Inject
    ErrorUtils errorUtils;

    private SyncService syncService;
    private String searchFilter;
    private ResultReceiver receiver;
    private Bundle bundle;

    public SyncIntentService() {
        super(SyncIntentService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((GSApplication) getApplication()).getComponent().inject(this);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        receiver = intent.getParcelableExtra(SYNC_RECEIVER);
        searchFilter = intent.getStringExtra(SYNC_FILTER_SEARCH);
        bundle = new Bundle();

        if (!networkUtils.checkEthernet()) {
            bundle.putString(Intent.EXTRA_TEXT, getResources().getString(R.string.error_internet_connecting));
            receiver.send(STATUS_ERROR_SYNC, bundle);
            return;
        }

        Map<String, String> paramsUrl = new HashMap<String, String>();
        paramsUrl.put("q", searchFilter);
        paramsUrl.put("sort", "stars");
        paramsUrl.put("order", "desc");

        syncService = SyncServiceFactory.createService(
                SyncService.class,
                this.getBaseContext());
        receiver.send(STATUS_STARTED_SYNC, bundle);
        try {
            Response<DownloadResponse> downloadResponse = syncService.search(paramsUrl).execute();
            if (downloadResponse.isSuccessful()) {
                DownloadResponse body = downloadResponse.body();
                if(body == null) {
                    bundle.putString(Intent.EXTRA_TEXT, getResources().getString(R.string.error_no_data_search));
                    receiver.send(STATUS_ERROR_SYNC, bundle);
                    return;
                }
                if(body.getItems().size()==0) {
                    bundle.putString(Intent.EXTRA_TEXT, getResources().getString(R.string.error_no_data_search));
                    receiver.send(STATUS_ERROR_SYNC, bundle);
                    return;
                }
                if(updateDb(body)) {
                    receiver.send(STATUS_FINISHED_SYNC, bundle);
                }
            } else {
                APIError error = errorUtils.parseErrorCode(downloadResponse.code());
                bundle.putString(Intent.EXTRA_TEXT, error.getMessage());
                receiver.send(STATUS_ERROR_SYNC, bundle);

            }
        } catch (Exception exception) {
            APIError error = errorUtils.parseErrorMessage(exception);
            bundle.putString(Intent.EXTRA_TEXT, error.getMessage());
            receiver.send(STATUS_ERROR_SYNC, bundle);
        }

    }

    private boolean updateDb(DownloadResponse response) {
        List<ResultItemDTO> resultItemDTOList = response.getItems();

//        dataBase.ownerDao().deleteAll();
//        dataBase.repositoryDao().deleteAll();
//        dataBase.searchTextDataDao().deleteAll();
        dataBase.searchHistoryDao().delete(searchFilter);
        dataBase.searchHistoryDao().insert(new SearchHistory(searchFilter, new Date().getTime()));


        for (ResultItemDTO resultItemDTO : resultItemDTOList) {
            Repository repository = convertRepository(resultItemDTO);
            if(dataBase.repositoryDao().getById(repository.getId())==null) {
                dataBase.repositoryDao().insert(repository);
            }

            try {
                Response<OwnerDTO> ownerResponse = syncService.getOwner(resultItemDTO.getOwner().getUrl()).execute();
                if (ownerResponse.isSuccessful()) {
                    updateOwner(ownerResponse.body());
                } else {
                    APIError error = errorUtils.parseErrorCode(ownerResponse.code());
                    bundle.putString(Intent.EXTRA_TEXT, error.getMessage());
                    receiver.send(STATUS_ERROR_SYNC, bundle);
                    return false;
                }
            } catch (Exception exception) {
                APIError error = errorUtils.parseErrorMessage(exception);
                bundle.putString(Intent.EXTRA_TEXT, error.getMessage());
                receiver.send(STATUS_ERROR_SYNC, bundle);
                return false;
            }

        }

        try {
            dataBase.searchTextDataDao().insert(new SearchTextData(searchFilter));
        } catch (Exception exception) {
            dataBase.searchTextDataDao().update(new SearchTextData(searchFilter));
        }

        return true;
    }

    private void updateOwner(OwnerDTO ownerDTO) {
        Owner owner = convertOwner(ownerDTO);
        if(dataBase.ownerDao().getById(owner.getId())==null) {
            dataBase.ownerDao().insert(owner);
        }
    }

}
