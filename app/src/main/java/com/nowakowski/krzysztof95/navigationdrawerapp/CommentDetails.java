package com.nowakowski.krzysztof95.navigationdrawerapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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

import static java.security.AccessController.getContext;

public class CommentDetails extends AppCompatActivity implements OnMapReadyCallback {
    private static final String url = "http://192.168.0.73:8888";
    private GoogleMap mMap;
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textViewId = (TextView) findViewById(R.id.d_textVievId);
        TextView textViewBookId = (TextView) findViewById(R.id.d_textVievBookId);
        TextView textViewAuthor = (TextView) findViewById(R.id.d_textVievAuthor);
        TextView textViewCommentDate = (TextView) findViewById(R.id.d_textVievCommentDate);
        TextView textViewBookComment = (TextView) findViewById(R.id.d_textVievBookComment);
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        textViewId.setText(String.format("Id: %s", getIntent().getStringExtra("id")));
        textViewBookId.setText(String.format("Book Id: %s", getIntent().getStringExtra("book_id")));
        textViewAuthor.setText(String.format("Author: %s", getIntent().getStringExtra("author")));
        textViewCommentDate.setText(String.format("Comment date: %s", getIntent().getStringExtra("comment_date")));
        textViewBookComment.setText(String.format("Comment: %s", getIntent().getStringExtra("book_comment")));
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


    private void DeleteCommentRequest(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();

        listItem.setId(id);

        Call<ListItem> call = service.DeleteComment(listItem);

        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                Toast.makeText(CommentDetails.this, "Comment Deleted", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                Toast.makeText(CommentDetails.this, "Error", Toast.LENGTH_LONG).show();
                Log.d("Log", "OnFailure" + t.getMessage());
            }
        });
    }

    public void onDeleteCommentClick(View view) {
        String id = getIntent().getStringExtra("id");
        DeleteCommentRequest(id);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMyLocationEnabled(true);

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(lat, lng)).title("Event");

        googleMap.addMarker(marker);

        CameraPosition Marker = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(16).bearing(0).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Marker));
    }

}
