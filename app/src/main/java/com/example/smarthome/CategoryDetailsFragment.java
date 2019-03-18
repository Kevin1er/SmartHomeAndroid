package com.example.smarthome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryDetailsFragment extends Fragment
{
    private View CategoryDetailsView;
    private RecyclerView myObjectsList;
    private DatabaseReference RoomsRef;
    private String categoryName;

    public CategoryDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CategoryDetailsView = inflater.inflate(R.layout.fragment_list, container, false);

        Bundle bundle=getArguments();
        categoryName = bundle.getString("category");

        getActivity().setTitle(categoryName);

        myObjectsList = (RecyclerView) CategoryDetailsView.findViewById(R.id.list);
        myObjectsList.setLayoutManager(new LinearLayoutManager(getContext()));

        RoomsRef = FirebaseDatabase.getInstance().getReference().child("raspberries").child("0").child("rooms").child("0").child("hObjects");

        return CategoryDetailsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Object>()
                        .setQuery(RoomsRef, Object.class)
                        .build();

        FirebaseRecyclerAdapter<Object, ObjectsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Object, ObjectsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ObjectsViewHolder holder, final int position, @NonNull Object model)
            {
                if(!model.getCategory().equals(categoryName))
                    holder.setVisibility(View.GONE);
                else
                {
                    holder.objectName.setText(model.getName());
                    if(model.getObjectType().equals("Sensor"))
                    {
                        holder.objectState.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        if(model.getState().equals("On"))
                        {
                            holder.objectState.setChecked(true);
                        }
                        else holder.objectState.setChecked(false);
                    }

                    holder.objectState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            System.out.println(position + "-----------------------------------------------------------");
                            String state;
                            if(b) state = "On";
                            else state = "Off";

                            getRef(position).child("state").setValue(state);
                        }
                    });
                }
            }

            @NonNull
            @Override
            public ObjectsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.object_display_layout, viewGroup, false);
                ObjectsViewHolder viewHolder = new ObjectsViewHolder(view);
                return viewHolder;
            }
        };

        myObjectsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ObjectsViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout objectLayout;
        LinearLayout objectSecondLayout;
        TextView objectName;
        Switch objectState;

        public ObjectsViewHolder(@NonNull View itemView) {
            super(itemView);

            objectLayout = itemView.findViewById(R.id.object_container);
            objectSecondLayout = itemView.findViewById(R.id.object_second_container);
            objectName = itemView.findViewById(R.id.object_name);
            objectState = itemView.findViewById(R.id.object_state);
        }

        public void setVisibility(int _value)
        {
            objectLayout.setVisibility(_value);
            objectSecondLayout.setVisibility(_value);
            objectName.setVisibility(_value);
            objectState.setVisibility(_value);
        }
    }
}
