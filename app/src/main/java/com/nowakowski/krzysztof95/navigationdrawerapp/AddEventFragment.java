package com.nowakowski.krzysztof95.navigationdrawerapp;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.nowakowski.krzysztof95.navigationdrawerapp.ConfigurationConstants.API_URL;


public class AddEventFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View v;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ActionProcessButton send;
    private Button date;
    private String dateTime;

    private double lat;
    private double lng;

    private int year, month, day, hour, minute;
    private int yearF, monthF, dayF, hourF, minuteF;

    @NotEmpty(message = "To pole jest wymagane")
    private EditText titleEditText;
    @NotEmpty(message = "To pole jest wymagane")
    private EditText descEditText;
    @NotEmpty(message = "Podaj przynajmniej jeden tag")
    private  EditText tagsEditText;

    private Validator validator = new Validator(this);

    private SharedPreferences prefs;


    public AddEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add_event, container, false);

        prefs = getApplicationContext().getSharedPreferences("Name", Context.MODE_PRIVATE);

        titleEditText = (EditText) v.findViewById(R.id.titleEditText);
        descEditText = (EditText) v.findViewById(R.id.descEditText);
        tagsEditText = (EditText) v.findViewById(R.id.tagsEditText);

        send = (ActionProcessButton) v.findViewById(R.id.sendButton);
        send.setOnClickListener(this);

        date = (Button) v.findViewById(R.id.date_pick_button);
        date.setOnClickListener(this);

        Button search = (Button) v.findViewById(R.id.search_address_button);
        search.setOnClickListener(this);

        onValidate();

        return v;
    }

    private void onValidate() {
        if (prefs.getString("user_id", "").equals("")) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Zaloguj się")
                    .setMessage("Aby dodawać wydarzenia musisz być zalogowany")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getActivity(), FacebookLoginActivity.class);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            titleEditText.setFocusable(false);
                            descEditText.setFocusable(false);
                            tagsEditText.setFocusable(false);
                            send.setEnabled(false);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                if (lat == 0 && lng == 0) {
                    send.setMode(ActionProcessButton.Mode.PROGRESS);
                    send.setProgress(0);
                    Toast.makeText(getContext(), R.string.choose_event_location, Toast.LENGTH_LONG).show();
                    return;
                } else if (dateTime == null) {
                    send.setMode(ActionProcessButton.Mode.PROGRESS);
                    send.setProgress(0);
                    Toast.makeText(getContext(), R.string.choose_event_date, Toast.LENGTH_LONG).show();
                    return;
                }
                CreateNewCommentRequest(titleEditText.getText().toString(),
                        prefs.getString("user_id", ""), prefs.getString("user_picture", ""),
                        prefs.getString("user", ""), descEditText.getText().toString(),
                        tagsEditText.getText().toString(), lat, lng, dateTime);
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getContext());

                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                        send.setMode(ActionProcessButton.Mode.PROGRESS);
                        send.setProgress(0);
                    } else {
                        send.setMode(ActionProcessButton.Mode.PROGRESS);
                        send.setProgress(0);
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) v.findViewById(R.id.map1);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap = googleMap;
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(52.41177549551888, 19.17423415929079), (float) 5.3173866));

        permissionCheck();

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                googleMap.clear();

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("Event");

                googleMap.addMarker(marker);

                lat = point.latitude;
                lng = point.longitude;
            }
        });
    }

    private void permissionCheck() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    public void onSearch() {
        EditText location_et = (EditText) v.findViewById(R.id.address_edit_text);
        String location = location_et.getText().toString();

        List<Address> addressList;

        if (!location.matches("")) {

            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

                if (addressList.size() != 0) {
                    mGoogleMap.clear();
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    lat = address.getLatitude();
                    lng = address.getLongitude();
                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                } else {
                    Toast.makeText(getContext(), "Nie znaleziono podanego adresu", Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                Toast.makeText(getContext(), "Nie znaleziono podanego adresu", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Podaj lokalizację", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void CreateNewCommentRequest(String title, String user_id, String user_picture, String author, String desc, String tags, double lat, double lng, String dateTime) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();

        listItem.setEvent_title(title);
        listItem.setUser_id(user_id);
        listItem.setUser_avatar(user_picture);
        listItem.setEvent_author(author);
        listItem.setEvent_desc(desc);
        listItem.setEvent_tag(tags);
        listItem.setEvent_lat(lat);
        listItem.setEvent_lng(lng);
        listItem.setEvent_start_time(dateTime);

        Call<ListItem> call = service.sendComment(listItem);

        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                send.setMode(ActionProcessButton.Mode.PROGRESS);
                send.setProgress(100);
                titleEditText.getText().clear();
                descEditText.getText().clear();
                tagsEditText.getText().clear();
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                send.setMode(ActionProcessButton.Mode.PROGRESS);
                send.setProgress(-1);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sendButton:
                send.setMode(ActionProcessButton.Mode.ENDLESS);
                send.setProgress(1);
                validator.validate();
                break;

            case R.id.date_pick_button:
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, AddEventFragment.this,
                        year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 604800000);
                datePickerDialog.show();
                break;

            case R.id.search_address_button:
                onSearch();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearF = i;
        monthF = i1 + 1;
        dayF = i2;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.DialogTheme, AddEventFragment.this,
                hour, minute, DateFormat.is24HourFormat(getApplicationContext()));

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourF = i;
        minuteF = i1;

        dateTime = (yearF + "-" + monthF + "-" + dayF + " " + hourF + ":" + minuteF + ":00");
    }
}
