package com.example.paula.securityapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.hoang8f.widget.FButton;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
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

        Typeface amatic = Typeface.createFromAsset(this.getAssets(), "fonts/Capture_it.ttf");
//        FButton broadcastButton = (FButton)findViewById(R.id.broadcastButton);

        FButton button = (FButton) findViewById(R.id.button);

        button.setTypeface(amatic);

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

        fb.uploadGPS(new android.util.Pair<String, String>("071","1"),"New Device ID LG");
//        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng san_fran = new LatLng(37.7749, -122.4194);
        mMap.addMarker(new MarkerOptions()
                .position(san_fran)
                .title("Marker in SF")
                .snippet("Snippet")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_google_signin_btn_icon_dark_disabled)));
        //add the selfie image here
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(san_fran,  14.0f));
    }


    ImageView mImageView;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //get url of photo saved.
        mImageView = new ImageView(getApplicationContext());
        Log.v("RequestCode",requestCode+"");
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // URI uri = data.getData();
            Log.e("onActivityResult","Made it");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(imageBitmap == null)
            {
                Log.v("NULLSHIT","ITs null guys");
            }
            mImageView.setImageBitmap(imageBitmap);
            fb.uploadPicture(imageBitmap);
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
