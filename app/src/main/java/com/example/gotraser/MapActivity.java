package com.example.gotraser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Widgets
    private RelativeLayout relative1;
    private EditText searchBox,searchDestination;
    private ImageView mGps,cancelEnteredText,userOptions,cancelEnteredText2;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout overLap,bookNowLl,rentalLl,bookNow,rental,outstation;
    private  TextView tv_stop1,tv_stop2,tv_pickUp,tv_destination,bookNowBtn,rentalBtn,outstationBtn,destinationCopyPaste;
    private Switch multiple;
    private Button button;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            // check ui settings
            init();
        }
    }

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM=18f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchBox=findViewById(R.id.enterLocation);
        overLap=findViewById(R.id.overLap);
        cancelEnteredText=findViewById(R.id.cancelText);
        bookNowLl=findViewById(R.id.bookNowLayout);
        rentalLl=findViewById(R.id.rentalLayout);
        bookNow=findViewById(R.id.bookNowClick);
        rental=findViewById(R.id.rentalClick);
        outstation=findViewById(R.id.outstationClick);
        bookNowBtn=findViewById(R.id.bookNowButton);
        rentalBtn=findViewById(R.id.rentalButton);
        outstationBtn=findViewById(R.id.outstationButton);
        userOptions=findViewById(R.id.userOptions);
        searchDestination=findViewById(R.id.enterDestination);
        button=findViewById(R.id.locationsConfirmed);
        cancelEnteredText2=findViewById(R.id.cancelText2);
        multiple=findViewById(R.id.multiple_switch);
        destinationCopyPaste=findViewById(R.id.destination);
        tv_stop1=findViewById(R.id.stop1);
        tv_stop2=findViewById(R.id.stop2);

        //Top first 3 options

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookNowBtn.setTextColor(getResources().getColor(R.color.orange));
                rentalBtn.setTextColor(getResources().getColor(R.color.black));
                outstationBtn.setTextColor(getResources().getColor(R.color.black));
                rentalLl.setVisibility(View.GONE);
                bookNowLl.setVisibility(View.VISIBLE);
            }
        });

        rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rentalBtn.setTextColor(getResources().getColor(R.color.orange));
                outstationBtn.setTextColor(getResources().getColor(R.color.black));
                bookNowBtn.setTextColor(getResources().getColor(R.color.black));
                bookNowLl.setVisibility(View.GONE);
                rentalLl.setVisibility(View.VISIBLE);
            }
        });

        outstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rentalBtn.setTextColor(getResources().getColor(R.color.black));
                outstationBtn.setTextColor(getResources().getColor(R.color.orange));
                bookNowBtn.setTextColor(getResources().getColor(R.color.black));
                bookNowLl.setVisibility(View.GONE);
                rentalLl.setVisibility(View.GONE);
            }
        });





        userOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this,UserOptions.class));
            }
        });






        cancelEnteredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox.setText("");
                searchBox.requestFocus();
            }
        });


        cancelEnteredText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDestination.setText("");
                searchDestination.requestFocus();
            }
        });

//        tv_stop1=findViewById(R.id.stop1);
//        tv_stop2=findViewById(R.id.stop2);
      //  tv_pickUp=findViewById(R.id.pick_up);
//        tv_destination=findViewById(R.id.destination);
//        multiple = findViewById(R.id.multiple_switch);
//        button=findViewById(R.id.locationsConfirmed);

       mGps=findViewById(R.id.gps);
      //  final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(overLap);


        getLocationPermission();


        if(searchDestination.getText().toString().isEmpty()){
            multiple.setClickable(false);
        }
        else {
            multiple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    destinationCopyPaste.setVisibility(View.VISIBLE);
                }
            });







        }





//
//
//        tv_pickUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_pickUp.setBackgroundColor(getResources().getColor(R.color.orange) );
//                tv_destination.setBackgroundColor(getResources().getColor(R.color.white));
//            }
//        });
//
//        tv_destination.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_destination.setBackgroundColor(getResources().getColor(R.color.orange));
//                tv_pickUp.setBackgroundColor(getResources().getColor(R.color.white) );
//            }
//        });

//
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocatePick();
                geoLocateDestination();
                String pickup = searchBox.getText().toString().trim();
                String destination = searchDestination.getText().toString().trim();
                if((!TextUtils.isEmpty(pickup)) && (!TextUtils.isEmpty(destination))){
                    Intent intent = new Intent(MapActivity.this,BookingActivity.class);
                    intent.putExtra("pickup",pickup);
                    intent.putExtra("destination",destination);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MapActivity.this, "Enter pick up and destination location", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void init(){
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH || actionId==EditorInfo.IME_ACTION_DONE || event.getAction()==KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocatePick();

                }
                return false;
            }
        });

        searchDestination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH || actionId==EditorInfo.IME_ACTION_DONE || event.getAction()==KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocateDestination();
                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, "Back to your current location ", Toast.LENGTH_SHORT).show();
                getDeviceLocation();
            }
        });


    }


    private void geoLocatePick(){
        String searchString = searchBox.getText().toString().trim();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list= new ArrayList<>();

        try {
            list=geocoder.getFromLocationName(searchString,1);
        } catch (IOException e) {

        }
        if(list.size()>0){
            Address address=list.get(0);
            Toast.makeText(this, "Found Location", Toast.LENGTH_SHORT).show();
            searchBox.setText(address.getAddressLine(0));
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }

    private void geoLocateDestination(){
        String destinationString = searchDestination.getText().toString().trim();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list= new ArrayList<>();
        try {
            list=geocoder.getFromLocationName(destinationString,1);
        } catch (IOException e) {

        }
        if(list.size()>0){
            Address address1=list.get(0);
            Toast.makeText(this, "Found Location", Toast.LENGTH_SHORT).show();
            searchDestination.setText(address1.getAddressLine(0));
            moveCamera(new LatLng(address1.getLatitude(),address1.getLongitude()),DEFAULT_ZOOM,address1.getAddressLine(0));
        }


    }


    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);

    }

    private void getDeviceLocation(){
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(MapActivity.this);
        try {
            if(mLocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation=(Location) task.getResult();
                            assert currentLocation != null;
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"My Location");
                           Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                           try{
                               List<Address> addresses=geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
                               String address=addresses.get(0).getAddressLine(0);
                               searchBox.setText(address);


                           }
                            catch (IOException e) {
                               e.printStackTrace();
                           }


                        }
                        else {
                            Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }
        catch (SecurityException e){

        }
    }

    private void moveCamera(LatLng latLng, float zoom,String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
    }




    private void getLocationPermission(){

        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();      // additional
            }
            else {
                ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this,permission,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){
                    for(int i=0;i<grantResults.length;i++) {
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                        mLocationPermissionGranted = false;
                        return;
                    }
                    mLocationPermissionGranted=true;
                    initMap();
                }
            }
        }
    }
}