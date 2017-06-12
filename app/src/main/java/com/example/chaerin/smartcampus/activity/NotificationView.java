package com.example.chaerin.smartcampus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.app.NotificationManager;

import com.example.chaerin.smartcampus.R;

public class NotificationView extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
//---look up the notification manager service---
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//---cancel the notification that we started---
        nm.cancel(getIntent().getExtras().getInt("notificationID"));
    }
}