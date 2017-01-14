package com.example.paula.securityapp;

import android.content.Context;
import android.media.Image;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONObject;

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
        storage = FirebaseStorage.getInstance();
        ID = "101010";
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
            userMap.put("User_"+ID,temp.toString());
            myref.setValue(userMap);
            return true;
        }
        catch (Exception e)
        {
            Log.v("Exception",e.toString());
        }
        Log.v("BUG","Not uploading");
        return true;
    }

    private boolean uploadPicture(Image selfie)
    {


        return true;
    }

    ArrayList<Image>  retrieveImages()
    {
        ArrayList<Image> images = new ArrayList<>();
        return images;
    }

    ArrayList<Pair<String,String>>  retrieveGPS()
    {
        ArrayList<Pair<String,String>> coordinates = new ArrayList<>();
        return coordinates;
    }



//    boolean broa
}
