package com.gmail.vanyadubik.gitsearch.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.gmail.vanyadubik.gitsearch.R;
import com.gmail.vanyadubik.gitsearch.adapter.RepositoryRecyclerAdapter;
import com.gmail.vanyadubik.gitsearch.app.GSApplication;
import com.gmail.vanyadubik.gitsearch.model.db.Owner;
import com.gmail.vanyadubik.gitsearch.model.db.OwnerDao;
import com.gmail.vanyadubik.gitsearch.model.db.Repository;
import com.gmail.vanyadubik.gitsearch.model.db.RepositoryDao;
import com.gmail.vanyadubik.gitsearch.utils.ActivityUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ReposActivity extends AppCompatActivity{

    public static final String REPOS_ACTIVITY_PARAM_OWNER_ID = "repos_activity_param_owner_id";

    @Inject
    ActivityUtils activityUtils;
    @Inject
    RepositoryDao repositoryDao;
    @Inject
    OwnerDao ownerDao;

    private RecyclerView recyclerView;
    private RepositoryRecyclerAdapter adapter;
    private Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        ((GSApplication) getApplication()).getComponent().inject(this);

        getSupportActionBar().setHomeButtonEnabled(true);

        final FloatingActionButton ownerUpFab = (FloatingActionButton) findViewById(R.id.owner_up);
        ownerUpFab.setVisibility(View.GONE);
        ownerUpFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.getLayoutManager().scrollToPosition(0);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.list_repos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                OrientationHelper.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView != null && recyclerView.getChildCount() > 0
                        && layoutManager.findFirstVisibleItemPosition() == 0) {

                    if (recyclerView.getChildAt(0).getTop() <
                            -activityUtils.dpToPx(ReposActivity.this, 16)) {

                        ownerUpFab.animate().translationX(0)
                                .setInterpolator(new LinearInterpolator()).start();

                        ownerUpFab.setVisibility(View.VISIBLE);
                    } else {
                        ownerUpFab.animate().translationX(ownerUpFab.getWidth() +
                                getResources().getDimension(R.dimen.margin_fab))
                                .setInterpolator(new LinearInterpolator()).start();

                        ownerUpFab.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int ownerId = extras.getInt(REPOS_ACTIVITY_PARAM_OWNER_ID);
            ownerDao.getFlowableById(ownerId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Owner>() {
                        @Override
                        public void accept(Owner owner1) throws Exception {
                            owner = owner1;

                            if(owner==null) {
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.owner_not_found),
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }

                            initData();
                        }

                    });
        } else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.owner_not_found),
                    Toast.LENGTH_LONG).show();
            finish();
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void initData(){
        repositoryDao.getByOwnerId(owner.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Repository>>() {
                    @Override
                    public void accept(List<Repository> repositories) throws Exception {

                        SpannableString spanString = new SpannableString(owner.getLogin()
                                +" "+getString(R.string.repositories_title)+" ("+repositories.size()+")");
                        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);

                        getSupportActionBar().setTitle(spanString);

                        adapter = new RepositoryRecyclerAdapter(ReposActivity.this, repositories);
                        recyclerView.setAdapter(adapter);

                    }

                });

    }
}
