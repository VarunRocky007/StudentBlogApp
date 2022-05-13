package com.project.studentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.project.studentapp.Fragment.HomeFragment;
import com.project.studentapp.Fragment.NotificationFragment;
import com.project.studentapp.Fragment.ProfileFragment;
import com.project.studentapp.Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selecterFragment = null;
    ImageView chatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS" , MODE_PRIVATE).edit();
            editor.putString("profileid" , publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new ProfileFragment()).commit();
        } else {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();

        }

    }
    public void changeLayout(){
        startActivity(new Intent(MainActivity.this,ChatActivity.class));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selecterFragment = new HomeFragment();
                            break;

                        case R.id.nav_search:
                            selecterFragment = new SearchFragment();
                            break;

                        case R.id.nav_add:
                            selecterFragment = null;
                            startActivity(new Intent(MainActivity.this , PostActivity.class));
                            break;

                        case R.id.nav_heart:
                            selecterFragment = new NotificationFragment();
                            break;

                        case R.id.nav_profile:
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS" , MODE_PRIVATE).edit();
                            editor.putString("profileid" , FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selecterFragment = new ProfileFragment();
                            break;
                    }

                    if (selecterFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selecterFragment).commit();
                    }

                    return true;
                }
            };
}
