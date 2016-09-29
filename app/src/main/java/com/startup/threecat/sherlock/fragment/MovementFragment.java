package com.startup.threecat.sherlock.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.activity.MovementDetailActivity;
import com.startup.threecat.sherlock.adapter.RVMovementsAdapter;
import com.startup.threecat.sherlock.databasehelper.DatabaseUtil;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.Movement;
import com.startup.threecat.sherlock.model.Person;
import com.startup.threecat.sherlock.util.SpaceItem;

import java.util.ArrayList;

/**
 * Created by Dell on 20-Jul-16.
 */
public class MovementFragment extends Fragment{

    private Person person;
    private Context context;
    private RecyclerView rvMovements;
    private TextView txtNoMovement;
    private ArrayList<Movement> movements;
    private RVMovementsAdapter rvMovementsAdapter;

    public static MovementFragment newInstance(Person person) {

        Bundle arg = new Bundle();
        arg.putSerializable(ConstantData.PERSON_TAG, person);
        MovementFragment fragment = new MovementFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        person = (Person) arg.getSerializable(ConstantData.PERSON_TAG);
        movements = person.getMovements();
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movement_list, container, false);
        txtNoMovement = (TextView)view.findViewById(R.id.txtNoMovement);
        rvMovements = (RecyclerView)view.findViewById(R.id.rvMovements);
        initRV();
//        init recycler
        if(movements.size() == 0) {
            changeVisibleView(View.GONE, View.VISIBLE);
        }else {
            changeVisibleView(View.VISIBLE, View.GONE);
        }
        return view;
    }

    public void initRV() {
        rvMovementsAdapter = new RVMovementsAdapter(context, movements);
        rvMovementsAdapter.setListener(new RVMovementsAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                handleItemClick(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                handleLongItemClick(position);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpaceItem space = new SpaceItem(5, SpaceItem.VERTICAL);
        rvMovements.setAdapter(rvMovementsAdapter);
        rvMovements.setLayoutManager(layoutManager);
        rvMovements.addItemDecoration(space);
    }

    public void handleItemClick(int position) {

        Movement movement = movements.get(position);
        String name = person.getName();

        Intent intent = new Intent(context, MovementDetailActivity.class);
        Bundle data = new Bundle();
        data.putString(ConstantData.NAME_PERSON, name);
        data.putSerializable(ConstantData.MOVEMENT, movement);
        intent.putExtra(ConstantData.PACKAGE, data);
        context.startActivity(intent);

    }

    public void handleLongItemClick(final int position) {

        String title = "Are you want to delete this\n movement";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeMovement(position);
            }
        });
        builder.create().show();

    }

    public void removeMovement(int position) {

        Movement movement = movements.get(position);
        boolean isDel = DatabaseUtil.deleteMovement(context, movement);
        if(isDel) {
            movements.remove(position);
            rvMovementsAdapter.notifyItemRemoved(position);
        }
    }

    public RVMovementsAdapter getRvMovementsAdapter() {
        return rvMovementsAdapter;
    }

    public void changeVisibleView(int rvViewVisiblity, int txtViewVisiblity) {
        rvMovements.setVisibility(rvViewVisiblity);
        txtNoMovement.setVisibility(txtViewVisiblity);
    }



}
