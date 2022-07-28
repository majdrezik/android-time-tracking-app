package com.example.hourstracker.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.hourstracker.R;

import java.util.Calendar;
import java.util.Date;

public class ExceededHoursService extends android.app.Service {
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public ExceededHoursService(){

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "descriptionm";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showExceededNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "2")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("You have forget to record exit from your work!")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(2, builder.build());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Date startDAte = (Date)intent.getExtras().get("startDate");
this.createNotificationChannel();

// notificationId is a unique int for each notification that you must define
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean notDipslayed=true;
                while (notDipslayed==true) {
                    try {
                        Log.e("ExceededHoursService", "ExceededHoursService is running");
                        Date now= Calendar.getInstance().getTime();
                        long diffMS =now.getTime()-startDAte.getTime();
                        long diff = diffMS/1000; // parse to seconds
                        long diffMinutes = diff/60; // parse to minutes
                        int hours = (int)diffMinutes/60;
                        if(hours>=9){
                            notDipslayed=false;
                            ExceededHoursService.this.showExceededNotification();
                        }else{
                            Thread.sleep(10000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}