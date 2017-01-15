package com.example.paula.securityapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.hoang8f.widget.FButton;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    LatLng latLng;
    HashMap<String, Marker> markerHashMap;

    SupportMapFragment mFragment;
    Marker currLocationMarker;

    FirebaseManager fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FirebaseManager fb = new FirebaseManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fb = new FirebaseManager(getApplicationContext());

//        fb.retrieveGPS();


        Typeface capture_it = Typeface.createFromAsset(this.getAssets(), "fonts/Capture_it.ttf");

        FButton button = (FButton) findViewById(R.id.button);

        button.setTypeface(capture_it);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 1);

//              try{
//                File file = createImageFile();
//              }
//              catch(IOException e)
//              {
//                Log.d("ERROR", "onCreate: IOEXCEPTION ");
//              }

            }
        });


//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);

//        mapFragment.getMapAsync(this);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 1
                );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }


    //    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults)     {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            mMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Your Current Position");
            currLocationMarker = mMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000); //10 seconds
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    private boolean isClose(double x1, double y1) {
        double x2 = currLocationMarker.getPosition().longitude;
        double y2 = currLocationMarker.getPosition().latitude;

        //return sqrt(pow(x2-x1,2) + pow(y2-y1,2)) <= 0.04347826086;
        return true;
    }

    protected void onStart() {
        super.onStart();
        markerHashMap = new HashMap<>();
        DatabaseReference mDatabase = fb.getDB().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 Log.v("Count " ,""+dataSnapshot);
                 HashMap<String,Object> coords = (HashMap<String,Object>) dataSnapshot.getChildren().iterator().next().getValue();
                 ArrayList<String []> coordList = new ArrayList<String[]>();
                 Iterator it = coords.entrySet().iterator();
                 while (it.hasNext()) {
                     Map.Entry pair = (Map.Entry)it.next();
                     Log.e("Class",(String)pair.getValue());
                     String line = (String)pair.getValue();

                     Pattern p = Pattern.compile("\"([^\"]*)\"");
                     Matcher m = p.matcher(line);
                     int i = 1;
                     int j =0;
                     String [] s = new String[3];
                     while (m.find()) {
                         Log.e("Regex:", m.group());
                         if(i %2 == 0)
                         {
                             s[j] = m.group();
                             j++;
                         }
                         i++;
                     }
                     coordList.add(s);
                     Marker newMarker = mMap.addMarker(new MarkerOptions()
                             .position(new LatLng(Double.parseDouble(s[0].substring(1,s[0].length()-2)), Double.parseDouble(s[1].substring(1,s[1].length()-2))))
                             .title("A User")
                             .snippet(s[2])
                     );
                     if (newMarker == null)
                        Log.v("IS NULL","AAA");
                     markerHashMap.put((String) pair.getKey(), newMarker);
                     it.remove(); // avoids a ConcurrentModificationException
                 }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {
             }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object> coords = (HashMap<String,Object>) dataSnapshot.getChildren().iterator().next().getValue();
                ArrayList<String []> coordList = new ArrayList<String[]>();
                Iterator it = coords.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    Log.e("Class",(String)pair.getValue());
                    String line = (String)pair.getValue();

                    Pattern p = Pattern.compile("\"([^\"]*)\"");
                    Matcher m = p.matcher(line);
                    int i = 1;
                    int j =0;
                    String [] s = new String[3];
                    while (m.find()) {
                        Log.e("Regex:", m.group());
                        if(i %2 == 0)
                        {
                            s[j] = m.group();
                            j++;
                        }
                        i++;
                    }
                    coordList.add(s);

                    markerHashMap.get((String)pair.getKey()).remove();
                    Marker newMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(s[0].substring(1,s[0].length()-2)), Double.parseDouble(s[1].substring(1,s[1].length()-2))))
                            .title("A User")
                            .snippet(s[2])
                    );
                    markerHashMap.put((String)pair.getKey(), newMarker);
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /*
        final long period = 10000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // do your task here
                ArrayList<String[]> coords = fb.retrieveGPS();
                if (mMap != null) mMap.clear();
                for (int i = 0; i < coords.size();i++) {
                    //order: log, lat, des
                    if (isClose(Double.parseDouble(coords.get(i)[0]), Double.parseDouble(coords.get(i)[1]))) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(coords.get(i)[0]), Double.parseDouble(coords.get(i)[1])))
                                .title("A User")
                                .snippet(coords.get(i)[2])
                        );
                    }
                }
            }
        }, 0, period);
        mGoogleApiClient.connect();
        super.onStart();
        */
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        // Add a marker in Sydney and move the camera
        LatLng san_fran = new LatLng(37.7749, -122.4194);
        mMap.addMarker(new MarkerOptions()
                .position(san_fran)
                .title("Marker in SF")
                .snippet("Snippet"));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_google_signin_btn_icon_dark_disabled)));
        //add the selfie image here
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(san_fran, 14.0f));
    }
    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        currLocationMarker = mMap.addMarker(markerOptions);

        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }


    ImageView mImageView;

    @Override
    protected void onDestroy() {
        fb.removeEntry();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //get url of photo saved.
        mImageView = new ImageView(getApplicationContext());
        Log.v("RequestCode", requestCode + "");
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // URI uri = data.getData();
            Log.e("onActivityResult", "Made it111111");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");


            mImageView.setImageBitmap(imageBitmap);

            try {
               Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Log.w("In TRY","");
                if(mLastLocation != null)
                {
                    Log.v("In uploading", "In MapsActivity");

                    fb.broadcast(imageBitmap, mLastLocation.getLongitude() + "", mLastLocation.getLatitude() + "");
                }
                else
                {
                    Log.e("Error Location is null","");
                }
//            fb.uploadPicture(imageBitmap);
            } catch (SecurityException e) {
                Log.v("Permision Errors", "No Permissions");
                this.finishAffinity();
            }
        }


    }





    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("ONC", "createImageFile: ");

        return image;
    }

}
