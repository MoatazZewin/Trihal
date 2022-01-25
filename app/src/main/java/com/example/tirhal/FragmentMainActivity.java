package com.example.tirhal;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainsplash);
        BottomNavigationView bottomNav = findViewById(R.id.botton_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item){
           Fragment selectedFragment = null;

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
