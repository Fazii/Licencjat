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

        holder.textViewId.setText(listItem.getId());
        holder.textViewBookId.setText(listItem.getBookId());
        holder.textViewAuthor.setText(listItem.getAuthor());
        holder.textViewCommentDate.setText(listItem.getCommentDate());
        holder.textViewBookComment.setText(listItem.getBookComment());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentDetails.class);
                intent.putExtra("id", listItem.getId());
                intent.putExtra("book_id", listItem.getBookId());
                intent.putExtra("author", listItem.getAuthor());
                intent.putExtra("comment_date", listItem.getCommentDate());
                intent.putExtra("book_comment", listItem.getBookComment());


                ((Activity) context).startActivityForResult(intent, DELETE_COMMENT_REQUEST);


            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId;
        TextView textViewBookId;
        TextView textViewAuthor;
        TextView textViewCommentDate;
        TextView textViewBookComment;
        LinearLayout linearLayout;


        ViewHolder(View itemView) {
            super(itemView);

            textViewId = (TextView) itemView.findViewById(R.id.textVievId);
            textViewBookId = (TextView) itemView.findViewById(R.id.textVievBookId);
            textViewAuthor = (TextView) itemView.findViewById(R.id.textVievAuthor);
            textViewCommentDate = (TextView) itemView.findViewById(R.id.textVievCommentDate);
            textViewBookComment = (TextView) itemView.findViewById(R.id.textVievBookComment);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }


}
