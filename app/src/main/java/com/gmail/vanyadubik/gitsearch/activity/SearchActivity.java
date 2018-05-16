package com.gmail.vanyadubik.gitsearch.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gmail.vanyadubik.gitsearch.R;
import com.gmail.vanyadubik.gitsearch.adapter.OrgRecyclerAdapter;
import com.gmail.vanyadubik.gitsearch.adapter.SearchHistoryListAdapter;
import com.gmail.vanyadubik.gitsearch.app.GSApplication;
import com.gmail.vanyadubik.gitsearch.model.db.DataBase;
import com.gmail.vanyadubik.gitsearch.model.db.Owner;
import com.gmail.vanyadubik.gitsearch.service.sync.SyncIntentService;
import com.gmail.vanyadubik.gitsearch.service.sync.SyncReceiver;
import com.gmail.vanyadubik.gitsearch.utils.ActivityUtils;
import com.gmail.vanyadubik.gitsearch.utils.ErrorUtils;
import com.gmail.vanyadubik.gitsearch.utils.NetworkUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.gmail.vanyadubik.gitsearch.common.Consts.STATUS_ERROR_SYNC;
import static com.gmail.vanyadubik.gitsearch.common.Consts.STATUS_FINISHED_SYNC;
import static com.gmail.vanyadubik.gitsearch.common.Consts.STATUS_STARTED_SYNC;
import static com.gmail.vanyadubik.gitsearch.service.sync.SyncIntentService.SYNC_FILTER_SEARCH;
import static com.gmail.vanyadubik.gitsearch.service.sync.SyncIntentService.SYNC_RECEIVER;

public class SearchActivity extends AppCompatActivity implements SyncReceiver.Receiver {

    public static final int DURATION = 400;
    public static final long TIME_DEPLAY = 1500;

    @Inject
    DataBase dataBase;
    @Inject
    ActivityUtils activityUtils;
    @Inject
    NetworkUtils networkUtils;
    @Inject
    ErrorUtils errorUtils;

    private RecyclerView recyclerView;
    private LinearLayout contSearch;
    private ProgressBar progressBar;
    private AppCompatAutoCompleteTextView searchET;
    private SearchHistoryListAdapter searhHistAdapter;
    private OrgRecyclerAdapter adapter;
    private List<Owner> ownersList;
    private List<String> searhHist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ((GSApplication) getApplication()).getComponent().inject(this);

        contSearch = (LinearLayout) findViewById(R.id.container_search);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        final FloatingActionButton ownerUpFab = (FloatingActionButton) findViewById(R.id.owner_up);
        ownerUpFab.setVisibility(View.GONE);
        ownerUpFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.getLayoutManager().scrollToPosition(0);
            }
        });

        final Handler mHandler = new Handler() {
            public void handleMessage(Message msg){
                searchET.dismissDropDown();
                searchData();
            }};
        searchET = (AppCompatAutoCompleteTextView) findViewById(R.id.search_text);
        searchET.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;
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
            boolean isTyping = false;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            private Timer timer = new Timer();
            @Override
            public void afterTextChanged(final Editable s) {
                if(s.length()==4){
                    searchET.dismissDropDown();
                    searchData();
                }else {
                    if (!isTyping) {
                        isTyping = true;
                    }
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    isTyping = false;
                                    if(s.length()>4){
                                        mHandler.sendEmptyMessage(0);
                                    }
                                }
                            },
                            TIME_DEPLAY
                    );
                }
            }
        });
        searchET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchET.setText(searhHistAdapter.getSuggestions().get(position));
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.list_searc_result);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                OrientationHelper.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView != null && recyclerView.getChildCount() > 0 && layoutManager.findFirstVisibleItemPosition() == 0) {
                    if (recyclerView.getChildAt(0).getTop() < -activityUtils.dpToPx(SearchActivity.this, 16)) {
                        ownerUpFab.animate().translationX(0).setInterpolator(new LinearInterpolator()).start();
                        ownerUpFab.setVisibility(View.VISIBLE);
                    } else {
                        ownerUpFab.animate().translationX(ownerUpFab.getWidth() +
                                getResources().getDimension(R.dimen.margin_fab)).setInterpolator(new LinearInterpolator()).start();
                        ownerUpFab.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        dataBase.searchHistoryDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> stringList) throws Exception {
                        searhHist = stringList;
                        searhHistAdapter = new SearchHistoryListAdapter(SearchActivity.this, searhHist);
                        searchET.setThreshold(1);
                        searchET.setAdapter(searhHistAdapter);
                    }

                });

    }

    private void startSync(){

        SyncReceiver mReceiver = new SyncReceiver(new Handler(), this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SyncIntentService.class);
        intent.putExtra(SYNC_RECEIVER, mReceiver);
        intent.putExtra(SYNC_FILTER_SEARCH, searchET.getText().toString());
        startService(intent);
    }

    private void searchData(){

        if (TextUtils.isEmpty(searchET.getText().toString())) {
            activityUtils.showMessage(getResources().getString(R.string.text_search_empry), this);
            return;
        }

        dataBase.searchTextDataDao().getLastText()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String text){
                        if(searchET.getText().toString().equalsIgnoreCase(text)) {
                            initSearchData();
                        }else{
                            startSync();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        startSync();
                    }
                });
    }

    private void initSearchData(){

        progressBar.setVisibility(View.VISIBLE);
        dataBase.ownerDao().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Owner>>() {
                    @Override
                    public void accept(List<Owner> owners) throws Exception {
                        ownersList = owners;
                        dataBase.searchHistoryDao().getAll()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<String>>() {
                                    @Override
                                    public void accept(List<String> stringList) throws Exception {
                                        searhHist = stringList;
                                        if(searhHistAdapter!=null){
                                            searhHistAdapter.notifyDataSetChanged();
                                        }else{
                                            searhHistAdapter = new SearchHistoryListAdapter(SearchActivity.this, searhHist);
                                        }
                                    }

                                });

                        hideViewLoad();

                        if (ownersList.size()==0) {
                            activityUtils.showMessage(getResources().getString(R.string.error_data_not_found), SearchActivity.this);
                        }
                        adapter = new OrgRecyclerAdapter(SearchActivity.this, ownersList);
                        recyclerView.setAdapter(adapter);

                    }

                });

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case STATUS_STARTED_SYNC:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case STATUS_FINISHED_SYNC:
                initSearchData();
                break;
            case STATUS_ERROR_SYNC:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                activityUtils.showMessage(error, this);
                progressBar.setVisibility(View.GONE);
                break;
        }

    }

    private void hideViewLoad(){

        progressBar.setVisibility(View.GONE);
        if (ownersList.size()>0) {
            recyclerView.setVisibility(View.VISIBLE);
            ObjectAnimator objectanimator = ObjectAnimator.ofFloat(contSearch,"y",
                    getResources().getDimension(R.dimen.margin_cap));
            objectanimator.setDuration(DURATION);
            objectanimator.start();
        } else {
            recyclerView.setVisibility(View.GONE);
            ObjectAnimator objectanimator = ObjectAnimator.ofFloat(contSearch,"y",
                    getResources().getDimension(R.dimen.margin_cap_top));
            objectanimator.setDuration(DURATION);
            objectanimator.start();
        }

//        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
//        layoutParams.setMargins(
//                (int) getResources().getDimension(R.dimen.margin_cap),
//                contSearch.getHeight(),
//                (int) getResources().getDimension(R.dimen.margin_cap),
//                (int) getResources().getDimension(R.dimen.margin_cap));
//
//        recyclerView.setLayoutParams(layoutParams);
    }


}
