package com.android.phone.assistant.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by mosida on 13-6-27.
 */
public class AppScreenShotScrollView extends HorizontalScrollView{

    private LinearLayout container;
//    private ImageView[] imageViews;
    private Context context;
//    private Bitmap[] bitmaps;
    private int picWidth = 120;
    private int picHeight = 180;

    public AppScreenShotScrollView(Context context){
        super(context);
    }

    public AppScreenShotScrollView(Context context, AttributeSet attr)
    {
        super(context, attr);
    }

    public AppScreenShotScrollView(Context context, AttributeSet attr, int paramInt)
    {
        super(context, attr, paramInt);
    }


    public void initView(){
        this.context = getContext();
        container = new LinearLayout(context);
        container.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
        container.setOrientation(LinearLayout.HORIZONTAL);
        int padding = count(10.0F, context);
        container.setPadding(padding/2, padding, padding/2, padding);
        addView(container);
    }


    public void setImages(Bitmap[] bitmaps){
        this.picWidth = count(picWidth, context);
        this.picHeight = count(picHeight, context);
        int padding = count(5.0F, context);
//        this.bitmaps = bitmaps;
        for (int i=0; i<bitmaps.length; i++){
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(picWidth, picHeight);
            ll.setMargins(padding,0,padding,0);
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(ll);
            imageView.setImageBitmap(bitmaps[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
        }
    }




    public static int count(float theFloat, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, theFloat, context.getResources().getDisplayMetrics());
    }

}
