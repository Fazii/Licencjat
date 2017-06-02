package com.nowakowski.krzysztof95.navigationdrawerapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nowakowski.krzysztof95.navigationdrawerapp.transform.CircleTransform;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ShowEventsFragment showEventsFragment;
    private MaterialSearchView searchView;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onCheckPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onSearchView();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("Name", Context.MODE_PRIVATE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = (TextView) headerView.findViewById(R.id.name_nav_header);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);

        Picasso.with(getApplicationContext())
                .load(prefs.getString("user_picture", "https://static-hive-images-ticketlabsinc1.netdna-ssl.com/upload/c_fill,g_faces,h_150,w_150/placeholder-man_vqukjf.jpg")).transform(new CircleTransform()).into(imageView);

        navUsername.setText(prefs.getString("user", "MeetWithMe"));
        navigationView.setNavigationItemSelectedListener(this);

        setTitle(getString(R.string.show_events));
        showEventsFragment = new ShowEventsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.relativelayout_for_fragment, showEventsFragment, "ShowEvents");
        fragmentTransaction.commit();
    }

    private void onSearchView() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setHintTextColor(getResources().getColor(R.color.grey));
        searchView.setHint(getString(R.string.write_event_tag));

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                setTitle(query);
                showEventsFragment = new ShowEventsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                showEventsFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.relativelayout_for_fragment, showEventsFragment, "ShowEvents");
                fragmentTransaction.commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

        });
    }

    private void onCheckPermission() {
        //noinspection StatementWithEmptyBody
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_search || super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        searchView.closeSearch();

        if (id == R.id.nav_camera) {
            setTitle(getString(R.string.show_events));
            ShowEventsFragment showEventsFragment = new ShowEventsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativelayout_for_fragment, showEventsFragment, "ShowEvents");
            fragmentTransaction.commit();
        } else if (id == R.id.nav_gallery) {
            setTitle(getString(R.string.add_event));
            AddEventFragment addEventFragment = new AddEventFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativelayout_for_fragment, addEventFragment, "AddEvent");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_slideshow) {
            setTitle(getString(R.string.my_events));
            PagerFragment pagerFragment = new PagerFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativelayout_for_fragment, pagerFragment, "ShowYourEvents");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_manage) {
            setTitle(getString(R.string.show_EventsMap));
            ShowMapFragment showMapFragment = new ShowMapFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.relativelayout_for_fragment, showMapFragment, "ShowMap");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(MainActivity.this, FacebookLoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
