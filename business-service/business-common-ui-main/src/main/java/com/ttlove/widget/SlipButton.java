package com.ttlove.widget;

import com.android.ttlove.ttlovelibs.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;

public class SlipButton extends CompoundButton implements OnTouchListener
{
    private boolean NowChoose = false;

    private boolean isChecked;

    private boolean OnSlip = false;

    private float DownX, NowX;

    private Rect Btn_On, Btn_Off;

    private Bitmap  slip_btn;
    
    private Bitmap bg_on_off; 
    private boolean isTouch = false;
    
    private int[] drawableId={R.drawable.btn_split_on,R.drawable.btn_split_off};
    private int slipbtnId = R.drawable.btn_split_on_off;
    
    public SlipButton(Context context)
    {
        this(context, null);
    }

    public SlipButton(Context context, AttributeSet attrs)
    {
    	this(context, attrs, 0);    
    }

    public SlipButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    	updataBitmapResource();      
        setOnTouchListener(this);
    }

    private void updataBitmapResource(){
    	if(isChecked() == true)
        	bg_on_off = BitmapFactory.decodeResource(getResources(), drawableId[0]);       	
        else
        	bg_on_off = BitmapFactory.decodeResource(getResources(), drawableId[1]);
        	    	
    	slip_btn = BitmapFactory.decodeResource(getResources(), slipbtnId);
        Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
        Btn_Off = new Rect(bg_on_off.getWidth() - slip_btn.getWidth(), 0, bg_on_off.getWidth(),slip_btn.getHeight());
       
    }
    
    
    public void setOnBackground(int resId){
    	drawableId[0] = resId;
    }
    
    public void setOffBackground(int resId){
    	drawableId[1] = resId;
    }
    
    public void setSlipBackground(int resId){
    	slipbtnId = resId;
    }
      
    @Override
    public void setButtonDrawable(Drawable d){

	return;
     }
    
    @Override
	protected void onMeasure(int arg0, int arg1) {
		// TODO Auto-generated method stub
		super.onMeasure(arg0, arg1);
	}

	@Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        float x;
        
        updataBitmapResource();
        
        if (NowX < (bg_on_off.getWidth() / 2))
            x = NowX - slip_btn.getWidth() / 2; 
        else     
            x = bg_on_off.getWidth() - bg_on_off.getWidth() / 2;            
        
        canvas.drawBitmap(bg_on_off, matrix, paint);
        
        if (OnSlip)
        {
            if (NowX >= bg_on_off.getWidth())
                x = bg_on_off.getWidth() - slip_btn.getWidth() / 2;

            else if (NowX < 0)
            {
                x = 0;
            }
            else
            {
                x = NowX - slip_btn.getWidth() / 2;
            }
        }
        else
        {

            if (NowChoose)
            {
                x = Btn_Off.left;
                canvas.drawBitmap(bg_on_off, matrix, paint);
            }
            else
                x = Btn_On.left;
        }
        if (isChecked)
        {
            canvas.drawBitmap(bg_on_off, matrix, paint);
            x = Btn_Off.left;
            isChecked = !isChecked;
        }

        
        if (x < 0){
            x = 0;
        }else if (x > bg_on_off.getWidth() - slip_btn.getWidth()){
            x = bg_on_off.getWidth() - slip_btn.getWidth();           
        }
        
        if(isTouch){
        	isTouch = false;
        	canvas.drawBitmap(slip_btn, x, 0, paint);
        }else{
        	if(isChecked() == true){
        		x = bg_on_off.getWidth() - slip_btn.getWidth(); 
            	canvas.drawBitmap(slip_btn, x, 0, paint); 
            }else{
            	canvas.drawBitmap(slip_btn, 0, 0, paint);
            }
        }
    }

	@Override
	public void setChecked(boolean checked){
		super.setChecked(checked);
		invalidate();
	}
    public boolean onTouch(View v, MotionEvent event)
    {
    	
    	isTouch = true;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                NowX = event.getX();
                break;

            case MotionEvent.ACTION_DOWN:

                if (event.getX() > bg_on_off.getWidth() || event.getY() > bg_on_off.getHeight())
                    return false;
                OnSlip = true;
                DownX = event.getX();
                NowX = DownX;
                break;

            case MotionEvent.ACTION_CANCEL:

                OnSlip = false;
                if (NowX >= (bg_on_off.getWidth() / 2))
                {
                    NowX = bg_on_off.getWidth() - slip_btn.getWidth() / 2;
                    NowChoose = true;
                }
                else
                {
                    NowX = NowX - slip_btn.getWidth() / 2;
                    NowChoose = false;
                }
                	setChecked(NowChoose);
                	
                break;
            case MotionEvent.ACTION_UP:

                OnSlip = false;

                if (event.getX() >= (bg_on_off.getWidth() / 2))
                {
                    NowX = bg_on_off.getWidth() - slip_btn.getWidth() / 2;
                    NowChoose = true;
                }
                else
                {
                    NowX = NowX - slip_btn.getWidth() / 2;
                    NowChoose = false;
                }
                	setChecked(NowChoose);
                	
                break;
            default:
        }
        invalidate();
        return true;
    }

}
