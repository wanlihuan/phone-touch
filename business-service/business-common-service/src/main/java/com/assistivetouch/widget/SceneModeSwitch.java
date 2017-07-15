package com.assistivetouch.widget;

import com.android.assistivetouch.R;

import android.content.Context;
import android.media.AudioManager;
import android.widget.TextView;

public class SceneModeSwitch {
	Context mContext;
	/**
	 * 0:标准
	 */
	public static final int SCENE_MODE_GENERAL = 0;
	/**
	 * 1：静音；
	 */
	public static final int SCENE_MODE_SILENT = 1;
	/**
	 * 2：会议 
	 */
	public static final int SCENE_MODE_MEETING = 2;
	/**
	 * 3：户外；
	 */
	public static final int SCENE_MODE_OUTDOOR = 3;
	
	public static int sceneModeType = 0;
	
	
	private AudioManager mAudioManager;
	
	public SceneModeSwitch(Context context){
		mContext = context;
		mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);//new AudioManager(context);
		checkCurrentSceneMode();
		
//		getInitring(mAudioManager);
	}
	
	
	/*void getInitring(AudioManager audio)
    {
          //取得手机的初始音量，并初始化进度条
        int volume = audio.getStreamVolume(AudioManager.STREAM_RING);  //取得初始音量
        //取得初始模式，并分别设置图标
        int mode = audio.getRingerMode();  //取得初始模式
        
    }*/
	
	public int checkCurrentSceneMode(){
		int mode = mAudioManager.getRingerMode();  //取得初始模式
		
		if(mode == AudioManager.RINGER_MODE_NORMAL){
			if(mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF &&
					mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION) == AudioManager.VIBRATE_SETTING_OFF){
				sceneModeType = SCENE_MODE_GENERAL;//标准
			}else if(mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ON &&
					mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION) == AudioManager.VIBRATE_SETTING_ON){
				sceneModeType = SCENE_MODE_SILENT;//静音
			}else
				sceneModeType = SCENE_MODE_GENERAL;
		}else if(mode == AudioManager.RINGER_MODE_VIBRATE){
		//	if(mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ON &&
		//			mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION) == AudioManager.VIBRATE_SETTING_ON){
				sceneModeType = SCENE_MODE_MEETING;//会议
		//	}
				
		}/*else if(mode == AudioManager.RINGER_MODE_SILENT){
			//if(mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF &&
			//		mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION) == AudioManager.VIBRATE_SETTING_OFF)
				sceneModeType = SCENE_MODE_OUTDOOR;//户外
		}*/
		
		return sceneModeType;
	}
	
	public void setSceneMode(int sceneMode){
		sceneModeType = sceneMode;
	}
	
	public int getSceneMode(){
		return sceneModeType;
	}
	
	public int getNextMode(){
		
		if(getSceneMode() == SCENE_MODE_GENERAL){
			setSceneMode(SCENE_MODE_SILENT);
			
		}else if(getSceneMode() == SCENE_MODE_SILENT){
			setSceneMode(SCENE_MODE_MEETING);
			
		}else if(getSceneMode() == SCENE_MODE_MEETING){
		//	setSceneMode(SCENE_MODE_OUTDOOR);
			setSceneMode(SCENE_MODE_GENERAL);
			
		}/*去掉户外else if(getSceneMode() == SCENE_MODE_OUTDOOR){
			setSceneMode(SCENE_MODE_GENERAL);
			
		}*/
		
		return getSceneMode();
	}
	
	/**
	 * 户外  设置情景模式一：只声音，无振动
	 */
	public void outdoorScene() {
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_OFF);
		mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_OFF);
        sceneModeType = SCENE_MODE_OUTDOOR;//户外
    //    Toast.makeText(mContext, "设置成功！当前为铃声", Toast.LENGTH_LONG).show();
    }
	
	/**
	 * 标准  设置情景模式二：即有声音也有振动
	 */
	public void normalScene() {
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_ON);
		mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_ON);
        sceneModeType = SCENE_MODE_GENERAL;//标准
   //     Toast.makeText(mContext, "设置成功！当前为铃声加振动", Toast.LENGTH_LONG).show();
    }
	
	/**
	 * 会议   设置情景模式三：只能振动
	 */
	public void meetingScene() {
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_ON);
		mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_ON);
        sceneModeType = SCENE_MODE_MEETING;//会议
    //    Toast.makeText(mContext, "设置成功！当前为振动", Toast.LENGTH_LONG).show();
    }
	
	/**
	 * 静音 设置情景模式四：无声无振动
	 */
	public void silentScene() {
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_OFF);
		mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_OFF);
        sceneModeType = SCENE_MODE_SILENT;//静音
    //    Toast.makeText(mContext, "设置成功！当前为无声无振动", Toast.LENGTH_LONG).show();
    }
	 
	TextView mTextView;
	public void setView(TextView textView){
		mTextView = textView;
	}
	
	public void updateUiShow(){
		int scenario = getSceneMode();
		switch (scenario) {
        case SceneModeSwitch.SCENE_MODE_GENERAL:
        	mTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_ic_ringer_normal, 0, 0);
        	mTextView.setText("标准");  
            break;
        case SceneModeSwitch.SCENE_MODE_SILENT:
        	mTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_ic_ringer_silent, 0, 0);
        	mTextView.setText("静音");
            break;
        case SceneModeSwitch.SCENE_MODE_MEETING:
        	mTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_ic_ringer_vibration, 0, 0);
        	mTextView.setText("震动");
            break;
        case SceneModeSwitch.SCENE_MODE_OUTDOOR:
        	mTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_ic_ringer_normal, 0, 0);
        	mTextView.setText("户外");
            break;
        
        }
	}
	
	public void switchProfile(){
		int sceneMode = getNextMode();
		switch (sceneMode) {
	        case SceneModeSwitch.SCENE_MODE_GENERAL:
	        	normalScene();
	            break;
	        case SceneModeSwitch.SCENE_MODE_SILENT:
	        	silentScene();
	            break;
	        case SceneModeSwitch.SCENE_MODE_MEETING:
	        	meetingScene();
	            break;
	        case SceneModeSwitch.SCENE_MODE_OUTDOOR:
	        	outdoorScene();
	            break;
        }
	}
	
}
