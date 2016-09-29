package com.startup.threecat.sherlock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.model.Movement;

import java.util.ArrayList;

/**
 * Created by Dell on 20-Jul-16.
 */
public class RVMovementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Movement> movements;
    private Context context;
    private OnItemListener listener;

    public RVMovementsAdapter(Context context, ArrayList<Movement> movements) {
        this.context = context;
        this.movements = movements;
    }

    public void setListener(OnItemListener listener) {
        this.listener = listener;
    }

    public interface OnItemListener  {

        public void onItemClick(View view, int position);

        public void onLongItemClick(View view, int position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view_movement, parent, false);
        return new MovementHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MovementHolder movementHolder = (MovementHolder)holder;
        Movement movement = movements.get(position);
        movementHolder.populate(movement);
    }

    @Override
    public int getItemCount() {
        return movements.size();
    }

    private class MovementHolder extends RecyclerView.ViewHolder {

        TextView txtMovementNote;
        TextView txtMovementLocation;
        TextView txtMovementTime;

        public MovementHolder(final View itemView) {
            super(itemView);

            txtMovementLocation = (TextView)itemView.findViewById(R.id.txt_movement_location);
            txtMovementNote = (TextView)itemView.findViewById(R.id.txt_movement_note);
            txtMovementTime = (TextView)itemView.findViewById(R.id.txt_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener!= null) {
                        listener.onLongItemClick(itemView, getLayoutPosition());
                    }
                    return false;
                }
            });
        }

        public void populate(Movement movement) {
            txtMovementNote.setText(movement.getMovementNote());
            txtMovementLocation.setText(movement.getLocation());
            txtMovementTime.setText(movement.getTime());
        }
    }
}

