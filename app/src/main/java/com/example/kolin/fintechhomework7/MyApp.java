package com.example.kolin.fintechhomework7;

import android.app.Application;

import com.example.kolin.fintechhomework7.db.query.Queries;
import com.facebook.stetho.Stetho;

/**
 * Created by kolin on 12.11.2017.
 */

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Queries.initWithContext(this);
        Stetho.initializeWithDefaults(this);
    }
}
