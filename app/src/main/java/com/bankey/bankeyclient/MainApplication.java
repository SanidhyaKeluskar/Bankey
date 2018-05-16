package com.bankey.bankeyclient;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.bankey.bankeyclient.api.BankeyApi;
import com.bankey.bankeyclient.api.BankeyApiImpl;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.math.BigDecimal;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Dima on 24.02.2018.
 */

public class MainApplication extends Application {

    public static final BigDecimal MIN_BALANCE_FOR_KEY = new BigDecimal(100);
    public static final String KEY_SERVICE_FEE_HTML = "<b> $2.00 </b>";

    public static final String CHANEL_ID = "bankey";
    public static final String CHANEL_NAME = "Bankey";
    public static final String CHANEL_DESC = "Bankey description";

    private static MainApplication instance;

    private BankeyApi mApi = new BankeyApiImpl();

    public static MainApplication instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Fabric.with(this, new Crashlytics());

        initImageLoader();
        initChannels(this);
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANEL_DESC);
        notificationManager.createNotificationChannel(channel);
    }

    public BankeyApi getApi() {
        return mApi;
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    public static void sendNotification(String message) {

        Intent intent = new Intent(instance, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(instance, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String title = instance.getString(R.string.app_name);

        int icon = R.mipmap.ic_launcher;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(instance, CHANEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) instance.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
