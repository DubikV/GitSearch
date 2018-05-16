package com.gmail.vanyadubik.gitsearch.adapter;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gmail.vanyadubik.gitsearch.R;
import com.gmail.vanyadubik.gitsearch.model.db.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RepositoryRecyclerAdapter extends RecyclerView.Adapter {

    private static final int ANIM_DURATION = 200;

    private List<Repository> list;
    private LayoutInflater layoutInflater;
    private Context context;
    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;


    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;

        public RepositoryViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.description = (TextView) itemView.findViewById(R.id.description);
        }
    }

    public RepositoryRecyclerAdapter(Context context, List<Repository> list) {
        this.list = list;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RepositoryViewHolder(layoutInflater.inflate(R.layout.list_repo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        final Repository repository = getRepository(listPosition);
        if (repository != null) {
            ((RepositoryViewHolder) holder).name.setText(repository.getName());
            ((RepositoryViewHolder) holder).description.setText(repository.getDescription());

            ((RepositoryViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View viewPopUp = layoutInflater.inflate(R.layout.list_repo_detail, null);
                    PopupWindow window = new PopupWindow(
                            viewPopUp,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);

                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setFocusable(true);
                    window.setOutsideTouchable(true);
                    window.update();

                    window.showAtLocation(((RepositoryViewHolder) holder).itemView, Gravity.CENTER, 0, 0);

                    enterAnimation(((RepositoryViewHolder) holder).itemView, viewPopUp);

                    fillView(viewPopUp, repository, window);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Repository getRepository(int position) {
        return (Repository) list.get(position);
    }

    public View fillView(final View view, final Repository repository, final PopupWindow window) {

        ImageButton closeButton = (ImageButton) view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(window!=null){
                    if(window.isShowing()){
                        exitAnimation(view, new Runnable() {
                            public void run() {
                                window.dismiss();
                            }
                        });
                    }
                }
            }
        });

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(repository.getName());

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(repository.getDescription());

        TextView dateCreate = (TextView) view.findViewById(R.id.date_create);
        dateCreate.setText(context.getString(R.string.date_create)+ " "
                +convertDate(repository.getDateCreate()));

        TextView dateUpdate = (TextView) view.findViewById(R.id.date_update);
        dateUpdate.setText(context.getString(R.string.date_update)+ " "
                +convertDate(repository.getDateUpdate()));

        TextView size = (TextView) view.findViewById(R.id.size);
        size.setText(context.getString(R.string.size)+ " " +repository.getSize());

        TextView watchers = (TextView) view.findViewById(R.id.watchers);
        watchers.setText(context.getString(R.string.watchers)+ " " +repository.getWatchers());

        TextView url = (TextView) view.findViewById(R.id.url);
        url.setText(repository.getUrl());

        return view;
    }



    private void enterAnimation(final View viewParent, final View viewChild) {

        ViewTreeObserver observer = viewChild.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                viewChild.getViewTreeObserver().removeOnPreDrawListener(this);

                int[] screenLocation1 = new int[2];
                viewParent.getLocationOnScreen(screenLocation1);

                int thumbnailTop = screenLocation1[1];
                int thumbnailLeft = screenLocation1[0];
                int thumbnailWidth = viewParent.getWidth();
                int thumbnailHeight = viewParent.getHeight();

                int[] screenLocation = new int[2];
                viewChild.getLocationOnScreen(screenLocation);

                mLeftDelta = thumbnailLeft - screenLocation[0];
                mTopDelta = thumbnailTop - screenLocation[1];
                mWidthScale = (float) thumbnailWidth / viewChild.getWidth();
                mHeightScale = (float) thumbnailHeight / viewChild.getHeight();

                viewChild.setPivotX(0);
                viewChild.setPivotY(0);
                viewChild.setScaleX(mWidthScale);
                viewChild.setScaleY(mHeightScale);
                viewChild.setTranslationX(mLeftDelta);
                viewChild.setTranslationY(mTopDelta);

                // interpolator where the rate of change starts out quickly and then decelerates.
                TimeInterpolator sDecelerator = new DecelerateInterpolator();

                // Animate scale and translation to go from thumbnail to full size
                viewChild.animate().setDuration(ANIM_DURATION).scaleX(1).scaleY(1).
                        translationX(0).translationY(0).setInterpolator(sDecelerator);

                ObjectAnimator bgAnim = ObjectAnimator.ofInt(R.color.colorTrans, "alpha", 0, 255);
                bgAnim.setDuration(ANIM_DURATION);
                bgAnim.start();

                return true;
            }
        });
    }

    private void exitAnimation(View view, final Runnable endAction) {

        TimeInterpolator sInterpolator = new AccelerateInterpolator();
        view.animate().setDuration(ANIM_DURATION).scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta)
                .setInterpolator(sInterpolator).withEndAction(endAction);

        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(R.color.colorTrans, "alpha", 0);
        bgAnim.setDuration(ANIM_DURATION);
        bgAnim.start();
    }

    private String convertDate(String dateString){

        SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = spf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(newDate == null){
            return dateString;
        }
        spf= new SimpleDateFormat("yyyy MM dd");
        return spf.format(newDate);
    }
}
