package com.example.maskbackgroundtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class MaskView extends View {
    private Paint paint;
    private Paint maskPaint;
    private Context _context;

    private float left = 386f;
    private float right = 630f;
    private float top = 333f ;
    private float bottom = 376f + 70f;

    public void setContext(Context context){
        _context = context;
    }

    public MaskView(Context context) {
        super(context);
        _context = context;
        init();
    }

    public MaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.parseColor("#CC000000"));
        maskPaint = new Paint();
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // recorta a parte do circulo
        maskPaint.setAntiAlias(true ); // suaviza as bordas



    }

    public void setPositions(BoundsModel boundsModel){
        this.left = boundsModel.getLeft() - 20;
        this.top = boundsModel.getTop() - 80;
        this.right = boundsModel.getRight() + 20;
        this.bottom = boundsModel.getBottom() -30;
        invalidate(); // Redesenhar
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        canvas.drawOval(left, top, right, bottom, maskPaint);
        canvas.restoreToCount(layer);

    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//
//        if (x >= left && x <= right && y >= top && y <= bottom) {
//            Log.e("MaskViewComponent", "onTouchEvent: Clicou" );
//            return true;
//        }
//
//        return false;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (x >= left && x <= right && y >= top && y <= bottom) {
            Log.e("MaskViewComponent", "onTouchEvent: Clicou" );
            Intent intent = new Intent("com.example.maskbackgroundtest.CLICK_ELEMENT");
            _context.sendBroadcast(intent);

            return false;
        }

        return true;
    }

}
