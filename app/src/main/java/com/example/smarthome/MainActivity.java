package com.example.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Home");
                    Fragment fragment1 = new HomeFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, fragment1, "fragment")
                            .commit();
                    return true;
                case R.id.navigation_dashboard:
                    setTitle("Dashboard");
                    Fragment fragment2 = new DashboardFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, fragment2, "fragment")
                            .commit();
                    return true;
                case R.id.navigation_categories:
                    setTitle("Categories");
                    Fragment fragment3 = new CategoriesFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, fragment3, "fragment")
                            .commit();
                    return true;
                case R.id.navigation_settings:
                    setTitle("Settings");
                    Fragment fragment4 = new SettingsFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, fragment4, "fragment")
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(startIntent);
            finish();
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().add(R.id.main_container, new HomeFragment(), "1").commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
