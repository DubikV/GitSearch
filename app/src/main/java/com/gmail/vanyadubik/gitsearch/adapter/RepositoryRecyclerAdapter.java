package com.gmail.vanyadubik.gitsearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.vanyadubik.gitsearch.R;
import com.gmail.vanyadubik.gitsearch.model.db.Repository;

import java.util.List;

public class RepositoryRecyclerAdapter extends RecyclerView.Adapter {

    private List<Repository> list;
    private LayoutInflater layoutInflater;
    private Context context;


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

        Repository repository = getRepository(listPosition);
        if (repository != null) {
            ((RepositoryViewHolder) holder).name.setText(repository.getName());
            ((RepositoryViewHolder) holder).description.setText(repository.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Repository getRepository(int position) {
        return (Repository) list.get(position);
    }
}
