package com.nowakowski.krzysztof95.navigationdrawerapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CommentDetails extends AppCompatActivity implements OnMapReadyCallback {
    private static final String url = "http://192.168.0.73:8888";
    private GoogleMap mMap;
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        TextView textViewTitle = (TextView) findViewById(R.id.d_textViewTitle);
        TextView textViewAuthor = (TextView) findViewById(R.id.d_textViewAuthor);
        TextView textViewDesc = (TextView) findViewById(R.id.d_textViewDesc);
        TextView textViewTime = (TextView) findViewById(R.id.d_textViewTime);
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        textViewTitle.setText(String.format("Tytuł: %s", getIntent().getStringExtra("title")));
        textViewAuthor.setText(String.format("Autor: %s", getIntent().getStringExtra("author")));
        textViewDesc.setText(String.format("Opis: %s", getIntent().getStringExtra("desc")));
        textViewTime.setText(String.format("Data dodania: %s", getIntent().getStringExtra("time")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void DeleteCommentRequest(String event_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();

        listItem.setEvent_id(event_id);

        Call<ListItem> call = service.DeleteComment(listItem);

        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                Toast.makeText(CommentDetails.this, R.string.event_deleted, Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                Toast.makeText(CommentDetails.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onDeleteCommentClick(View view) {
        String event_id = getIntent().getStringExtra("id");
        DeleteCommentRequest(event_id);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
        }
        else {
            googleMap.setMyLocationEnabled(true);
            }

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(lat, lng)).title("Event");
        googleMap.addMarker(marker);
        CameraPosition Marker = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(16).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Marker));
    }
}
