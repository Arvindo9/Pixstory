package com.app.pixstory.ui.messages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.pixstory.R;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.db.messages.MessageUsersNew;
import com.app.pixstory.utils.Constants;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Author       : Arvindo Mondal
 * Created date : 13-08-2019
 */
public class AutoSuggestAdapter extends ArrayAdapter<MessageUsersNew> implements Filterable {
    private List<MessageUsersNew> list;
    private ArrayList<MessageUsersNew> itemsAll;
    private ArrayList<MessageUsersNew> suggestions;

    public AutoSuggestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        list = new ArrayList<>();
    }

    public void addItems(List<MessageUsersNew> mList) {
        list.clear();
        list.addAll(mList);
        notifyDataSetChanged();


        this.itemsAll = ((ArrayList<MessageUsersNew>) ((ArrayList<MessageUsersNew>)list).clone());
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
            if(vi!=null) {
                v = vi.inflate(R.layout.message_new_users, null);
            }
        }
        try {
            MessageUsersNew model = list.get(position);
            if (model != null) {
                TextView customerNameLabel = v.findViewById(R.id.name);
                ImageView pic = v.findViewById(R.id.pic);
                ProgressBar progressBar = v.findViewById(R.id.progressBar);
                if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView MessageUsersNew Name:"+model.getName());
                    customerNameLabel.setText(model.getUsername());
                }
                if (model.getProfileImage() != null && !model.getProfileImage().isEmpty()) {
                    BindingUtils.setStringPhotos(pic, model.getProfileImage(), progressBar);
                } else {
                    BindingUtils.setStringPhotos(pic, Constants.DEFAULT_PROFILE_PIC_URL, progressBar);
                }
            }
        }catch (IndexOutOfBoundsException ignore){}
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
            return ((MessageUsersNew)(resultValue)).getUsername();
        }
        @NotNull
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (MessageUsersNew customer : itemsAll) {
                    if(customer.getUsername().toLowerCase().startsWith(constraint.toString().toLowerCase())){
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
            ArrayList<MessageUsersNew> filteredList = (ArrayList<MessageUsersNew>) results.values;
            if(results.count > 0) {
                clear();
                for (MessageUsersNew c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
