package com.example.smarthome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment
{
    private View SettingsView;
    private Button logoutButton;
    private FirebaseAuth mAuth;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SettingsView =  inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();
        logoutButton = SettingsView.findViewById(R.id.button_logout);

        logoutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);// New activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return SettingsView;
    }
}
