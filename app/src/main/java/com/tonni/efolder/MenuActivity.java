package com.tonni.efolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tonni.efolder.fragment.home_fragment;
import com.tonni.efolder.fragment.internal_fragment;
import com.tonni.efolder.fragment.sdcard_fragment;

public class MenuActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    FloatingActionButton  fab;

    MenuItem tvMeenu;


    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth=FirebaseAuth.getInstance();

        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open_Drawer,R.string.Close_Drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new internal_fragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);

        tvMeenu=findViewById(R.id.nav_logout);



        user=auth.getCurrentUser();

        if(user==null){
            Intent intent = new Intent(getApplicationContext(),login__.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Current User  loged in:" +  user.getEmail(), Toast.LENGTH_SHORT).show();

            // tvMeenu.setTitle(user.getEmail()+ "Logout");

        }




    }

    //TODO seting the navigat

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                 home_fragment home_frag = new home_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home_frag).addToBackStack(null).commit();
                break;
            case R.id.nav_sdcard:
                sdcard_fragment card_fragment=new sdcard_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, card_fragment).addToBackStack(null).commit();
                break;
            case R.id.nav_internal:
                internal_fragment internal_frag = new internal_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, internal_frag).addToBackStack(null).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),login__.class);
                startActivity(intent);
                break;


        }
        //TODO closng the drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStackImmediate();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }
}