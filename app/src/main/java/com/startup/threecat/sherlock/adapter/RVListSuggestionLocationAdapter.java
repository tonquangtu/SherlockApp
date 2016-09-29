package com.startup.threecat.sherlock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.model.InfoLocation;

import java.util.ArrayList;

/**
 * Created by Dell on 21-Jul-16.
 */
public class RVListSuggestionLocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<InfoLocation> infoLocations;
    Context context;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public RVListSuggestionLocationAdapter(Context context, ArrayList<InfoLocation> infoLocations) {
        this.infoLocations = infoLocations;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.card_view_suggestion_location, parent, false);
        return new MovementHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MovementHolder movementHolder = (MovementHolder)holder;
        movementHolder.populate(infoLocations.get(position));
    }

    @Override
    public int getItemCount() {
        return infoLocations.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class MovementHolder extends RecyclerView.ViewHolder {

        TextView txtMovementNote;
        TextView txtMovementLocation;

        public MovementHolder(final View itemView) {
            super(itemView);

            txtMovementLocation = (TextView)itemView.findViewById(R.id.txt_card_movement_location);
            txtMovementNote = (TextView)itemView.findViewById(R.id.txt_card_movement_note);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });
        }

        public void populate(InfoLocation infoLocation) {
            txtMovementNote.setText(infoLocation.getName());
            txtMovementLocation.setText(infoLocation.getVicinity());
        }
    }

}
