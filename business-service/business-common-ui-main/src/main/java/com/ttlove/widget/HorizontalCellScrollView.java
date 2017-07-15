package com.ttlove.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.Scroller;


public class HorizontalCellScrollView extends ViewGroup {
    private static final String TAG = "TaskScrollView";

    private static final boolean DEBUG = false;

    private static final int INVALID_SCREEN = -1;

    /**
     * The velocity at which a fling gesture will cause us to snap to the next
     * screen
     */
    private static final int SNAP_VELOCITY = 600;

    private int mDefaultScreen;

    private boolean mFirstLayout = true;

    private int mCurrentScreen;
    private int mNextScreen = INVALID_SCREEN;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mOverscrollDistance;
   // private int mOverflingDistance;
    

    private float mLastMotionX;
    private float mLastMotionY;

    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;

    private int mTouchState = TOUCH_STATE_REST;
    private boolean mAllowLongPress = true;
    //private boolean mIsUpdated = false;
    private int mTouchSlop;
    private int mMaximumVelocity;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;
    private WorkspaceOvershootInterpolator mScrollInterpolator;
    private OnScrollToScreenCallback mOnScrollToScreenCallback;
    private static final float BASELINE_FLING_VELOCITY = 2500.f;
    private static final float FLING_VELOCITY_INFLUENCE = 0.4f;   
    private Display mDisplay;

    private static class WorkspaceOvershootInterpolator implements Interpolator {
        private static final float DEFAULT_TENSION = 1.3f;
        private float mTension;

        public WorkspaceOvershootInterpolator() {
            mTension = DEFAULT_TENSION;
        }

        public void setDistance(int distance) {
            mTension = distance > 0 ? DEFAULT_TENSION / distance : DEFAULT_TENSION;
        }

        public void disableSettle() {
            mTension = 0.f;
        }

        public float getInterpolation(float t) {
            // _o(t) = t * t * ((tension + 1) * t + tension)
            // o(t) = _o(t - 1) + 1
            t -= 1.0f;
            return t * t * ((mTension + 1) * t + mTension) + 1.0f;
        }
    }

    /**
     * Used to inflate the Workspace from XML.
     * 
     * @param context The application's context.
     * @param attrs The attribtues set containing the Workspace's customization values.
     */
    public HorizontalCellScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Used to inflate the ToolBarView from XML.
     * 
     * @param context The application's context.
     * @param attrs The attribtues set containing the Workspace's customization values.
     * @param defStyle Unused.
     */
    @SuppressLint("NewApi")
	public HorizontalCellScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
       
