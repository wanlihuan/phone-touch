package com.fiona.panel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.fiona.fwindow.IFloatView;
import com.fiona.fwindow.WindowLayoutParams;

/**
 * Created by laihuan.wan on 2017/7/15 0015.
 */

public class FloatView extends AppCompatImageView implements IFloatView {

    public static final int maxAlpha = 250;
    public static final int minAlpha = 50;
    public static int normalAlpha = minAlpha + 80;

    public static final int DaxiaoMaxProgress = 1000;
    public static float DaxiaoMaxRatio = 0.250f;
    public static float DaxiaoMinRatio = 0.050f;
    public static float DaxiaoNormalRatio = 0.14f;

    public static final int DURATION = 500;
    private final int DELAY_MILLIS = 50;

    private final int WHAT_SHAPE_ALPHA = 1;

    //private Bitmap mNormalBitmap = null;

    private float startX = 0.0f;//记录DOWN事件手指相对屏幕坐标
    private float startY = 0.0f;

    private float currentX;//记录move事件后手指相对屏幕的位置
    private float currentY;

    private float mTouchStartX;//记录手指相对浮动窗内部的坐标
    private float mTouchStartY;

    private int floatPx;//记录最终浮动窗的显示位置
    private int floatPy;

    private int floatEndPx = 0;//浮动窗最终的位置
    private int floatEndPy = 0;

    private int statusBarHeight = 0;//状态栏高度
    private int pixWidth = 0; //屏幕宽度
    private int pixHeight = 0;
    private int fpx = 0;
    private int fpy = 0;

    private final int MOVE_TO_LEFT = 0;
    private final int MOVE_TO_TOP = 1;
    private final int MOVE_TO_RIGHT = 2;
    private final int MOVE_TO_BOTTOM = 3;
    private boolean shapeAlphaFlag = false;
    private int maxCount;


    /**
     * 移动的方向标志
     */
    private int mMoveDirection = MOVE_TO_LEFT;

