package com.example.paula.securityapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cta on 1/14/17.
 */

public class FirebaseManager {
    private FirebaseDatabase db;
    private FirebaseStorage storage;
    String ID;

    FirebaseManager(Context context)
    {
        db = FirebaseDatabase.getInstance();
        DatabaseReference myref = db.getReference("message");
        myref.setValue("Hello");
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        ID = tm.getDeviceId();

        //get ID eventually
        //TODO get actual id eventually
        storage = FirebaseStorage.getInstance();
        retrieveImages();
        ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    boolean uploadGPS(Pair<String,String> coordinates, String description)
    {
        //upload to firebase
//        Log.d("ERROR", "uploadGPS: ");
        DatabaseReference myref = db.getReference("Coordinates");
        Map<String,String> userMap = new HashMap<>();
        JSONObject temp = new JSONObject();
        try {


            temp.put("Longitude", coordinates.first);
            temp.put("Latitude", coordinates.second);
            temp.put("Description" , description);
            //maps user ID to json object
            userMap.put("User_"+ID,temp.toString());
            myref.child(ID).setValue(userMap);
            return true;
        }
        catch (Exception e)
        {
            Log.v("Exception",e.toString());
        }
//        Log.v("BUG","Not uploading");
        return false;
    }

    boolean uploadPicture(ImageView img)
    {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://hackuci-project.appspot.com");

        StorageReference selfieRef = storageRef.child("userSelfie"+ID+".jpg");

        StorageReference selfieImageRef = storageRef.child("images/userSelfie"+ID+".jpg");

        selfieRef.getName().equals(selfieImageRef.getName());
        selfieRef.getPath().equals(selfieImageRef.getPath());

        ImageView imageView = img;
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = selfieRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.v("Did not upload","Failed to Upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.v("Uploading","Currently Uploading");
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
        //Approach depends on how image is passed through

        return true;
    }

    ArrayList<Image>  retrieveImages()
    {
        //should retrive gps coordinates than find image IDs
        ArrayList<Image> images = new ArrayList<>();
        // Create a storage reference from our app
//        StorageReference storageRef = storage.getReferenceFromUrl("gs://hackuci-project.appspot.com");

// Create a reference with an initial file path and name
//        StorageReference pathReference = storageRef.child("images/01 - HS02B4f.png");

// Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://hackuci-project.appspot.com/testimage.png");

// Create a reference from an HTTPS URL
// Note that in the URL, characters are URL escaped!
//        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/hackuci-project.appspot.com/o/01%20-%20HS02B4f.png?alt=media&token=ed5490d3-e7ea-4d00-b4a3-b31b2f96f489");
        final long MAX_SIZE = (1024 * 1024) * 15;
        gsReference.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.v("Download Complete","DOWNLOAD COMPLETE");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("Failure","Failed to download");
            }
        });
        return images;
    }

    ArrayList<Pair<String,String>>  retrieveGPS()
    {
        ArrayList<Pair<String,String>> coordinates = new ArrayList<>();

        return coordinates;
    }



//    boolean broa
}
