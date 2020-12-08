package com.example.loginregisterfire.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Common.Common;
import com.example.loginregisterfire.Interface.IRecyclerItemSelectedListener;
import com.example.loginregisterfire.Model.Section;
import com.example.loginregisterfire.R;

import java.util.ArrayList;
import java.util.List;

public class MySectionAdapter extends RecyclerView.Adapter<MySectionAdapter.MyViewHolder> {

    Context context;
    List<Section> sectionList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MySectionAdapter(Context context, List<Section> sectionList) {
        this.context = context;
        this.sectionList = sectionList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_section, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_section_name.setText(sectionList.get(i).getName());
        myViewHolder.ratingBar.setRating((float)sectionList.get(i).getRating());
        if(!cardViewList.contains(myViewHolder.card_section))
            cardViewList.add(myViewHolder.card_section);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set background for all item
                for(CardView cardView : cardViewList)
                {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }

                //Set background for choice
                myViewHolder.card_section.setCardBackgroundColor(
                        context.getResources()
                        .getColor(android.R.color.holo_orange_dark)
                );

                //Send Local broadcast to enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_SECTION_SELECTED, sectionList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager .sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_section_name;
        RatingBar ratingBar;
        CardView card_section;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_section = (CardView)itemView.findViewById(R.id.card_section);
            txt_section_name = (TextView) itemView.findViewById(R.id.txt_section_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_section);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
