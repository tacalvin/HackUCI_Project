package com.example.paula.securityapp;

import android.util.Pair;

import com.socrata.api.Soda2Consumer;
import com.socrata.builders.SoqlQueryBuilder;
import com.socrata.model.soql.OrderByClause;
import com.socrata.model.soql.SoqlQuery;
import com.socrata.model.soql.SortOrder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Explo on 1/14/2017.
 */

public class SODAHandler {
    private Soda2Consumer consumer;
    private SoqlQueryBuilder userQueryBuilder;

    private void exceptionCatcher(Exception e) {
        try {
            //write exception to file
            FileWriter fstream = new FileWriter("exception.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(e.toString());
            out.close();
        } catch (IOException ex) {
            System.exit(1);
        }
    }

    SODAHandler() {
        consumer = Soda2Consumer.newConsumer("@string/sf_open_url");
        userQueryBuilder = new SoqlQueryBuilder();
    }

    void addSelectPhrase(String nominee) {
        // assuming there is exception handling done in this library
        try {
            userQueryBuilder.addSelectPhrase(nominee);
        } catch (Exception e) {
            exceptionCatcher(e);
        }
    }

    void setWhereClause(String nominee) {
        try {
            userQueryBuilder.setWhereClause(nominee);
        } catch (Exception e) {
            exceptionCatcher(e);
        }
    }

    void addOrderByPhraseDesc(String nominee) {
        try {
            userQueryBuilder.addOrderByPhrase(new OrderByClause(SortOrder.Descending, "position"));
        } catch (Exception e) {
            exceptionCatcher(e);
        }
    }

    void resetPhrase() {
        try {
            userQueryBuilder = new SoqlQueryBuilder();
        } catch (Exception e) {
            exceptionCatcher(e);
        }
    }


}
