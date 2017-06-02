package com.nowakowski.krzysztof95.navigationdrawerapp.dialogRecycleViev;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nowakowski.krzysztof95.navigationdrawerapp.R;


class rHolder  extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView dateTextView;

    rHolder(View itemView) {
        super(itemView);

        nameTextView = (TextView) itemView.findViewById(R.id.textViewAuthor_details);
        dateTextView = (TextView) itemView.findViewById(R.id.textViewDate_details);
    }
}
