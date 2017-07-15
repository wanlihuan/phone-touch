package com.ttlove.widget;

import com.android.ttlove.ttlovelibs.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;

public class RadioButton extends CompoundButton implements OnTouchListener{

    private Bitmap bg_on_off; 
    
    private int[] drawableId={R.drawable.btn_check_on_normal,R.drawable.btn_check_off_normal};
      
    public RadioButton(Context context)
    {
        this(context, null);
    }

    public RadioButton(Context context, AttributeSet attrs)
    {
    	this(context, attrs, 0);    
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyle)
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
       
    }
    
    
    public void setOnBackground(int resId){
    	drawableId[0] = resId;
    }
    
    public void setOffBackground(int resId){
    	drawableId[1] = resId;
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
        
        updataBitmapResource();
        canvas.drawBitmap(bg_on_off, matrix, paint);
    }

	@Override
	public void setChecked(boolean checked){
		super.setChecked(checked);
		invalidate();
	}
	
    public boolean onTouch(View v, MotionEvent event)
    {
    	
        switch (event.getAction())
        {
            
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            	if(!isChecked())
            		setChecked(!isChecked());
                break;
            default:
        }
        
        return true;
    }

}
