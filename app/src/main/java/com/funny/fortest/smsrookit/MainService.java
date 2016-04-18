package com.funny.fortest.smsrookit;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

public class MainService extends Service {
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("MainService", "onBind executed");
        return null;
    }

    public void onCreate(){
        super.onCreate();
        Log.d("zhaochengyu", "MainService onCreate executed");
        SMSDatabaseHelper dbHelper = new SMSDatabaseHelper(this, "SMSrookit.db", null, 1);

        startNetChangeReceiver();
        //registerAlarmIntent();
        //addNotification();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("zhaochengyu", "MainService onStartCommand executed");
        flags = START_REDELIVER_INTENT;
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i("zhaochengyu", "MainService onDestroy: executed");
        Intent startIntent = new Intent(this, MainService.class);
        startService(startIntent);
    }

    public void addNotification(){
        Notification notification = new Notification(R.mipmap.icon,
                "", System.currentTimeMillis());
        notification.contentView = new RemoteViews(this.getPackageName(),R.layout.notification_template_lines);
        notification.contentIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        startForeground(345, notification);
    }

    public int startNetChangeReceiver(){
        Log.d("zhaochengyu", "startNetChangeReceiver: executed");
        IntentFilter netChangereceiveFilter;
        NetChangeReceiver NetChangeReceiver;
        netChangereceiveFilter = new IntentFilter();
        netChangereceiveFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        NetChangeReceiver = new NetChangeReceiver();
        registerReceiver(NetChangeReceiver, netChangereceiveFilter);
        return 0;
    }

    public void registerAlarmIntent()
    {
        String almaction = "com.funny.fortest.ALARMSTART";

        Intent i = new Intent();
        i.setAction(almaction);
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        long firsttime = SystemClock.elapsedRealtime();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firsttime, 5 * 1000, pi);
    }

}
