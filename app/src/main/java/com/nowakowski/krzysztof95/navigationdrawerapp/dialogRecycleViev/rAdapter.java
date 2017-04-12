package com.nowakowski.krzysztof95.navigationdrawerapp.dialogRecycleViev;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nowakowski.krzysztof95.navigationdrawerapp.ListItem;
import com.nowakowski.krzysztof95.navigationdrawerapp.R;
import com.nowakowski.krzysztof95.navigationdrawerapp.transform.DateTransform;

import java.util.ArrayList;
import java.util.List;

public class rAdapter extends RecyclerView.Adapter<rHolder> {

    private List<ListItem> listItems = new ArrayList<>();
    private Context context;
    private DateTransform dateTransform = new DateTransform();

    public rAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public rHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_list_item, parent, false);
        return new rHolder(v);
    }

    @Override
    public void onBindViewHolder(rHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        holder.nameTextView.setText(listItem.getUser_name());
        holder.dateTextView.setText(dateTransform.countDownToDate(listItem.getJoin_time()));
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }
}
