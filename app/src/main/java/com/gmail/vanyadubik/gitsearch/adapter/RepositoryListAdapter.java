package com.gmail.vanyadubik.gitsearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.vanyadubik.gitsearch.R;
import com.gmail.vanyadubik.gitsearch.model.db.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RepositoryListAdapter extends BaseAdapter {

    private List<Repository> list;
    private LayoutInflater layoutInflater;
    private Context context;


    public RepositoryListAdapter(Context context, List<Repository> list) {
        this.list = list;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_repo_item, parent, false);
        }
        final Repository repository = getRepository(position);


        LinearLayout nameLL = (LinearLayout) view.findViewById(R.id.repo_name_ll);
        if(!TextUtils.isEmpty(repository.getUrl())) {
            nameLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = repository.getUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }

        TextView login = (TextView) view.findViewById(R.id.repo_login);
        //login.setText(String.valueOf(repository.getOwnerLogin()));


        TextView name = (TextView) view.findViewById(R.id.repo_name);
        name.setText(repository.getName());

        TextView fullName = (TextView) view.findViewById(R.id.repo_full_name);
        if(TextUtils.isEmpty(repository.getFull_name())){
            fullName.setVisibility(View.GONE);
        }else {
            fullName.setText(String.valueOf(repository.getFull_name()));
        }

        TextView description = (TextView) view.findViewById(R.id.repo_description);
        if(TextUtils.isEmpty(repository.getDescription())){
            description.setVisibility(View.GONE);
        }else {
            description.setText(String.valueOf(repository.getDescription()));
        }

        TextView watchers = (TextView) view.findViewById(R.id.repo_watchers);
        watchers.setText(String.format("%.2f", repository.getScore()));

        TextView stars = (TextView) view.findViewById(R.id.repo_stars);
        stars.setText(String.valueOf(repository.getWatchers()));

        TextView size = (TextView) view.findViewById(R.id.repo_size);
        size.setText(String.valueOf(repository.getSize()));

        TextView dateCreate = (TextView) view.findViewById(R.id.repo_date_create);
        dateCreate.setText("Crt." + convertDate(repository.getDateCreate()));

        TextView dateUpdate = (TextView) view.findViewById(R.id.repo_date_update);
        dateUpdate.setText("Upd." + convertDate(repository.getDateUpdate()));

        ImageView mAvatar  = (ImageView) view.findViewById(R.id.avatar);
//        Glide.with(context).
//                load(repository.getOwnerAvatar() == null ? android.R.drawable.ic_menu_gallery : repository.getOwnerAvatar())
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(mAvatar);

        return view;
    }

    private Repository getRepository(int position) {
        return (Repository) getItem(position);
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
        spf= new SimpleDateFormat("dd MMM yyyy");
        return spf.format(newDate);
    }
}


