package com.example.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter implements Filterable{

    private Activity activity;
    private ArrayList<DataContract.DataObject> filteredList;
    private LayoutInflater mInflater;
    private SearchFilter searchFilter;
    DataControl DC;

    public SearchAdapter(Activity activity) {
        this.activity = activity;
        this.filteredList = new ArrayList<>();
        DC = MainActivity.DC;
        mInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.search_list_item, parent, false);
        final DataContract.DataObject found = (DataContract.DataObject) getItem(position);
        ((TextView) rowView.findViewById((R.id.strID))).setText("Device ID: " + found.id);
        ((TextView) rowView.findViewById((R.id.strEmail))).setText("Email: "+ found.email);
        ((TextView) rowView.findViewById((R.id.strReg))).setText("Registered: " + Boolean.toString(found.reg));
        ((TextView) rowView.findViewById((R.id.strServer))).setText("Server: " + found.server);
        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (searchFilter == null) searchFilter = new SearchFilter();
        return searchFilter;
    }

    private class SearchFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            String[] token = String.valueOf(constraint).split(" ");
            if (constraint != null && constraint.length()>0 && token.length == 2) {
                ArrayList<DataContract.DataObject> tmp = new ArrayList<DataContract.DataObject>();
                for (DataContract.DataObject o : DC.existES(token[0],token[1])) {
                    tmp.add(o);
                }
                filterResults.count = tmp.size();
                filterResults.values = tmp;
            } else {
                filterResults.count = 0;
                filterResults.values = new ArrayList<>();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<DataContract.DataObject>) results.values;
            notifyDataSetChanged();
        }
    }

    public DataContract.DataObject getSearchItem(int i) {
        return filteredList.get(i);
    }
}
