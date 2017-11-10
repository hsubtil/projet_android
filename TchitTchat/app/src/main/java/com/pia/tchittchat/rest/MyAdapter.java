package com.pia.tchittchat.rest;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

        import android.view.LayoutInflater;
        import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pia.tchittchat.R;
import com.pia.tchittchat.model.Attachment;
import com.pia.tchittchat.model.Messages;
import com.pia.tchittchat.view.MainActivity;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 20/10/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Messages> dataset;
    private Context ActivityContext;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sender;
        public TextView message;
        public ImageView attachement;


        public ViewHolder(View v) {
            super(v);
            sender = v.findViewById(R.id.sender);
            message = v.findViewById(R.id.message);
            attachement = v.findViewById(R.id.attachement);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Messages> dataset, Context ActivityContext) {

        this.dataset = dataset;
        this.ActivityContext = ActivityContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.sender.setText(dataset.get(position).getLogin());
        holder.message.setText(dataset.get(position).getMessage());
        if(dataset.get(position).getImage() != null){
            if(dataset.get(position).getImage().length != 0)
            if(ActivityContext instanceof MainActivity){
                ((MainActivity)ActivityContext).getMessageAttachments(dataset.get(position),holder.attachement);
            }
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }




}