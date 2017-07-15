package com.assistivetouch.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

public class WifiStateTracker extends StateTracker{
	Context mContext;
	private boolean mIsAirlineMode = false;
	
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                onActualStateChange(context, intent);
                	setImageViewResources(context);
            } 
		}
	};
	public WifiStateTracker(Context context, Drawable[] drawable){
		mContext = context;
		setDrawable(drawable);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//wifi
        mContext.registerReceiver(mIntentReceiver, filter);
	}
	
	public void unregisterReceiver(){
		mContext.unregisterReceiver(mIntentReceiver);
	}
	
	public void setAirlineMode(boolean enable) {
		Log.i("TAG", "Mobile setAirlineMode called, enabled is: " + enable);
		mIsAirlineMode = enable;
	}
	
	public boolean isClickable() {
		Log.i("TAG", "wifi mIsAirlineMode is " + mIsAirlineMode + ", mIsUserSwitching is " + mIsUserSwitching);
        return !mIsAirlineMode && super.isClickable();
	}
	
    @Override
    public int getActualState(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            return wifiStateToFiveState(wifiManager.getWifiState());
        }
        return STATE_DISABLED;
    }

    @Override
    protected void requestStateChange(Context context, final boolean desiredState) {
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
        	Log.i("TAG", "No wifiManager.");
            setCurrentState(context, STATE_DISABLED);
            return;
        }

        // Actually request the wifi change and persistent
        // settings write off the UI thread, as it can take a
        // user-noticeable amount of time, especially if there's
        // disk contention.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... args) {
                /**
                 * Disable tethering if enabling Wifi
                 */
                // delete these statement, from zte73 we support tether and wifi both eanbled
                /*int wifiApState = wifiManager.getWifiApState();
                if (desiredState && ((wifiApState == WifiManager.WIFI_AP_STATE_ENABLING) || (wifiApState == WifiManager.WIFI_AP_STATE_ENABLED))) {
                    wifiManager.setWifiApEnabled(null, false);
                }*/
                wifiManager.setWifiEnabled(desiredState);
                return null;
            }
        }.execute();
    }

    @Override
    public void onActualStateChange(Context context, Intent intent) {
        if (!WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            return;
        }
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        setCurrentState(context, wifiStateToFiveState(wifiState));
    }

    /**
     * Converts WifiManager's state values into our Wifi/Bluetooth-common
     * state values.
     */
    private int wifiStateToFiveState(int wifiState) {
        switch (wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
                return STATE_DISABLED;
            case WifiManager.WIFI_STATE_ENABLED:
                return STATE_ENABLED;
            case WifiManager.WIFI_STATE_DISABLING:
                return STATE_TURNING_OFF;
            case WifiManager.WIFI_STATE_ENABLING:
                return STATE_TURNING_ON;
            default:
                return STATE_DISABLED;
        }
    }

}
