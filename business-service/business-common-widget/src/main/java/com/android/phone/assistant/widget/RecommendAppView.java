package com.android.phone.assistant.widget;

import java.util.List;

import com.android.phone.assistant.DiyAppDetailActivity;
import com.android.phone.assistant.R;
import com.android.phone.assistant.util.YouMi;
import com.android.phone.assistant.widget.RecommendAdapter.OnClickStatusListener;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * app列表视图
 * @author wanlh
 *
 */
public class RecommendAppView extends FrameLayout{  
	private final int SET_APP_ADAPTER_UPDATE_LIST = 0;
	
	private Context mContext;
	private ListView mRecommendList;
	private RecommendAdapter mRecommendAdapter;
	private boolean isShowComplete = false;
	private boolean loadFlag = true;
	
	private Handler mHandler = new Handler(){
	    
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.what){
			
			case SET_APP_ADAPTER_UPDATE_LIST:
				Log.d("TAG", "SET_APP_ADAPTER_UPDATE_LIST");
				updateAppList();
				
				break;
			}
		}
     };
     
	public RecommendAppView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public RecommendAppView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public RecommendAppView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.recommend_app_layout, this, true);
		
		mRecommendList = (ListView) this.findViewById(R.id.recommendList);
		mRecommendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("adId", i);
                intent.setClass(mContext, DiyAppDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
		
		mRecommendAdapter = new RecommendAdapter(mContext);
		mRecommendAdapter.setOnClickStatusListener(new OnClickStatusListener(){    
			public void OnClickStatus(int status, int adId) {
				// TODO Auto-generated method stub
				
				//有米
				YouMi.getInstance().downloadAd(adId);
			}
        });
		
	}
	
    /**
     * 更新列表数据
     * @param listView
     * @param itemList
     */
    public void updateAppList(){
    	
    	if(loadFlag == false){
    		isShowComplete = false;
    		return;
    	}
    	
    	 List<AppInfo> appInfoList = null;
		try {
			appInfoList = YouMi.getInstance().getAppInfo(YouMi.getInstance().getAdList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    	 if(appInfoList != null){
    		Log.d("WANLAIHUAN_DEBUG", "正在加载 setAppListAdapter");
    		isShowComplete = true;
    		mRecommendAdapter.setList(appInfoList);
	        mRecommendList.setAdapter(mRecommendAdapter);
	        
    	 }else{
			 Log.d("WANLAIHUAN_DEBUG", "加载中... setAppListAdapter");
			 mHandler.removeMessages(SET_APP_ADAPTER_UPDATE_LIST);
			 mHandler.sendEmptyMessageDelayed(SET_APP_ADAPTER_UPDATE_LIST, 500);    
    	 }
    }
    
    /**
     * 加载列表是否完成
     * @return
     */
    public boolean isShowComplete(){
    	
    	return isShowComplete;
    }
    
    /**
     * 停止加载摔适配器
     */
    public void setLoadFlag(boolean flag){
    	loadFlag = flag;
    	
    	if(flag)
    		Log.d("WANLAIHUAN_DEBUG", "开始加载 stopSetAdapter");
    	else
    		Log.d("WANLAIHUAN_DEBUG", "停止加载 stopSetAdapter");
    }
}
