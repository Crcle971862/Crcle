package com.example.crcle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.crcle.fragments.HomeFragment;
import com.example.crcle.fragments.NotificationFragment;
import com.example.crcle.fragments.PostFragment;
import com.example.crcle.fragments.SearchFragment;
import com.example.crcle.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Menu_activity extends AppCompatActivity {

    FrameLayout Container;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Container=findViewById(R.id.container);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new HomeFragment());
        fragmentTransaction.commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        FragmentTransaction home=getSupportFragmentManager().beginTransaction();
                        home.replace(R.id.container,new HomeFragment());
                        home.commit();
                        break;
                    case R.id.search:
                        FragmentTransaction search=getSupportFragmentManager().beginTransaction();
                        search.replace(R.id.container,new SearchFragment());
                        search.commit();
                        break;
                    case R.id.notification:
                        FragmentTransaction notification=getSupportFragmentManager().beginTransaction();
                        notification.replace(R.id.container,new NotificationFragment());
                        notification.commit();
                        break;
                    case R.id.user:
                        FragmentTransaction user=getSupportFragmentManager().beginTransaction();
                        user.replace(R.id.container,new UserFragment());
                        user.commit();
                        break;
                    case R.id.add:
                        FragmentTransaction add=getSupportFragmentManager().beginTransaction();
                        add.replace(R.id.container,new PostFragment());
                        add.commit();
                        break;
                }
                return true;
            }
        });

    }
}