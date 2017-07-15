package com.android.phone.assistant.widget;

import com.android.phone.assistant.widget.NetStatusHintView;
import com.android.phone.assistant.widget.RecommendAppView;
import com.android.phone.assistant.widget.TextProgress;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 应用精品推荐界面
 * @author wanlh
 */
public class UiRecommendApp extends FrameLayout{
	final int UPDATE_VIEW_SHOW = 0;
	TextProgress mRefProgress;
	NetStatusHintView mNetStatusHintView;
	RecommendAppView mRecommendAppView;
	
	private Handler mHandler = new Handler(){
		final int TIME_MS = 3 * 1000;//最长连接3秒钟的时间
		int count = 0;
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.what){
			case UPDATE_VIEW_SHOW:
				if(mRecommendAppView.isShowComplete()){
					 count = 0;
					 mRefProgress.setVisibility(View.GONE);
					 mNetStatusHintView.setVisibility(View.GONE);
					 mRecommendAppView.setVisibility(View.VISIBLE);
					 
				}else{ 
					 
					if(++count >= TIME_MS / 100){
						count = 0;
						
						/*if(CommonUtil.getNetworkIsAvailable(mContext))
							Toast.makeText(mContext, "退出再试试！", 500).show();
						else
							Toast.makeText(mContext, "无法连接服务器或网络！", 500).show();*/
						
						mRecommendAppView.setLoadFlag(false);  //此时应该停止加载
						mRefProgress.setVisibility(View.GONE);
						mRecommendAppView.setVisibility(View.GONE);
						mNetStatusHintView.setVisibility(View.VISIBLE);
						
					}else{
						mHandler.removeMessages(UPDATE_VIEW_SHOW);
						mHandler.sendEmptyMessageDelayed(UPDATE_VIEW_SHOW, 100); 
					}
				}
				break;
			}
			
		}
	};
	
	/**
	 * 精品推荐列表界面
	 * @param context
	 */
	public UiRecommendApp(Context context){
		this(context, null);
		
    }
	public UiRecommendApp(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	private Context mContext;

	public UiRecommendApp(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		startShowList();
	}
	
	public void startShowList(){
		//进度条view
		mRefProgress = new TextProgress(mContext);
		mRefProgress.setVisibility(View.VISIBLE);
		
		 //网络不好的提示View
        mNetStatusHintView = new NetStatusHintView(mContext);
        mNetStatusHintView.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				refurbishRecommendAppList();
			}
        });
        
    	//精品展示的View
        mRecommendAppView = new RecommendAppView(mContext);
        
        addView(mRefProgress);
        addView(mNetStatusHintView);
        addView(mRecommendAppView);
        
        refurbishRecommendAppList();
	}
	
    private void refurbishRecommendAppList(){
		mRefProgress.setVisibility(View.VISIBLE);
    	mNetStatusHintView.setVisibility(View.GONE);
    	mRecommendAppView.setVisibility(View.GONE); 
    	
    	mRecommendAppView.updateAppList();
    	
		mHandler.removeMessages(UPDATE_VIEW_SHOW);
		mHandler.sendEmptyMessage(UPDATE_VIEW_SHOW);
   }
}