        mDefaultScreen = 0;
        mDisplay = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        setHapticFeedbackEnabled(false);
        initWorkspace();
    }

    /**
     * Initializes various states for this workspace.
     */
	@SuppressLint("NewApi")
	private void initWorkspace() {
        Context context = getContext();
        mScrollInterpolator = new WorkspaceOvershootInterpolator();
        mScroller = new Scroller(context, mScrollInterpolator);
        mCurrentScreen = mDefaultScreen;

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        //mOverflingDistance = configuration.getScaledOverflingDistance();
    }

    /*private void initConfigurationPanels() {
        for (int i = 0; i < this.getChildCount(); i++) {
            ViewGroup childView = (ViewGroup) this.getChildAt(i);
            if (childView instanceof Configurable) {
                ((Configurable) childView).initConfigurationState();
            }
        }
        this.invalidate();
    }*/

   
    public int getIndicatorCount() {
        return this.getChildCount();
    }


    public void setOnScrollToScreenCallback(OnScrollToScreenCallback onScrollToScreenCallback) {
        mOnScrollToScreenCallback = onScrollToScreenCallback;
    }

    /**
     * @return The open folder on the current screen, or null if there is none
     */
    boolean isDefaultScreenShowing() {
        return mCurrentScreen == mDefaultScreen;
    }

    /**
     * Returns the index of the currently displayed screen.
     * @return The index of the currently displayed screen.
     */
    public int getCurrentScreen() {
        return mCurrentScreen;
    }

    /**
     * Sets the current screen.
     * @param currentScreen
     */
    public void setCurrentScreen(int currentScreen) {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        mCurrentScreen = Math.max(0, Math.min(currentScreen, getChildCount() - 1)); 
        /*if (mOnScrollToScreenCallback != null) {
        	Log.d("tag", "onScrollFinish setCurrentScreen");
            mOnScrollToScreenCallback.onScrollFinish(mCurrentScreen);
        }*/
        int width = mDisplay.getWidth();
        Log.i("Width", "width is: " + width);
        scrollTo(mCurrentScreen * width, 0);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
//            mScrollX = mScroller.getCurrX();
//            mScrollY = mScroller.getCurrY();
            postInvalidate();
        } else if (mNextScreen != INVALID_SCREEN) {
            mCurrentScreen = Math.max(0, Math.min(mNextScreen, getChildCount() - 1));
            if (DEBUG) {
                Log.d(TAG, "computeScroll  mCurrentScreen is " + mCurrentScreen);
            }
            if (mOnScrollToScreenCallback != null) {
            	Log.d("tag", "onScrollFinish computeScroll");
            	mOnScrollToScreenCallback.onScrollFinish(mCurrentScreen);
            }
            mNextScreen = INVALID_SCREEN;
            clearChildrenCache();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
    	super.dispatchDraw(canvas);
       /* boolean restore = false;
        int restoreCount = 0;

        // ViewGroup.dispatchDraw() supports many features we don't need:
        // clip to padding, layout animation, animation listener, disappearing
        // children, etc. The following implementation attempts to fast-track
        // the drawing dispatch by drawing only what we know needs to be drawn.

        boolean fastDraw = mTouchState != TOUCH_STATE_SCROLLING && mNextScreen == INVALID_SCREEN;
        // If we are not scrolling or flinging, draw only the current screen
        if (fastDraw) {
        	try{
        		drawChild(canvas, getChildAt(mCurrentScreen), getDrawingTime());
        	}catch(NullPointerException e){
        		scrollLeft();
        		 mCurrentScreen -- ;
        		 drawChild(canvas, getChildAt(mCurrentScreen), getDrawingTime());
        	}
            
        } else {
            final long drawingTime = getDrawingTime();
            final float scrollPos = (float) mScrollX / getWidth();
            final int leftScreen = (int) scrollPos;
            final int rightScreen = leftScreen + 1;
            if (leftScreen >= 0) {
                drawChild(canvas, getChildAt(leftScreen), drawingTime);
            }
            if (scrollPos != leftScreen && rightScreen < getChildCount()) {
                drawChild(canvas, getChildAt(rightScreen), drawingTime);
            }
        }

        if (restore) {
            canvas.restoreToCount(restoreCount);
        }*/
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        //final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // if (widthMode != MeasureSpec.EXACTLY) {
        // throw new
        // IllegalStateException("Workspace can only be used in EXACTLY mode.");
        // }
        //
        // final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // if (heightMode != MeasureSpec.EXACTLY) {
        // throw new
        // IllegalStateException("Workspace can only be used in EXACTLY mode.");
        // }

        // The children are given the same width and height as the workspace
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
        	/*if(getChildAt(i).getTag() instanceof ShortcutInfo){
        		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(CELL_WIDTH,
    					MeasureSpec.EXACTLY);
    			int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(CELL_HEIGHT,
    					MeasureSpec.EXACTLY);
    		
        		getChildAt(i).measure(childWidthMeasureSpec, childheightMeasureSpec);
        	}else*/{
        		getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        	}
        	
        }
        
        if (mFirstLayout) {
            setHorizontalScrollBarEnabled(false);
            scrollTo(mCurrentScreen * width, 0);
            setHorizontalScrollBarEnabled(true);
            mFirstLayout = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    	final int padding = 19;
        int childLeft = 0;
        final int count = getChildCount();
        Log.d(TAG, "TASKSCROLLVIEW count="+count);
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
               // child.setBackgroundColor(Color.RED);
                /*if(child.getTag() instanceof ShortcutInfo){
                	
                	if(i == 2)
                		childLeft += padding;
                	else if((i+2) % 4 == 0)
                		childLeft += 2 * padding + 3;
                	
                	child.layout(childLeft, 25, childLeft + childWidth, 25+child.getMeasuredHeight());
                	
                }else*/{
                	child.layout(childLeft, 0, childLeft + childWidth, child.getMeasuredHeight());
                }
                
              
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("NewApi")
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }
        
        acquireVelocityTrackerAndAddMovement(ev);

        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_MOVE: {
            /*
             * mIsBeingDragged == false, otherwise the shortcut would have
             * caught it. Check whether the user has moved far enough from his
             * original down touch.
             */

            /*
             * Locally do absolute value. mLastMotionX is set to the y value of
             * the down event.
             */
            final int pointerIndex = ev.findPointerIndex(mActivePointerId);
            final float x = ev.getX(pointerIndex);
            final float y = ev.getY(pointerIndex);
            final int xDiff = (int) Math.abs(x - mLastMotionX);
            final int yDiff = (int) Math.abs(y - mLastMotionY);

            final int touchSlop = mTouchSlop;
            boolean xMoved = xDiff > touchSlop;
            boolean yMoved = yDiff > touchSlop;

            if (xMoved || yMoved) {
                if (xMoved) {
                    // Scroll if the user moved far enough along the X axis
                    mTouchState = TOUCH_STATE_SCROLLING;
                    enableChildrenCache(mCurrentScreen - 1, mCurrentScreen + 1);
                }
                // Either way, cancel any pending longpress
                if (mAllowLongPress) {
                    mAllowLongPress = false;
                    // Try canceling the long press. It could also have been
                    // scheduled
                    // by a distant descendant, so use the mAllowLongPress flag
                    // to block
                    // everything
                    final View currentScreen = getChildAt(mCurrentScreen);
                    currentScreen.cancelLongPress();
                }
            }
            break;
        }

        case MotionEvent.ACTION_DOWN: {
            final float x = ev.getX();
            final float y = ev.getY();
            // Remember location of down touch
            mLastMotionX = x;
            mLastMotionY = y;
            mActivePointerId = ev.getPointerId(0);
            mAllowLongPress = true;

            /*
             * If being flinged and user touches the screen, initiate drag;
             * otherwise don't. mScroller.isFinished should be false when being
             * flinged.
             */
            mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
            break;
        }

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            // Release the drag
            clearChildrenCache();
            mTouchState = TOUCH_STATE_REST;
            mActivePointerId = INVALID_POINTER;
            mAllowLongPress = false;
            releaseVelocityTracker();
            break;

        case MotionEvent.ACTION_POINTER_UP:
            onSecondaryPointerUp(ev);
            break;
        }

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return mTouchState != TOUCH_STATE_REST;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = ev.getX(newPointerIndex);
            mLastMotionY = ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    /**
     * If one of our descendant views decides that it could be focused now, only
     * pass that along if it's on the current screen.
     * 
     * This happens when live folders requery, and if they're off screen, they
     * end up calling requestFocus, which pulls it on screen.
     */
    @Override
    public void focusableViewAvailable(View focused) {
        View current = getChildAt(mCurrentScreen);
        View v = focused;
        while (true) {
            if (v == current) {
                super.focusableViewAvailable(focused);
                return;
            }
            if (v == this) {
                return;
            }
            ViewParent parent = v.getParent();
            if (parent instanceof View) {
                v = (View) v.getParent();
            } else {
                return;
            }
        }
    }

    void enableChildrenCache(int fromScreen, int toScreen) {
        if (fromScreen > toScreen) {
            final int temp = fromScreen;
            fromScreen = toScreen;
            toScreen = temp;
        }

        final int count = getChildCount();

        fromScreen = Math.max(fromScreen, 0);
        toScreen = Math.min(toScreen, count - 1);

        for (int i = fromScreen; i <= toScreen; i++) {
            final View layout = /*(ViewGroup)*/ getChildAt(i);
            layout.setDrawingCacheEnabled(true);
            layout.setDrawingCacheEnabled(true);
        }
    }

    void clearChildrenCache() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View layout = /*(ViewGroup)*/ getChildAt(i);
            layout.setDrawingCacheEnabled(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        acquireVelocityTrackerAndAddMovement(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            /*
             * If being flinged and user touches, stop the fling. isFinished
             * will be false if being flinged.
             */
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }

            // Remember where the motion event started
            mLastMotionX = ev.getX();
            mActivePointerId = ev.getPointerId(0);
            if (mTouchState == TOUCH_STATE_SCROLLING) {
                enableChildrenCache(mCurrentScreen - 1, mCurrentScreen + 1);
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (mTouchState == TOUCH_STATE_SCROLLING) {
                // Scroll to follow the motion event
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float deltaX = mLastMotionX - x;
                mLastMotionX = x;

                if (deltaX < 0) {
//                	if (mScrollX > -mOverscrollDistance) {
//                		scrollBy((int) Math.max(-mOverscrollDistance, deltaX), 0);
//                	}
                } else if (deltaX > 0) {
//                    final int availableToScroll = getChildAt(getChildCount() - 1).getRight() - mScrollX - getWidth() + mOverscrollDistance;
//                    if (availableToScroll > 0) {
//                        scrollBy((int) Math.min(availableToScroll, deltaX), 0);
//                    }
                } else {
                    awakenScrollBars();
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            if (mTouchState == TOUCH_STATE_SCROLLING) {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                final int velocityX = (int) velocityTracker.getXVelocity(mActivePointerId);

                final int screenWidth = getWidth();
                int whichScreen = (/*mScrollX + */(screenWidth / 2)) / screenWidth;
                final float scrolledPos = /*(float) mScrollX / screenWidth*/2f;

                if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
                    // Fling hard enough to move left.
                    // Don't fling across more than one screen at a time.
                    final int bound = scrolledPos < whichScreen ? mCurrentScreen - 1 : mCurrentScreen;
                    snapToScreen(Math.min(whichScreen, bound), velocityX, true);
                } else if (velocityX < -SNAP_VELOCITY && mCurrentScreen < getScreenCount() - 1) {
                    // Fling hard enough to move right
                    // Don't fling across more than one screen at a time.
                    final int bound = scrolledPos > whichScreen ? mCurrentScreen + 1 : mCurrentScreen;
                    snapToScreen(Math.max(whichScreen, bound), velocityX, true);
                } else {
                	//add by wanlaihuan
                	if(whichScreen > getScreenCount() - 1)
                		whichScreen = getScreenCount() - 1;
                	
                    snapToScreen(whichScreen, 0, true);
                }
            }
            mTouchState = TOUCH_STATE_REST;
            mActivePointerId = INVALID_POINTER;
            releaseVelocityTracker();
            break;
        case MotionEvent.ACTION_CANCEL:
            if (mTouchState == TOUCH_STATE_SCROLLING) {
                final int screenWidth = getWidth();
                final int whichScreen = (/*mScrollX + */(screenWidth / 2)) / screenWidth;
                snapToScreen(whichScreen, 0, true);
            }
            mTouchState = TOUCH_STATE_REST;
            mActivePointerId = INVALID_POINTER;
            releaseVelocityTracker();
            break;
        case MotionEvent.ACTION_POINTER_UP:
            onSecondaryPointerUp(ev);
            break;
        }

        return true;
    }
    
    private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void snapToScreen(int whichScreen) {
        snapToScreen(whichScreen, 0, false);
    }

    private void snapToScreen(int whichScreen, int velocity, boolean settle) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        enableChildrenCache(mCurrentScreen, whichScreen);
        mNextScreen = whichScreen;
        /*if (mOnScrollToScreenCallback != null) {
        	Log.d("tag", "onScrollFinish snapToScreen");
        	mOnScrollToScreenCallback.onScrollFinish(mNextScreen);
        }*/
        if (DEBUG) {
            Log.d(TAG, "snapToScreen  mNextScreen is " + mNextScreen);
        }

        View focusedChild = getFocusedChild();
        if (focusedChild != null && whichScreen != mCurrentScreen
                && focusedChild == getChildAt(mCurrentScreen)) {
            focusedChild.clearFocus();
        }

        final int screenDelta = Math.max(1, Math.abs(whichScreen - mCurrentScreen));
        final int newX = whichScreen * getWidth();
        final int delta = newX/* - mScrollX*/;
        int duration = (screenDelta + 1) * 100;

        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        if (settle) {
            mScrollInterpolator.setDistance(screenDelta);
        } else {
            mScrollInterpolator.disableSettle();
        }

        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration += (duration / (velocity / BASELINE_FLING_VELOCITY)) * FLING_VELOCITY_INFLUENCE;
        } else {
            duration += 100;
        }
        awakenScrollBars(duration);
        mScroller.startScroll(/*mScrollX*/0, 0, delta, 0, duration);
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final SavedState state = new SavedState(super.onSaveInstanceState());
        state.currentScreen = mCurrentScreen;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.currentScreen != -1) {
            mCurrentScreen = savedState.currentScreen;
        }
    }

    public void scrollLeft() {
        if (mScroller.isFinished()) {
            if (mCurrentScreen > 0) {
                snapToScreen(mCurrentScreen - 1);
            }
        } else {
            if (mNextScreen > 0) {
                snapToScreen(mNextScreen - 1);
            }
        }
    }

    public void scrollRight() {
        if (mScroller.isFinished()) {
            if (mCurrentScreen < getChildCount() - 1) {
                snapToScreen(mCurrentScreen + 1);
            }
        } else {
            if (mNextScreen < getChildCount() - 1) {
                snapToScreen(mNextScreen + 1);
            }
        }
    }

    public int getScreenForView(View v) {
        int result = -1;
        if (v != null) {
            ViewParent vp = v.getParent();
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (vp == getChildAt(i)) {
                    return i;
                }
            }
        }
        return result;
    }

    /**
     * @return True is long presses are still allowed for the current touch
     */
    public boolean allowLongPress() {
        return mAllowLongPress;
    }

    /**
     * Set true to allow long-press events to be triggered, usually checked by
     */
    public void setAllowLongPress(boolean allowLongPress) {
        mAllowLongPress = allowLongPress;
    }

    public void moveToDefaultScreen(boolean animate) {
        if (animate) {
            snapToScreen(mDefaultScreen);
        } else {
            setCurrentScreen(mDefaultScreen);
        }
        getChildAt(mDefaultScreen).requestFocus();
    }

    static class SavedState extends BaseSavedState {
        int currentScreen = -1;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentScreen = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentScreen);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
    
   public int getScreenCount(){
	 
	   int viewCount = getChildCount() - 2;
	   int screenCount = 2 + viewCount / 4 + (viewCount % 4 > 0? 1 : 0);
	   
	   return screenCount;	   
   }
   
   
    // callback for updating indicator
   public interface OnScrollToScreenCallback {
	   /**
	    * ������ɺ�Ļص�
	    * @param currentIndex
	    */
        public void onScrollFinish(int currentIndex);
    }
  
}
