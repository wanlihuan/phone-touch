package com.assistivetouch.widget;

import android.content.Context;
import android.media.AudioManager;

public class VolumeAdjust extends ToastProgressBar{

	public  AudioManager audiomanage;  
    private int maxVolume;  
    private int mVolumeType = AudioManager.STREAM_MUSIC;
    
    /**
     * 
     * @param context
     * @param volumeType 如：AudioManager.STREAM_MUSIC  
     */
    public VolumeAdjust(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
		
	}
    
  
    public VolumeAdjust(Context context, CharSequence text, int duration, int volumeType) {
		this(context, text, duration);
		// TODO Auto-generated constructor stub
		mVolumeType = volumeType;
		maxVolume = audiomanage.getStreamMaxVolume(volumeType);
	}
    
    private VolumeAdjust(Context context, CharSequence text, int duration) {
		super(context, text, duration);
		// TODO Auto-generated constructor stub
		
		audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); 
	}

    public int getMaxVolume(){
    	
    	return maxVolume;
    }
    
    public void updateVolume(int volumeValue){
    	int currentVolume = getCurrentStreamVolume();  //获取当前值  
   	 if(currentVolume > maxVolume || currentVolume < 0)
   		 return;
   	 
   	 audiomanage.setStreamVolume(mVolumeType, volumeValue, 0);
    }
  
    public int getCurrentStreamVolume(){
    	return audiomanage.getStreamVolume(mVolumeType);  //获取当前值  
    }
    
    public int volume2progress(int volume){
    	
		float progressPercent = (float)volume / (float)maxVolume;//百分比
		int currentProgress = (int)(progressPercent * maxProgress);
		
		return currentProgress;
    }
    
    public int progress2volume(int progress){
    	
		float progressPercent = progress / (float)maxProgress;
		int currentValue = (int) (progressPercent * maxVolume);
		
		return currentValue;
    }
}
