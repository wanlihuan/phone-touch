package com.android.phone.assistant.widget;

import com.android.phone.assistant.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * �ײ�Table ѡ��ѡ��View
 * @author wanlh
 *
 */
public class BottomSelectBar extends ViewGroup{
	private Context mContext;
	private int mItemsCount = 0;
	private int mItemWidth = 0;
	public BottomSelectBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public BottomSelectBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public BottomSelectBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.setBackgroundResource(R.drawable.eoe_botbg_navi);
		
	}

	/**
	 * ��ʼ��ѡ��
	 * @param onItemSwitchListener �л�������
	 * @param itemNum ѡ��ĸ���
	 * @param title  ѡ���title
	 * @param drawableId ѡ���icon
	 * @param selected Ĭ��ѡ���˵�ѡ��
	 */
	public void initItems(OnItemSwitchListener onItemSwitchListener, int itemNum, String[] title, Drawable[] drawableId, int selectedItem){
		
		itemNum = itemNum < 0? 0 : itemNum;
		
		selectedItem = selectedItem >= itemNum ? itemNum - 1 : selectedItem;
		selectedItem = selectedItem < 0? 0 : selectedItem;
		
		mItemsCount = itemNum;
		
		LayoutParams lp;

		
		//ָʾ����
		/*View indicateBar = new View(mContext);
		indicateBar.setBackgroundDrawable(mContext.getResources().getDrawable(
				Rs.drawable.bottom_bar_shadow));
		
		lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
	   			ViewGroup.LayoutParams.WRAP_CONTENT);
		//lp.cellx = i;
		lp.type = LayoutParams.INDICATE_BAR;
		indicateBar.setLayoutParams(lp);
		indicateBar.setVisibility(View.GONE);
		addView(indicateBar);*/

		//ָʾ����
		
		View mIndicateView = new View(mContext);
	//	mIndicateView.setBackgroundResource(R.drawable.eoe_botbg_selected/*Rs.drawable.bottom_button_pressed*/);
		mIndicateView.setBackgroundColor(Color.rgb(0x00, 0xa0, 0x00));
		lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
	   			ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.cellx = selectedItem;
		lp.type = LayoutParams.INDICATE_PAND;
		mIndicateView.setLayoutParams(lp);
		addView(mIndicateView);
		
		//item �ؼ�
		for(int i = 0; i < itemNum; i++){
			TextView textView = new TextView(mContext);
			
			lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
    	   			ViewGroup.LayoutParams.MATCH_PARENT);
			lp.cellx = i;
			lp.type = LayoutParams.ITEM_VIEW;

			textView.setLayoutParams(lp);
			textView.setGravity(Gravity.CENTER);
			
			textView.setText(title[i]);
			//textView.setTextSize(14.00f);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableId[i], null, null);
			if(lp.cellx == selectedItem){
				textView.setSelected(true);
				textView.setTextColor(Color.WHITE);
			}else{
				textView.setSelected(false);
				textView.setTextColor(Color.BLACK);
			}
			
			addView(textView);
		}
		
		mOnItemSwitchListener = onItemSwitchListener;
		onBottomItemSwitch(selectedItem);
	}
	
	public void switchItems(int selectedItem){
		int count = getChildCount();
		for(int i = 0; i < count; i++){
			View childView = getChildAt(i);
			
			LayoutParams lp = (LayoutParams) childView.getLayoutParams();
			if(lp.type == LayoutParams.INDICATE_PAND){
				lp.cellx = selectedItem;
				lp.lx = lp.cellx * lp.width;
			}else if(lp.type == LayoutParams.ITEM_VIEW){
				
				if(lp.cellx == selectedItem){
					((TextView)childView).setSelected(true);
					((TextView)childView).setTextColor(Color.WHITE);
				}else{
					((TextView)childView).setSelected(false);
					((TextView)childView).setTextColor(Color.BLACK);
				}
			}
		}
		
		requestLayout();
		
		onBottomItemSwitch(selectedItem);
		
	 }
	
	private void onBottomItemSwitch(final int selectedItem){
		/*new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();*/
				if(mOnItemSwitchListener != null)
					 mOnItemSwitchListener.onBottomItemSwitch(selectedItem);
			/*}
		}.start();*/
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: 
             switchItems(getTouchCell(event.getX()));
        	break;
		}
		return super.onTouchEvent(event);
	}

	private int getTouchCell(float touchP){
		int selectItemNum = 0;
		for(int i = 0; i < mItemsCount; i++){
			if(touchP >= mItemWidth * i && touchP < mItemWidth * (i + 1)){
				selectItemNum = i;
				break;
			}
		}
		
		return selectItemNum;
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		final int count = getChildCount();
        for (int i = 0; i < count; i++) {
        	View view = getChildAt(i);
        	LayoutParams lp = (LayoutParams) view.getLayoutParams(); 
            view.layout(lp.lx, lp.ly, lp.lx + lp.width, lp.height+lp.ly);
        }
	}

	@Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        // TODO: currently ignoring padding            
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	    	int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
	        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);            
	        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//	        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);
	        
	        int maxMeasureHeight = 0;
	        int indicateBarHeight = 0;
	        int itemTBPanding = 6;
	        
	        
	        if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
	                throw new RuntimeException("Dockbar cannot have UNSPECIFIED dimensions");
	        }
	        
	        final int count = getChildCount();
	        
	        //��ȡ�������߶�
	        for (int i = 0; i < count; i++) {
	        	View childView = getChildAt(i);
	        	childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	          	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        	
	        	//��ȡָʾ�����ĸ߶�
	        	LayoutParams lp = (LayoutParams) childView.getLayoutParams();
	        	if(lp.type == LayoutParams.INDICATE_BAR)
	        		indicateBarHeight = childView.getMeasuredHeight();
	            
	        	maxMeasureHeight = Math.max(maxMeasureHeight, childView.getMeasuredHeight());
	        }
	        int pandH = maxMeasureHeight + 2 * itemTBPanding;
	        //���ò��ֲ���
	        for (int i = 0; i < count; i++) {
	        	View childView = getChildAt(i);
	        	LayoutParams lp = (LayoutParams) childView.getLayoutParams();
	        	
	        	if(lp.type == LayoutParams.INDICATE_BAR){
	            	lp.width = widthSpecSize;
	            	lp.height = indicateBarHeight;
		            lp.lx = 0;//lp.cellx * lp.width;
		            lp.ly = 0;

	            }else if(lp.type == LayoutParams.INDICATE_PAND){
	            	
	            	lp.width = widthSpecSize / mItemsCount;
	            	mItemWidth = lp.width;
		            lp.height = pandH;
		            lp.lx = lp.cellx * lp.width;
		            lp.ly = indicateBarHeight;
		            	
	            } else if(lp.type == LayoutParams.ITEM_VIEW){
	            	
		        	lp.width = childView.getMeasuredWidth();
		            lp.height = childView.getMeasuredHeight();
		            lp.lx = (widthSpecSize / mItemsCount - lp.width) / 2 + 
		            		lp.cellx * widthSpecSize / mItemsCount;
		            lp.ly = (pandH / 2)+indicateBarHeight- lp.height / 2;//itemTBPanding;
		            
	            }
	            
	            childView.setLayoutParams(lp);
	        } 
	        

	        setMeasuredDimension(widthSpecSize, maxMeasureHeight + 2 * itemTBPanding); 
	 }
	 
	 
	 OnItemSwitchListener mOnItemSwitchListener = null;
	/* public void setOnItemSwitchListener(OnItemSwitchListener onItemSwitchListener){
		 mOnItemSwitchListener = onItemSwitchListener;
	 }*/
	 public interface OnItemSwitchListener{
		 void onBottomItemSwitch(int selectedItem);
	 }
	 
	 public class LayoutParams extends MarginLayoutParams {
		 static final int  INDICATE_BAR = 0;
		 static final int  INDICATE_PAND = 1;
		 static final int  ITEM_VIEW = 2;

		 public LayoutParams(int wrapContent, int wrapContent2) {
	            super(wrapContent, wrapContent2);
	     }

         public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
         }

	        /**
	         * ���ֵ�X����
	         */
	        int lx;
	        
	        /**
	         * ���ֵ�Y����
	         */
	        int ly;
	        
	        /**
	         * �ڵڼ�ѡ��
	         */
	        int cellx;

	        /**
	         * �ӿؼ������ͣ�ָʾ���塢ѡ��View
	         * 
	         */
	        int type;
	 }

}
