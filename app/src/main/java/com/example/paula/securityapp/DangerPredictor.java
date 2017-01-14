package com.example.paula.securityapp;

import android.util.Pair;

import java.util.List;

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

    private void standardQueryBuilder(Pair<String, String> coords) {
        StringBuilder stringBuilder = new StringBuilder("within_circle(location,");
        stringBuilder.append(coords.first);
        stringBuilder.append(',');
        stringBuilder.append(coords.second);
        handler.setWhereClause(stringBuilder.toString());
        handler.addOrderByPhraseDesc("date");
        handler.setLimit(300);
    }

    int checkDanger(Pair<String, String> coords) {
        standardQueryBuilder(coords);
        List<CrimeReport> crimeReports = handler.sendRequest();
        return 0;
    }
}
