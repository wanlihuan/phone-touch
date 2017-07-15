package com.assistivetouch.widget;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class BluetoothStateTracker extends StateTracker{
	Context mContext;
		
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                onActualStateChange(context, intent);
                setImageViewResources(context);
            }
		}
	};
	public BluetoothStateTracker(Context context, Drawable[] drawable){
		mContext = context;
		setDrawable(drawable);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙
        mContext.registerReceiver(mIntentReceiver, filter);
	}
	
	public void unregisterReceiver(){
		mContext.unregisterReceiver(mIntentReceiver);
	}
	
    @Override
    public int getActualState(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
        	return STATE_DISABLED;
        }
        return bluetoothStateToFiveState(bluetoothAdapter.getState());
    }

    @Override
    protected void requestStateChange(Context context, final boolean desiredState) {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            setCurrentState(context, STATE_DISABLED);
        	return;
        }
        // Actually request the Bluetooth change and persistent
        // settings write off the UI thread, as it can take a
        // user-noticeable amount of time, especially if there's
        // disk contention.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... args) {
                if (desiredState) {
                    bluetoothAdapter.enable();
                } else {
                    bluetoothAdapter.disable();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onActualStateChange(Context context, Intent intent) {
        if (!BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
            return;
        }
        int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
        setCurrentState(context, bluetoothStateToFiveState(bluetoothState));
    }

    /**
     * Converts BluetoothAdapter's state values into our
     * Wifi/Bluetooth-common state values.
     */
    private int bluetoothStateToFiveState(int bluetoothState) {
        switch (bluetoothState) {
            case BluetoothAdapter.STATE_OFF:
                return STATE_DISABLED;
            case BluetoothAdapter.STATE_ON:
                return STATE_ENABLED;
            case BluetoothAdapter.STATE_TURNING_ON:
                return STATE_TURNING_ON;
            case BluetoothAdapter.STATE_TURNING_OFF:
                return STATE_TURNING_OFF;
            default:
                return STATE_UNKNOWN;
        }
    }
}
