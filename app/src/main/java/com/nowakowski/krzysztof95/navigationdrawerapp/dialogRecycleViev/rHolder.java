package com.nowakowski.krzysztof95.navigationdrawerapp.dialogRecycleViev;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowakowski.krzysztof95.navigationdrawerapp.R;


/**
 * Created by Krzysztof on 2017-03-26.
 */

public class rHolder  extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView dateTextView;
    ImageView avatarImageView;

    public rHolder(View itemView) {
        super(itemView);

        nameTextView = (TextView) itemView.findViewById(R.id.textViewAuthor_details);
        dateTextView = (TextView) itemView.findViewById(R.id.textViewDate_details);
    }
}
