package com.rb.workcatalog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rb.workcatalog.R;

import java.util.ArrayList;

/**
 * Created by Programmer on 20/04/17.
 */

public class MappAdapter extends BaseAdapter {
    private LayoutInflater inflater=null;
    ArrayList<String> data;
    Context context;
    public MappAdapter(Context ctx, ArrayList<String> items){
        this.data=items;
        this.context = ctx;
        this.inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = data.get(position);
        View vi=convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.mapp_row, parent, false);
        }
        TextView mappTv=(TextView)vi.findViewById(R.id.mappTextView);
        mappTv.setText(item);


        return vi;
    }
}
