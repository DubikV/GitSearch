package com.gmail.vanyadubik.gitsearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<String> originalList;
    private List<String> suggestions = new ArrayList<>();
    private Filter filter = new CustomFilter();


    public SearchHistoryListAdapter(Context context, List<String> originalList) {
        this.context = context;
        this.originalList = originalList;
    }

    public List<String> getOriginalList() {
        return originalList;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int position) {
        return suggestions.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.select_dialog_singlechoice,
                    parent,
                    false);
            holder = new ViewHolder();
            holder.autoText = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.autoText.setText(suggestions.get(position));

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private static class ViewHolder {
        TextView autoText;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();

            if (originalList != null && constraint != null) {
                for (int i = 0; i < originalList.size(); i++) {
                    if (originalList.get(i).toLowerCase().contains(constraint)) {
                        suggestions.add(originalList.get(i));
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}