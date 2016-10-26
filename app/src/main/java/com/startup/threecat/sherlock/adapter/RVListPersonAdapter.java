package com.startup.threecat.sherlock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.model.Person;
import com.startup.threecat.sherlock.util.LoadImage;

import java.util.ArrayList;


/**
 * Created by Dell on 15-Jul-16.
 */
public class RVListPersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static Context context;
    private ArrayList<Person> persons;
    private static final String MOVEMENT = "Movements : ";
    private OnItemClickListener listener;

    public RVListPersonAdapter(Context context, ArrayList<Person> persons) {
        this.context = context;
        this.persons = persons;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);

        public void onLongItemClick(View view, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view_person, parent, false);
        return new PersonHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final PersonHolder personHolder = (PersonHolder)holder;
        Person person = persons.get(position);
        personHolder.populate(person);

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public class PersonHolder extends RecyclerView.ViewHolder {

        ImageView imgPerson;
        TextView txtName;
        TextView txtMovements;

        public PersonHolder(View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txtPersonName);
            txtMovements = (TextView)itemView.findViewById(R.id.txtMovements);
            imgPerson = (ImageView)itemView.findViewById(R.id.imgPerson);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(v, getLayoutPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null) {
                        listener.onLongItemClick(v, getLayoutPosition());
                    }
                    return false;
                }
            });
        }

        public void populate(Person person) {
            txtName.setText(person.getName());
            txtMovements.setText(MOVEMENT + person.getMovements().size());
            LoadImage.loadImagePerson(context, imgPerson, person.getUrlImage());
        }
    }
}
