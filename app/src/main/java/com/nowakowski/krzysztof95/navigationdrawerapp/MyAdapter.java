package com.nowakowski.krzysztof95.navigationdrawerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nowakowski.krzysztof95.navigationdrawerapp.transform.CircleTransform;
import com.nowakowski.krzysztof95.navigationdrawerapp.transform.DateTransform;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int DELETE_COMMENT_REQUEST = 1;
    private List<ListItem> listItems = new ArrayList<>();
    private Context context;
    private String className;
    private SharedPreferences prefs;

    MyAdapter(List<ListItem> listItems, Context context, String className) {
        this.listItems = listItems;
        this.context = context;
        this.className = className;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);


        prefs = context.getSharedPreferences("Name", Context.MODE_PRIVATE);
        JodaTimeAndroid.init(context);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        final DateTransform dateTransform = new DateTransform();


        holder.textViewTitle.setText(listItem.getEvent_title());
        holder.textViewAuthor.setText(listItem.getEvent_author());
        holder.textViewDesc.setText(listItem.getEvent_desc());
        holder.textViewDate.setText(dateTransform.countDownToDate(listItem.getEvent_time()));
        holder.textViewStartDate.setText(dateTransform.countDownToEvent(listItem.getEvent_start_time()));
        holder.textViewJoinCounter.setText(String.valueOf(listItem.getJoined_num()));

        Picasso.with(context)
                .load(listItem.getUser_avatar()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).resize(100, 100).transform(new CircleTransform()).into(holder.imageViewAuthorPicture);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("id", listItem.getEvent_id());
                intent.putExtra("user_id", listItem.getUser_id());
                intent.putExtra("user_avatar", listItem.getUser_avatar());
                intent.putExtra("title", listItem.getEvent_title());
                intent.putExtra("author", listItem.getEvent_author());
                intent.putExtra("desc", listItem.getEvent_desc());
                intent.putExtra("time", dateTransform.countDownToDate(listItem.getEvent_time()));
                intent.putExtra("start_time", listItem.getEvent_start_time());
                intent.putExtra("lat", listItem.getEvent_lat());
                intent.putExtra("lng", listItem.getEvent_lng());
                intent.putExtra("class_name", className);


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
        TextView textViewStartDate;
        TextView textViewJoinCounter;
        LinearLayout linearLayout;
        ImageView imageViewAuthorPicture;


        ViewHolder(View itemView) {
            super(itemView);


            textViewTitle = (TextView) itemView.findViewById(R.id.textVievTitle);
            textViewAuthor = (TextView) itemView.findViewById(R.id.textVievAuthor);
            textViewDesc = (TextView) itemView.findViewById(R.id.textVievDesc);
            textViewDate = (TextView) itemView.findViewById(R.id.textVievDate);
            textViewStartDate = (TextView) itemView.findViewById(R.id.textVievStartDate);
            textViewJoinCounter = (TextView) itemView.findViewById(R.id.textVievJoinCounter);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            imageViewAuthorPicture = (ImageView) itemView.findViewById(R.id.list_item_image_view);

        }
    }
}
