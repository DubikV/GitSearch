package com.gmail.vanyadubik.gitsearch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gmail.vanyadubik.gitsearch.R;
import com.gmail.vanyadubik.gitsearch.adapter.OrgRecyclerAdapter;
import com.gmail.vanyadubik.gitsearch.app.GSApplication;
import com.gmail.vanyadubik.gitsearch.model.db.DataBase;
import com.gmail.vanyadubik.gitsearch.model.db.Owner;
import com.gmail.vanyadubik.gitsearch.model.json.DownloadResponse;
import com.gmail.vanyadubik.gitsearch.service.sync.SyncService;
import com.gmail.vanyadubik.gitsearch.service.sync.SyncServiceFactory;
import com.gmail.vanyadubik.gitsearch.utils.ActivityUtils;
import com.gmail.vanyadubik.gitsearch.utils.ErrorUtils;
import com.gmail.vanyadubik.gitsearch.utils.NetworkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {


    @Inject
    DataBase dataBase;
    @Inject
    ActivityUtils activityUtils;
    @Inject
    NetworkUtils networkUtils;
    @Inject
    ErrorUtils errorUtils;

    private RecyclerView recyclerView;
    private LinearLayout contSearch, contSync;
    private EditText searchET;
    private Observable observable;
    private OrgRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ((GSApplication) getApplication()).getComponent().inject(this);

        contSearch = (LinearLayout) findViewById(R.id.container_search);
        //contSync = (LinearLayout) findViewById(R.id.container_sync);
        //contSync.setVisibility(View.GONE);

        searchET = (EditText) findViewById(R.id.search_text);
        searchET.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable = searchET.getRight()
                            - searchET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        searchET.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>3){
                    searchData();
                }
            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.list_searc_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                OrientationHelper.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void startSync(){

        if (!networkUtils.checkEthernet()) {
            activityUtils.showMessage(getResources().getString(R.string.error_internet_connecting), this);
            return;
        }

        SyncService syncService = SyncServiceFactory.createService(
                SyncService.class, SearchActivity.this);

        Map<String, String> paramsUrl = new HashMap<String, String>();
        paramsUrl.put("q", searchET.getText().toString());
        paramsUrl.put("sort", "stars");
        paramsUrl.put("order", "desc");

        observable = syncService.download(paramsUrl);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadResponse>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(DownloadResponse downloadResponse) {
                        if(downloadResponse == null) {
                            activityUtils.showMessage(getResources().getString(R.string.error_no_data_search), SearchActivity.this);
                            contSync.setVisibility(View.GONE);
                            contSearch.setVisibility(View.VISIBLE);
                            return;
                        }
                        if(downloadResponse.getItems().size()==0) {
                            activityUtils.showMessage(getResources().getString(R.string.error_no_data_search), SearchActivity.this);
                            contSync.setVisibility(View.GONE);
                            contSearch.setVisibility(View.VISIBLE);
                            return;
                        }
                        updateDb(downloadResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!networkUtils.checkEthernet()) {
                            activityUtils.showMessage(
                                    getResources().getString(R.string.error_internet_connecting), SearchActivity.this);
                            return;
                        }
                        activityUtils.showMessage(e.getMessage(),
                                SearchActivity.this);

                        contSync.setVisibility(View.GONE);
                        contSearch.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void searchData(){

        if (TextUtils.isEmpty(searchET.getText().toString())) {
            activityUtils.showMessage(getResources().getString(R.string.text_search_empry), this);
            return;
        }

        contSync.setVisibility(View.VISIBLE);
        contSearch.setVisibility(View.GONE);
        dataBase.ownerDao().getByTextSearch(searchET.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Owner>>() {
                    @Override
                    public void accept(List<Owner> owners) throws Exception {
                        if (owners.size() == 0){
                            startSync();
                            return;
                        }

                        adapter = new OrgRecyclerAdapter(SearchActivity.this, owners);
                        recyclerView.setAdapter(adapter);

                        contSync.setVisibility(View.GONE);
                        contSearch.setVisibility(View.VISIBLE);
                    }

                });

    }

    private void updateDb(final DownloadResponse response) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                dataBase.ownerDao().deleteAll();
                dataBase.repositoryDao().deleteAll();
            //    ownerDao.insertList(convertRepositoryList(response.getItems(), searchET.getText().toString()));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable e) {}
        });
    }
}