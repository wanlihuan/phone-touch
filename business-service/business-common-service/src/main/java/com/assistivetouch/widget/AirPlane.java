package com.assistivetouch.widget;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.widget.TextView;

public class AirPlane {
	Context mContext;
	AirPlaneBrocastReceiver mAirPlaneBrocastReceiver;
	class AirPlaneBrocastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
		//	changeAirPlaneImage();

		}
	}
	public AirPlane(Context context){
		mContext = context;
		IntentFilter airPlaneIntentFilter = new IntentFilter(
				Intent.ACTION_AIRPLANE_MODE_CHANGED);
		mAirPlaneBrocastReceiver = new AirPlaneBrocastReceiver();
		context.registerReceiver(mAirPlaneBrocastReceiver, airPlaneIntentFilter);
		
	}
	
	public void unregisterReceiver(){
		mContext.unregisterReceiver(mAirPlaneBrocastReceiver);
	}
	
	TextView mTextView;
	public void setView(TextView textView){
		mTextView = textView;
	}
	
	/*public void changeAirPlaneImage() {
		try {
			boolean isAirPlaneOn = Settings.System.getInt(mContext.getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON) == 1 ? true : false;
			if (isAirPlaneOn) {
				mTextView.setCompoundDrawablesWithIntrinsicBounds(0, Rs.drawable.action_point_toolbox_airplane_normal, 0, 0);
			} else {
				mTextView.setCompoundDrawablesWithIntrinsicBounds(0, Rs.drawable.action_point_toolbox_airplane_press, 0, 0);
			}
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public void AirPlaneOnAndOff(){
		//取得目前飞行模式的状态
		boolean isAirPlaneOn;
		try {
			isAirPlaneOn = Settings.System.getInt(mContext.getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON) == 1 ? true : false;

			setAirplaneMode(!isAirPlaneOn);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void setAirplaneMode(boolean setAirPlane) {

		Settings.System.putInt(mContext.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, setAirPlane ? 1 : 0);

		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", setAirPlane);//平板没加这句话不起作用

		mContext.sendBroadcast(intent);
	}
}