    private Handler mHandler = new Handler() {
        final int offset = 18;//小白点移动的步伐
        int fromAlpha = 0;
        int alphaOffset = 0;

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case 0:

                    if (mMoveDirection == MOVE_TO_LEFT) {
                        fpx -= offset;
                    } else if (mMoveDirection == MOVE_TO_TOP) {
                        fpy -= offset;
                    } else if (mMoveDirection == MOVE_TO_RIGHT) {
                        fpx += offset;
                    } else if (mMoveDirection == MOVE_TO_BOTTOM) {
                        fpy += offset;
                    }

                    updateViewPosition(fpx, fpy);

                    if (fpx > 0 && fpx < pixWidth && fpy > 0 && fpy < pixHeight)
                        sendEmptyMessage(0);
                    else
                        setShapeAlpha(null, FloatView.maxAlpha, FloatView.normalAlpha, DURATION);

                    break;

                case WHAT_SHAPE_ALPHA:
                    if (shapeAlphaFlag == false) {
                        fromAlpha = msg.arg1;
                        alphaOffset = msg.arg2;
                        shapeAlphaFlag = true;
                    } else {
                        maxCount--;
                        fromAlpha += alphaOffset;
                        FloatView.this.setAlpha(fromAlpha);
                    }

                    if (maxCount >= 0)
                        mHandler.sendEmptyMessageDelayed(WHAT_SHAPE_ALPHA, DELAY_MILLIS);
                    else {
                        if (mOnShapeAlphaListener != null)
                            mOnShapeAlphaListener.shapeAlphaEnd();
                    }

                    break;
            }
        }

    };

    public FloatView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param normalBitmap 正常Bitmap
     */
    public void setImageBitmap(Bitmap normalBitmap) {
        if (normalBitmap == null)
            this.setBackgroundResource(R.drawable.btn_assistivetouch);
        else {
            super.setImageBitmap(normalBitmap);
            this.setBackgroundDrawable(null);
        }
    }

    /**
     * 渐进方式显示touch
     *
     * @param onShapeAlphaListene
     * @param fromAlpha
     * @param toAlpha
     * @param Duration
     */
    public void setShapeAlpha(OnShapeAlphaListener onShapeAlphaListene, int fromAlpha, int toAlpha, int Duration) {
        mOnShapeAlphaListener = onShapeAlphaListene;

        setAlpha(fromAlpha);
        maxCount = Duration / DELAY_MILLIS;
        int offset = (toAlpha - fromAlpha) / maxCount;

        Message msg = new Message();
        msg.what = WHAT_SHAPE_ALPHA;
        msg.arg1 = fromAlpha;
        msg.arg2 = offset;

        mHandler.removeMessages(WHAT_SHAPE_ALPHA);
        shapeAlphaFlag = false;
        mHandler.sendMessageDelayed(msg, 500);
    }

    public static float getDaxiaoRatio(int progress, int maxProgress) {

        return (((float) progress) / maxProgress) * (DaxiaoMaxRatio - DaxiaoMinRatio) + DaxiaoMinRatio;
    }

    OnShapeAlphaListener mOnShapeAlphaListener = null;

    public interface OnShapeAlphaListener {
        public void shapeAlphaEnd();
    }

	/*public void recycleIcon(){
        if(mNormalBitmap != null && !mNormalBitmap.isRecycled()){
			mNormalBitmap.recycle();
			mNormalBitmap = null;
		}
	}*/

    public int getStatusBarHeight() {
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        statusBarHeight = rect.top;
        // if(pixWidth <= 0 || pixHeight <= 0)
        {
            pixWidth = rect.right;
            pixHeight = rect.bottom;
        }

        return statusBarHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//	 if(statusBarHeight <= 0){
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        statusBarHeight = rect.top;
        pixWidth = rect.right;
        pixHeight = rect.bottom;
//	 }

        //手指相对屏幕的坐标
        currentX = event.getRawX();
        currentY = event.getRawY() - statusBarHeight;
        Log.d("tag", "onTouchEvent(MotionEvent event) {");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoving = false;
                startX = currentX;
                startY = currentY;
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                setAlpha(FloatView.maxAlpha);

                break;

            case MotionEvent.ACTION_MOVE:
                floatPx = (int) (currentX - mTouchStartX);
                floatPy = (int) (currentY - mTouchStartY);

                if (Math.abs(currentX - startX) > 8 || Math.abs(currentY - startY) > 8)//看做是在移动事件后的up
                    updateViewPosition(floatPx, floatPy);

                break;
            case MotionEvent.ACTION_UP:
                //       	 setAlpha(currentAlpha);
                mTouchStartX = mTouchStartY = 0;

                //为了防止移动的时候也出现单击事件
                if (Math.abs(currentX - startX) > 8 || Math.abs(currentY - startY) > 8) {//看做是在移动事件后的up
                    //	Log.d("TAG", "MotionEvent.ACTION_UP: floatPx="+floatPx+",floatPy="+floatPy);
                    fpx = floatPx;
                    fpy = floatPy;

                    //计算出浮动窗最终的位置坐标
                    int tempfloatPx = (floatPx <= pixWidth - floatPx) ? 0 : pixWidth;
                    int tempfloatPy = (floatPy <= pixHeight - floatPy) ? 0 : pixHeight;

                    if (Math.min(floatPx, pixWidth - floatPx) <= Math.min(floatPy, pixHeight - floatPy)) {
                        floatEndPx = tempfloatPx;
                        floatEndPy = floatPy;
                    } else {
                        floatEndPx = floatPx;
                        floatEndPy = tempfloatPy;
                    }

                    mMoveDirection = getMoveDirection();

                    mHandler.sendEmptyMessage(0);

                    return true;
                }

                break;
        }

        return super.onTouchEvent(event);//一定要这么写，才可把事件传出去，如点击事件，不然点击事件无效
    }

    /**
     * 确定移动方向
     *
     * @return
     */
    private int getMoveDirection() {

        if (floatEndPx == floatPx) {//上下移动
            return floatEndPy >= floatPy ? MOVE_TO_BOTTOM : MOVE_TO_TOP;

        } else {//左右移动
            return floatEndPx >= floatPx ? MOVE_TO_RIGHT : MOVE_TO_LEFT;

        }
    }

    private boolean isMoving = false;
    OnMoveListener mOnMoveListener;

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        mOnMoveListener = onMoveListener;
    }

    public interface OnMoveListener {
        public void onMove(int x, int y);
    }

    private void updateViewPosition(int x, int y) {
        if (mOnMoveListener != null) {
            isMoving = true;
            mOnMoveListener.onMove(x, y);
        }
    }

    public void setIsMovingFlag(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void clearMovingFlag() {
        isMoving = false;
    }

    public boolean getIsMovingFlag() {
        return isMoving;
    }

    @Override
    public View getView() {
        return this;
    }

    private WindowManager.LayoutParams layoutParams;

    @Override
    public WindowManager.LayoutParams getWindowLayoutParams() {
        if (layoutParams == null)
            layoutParams = WindowLayoutParams.getLayoutParamsFullScreen(this.getContext());
        return layoutParams;
    }

    @Override
    public void setWindowLayout(int x, int y) {
        setWindowLayout(x, y, -1, -1);
    }

    @Override
    public final void setWindowLayout(int x, int y, int width, int height) {
        if (layoutParams == null)
            layoutParams = WindowLayoutParams.getLayoutParamsFullScreen(this.getContext());

        if (layoutParams != null) {
            layoutParams.x = x;
            layoutParams.y = y;
            if (width >= 0)
                layoutParams.width = width;
            if (height >= 0)
                layoutParams.height = height;
        }
    }
}
