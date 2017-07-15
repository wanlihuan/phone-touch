package com.assistivetouch.widget;

import com.android.assistivetouch.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FolderTaskView extends ViewGroup implements View.OnClickListener, View.OnLongClickListener{
	
	private int hCount;
	private int lCount = 4;
	private int mCellWidth = 0;
	private int mCellHeight = 0;
	private final int PANDDING = 18;
	private static final int CLOSE_FOLDER_HANDLER = 1;
	private static final int IN_FOLDER_EDIT_MODE = 2;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public FolderTaskView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
		
	}

	public FolderTaskView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public FolderTaskView(Context context, AttributeSet attrs, int mode) {
		super(context, attrs, 0);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public void createShortcutTask(ShortcutInfo shortcutInfo) {
		
		TextView favorite = (TextView) mInflater.inflate(
				R.layout.item_text_view, null);
		
		favorite.setLayoutParams(new MyLayoutParams());
		
		favorite.setCompoundDrawablesWithIntrinsicBounds(null,
				shortcutInfo.icon, null, null);
		favorite.setText(shortcutInfo.title);
		favorite.setCompoundDrawablePadding(0);
		favorite.setTag(shortcutInfo);
		
		addView(favorite);
		
		favorite.setOnLongClickListener(this);
		favorite.setOnClickListener(this);
	}
	
	public void setlhCount(int allCount){
		lCount = 4;
		hCount = allCount / lCount + ((allCount % lCount) > 0?1 : 0);
	}
	
	View getViewByCellId(int index) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View cell = getChildAt(i);
			if (cell.getVisibility() != View.GONE) {
				MyLayoutParams lp = (MyLayoutParams) cell
						.getLayoutParams();
				if (index == (lp.cellY * lCount + lp.cellX))
					return cell;
			}
		}
		return null;
	}

	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub

		return true;
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Object tag = v.getTag();
		if (tag instanceof ShortcutInfo) {
				final Intent intent = ((ShortcutInfo) tag).intent;
			    startActivitySafely(intent, tag);
			    
			    mContext.sendBroadcast(new Intent("android.touch.action.CLOSE_RECENT_TASK"));
			    mContext.sendBroadcast(new Intent("android.touch.action.TOUCH_PANEL_CLOSE"));
		}
	}
		
	private void startActivitySafely(Intent intent, Object tag) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				mContext.startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(mContext, R.string.activity_not_found,
						Toast.LENGTH_SHORT).show();
				Log.e("TAG", "Unable to launch. tag=" + tag + " intent=" + intent, e);
			} catch (SecurityException e) {
				Toast.makeText(mContext, R.string.activity_not_found,
						Toast.LENGTH_SHORT).show();
				Log.e("TAG",
						"Launcher does not have the permission to launch "
								+ intent
								+ ". Make sure to create a MAIN intent-filter for the corresponding activity "
								+ "or use the exported attribute for this activity. "
								+ "tag=" + tag + " intent=" + intent, e);
			}
		}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		//int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		//int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		//int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		
		mCellWidth = widthSpecSize / lCount;
		mCellHeight = (int)(mCellWidth * 1.2f);
		
		// The children are given the same width and height as the workspace
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {

			View child = getChildAt(i);
			MyLayoutParams lp = (MyLayoutParams) child
					.getLayoutParams();
			
			lp.cellX = i % lCount;
       	    lp.cellY = i / lCount;
			lp.width = mCellWidth;
			lp.height = mCellHeight;
			lp.x = lp.cellX * mCellWidth;
			lp.y = lp.cellY * mCellHeight;
       	  
			child.setLayoutParams(lp);
			
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width,
					MeasureSpec.EXACTLY);
			int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height,
					MeasureSpec.EXACTLY);
			child.measure(childWidthMeasureSpec, childheightMeasureSpec);
		}

		setMeasuredDimension(widthSpecSize, hCount * mCellHeight);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		MyLayoutParams lp;
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			View cell = getChildAt(i);
			if (cell != null && cell.getVisibility() != View.GONE) {
				lp = (MyLayoutParams) cell.getLayoutParams();
				cell.layout(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
			}
		}
	}
	
	public int getViewHeight(){
		return hCount * mCellHeight;
	}
	
	public int getVisibleCells(){
		int count = getChildCount();
		int visibleCells = 0;
		for (int i = 0; i < count; i ++) {
			View v = getChildAt(i);
			if(v.getVisibility() != View.GONE)
				visibleCells ++;			
		}
	
		return visibleCells;
	}
	
	public void pointToCellExact(int x, int y, int[] cellXY){
		cellXY[0] = x / mCellWidth;
		cellXY[1] = y / mCellHeight;
	}
	
	 void cellToPoint(int cellX, int cellY, int[] result) {
		 result[0] = cellX * mCellWidth + PANDDING;
		 result[1] = cellY * mCellHeight;
	 }
	

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case CLOSE_FOLDER_HANDLER:
					removeAllViews();
					
					break;
					
				case IN_FOLDER_EDIT_MODE:
					break;
			}
		}
	};
	
	public class MyLayoutParams extends MarginLayoutParams {
        /**
         * Horizontal location of the item in the grid.
         */
        @ViewDebug.ExportedProperty
        public int cellX;

        /**
         * Vertical location of the item in the grid.
         */
        @ViewDebug.ExportedProperty
        public int cellY;

        // X coordinate of the view in the layout.
        @ViewDebug.ExportedProperty
		public int x;
        // Y coordinate of the view in the layout.
        @ViewDebug.ExportedProperty
        public int y;

        public MyLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public MyLayoutParams(LayoutParams source) {
            super(source);
        }
        
        public MyLayoutParams() {
            super(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
   }
}
