package com.ttlove.widget;

import com.android.ttlove.ttlovelibs.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TopSelectBar extends FrameLayout{
	private Context mContext;
	private LinearLayout mRoot;
	int mOldItem = 0;
	
	public TopSelectBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public TopSelectBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public TopSelectBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		mRoot = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mRoot.setLayoutParams(lp);
		mRoot.setGravity(Gravity.CENTER_VERTICAL);
		mRoot.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(mRoot);
		
		/*setItems(
				2, 
				new String[]{"我的主题","主题世界"}); 
		updateUiShow(1);*/
	}
	
	/**
	 * 
	 * @param onClickItemSwitchListener
	 * @param itemNum 总共的选项个数
	 * @param titleArray 数组长度为itemNum个的选项标题
	 * @param defaultItemNum 默认显示哪一项
	 */
	public void setInitItems(final OnClickItemSwitchListener onClickItemSwitchListener, int itemNum, String[] titleArray, int defaultItemNum){
		
		for(int i = 0; i < itemNum; i++){
			LinearLayout parentView = new LinearLayout(mContext);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.weight = 1.0f;
			parentView.setLayoutParams(lp);
			parentView.setOrientation(LinearLayout.VERTICAL);
			parentView.setGravity(Gravity.CENTER);
			parentView.setTag(""+i);
			
			MyTextView textView = new MyTextView(mContext);
			textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					mContext.getResources().getDimensionPixelSize(R.dimen.top_select_bar_h)));
			textView.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
			textView.setTag("textview");
			textView.setGravity(Gravity.CENTER);
			textView.setText(titleArray[i]);
			textView.setOnClickListener(new OnClickListener(){
				public void onClick(View view) {
					// TODO Auto-generated method stub
					ViewGroup parentView = (ViewGroup) view.getParent();
					String parentTag = (String) parentView.getTag();
					int selectedItem = Integer.parseInt(parentTag);
					if(selectedItem != mOldItem){
						if(onClickItemSwitchListener != null){
							onClickItemSwitchListener.onClickItemSwitch(selectedItem);
							updateUiShow(selectedItem);
						}
					}
				}
			});
			View view = new View(mContext);
			view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3)); 
			view.setBackgroundColor(Color.rgb(0xc0, 0xc0, 0xc0));
			view.setTag("indicator");
			parentView.addView(textView);
			parentView.addView(view);
			
			mRoot.addView(parentView); 
			
			if(i != itemNum-1){//竖直分割线
				View divider = new View(mContext);
				divider.setLayoutParams(new LayoutParams(1, 30));
				divider.setBackgroundColor(Color.rgb(0xc0, 0xc0, 0xc0));
				divider.setTag("divider");
				mRoot.addView(divider);
			}
		}
		
		updateUiShow(defaultItemNum);
		if(onClickItemSwitchListener != null)
			onClickItemSwitchListener.onClickItemSwitch(defaultItemNum);
	}
	
	public void updateUiShow(int selectedItem){
		mOldItem = selectedItem;
		
		//修改显示
		int rootChildCount = mRoot.getChildCount();
		for(int i = 0; i < rootChildCount; i++){
			View parentView = (View) mRoot.getChildAt(i);
			String tag = (String)parentView.getTag();
			
			if(tag.equals(""+selectedItem)){
				for(int j = 0; j < ((ViewGroup)parentView).getChildCount(); j++){
					View view = ((ViewGroup)parentView).getChildAt(j);
					String tag2 = (String) view.getTag();
					if(tag2.equals("textview")){
						((TextView)view).setTextColor(Color.rgb(0xff, 0x7c, 0x00));
					}else{
						view.setBackgroundColor(Color.rgb(0xff, 0x7c, 0x00));
					}
				}
			}else{
				if(!tag.equals("divider")){
					for(int j = 0; j < ((ViewGroup)parentView).getChildCount(); j++){
						View view = ((ViewGroup)parentView).getChildAt(j);
						String tag2 = (String) view.getTag();
						if(tag2.equals("textview")){
							((TextView)view).setTextColor(Color.BLACK);
						}else{
							view.setBackgroundColor(Color.rgb(0xc0, 0xc0, 0xc0));
						}
							
					}
				}
			}
		}
	}
	
	class MyTextView extends TextView{

		public MyTextView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			switch(event.getAction()){
	        case MotionEvent.ACTION_DOWN: 
	        	setBackgroundColor(Color.rgb(0xff, 0x7c, 0x00));
	        break;
	        case MotionEvent.ACTION_UP: 
		        setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
		    break;
			}
			return super.onTouchEvent(event);
		}
		
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO: currently ignoring padding            
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    	int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);            
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);
        
        int maxMeasureHeight = 0;
        
        if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
                throw new RuntimeException("Dockbar cannot have UNSPECIFIED dimensions");
        }
        
        final int count = getChildCount();
        
        //获取最大测量高度
        for (int i = 0; i < count; i++) {
        	View childView = getChildAt(i);
        	/*childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
          	                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));*/
        	
        	maxMeasureHeight = Math.max(maxMeasureHeight, childView.getMeasuredHeight());
        }
        setMeasuredDimension(widthSpecSize, maxMeasureHeight); 
	}
	
	OnClickItemSwitchListener mOnItemSwitchListener = null;
	/* public void setOnItemSwitchListener(OnItemSwitchListener onItemSwitchListener){
		 mOnItemSwitchListener = onItemSwitchListener;
	 }*/
	 public interface OnClickItemSwitchListener{
		 void onClickItemSwitch(int selectedItem);
	 }
}
