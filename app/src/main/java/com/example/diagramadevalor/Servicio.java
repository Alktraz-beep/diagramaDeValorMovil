package com.example.diagramadevalor;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.widget.TextView;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.view.PieChartView;

public class Servicio extends Service {

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart(Intent intent, int startId) {
        //do something

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
