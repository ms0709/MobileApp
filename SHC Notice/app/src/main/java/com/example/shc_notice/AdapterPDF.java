package com.example.shc_notice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPDF extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ItemClickListener listener;
    private final Context context;
    public TextView NoticeTitle,dtime;
    public ImageView dlt;

    public AdapterPDF(@NonNull View itemView) {
        super(itemView);
        context=itemView.getContext();

        NoticeTitle=itemView.findViewById(R.id.title);
        dtime=itemView.findViewById(R.id.timedate);
        dlt=itemView.findViewById(R.id.AdeleteNotice);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false );
    }

}
