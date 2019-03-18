package com.example.smarthome;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment
{
    private View CategoryView;
    private DatabaseReference RoomsRef;
    private List<Object> list_objects;
    private List<String> categories;
    private RecyclerView myCategoriesList;

    public CategoriesFragment()
    {
        list_objects = new ArrayList<>();
        categories = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CategoryView = inflater.inflate(R.layout.fragment_list, container, false);

        myCategoriesList = (RecyclerView) CategoryView.findViewById(R.id.list);
        myCategoriesList.setLayoutManager(new LinearLayoutManager(getContext()));
        //myCategoriesList.setAdapter(new CategoryFragmentAdapter());

        RoomsRef = FirebaseDatabase.getInstance().getReference().child("raspberries").child("0").child("rooms").child("0").child("hObjects");

        return CategoryView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(RoomsRef, Category.class)
                .build();

        FirebaseRecyclerAdapter<Category, CategoriesViewHolder> adapter
                = new FirebaseRecyclerAdapter<Category, CategoriesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position, @NonNull Category model)
            {
                if(categories.contains(model.getCategory()))
                {
                    holder.setVisible(View.GONE);
                }
                else
                {
                    holder.name.setText(model.getCategory());

                    holder.itemView.findViewById(R.id.button_name).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String category = model.getCategory();

                            Fragment objectsList = new CategoryDetailsFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("category", category);
                            objectsList.setArguments(bundle);

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_container, objectsList, "fragment")
                                    .commit();

                        }
                    });
                    categories.add(model.getCategory());
                }
            }

            @NonNull
            @Override
            public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.button_display_layout, viewGroup, false);
                CategoriesViewHolder viewHolder = new CategoriesViewHolder(view);
                return viewHolder;
            }
        };

        myCategoriesList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder
    {
        Button name;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.button_name);
        }

        public void setVisible(int _value)
        {
            name.setVisibility(_value);
        }
    }
}
