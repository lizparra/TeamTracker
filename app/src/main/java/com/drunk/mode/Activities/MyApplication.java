package com.drunk.mode.Activities;

import android.app.Application;

//import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;



public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        //MobileAds.initialize(this, this.getResources().getString(R.string.ADS_APP_ID));

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
