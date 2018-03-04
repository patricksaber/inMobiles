package com.something.patrick.inmobiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by patrick on 3/2/2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Item> items;

    public Adapter(List<Item> items) {

        this.items = items;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder viewholder, int position) {

        Item item = items.get(position);
        String url = item.getLink();
        viewholder.mTitle.setText(item.getTitle());
        viewholder.mDescription.setText(item.getDescription());
        Context context = viewholder.mLink.getContext();
        Picasso.with(context).load(url).into(viewholder.mLink);

    }

    @Override
    public int getItemCount() {    // Return the size of  dataset
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView    mTitle;
        TextView    mDescription;
        ImageView   mLink;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle      =   itemView.findViewById(R.id.title);
            mDescription=   itemView.findViewById(R.id.description);
            mLink       =   itemView.findViewById(R.id.imageLink);
        }
    }
}
