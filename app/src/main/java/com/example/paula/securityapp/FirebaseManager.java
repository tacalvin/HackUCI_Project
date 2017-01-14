package com.example.paula.securityapp;

import android.content.Context;
import android.media.Image;
import android.telephony.TelephonyManager;
import android.util.Pair;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by cta on 1/14/17.
 */

public class FirebaseManager {
    private FirebaseDatabase db;
    String ID;

    FirebaseManager(Context context)
    {
        db = FirebaseDatabase.getInstance();

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ID = tm.getDeviceId();
        //get ID
        //ID = getID()
    }

    boolean uploadGPS(String longitude,String latitude,String description)
    {
        //upload to firebase
        return true;
    }

    private boolean uploadPicture(Image selfie)
    {
        //upload to firebase
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
