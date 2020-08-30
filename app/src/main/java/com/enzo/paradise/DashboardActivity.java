package com.enzo.paradise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enzo.paradise.fragment.homefragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView slidername,slideremail;
    ImageView slideimage;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    //context


    //database
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    String name,email,image;
    private static FragmentManager fragmentManager;
    private Context context=DashboardActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.c3));
        }
        setContentView(R.layout.activity_dashboard);
        firebaseAuth=FirebaseAuth.getInstance();

        drawerLayout=findViewById(R.id.drawer);

        toolbar=findViewById(R.id.toolbar);
        navigationView=findViewById(R.id.navigation_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //test
        View headerView = navigationView.getHeaderView(0);
        slideremail=headerView.findViewById(R.id.slider_email);

        //load
        loadSavedData();

        //database
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =  firebaseDatabase.getReference("users");
        storageReference = getInstance().getReference();


        //context
        Context context=getApplicationContext();

        //fragmentmanager
        fragmentManager = getSupportFragmentManager();


        //nav view when app opens
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new homefragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.home:
         //       homefragment argumentFragment1=new homefragment();
        //        argumentFragment1.setArguments(bundle1);
        //        fragmentManager.beginTransaction().replace(R.id.fragment_container,new homefragment()).commit();
                break;
            case R.id.profile:
         //       profilefragment argumentFragment=new profilefragment();//Get Fragment Instance
          //      argumentFragment.setArguments(bundle);
         //       fragmentManager.beginTransaction().replace(R.id.fragment_container,argumentFragment).commit();
                break;
            case R.id.posts:
            //    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new postsfragment()).commit();
                break;
            case R.id.news:
             //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new newsfragment()).commit();
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                checkuserstatus();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStart() {

        checkuserstatus();
        super.onStart();
    }

    void loadSavedData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        slideremail.setText(sharedPreferences.getString("userEmail", String.valueOf(MODE_PRIVATE)));
    }

    private void checkuserstatus(){
        SharedPreferences sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        Boolean counter=sharedPreferences.getBoolean("loginCounter",Boolean.valueOf(String.valueOf(MODE_PRIVATE)));
        if(counter)
        {
            loadSavedData();
        }
        else{
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }
}