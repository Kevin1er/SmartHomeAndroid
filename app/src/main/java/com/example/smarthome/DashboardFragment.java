package com.example.smarthome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment
{
    private View DashboardView;
    private RecyclerView myDashboardList;
    private DatabaseReference RoomsRef;

    public DashboardFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DashboardView = inflater.inflate(R.layout.fragment_list, container, false);

        myDashboardList = (RecyclerView) DashboardView.findViewById(R.id.list);
        myDashboardList.setLayoutManager(new LinearLayoutManager(getContext()));

        RoomsRef = FirebaseDatabase.getInstance().getReference().child("raspberries").child("0").child("rooms");

        return DashboardView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(RoomsRef, Room.class)
                .build();

        FirebaseRecyclerAdapter<Room, RoomsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Room, RoomsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RoomsViewHolder holder, final int position, @NonNull Room model)
            {
                holder.name.setText(model.getRoomName());

                holder.itemView.findViewById(R.id.button_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String room_id = getRef(position).getKey();

                        //Intent roomIntent = new Intent(getActivity(), RoomDetails.class);
                        //roomIntent.putExtra("room_id", room_id);
                        //startActivity(roomIntent);

                        Fragment objectsList = new RoomDetailsFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("room_id", room_id);
                        objectsList.setArguments(bundle);

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, objectsList, "fragment")
                                .commit();

                    }
                });
            }

            @NonNull
            @Override
            public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.button_display_layout, viewGroup, false);
                RoomsViewHolder viewHolder = new RoomsViewHolder(view);
                return viewHolder;
            }
        };

        myDashboardList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class RoomsViewHolder extends RecyclerView.ViewHolder
    {
        Button name;

        public RoomsViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.button_name);
        }
    }
}
