package com.example.tirhal;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentMainActivity extends AppCompatActivity {
    public static TripDatabase database;
    public static String fireBaseUseerId;
    public static String fireBaseUserName;
    public static String fireBaseEmail;
    public static Uri fireBaseUserPhotoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfragment);
        BottomNavigationView bottomNav = findViewById(R.id.botton_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UpComingFragment()).commit();
        fireBaseUseerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fireBaseUserName=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        database = Room.databaseBuilder(this, TripDatabase.class, "tripDB").build();






    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UpComingFragment()).commit();
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item){
                    Fragment selectedFragment =new UpComingFragment();

                    switch (item.getItemId()){
                        case R.id.nav_upcoming:
                            selectedFragment = new UpComingFragment();
                            break;
                        case R.id.nav_history:
                            selectedFragment = new HistoryFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true; }

            };
}
