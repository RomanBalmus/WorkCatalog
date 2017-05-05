package com.rb.workcatalog.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rb.workcatalog.R;

import java.util.ArrayList;

/**
 * Created by Programmer on 19/04/17.
 */

public class TwoWayFileAdapter extends BaseAdapter{
    ArrayList<String> data;
    Context context;
    private LayoutInflater inflater=null;

    public TwoWayFileAdapter(Context ctx, ArrayList<String> paths){
        Log.e("paths: ",""+paths);
        this.context = ctx;
        this.data=paths;
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        View vi=convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.twoway_row, parent, false);
        }
        final String path= data.get(position);
        final ImageView imgTwvoWay= (ImageView)vi.findViewById(R.id.thumbnailContainer);
        Glide.with(context).load("file://"+path).crossFade().centerCrop().dontAnimate().skipMemoryCache(false).into(imgTwvoWay);

        final Button deleteBtn=(Button)vi.findViewById(R.id.deleteBtn);

        imgTwvoWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Handler handler = new Handler(Looper.getMainLooper());
                final Runnable r2 = new Runnable() {
                    public void run() {
                        //do your stuff here after DELAY sec
                        deleteBtn.setVisibility(View.GONE);



                    }
                };

                final Runnable r = new Runnable() {
                    public void run() {
                        //do your stuff here after DELAY sec

                        deleteBtn.setVisibility(View.VISIBLE);
                        handler.postDelayed(r2,2000);

                    }
                };



                handler.post(r);


            }
        });

        return vi;
    }
}
