package com.hasbis.android.roger;

/**
 * Created by hasbis on 18.02.2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    /**
     * 1 speaking
     * 2 shock
     * 3 sad
     * 4 think
     * 5 fun
     * 6 angry
     * 7 blinking
     * 8 listen
     */
    int status = 0;
    int otherImage = 0;
    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        Drawable d = null;

        if (status == 1) {
            d = speak();
        }else if (status == 2) {
            d = ContextCompat.getDrawable(context, R.drawable.shock);
        }else if (status == 3) {
            d = ContextCompat.getDrawable(context, R.drawable.sad);
            otherImage ++;
        }else if (status == 4) {
            d = ContextCompat.getDrawable(context, R.drawable.think);
        }else if (status == 5) {
            d = ContextCompat.getDrawable(context, R.drawable.fun);
            otherImage ++;
        }else if (status == 6) {
            d = ContextCompat.getDrawable(context, R.drawable.angry);
        }else if (status == 7) {
            d = blinking();
        }else if (status == 8) {
            d = ContextCompat.getDrawable(context, R.drawable.listen);
        } else {
            d = ContextCompat.getDrawable(context, R.drawable.s1);
        }

        if (otherImage > 250) {
            status = 0;
            otherImage = 0;
        }


        speak++;
        blink ++;
        d.setBounds(175, 300, 900, 1200);
        d.draw(canvas);
        invalidate();
    }

    public void setStatus(int s) {
        status = s;
    }

    public void canBlink() {
        blink = 0;
        if (status == 0) {
            status = 7;
        }
    }

    private Drawable speak() {
        Drawable d = null;
        if (speak < 3) {
            d = ContextCompat.getDrawable(context, R.drawable.s1);
        }else if (speak < 6) {
            d = ContextCompat.getDrawable(context, R.drawable.s2);
        }else if (speak < 9) {
            d = ContextCompat.getDrawable(context, R.drawable.s3);
        }else if (speak < 12) {
            d = ContextCompat.getDrawable(context, R.drawable.s4);
        }else if (speak < 15) {
            d = ContextCompat.getDrawable(context, R.drawable.s3);
        }else if (speak < 18) {
            d = ContextCompat.getDrawable(context, R.drawable.s2);
        }else if (speak < 21) {
            d = ContextCompat.getDrawable(context, R.drawable.s1);
        }else {
            speak = 0;
            d = ContextCompat.getDrawable(context, R.drawable.s1);
        }

        return d;
    }

    private Drawable blinking() {
        Drawable d = null;
        if (blink < 3) {
            d = ContextCompat.getDrawable(context, R.drawable.b1);
        }else if (blink < 6) {
            d = ContextCompat.getDrawable(context, R.drawable.b2);
        }else if (blink < 9) {
            d = ContextCompat.getDrawable(context, R.drawable.b3);
        }else if (blink < 12) {
            d = ContextCompat.getDrawable(context, R.drawable.b4);
        }else if (blink < 20) {
            d = ContextCompat.getDrawable(context, R.drawable.b5);
        }else if (blink < 23) {
            d = ContextCompat.getDrawable(context, R.drawable.b4);
        }else if (blink < 26) {
            d = ContextCompat.getDrawable(context, R.drawable.b3);
        }else if (blink < 29) {
            d = ContextCompat.getDrawable(context, R.drawable.b2);
        }else if (blink < 32) {
            d = ContextCompat.getDrawable(context, R.drawable.b1);
        }else {
            status = 0;
            blink = 0;
            d = ContextCompat.getDrawable(context, R.drawable.s1);
        }
        blink ++;

        return d;
    }

    int blink = 0;
    int speak = 0;

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        /*mPath.moveTo(x, y);
        mX = x;
        mY = y;*/
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        /*float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }*/
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        //mPath.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}