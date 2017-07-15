package com.assistivetouch.widget;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;

public class AutoRotationStateTracker {
	private static final boolean DBG = true;
	public static final int STATE_DISABLED = 0;
	public static final int STATE_ENABLED = 1;
	public static final int STATE_TURNING_ON = 2;
	public static final int STATE_TURNING_OFF = 3;
	public static final int STATE_INTERMEDIATE = -1;
	public static final int STATE_UNKNOWN = -2;
	// Is the state in the process of changing?
	protected boolean mInTransition = false;
	private Boolean mActualState = null; // initially not set
	private Boolean mIntendedState = null; // initially not set
	
	// Did a toggle request arrive while a state update was
	// already in-flight? If so, the mIntendedState needs to be
	// requested when the other one is done, unless we happened to
	// arrive at that state already.
	private boolean mDeferredStateChangeRequestNeeded = false;
	Context mContext;
	TextView mTextView;
	
	private ContentObserver mAutoRotationChangeObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
        	onActualStateChange(mContext, null);
        	if(mTextView != null)
        		updateUiShow();
        	
        }
    };
    
	public AutoRotationStateTracker(Context context){
		mContext = context;
		context.getContentResolver().registerContentObserver(
		            Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
		            true, mAutoRotationChangeObserver);
		
	}
    
	public void unregisterContentObserver(){
		if(mAutoRotationChangeObserver != null)
			mContext.getContentResolver().unregisterContentObserver(mAutoRotationChangeObserver);
	}
	
	public void setView(TextView textView){
		mTextView = textView;
	}
	
	public void updateUiShow(){
		switch (getTriState(mContext)) {
		case 0:
			mTextView.setSelected(false);
		  break;
		case 1://开横屏
			mTextView.setSelected(true);
		  break;
		case -1:
		  break;
		 }
	}
	
	public int getActualState(Context context) {
	  int state = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, -1);
	  if (state == 1) {
	      return STATE_ENABLED;
	  } else if (state == 0) {
	      return STATE_DISABLED;
	  } else {
	      return STATE_UNKNOWN;
	  }
	}
	
	public void onActualStateChange(Context context, Intent unused) {
	  // Note: the broadcast location providers changed intent
	  // doesn't include an extras bundles saying what the new value is.
	  setCurrentState(context, getActualState(context));
	}
	
	public void requestStateChange(final Context context, final boolean desiredState) {
	  final ContentResolver resolver = context.getContentResolver();
	  new AsyncTask<Void, Void, Boolean>() {
	      @Override
	      protected Boolean doInBackground(Void... args) {
	          Settings.System.putInt(context.getContentResolver(),
	                  Settings.System.ACCELEROMETER_ROTATION, desiredState ? 1 : 0);
	          return desiredState;
	      }
	
	      @Override
	      protected void onPostExecute(Boolean result) {
	          setCurrentState(context, result ? STATE_ENABLED : STATE_DISABLED);
	      }
	  }.execute();
	}
	
	public boolean isClickable() {
	return true;
	}
	
	/**
	* User pressed a button to change the state. Something should immediately
	* appear to the user afterwards, even if we effectively do nothing. Their
	* press must be heard.
	*/
	public void toggleState(Context context) {
	int currentState = getTriState(context);
	boolean newState = false;
	switch (currentState) {
	case STATE_ENABLED:
	  newState = false;
	  break;
	case STATE_DISABLED:
	  newState = true;
	  break;
	case STATE_INTERMEDIATE:
	  if (mIntendedState != null) {
	      newState = !mIntendedState;
	  }
	  break;
	}
	mIntendedState = newState;
	if (mInTransition) {
	  // We don't send off a transition request if we're
	  // already transitioning. Makes our state tracking
	  // easier, and is probably nicer on lower levels.
	  // (even though they should be able to take it...)
	  mDeferredStateChangeRequestNeeded = true;
	} else {
	  mInTransition = true;
	  requestStateChange(context, newState);
	}
    }
	public final void setCurrentState(Context context, int newState) {
	final boolean wasInTransition = mInTransition;
	switch (newState) {
	case STATE_DISABLED:
	  mInTransition = false;
	  mActualState = false;
	  break;
	case STATE_ENABLED:
	  mInTransition = false;
	  mActualState = true;
	  break;
	case STATE_TURNING_ON:
	  mInTransition = true;
	  mActualState = false;
	  break;
	case STATE_TURNING_OFF:
	  mInTransition = true;
	  mActualState = true;
	  break;
	}
	
	if (wasInTransition && !mInTransition) {
	  if (mDeferredStateChangeRequestNeeded) {
	     // Log.v(TAG, "processing deferred state change");
	      if (mActualState != null && mIntendedState != null
	              && mIntendedState.equals(mActualState)) {
	         //Log.v(TAG, "... but intended state matches, so no changes.");
	      } else if (mIntendedState != null) {
	          mInTransition = true;
	          requestStateChange(context, mIntendedState);
	      }
	      mDeferredStateChangeRequestNeeded = false;
	  }
	}
	}
	
	/**
	* If we're in a transition mode, this returns true if we're transitioning
	* towards being enabled.
	*/
	public final boolean isTurningOn() {
	return mIntendedState != null && mIntendedState;
	}
	
	/**
	* Returns simplified 3-state value from underlying 5-state.
	* 
	* @param context
	* @return STATE_ENABLED, STATE_DISABLED, or STATE_INTERMEDIATE
	*/
	public final int getTriState(Context context) {
	if (mInTransition) {
	  // If we know we just got a toggle request recently
	  // (which set mInTransition), don't even ask the
	  // underlying interface for its state. We know we're
	  // changing. This avoids blocking the UI thread
	  // during UI refresh post-toggle if the underlying
	  // service state accessor has coarse locking on its
	  // state (to be fixed separately).
	  return STATE_INTERMEDIATE;
	}
	switch (getActualState(context)) {
	case STATE_DISABLED:
	  return STATE_DISABLED;
	case STATE_ENABLED:
	  return STATE_ENABLED;
	default:
	  return STATE_INTERMEDIATE;
	}
}
}
