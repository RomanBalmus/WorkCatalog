package com.rb.workcatalog.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rb.workcatalog.R;
import com.rb.workcatalog.models.MappObject;

import java.util.ArrayList;

/**
 * Created by Programmer on 21/04/17.
 */

public class TwoWaySectionAdapter extends BaseAdapter {
    ArrayList<MappObject> data;
    Context context;
    private LayoutInflater inflater=null;

    public TwoWaySectionAdapter(Context ctx, ArrayList<MappObject> paths){
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
    public MappObject getItem(int position) {
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
        final MappObject path= data.get(position);


        TextView textView = (TextView)vi.findViewById(R.id.rowDescriptionTextView);

        textView.setText(path.getCreated_at());
        final ImageView other= (ImageView)vi.findViewById(R.id.overImageView);

        other.setVisibility(View.VISIBLE);
        final ImageView imgTwvoWay= (ImageView)vi.findViewById(R.id.thumbnailContainer);
        imgTwvoWay.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String pathStr = path.getGalleryImagePath();
/*if (pathStr!=null) {
    if (!TextUtils.isEmpty(pathStr)) {

    }else{
        pathStr = "http://www.myiconfinder.com/uploads/iconsets/256-256-30143a6a9d834ffff13944de73b1bfbd.png";

    }
}else{
    pathStr = "http://www.myiconfinder.com/uploads/iconsets/256-256-30143a6a9d834ffff13944de73b1bfbd.png";

}*/
        Glide.with(context).load(pathStr).placeholder(android.R.drawable.stat_notify_sync_noanim).error(android.R.drawable.stat_notify_sync_noanim).crossFade().fitCenter().dontAnimate().skipMemoryCache(false).into(imgTwvoWay);


        /*final Button deleteBtn=(Button)vi.findViewById(R.id.deleteBtn);

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
        });*/

        return vi;
    }
}
