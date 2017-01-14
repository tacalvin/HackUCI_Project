package com.example.paula.securityapp;

import android.util.Pair;

/**
 * Created by Explo on 1/14/2017.
 */

public class DangerPredictor {

    private String target_url;
    private String APIkey;
    private SODAHandler handler;

    DangerPredictor() {
        target_url = "@string/sf_open_url";
        APIkey = "@key/SODAKey";
        handler = new SODAHandler();
    }

    int checkDanger(Pair<String, String> coords) {

    }
}
