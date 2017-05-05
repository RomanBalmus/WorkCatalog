package com.rb.workcatalog.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rb.workcatalog.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Programmer on 20/04/17.
 */

public class ConfigurationRecyclerAdapter extends
        RecyclerView.Adapter<ConfigurationRecyclerAdapter.ViewHolder> {
    // Store a member variable for the contacts
    ArrayList<Map.Entry> data;
    // Store the context for easy access
    private Context context;

    // Pass in the contact array into the constructor
    public ConfigurationRecyclerAdapter(Context ctx, ArrayList<Map.Entry> items) {
        this.data=items;
        this.context = ctx;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return this.context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.configuration_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ConfigurationRecyclerAdapter.ViewHolder holder, final int position) {
// Get the data model based on position
        final Map.Entry item = data.get(position);

        // Set item views based on your views and data model
        TextView titleLbl = holder.titleLbl;
        EditText valueEt = holder.valueEt;
        holder.valueEt.setTag(position);

        titleLbl.setText(item.getKey().toString());
        valueEt.setText(item.getValue().toString());


        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onScoreSavedListener!=null){
                    Map.Entry iinME= data.get((Integer) holder.valueEt.getTag());
                    iinME.setValue(s.toString());
                    onScoreSavedListener.onScoreSaved(iinME,position);
                }
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        Button deleteBtn ;
        TextView titleLbl;
        EditText valueEt ;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View vi) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(vi);

            titleLbl = (TextView)vi.findViewById(R.id.labelPrompt);
            valueEt = (EditText)vi.findViewById(R.id.valueET);
            deleteBtn = (Button)vi.findViewById(R.id.deleteCfgBtn);
        }
    }
    private OnLabelChangedListener onScoreSavedListener;
    public interface OnLabelChangedListener {
        public void onScoreSaved(Map.Entry me,int position);
    }
    // ALLOWS YOU TO SET LISTENER && INVOKE THE OVERIDING METHOD
    // FROM WITHIN ACTIVITY
    public void setOnScoreSavedListener(OnLabelChangedListener listener) {
        onScoreSavedListener = listener;
    }
}
