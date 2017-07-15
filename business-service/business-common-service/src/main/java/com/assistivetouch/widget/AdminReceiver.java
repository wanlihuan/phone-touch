package com.assistivetouch.widget;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AdminReceiver extends DeviceAdminReceiver {
	private final String TAG="AdminReceiver";
	
	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, ">>>>>>>>>>>> onDisableRequested >>>>>>>>>>>>");
		return super.onDisableRequested(context, intent);
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, ">>>>>>>>>>>> onDisabled >>>>>>>>>>>>");
		super.onDisabled(context, intent);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, ">>>>>>>>>>>> onEnabled >>>>>>>>>>>>");
		super.onEnabled(context, intent);
	}

	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, ">>>>>>>>>>>> onPasswordChanged >>>>>>>>>>>>");
		super.onPasswordChanged(context, intent);
	}

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, ">>>>>>>>>>>> onPasswordFailed >>>>>>>>>>>>");
		super.onPasswordFailed(context, intent);
	}

	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, ">>>>>>>>>>>> onPasswordSucceeded >>>>>>>>>>>>");
		super.onPasswordSucceeded(context, intent);
	}

}
