package com.rb.workcatalog.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rb.workcatalog.utils.PrefManager;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    public static final long NOTIFY_INTERVAL = 10*1000;//10 sec
    private Timer yourTimer = null;
    Context ctx=this;
    PrefManager pref;

    public TimerService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // [START get_storage_ref]
        // [END get_storage_ref]
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new TimerService.ServiceHandler(mServiceLooper);
        pref = new PrefManager(ctx);
        if(yourTimer != null) {
            yourTimer.cancel();
        }
        // recreate new
        yourTimer = new Timer();

        // schedule task
        yourTimer.scheduleAtFixedRate(new TimerService.TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mServiceHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    {
                        pref.searchKeysAndUpload();
                    }
                }

            });
        }
    }
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //  stopSelf(msg.arg1);

        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SERVICE: ", "onStartCommand");
        //  Message msg = mServiceHandler.obtainMessage();
        //  msg.arg1 = startId;
        //    mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }
}
