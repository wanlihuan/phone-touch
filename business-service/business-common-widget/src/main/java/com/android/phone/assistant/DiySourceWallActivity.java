package com.android.phone.assistant;

import net.youmi.android.dev.OnlineConfigCallBack;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.android.phone.assistant.util.CommonUtil;
import com.android.phone.assistant.util.ThemeManager;
import com.android.phone.assistant.util.YouMi;
import com.android.phone.assistant.widget.BottomSelectBar;
import com.android.phone.assistant.widget.BottomSelectBar.OnItemSwitchListener;
import com.ttlove.widget.TitleBar;

public class DiySourceWallActivity extends Activity implements OnItemSwitchListener{
	private RelativeLayout mBodyParent;
	private TitleBar mTitleHomeBar;
	private BottomSelectBar mBottomSelectBar;
	
	private View[] bottomChildViewCache = null;
	
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case CommonUtil.CURRENT_POINTS:
				int currentPoints = msg.arg1;
				//String currentName = (String) msg.obj;
				mTitleHomeBar.setTitlePointsText("�ҵĻ��֣�"+currentPoints);
				break;
			}
		}
	};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
        
        //��ʼ������
        if(CommonUtil.mAdType == CommonUtil.YOU_MI){
        	if(YouMi.getInstance() == null){
	        	YouMi.creatYoumiInstance(this);
	        	YouMi.getInstance().initYoumi();
        	}
        	
        	YouMi.getInstance().setHandler(mHandler);
        	YouMi.getInstance().queryPoints();
        	YouMi.getInstance().checkAppUpdate(this, false);
        }
        
        setContentView(R.layout.activity_diysourcewall);
//        final RootRelativeLayout mRoot = (RootRelativeLayout)this.findViewById(Rs.id.root);
        //������
        mTitleHomeBar = (TitleBar) this.findViewById(R.id.home_title_view);
        mTitleHomeBar.getTitleButton().setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DiySourceWallActivity.this.finish();
			}
        });
        
        //�м䲿��
        mBodyParent = (RelativeLayout)this.findViewById(R.id.body_parent);
        //�ײ�ѡ�
        mBottomSelectBar = (BottomSelectBar)this.findViewById(R.id.bottom_bar_view);
       
    }
    
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		YouMi.getInstance().asyncGetOnlineConfig("ThemePrice", new OnlineConfigCallBack(){

			public void onGetOnlineConfigFailed(String key) {
				// TODO Auto-generated method stub
				Log.d("tag", "ThemePrice key="+key);
			}

			public void onGetOnlineConfigSuccessful(String key, String value) {
				// TODO Auto-generated method stub
				Log.d("tag", "ThemePrice key = "+key+",value = "+value);
				
				ThemeManager.themePrice = value;
			}
		});
	}


	/**
	 * ��ʼ��ѡ��
	 * @param onItemSwitchListener �л�������
	 * @param itemNum ѡ��ĸ���
	 * @param title  ѡ���title
	 * @param drawableId ѡ���icon
	 * @param selected Ĭ��ѡ���˵�ѡ��
	 */
    public void initItems(OnItemSwitchListener onItemSwitchListener, int itemNum, 
    		String[] title, Drawable[] drawableId, int selectedItem){  
    	
    	 bottomChildViewCache = new View[itemNum];
    	 mBottomSelectBar.initItems(
    			 onItemSwitchListener,
    			 itemNum, 
    			 title, 
    			 drawableId,
    			 selectedItem
 		);
    }
		
    public void addView2Bottom(View view, int selectedItem){
    	bottomChildViewCache[selectedItem] = view;
    	mBodyParent.addView(view);
    }
    
    public void setTitleText(String text){
    	mTitleHomeBar.setTitleText(text);
	}
    
	public void onBottomItemSwitch(int selectedItem) {
		// TODO Auto-generated method stub
		 if(CommonUtil.mAdType == CommonUtil.WAPS){
			 
		 }else  if(CommonUtil.mAdType == CommonUtil.YOU_MI){
			 YouMi.getInstance().queryPoints();
		 }
		//������ʾ�л�����
		for(int i = 0; i < bottomChildViewCache.length; i++){
			if(bottomChildViewCache[i] != null){
				if(selectedItem == i){
					bottomChildViewCache[i].setVisibility(View.VISIBLE);
				}else
					bottomChildViewCache[i].setVisibility(View.GONE);
			}
		}
	}

	/**
	 * ��ʾ����ǽ�Ƽ�
	 */
	public void showOffersList(){
		 if(CommonUtil.mAdType == CommonUtil.WAPS){

		 }else  if(CommonUtil.mAdType == CommonUtil.YOU_MI){
			 YouMi.getInstance().showOffersWall();
		 }
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 if(CommonUtil.mAdType == CommonUtil.WAPS){
			 
		 }else  if(CommonUtil.mAdType == CommonUtil.YOU_MI){
			 YouMi.getInstance().onDestroy();
		 }
	}
    
}
