package com.rb.workcatalog.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rb.workcatalog.R;
import com.rb.workcatalog.models.MappObject;

/**
 * Created by Programmer on 18/04/17.
 */

public class MappViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;

    public MappViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.mapp_row_textView);
       /* authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);*/
    }

    public void bindToPost(MappObject post, View.OnClickListener starClickListener) {
        titleView.setText(post.getName());
       /* authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);*/
    }
    public void bindToPost(MappObject post) {
        titleView.setText(post.getName());

    }
}