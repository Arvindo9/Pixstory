package com.app.pixstory.ui.interests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.pixstory.R;
import com.app.pixstory.data.model.global_search.GlobalInterestData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamlesh Yadav on 27-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class GlobalInterestAdapter extends ArrayAdapter<GlobalInterestData> implements Filterable {
    private List<GlobalInterestData> list;
    private ArrayList<GlobalInterestData> itemsAll;
    private ArrayList<GlobalInterestData> suggestions;

    public GlobalInterestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        list = new ArrayList<>();
    }

    public void addItems(List<GlobalInterestData> mList) {
        list.clear();
        list.addAll(mList);
        notifyDataSetChanged();


        this.itemsAll = ((ArrayList<GlobalInterestData>) ((ArrayList<GlobalInterestData>) list).clone());
        this.suggestions = new ArrayList<>();
    }

    public void clearItems() {
        list.clear();
    }

    @NotNull
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (vi != null) {
                v = vi.inflate(R.layout.adapter_global_interest_search, null);
            }
        }
        try {
            GlobalInterestData model = list.get(position);
            if (model != null) {
                TextView customerNameLabel = v.findViewById(R.id.name);
                if (customerNameLabel != null) {
                    customerNameLabel.setText(model.getTitle());
                }
            }
        } catch (IndexOutOfBoundsException ignore) {
        }
        return v;
    }

    @NotNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((GlobalInterestData) (resultValue)).getTitle();
        }

        @NotNull
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (GlobalInterestData customer : itemsAll) {
                    if (customer.getTitle().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, @NotNull FilterResults results) {
            ArrayList<GlobalInterestData> filteredList = (ArrayList<GlobalInterestData>) results.values;
            if (results.count > 0) {
                clear();
                for (GlobalInterestData c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}

