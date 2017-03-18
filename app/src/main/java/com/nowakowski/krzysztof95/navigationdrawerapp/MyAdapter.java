package com.nowakowski.krzysztof95.navigationdrawerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int DELETE_COMMENT_REQUEST = 1;
    private List<ListItem> listItems = new ArrayList<>();
    private Context context;

    MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.textViewTitle.setText(listItem.getEvent_title());
        holder.textViewAuthor.setText(listItem.getEvent_author());
        holder.textViewDesc.setText(listItem.getEvent_desc());
        holder.textViewDate.setText(listItem.getEvent_time());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentDetails.class);
                intent.putExtra("id", listItem.getEvent_id());
                intent.putExtra("title", listItem.getEvent_title());
                intent.putExtra("author", listItem.getEvent_author());
                intent.putExtra("desc", listItem.getEvent_desc());
                intent.putExtra("time", listItem.getEvent_time());
                intent.putExtra("lat", listItem.getEvent_lat());
                intent.putExtra("lng", listItem.getEvent_lng());

                ((Activity) context).startActivityForResult(intent, DELETE_COMMENT_REQUEST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewDesc;
        TextView textViewDate;
        LinearLayout linearLayout;


        ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textVievTitle);
            textViewAuthor = (TextView) itemView.findViewById(R.id.textVievAuthor);
            textViewDesc = (TextView) itemView.findViewById(R.id.textVievDesc);
            textViewDate = (TextView) itemView.findViewById(R.id.textVievDate);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
