package com.example.shc_notice;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ItemClickListener listener;
    private final Context context;
    public TextView NoticeTitle,datetime;


    public Adapter(@NonNull View itemView) {
        super(itemView);
        context=itemView.getContext();
        NoticeTitle=itemView.findViewById(R.id.title);
        datetime=itemView.findViewById(R.id.timedate);
    }

    @Override
    public void onClick(View view) {

        listener.onClick(view,getAdapterPosition(),false);
    }
}
