package com.nowakowski.krzysztof95.navigationdrawerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nowakowski.krzysztof95.navigationdrawerapp.transform.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String url = "http://52.174.235.185";
    private static final String JOINED_EVENT_CLASS="joined";
    private GoogleMap mMap;
    double lat;
    double lng;
    SharedPreferences prefs;
    Button unsubscribeEvent;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefs = getApplicationContext().getSharedPreferences("Name", Context.MODE_PRIVATE);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        Button deleteEvent = (Button) findViewById(R.id.delete_event_button);
        unsubscribeEvent = (Button) findViewById(R.id.unsubscribe_event_button);
        Button join = (Button) findViewById(R.id.join_event_button);

        if (Objects.equals(prefs.getString("user_id", ""), "")) {
            join.setVisibility(View.GONE);
            join.setClickable(false);
        }

        if(!Objects.equals(getIntent().getStringExtra("class_name"), JOINED_EVENT_CLASS))
        {
            unsubscribeEvent.setVisibility(View.GONE);
            unsubscribeEvent.setClickable(false);
        }
        if(!Objects.equals(getIntent().getStringExtra("user_id"), prefs.getString("user_id", ""))) {
            deleteEvent.setVisibility(View.GONE);
            deleteEvent.setClickable(false);
        }


        TextView textViewTitle = (TextView) findViewById(R.id.d_textViewTitle);
        TextView textViewAuthor = (TextView) findViewById(R.id.d_textViewAuthor);
        TextView textViewDesc = (TextView) findViewById(R.id.d_textViewDesc);
        TextView textViewTime = (TextView) findViewById(R.id.d_textViewTime);
        TextView textViewStartTime = (TextView) findViewById(R.id.d_textViewStartTime);
        ImageView imageVievAvatar = (ImageView) findViewById(R.id.d_imageView);
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        textViewTitle.setText(getIntent().getStringExtra("title"));
        textViewAuthor.setText(getIntent().getStringExtra("author"));
        textViewDesc.setText(getIntent().getStringExtra("desc"));
        textViewTime.setText(getIntent().getStringExtra("time"));
        textViewStartTime.setText(String.format("Data rozpoczęcia: %s", getIntent().getStringExtra("start_time")));

        Picasso.with(getApplicationContext())
                .load(getIntent().getStringExtra("user_avatar")).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder).resize(100, 100).transform(new CircleTransform()).into(imageVievAvatar);
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

    private void DeleteEventRequest(String event_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();

        listItem.setEvent_id(event_id);

        Call<ListItem> call = service.DeleteEvent(listItem);

        call.enqueue(new Callback<ListItem>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                Toast.makeText(EventDetailsActivity.this, R.string.event_deleted, Toast.LENGTH_SHORT).show();

                if(!Objects.equals(getIntent().getStringExtra("class_name"), JOINED_EVENT_CLASS)) {
                    onBackPressed();
                } else {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void UnsubscribeEventRequest(String user_id, String event_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();


        listItem.setUser_id(user_id);
        listItem.setEvent_id(event_id);

        Call<ListItem> call = service.UnsubscribeEvent(listItem);

        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                Toast.makeText(EventDetailsActivity.this, R.string.event_unsubscribed, Toast.LENGTH_SHORT).show();
                unsubscribeEvent.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                Toast.makeText(EventDetailsActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void JoinEventRequest(final String user_id, final String event_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();

        listItem.setUser_id(user_id);
        listItem.setEvent_id(event_id);

        Call<ListItem> call = service.joinEvent(listItem);

        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                Toast.makeText(getApplicationContext(), "Dołączono", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDeleteEventClick(View view) {
        String event_id = getIntent().getStringExtra("id");
        DeleteEventRequest(event_id);
    }


    public void onUnsubscribeEventClick(View view) {
        String event_id = getIntent().getStringExtra("id");

        UnsubscribeEventRequest(prefs.getString("user_id", ""), event_id);
    }

    public void onJoinEventClick(View view) {
        String user_id = prefs.getString("user_id", "");
        String event_id = getIntent().getStringExtra("id");

        JoinEventRequest(user_id, event_id);
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
