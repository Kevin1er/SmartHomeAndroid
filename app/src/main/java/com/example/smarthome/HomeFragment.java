package com.example.smarthome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{
    private View HomeView;
    private DatabaseReference RoomsRef;
    private DatabaseReference HomeRef;
    private final List<Object> list_objects;
    private final List<Long> list_objects_id;
    private final List<Object> list;
    private RecyclerView recyclerView;

    public HomeFragment() {
        list_objects = new ArrayList<>();
        list_objects_id = new ArrayList<>();
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HomeView =  inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) HomeView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HomeFragmentAdapter());

        HomeRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("home");
        HomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> objects_id = dataSnapshot.getChildren();
                list_objects_id.clear();

                for(DataSnapshot sid: objects_id)
                {
                    list_objects_id.add((Long) sid.getValue());
                }

                list.clear();
                for(Long id: list_objects_id)
                {
                    for(Object o: list_objects)
                    {
                        if(o.getId() == id)
                            list.add(o);
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                System.out.println("--------------> " + list.size());

                //for(Long i: list_objects_id) System.out.println("-> " + i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        RoomsRef = FirebaseDatabase.getInstance().getReference().child("raspberries").child("0").child("rooms");
        RoomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                list_objects.clear();

                for(DataSnapshot sr: rooms)
                {
                    Iterable<DataSnapshot> objects = sr.child("hObjects").getChildren();
                    for(DataSnapshot so: objects)
                    {
                        list_objects.add(so.getValue(Object.class));
                    }
                }

                list.clear();
                for(Long id: list_objects_id)
                {
                    for(Object o: list_objects)
                    {
                        if(o.getId() == id)
                            list.add(o);
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                System.out.println("--------------> " + list.size());

                //for(Object o: list_objects) System.out.println(o.getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return HomeView;
    }

    class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>
    {
        @NonNull
        @Override
        public HomeFragmentAdapter.HomeFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.object_display_layout, viewGroup, false);
            return new HomeFragmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeFragmentAdapter.HomeFragmentViewHolder homeFragmentViewHolder, int i) {
            Object object = list.get(i);
            homeFragmentViewHolder.objectName.setText(object.getName());
            if(object.getObjectType().equals("Sensor"))
            {
                homeFragmentViewHolder.objectState.setVisibility(View.INVISIBLE);
            }
            else
            {
                if(object.getState().equals("On"))
                {
                    homeFragmentViewHolder.objectState.setChecked(true);
                }
                else homeFragmentViewHolder.objectState.setChecked(false);
            }

            homeFragmentViewHolder.objectState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //System.out.println(i + "-----------------------------------------------------------");
                    String state;
                    if(b) state = "On";
                    else state = "Off";

                    RoomsRef.child(String.valueOf(object.getId_room())).child("hObjects").child(String.valueOf(object.getId())).child("state").setValue(state);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class HomeFragmentViewHolder extends RecyclerView.ViewHolder
        {
            private final TextView objectName;
            private final Switch objectState;

            public HomeFragmentViewHolder(@NonNull View itemView) {
                super(itemView);

                objectName = itemView.findViewById(R.id.object_name);
                objectState = itemView.findViewById(R.id.object_state);
            }
        }
    }
}
