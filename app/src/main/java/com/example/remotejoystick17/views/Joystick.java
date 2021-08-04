//based on https://www.instructables.com/A-Simple-Android-UI-Joystick/
package com.example.remotejoystick17.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private static double centerX, centerY, baseRadius, radiusHat;
    private OnJoystickChangeListener mOnJoystickChangeListener;


    public Joystick(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);

    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);

    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);

    }

    private void drawJoystick(double x, double y) {
        if (getHolder().getSurface().isValid()) {
            //get the canvas and set color to draw.
            Canvas myCanvas = this.getHolder().lockCanvas();

            Paint colors = new Paint();
            //clear the canvas.
            myCanvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);

            colors.setColor(Color.BLUE);
//            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
            myCanvas.drawCircle((float) x, (float) y, (float) radiusHat, colors);
            getHolder().unlockCanvasAndPost(myCanvas);


        }
    }

    private void setupDimension() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        radiusHat = Math.min(getWidth(), getHeight()) / 5;


    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        setupDimension();


        drawJoystick(centerX, centerY);

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {

        if (v.equals(this)) {
            float displacement = (float) Math.sqrt(Math.pow(e.getX() - centerX, 2) + Math.pow(e.getY() - centerY, 2));
            if (e.getAction() != e.ACTION_UP) {
                if (displacement < baseRadius) {
                    drawJoystick(e.getX(), e.getY());
                    mOnJoystickChangeListener.onChange(normalize(e.getX(),'x'), normalize(e.getY(),'y'));
                } else {
                    double ratio = baseRadius / displacement;
                    double constrainedX = centerX + (e.getX() - centerX) * ratio ;
                    double constrainedY = centerY + (e.getY() - centerY) * ratio ;
                    drawJoystick(constrainedX, constrainedY);

                    mOnJoystickChangeListener.onChange(normalize(constrainedX, 'x') , normalize(constrainedY, 'y')-0.1);
                }

            } else {
                drawJoystick(centerX, centerY);
                mOnJoystickChangeListener.onChange(normalize(centerX, 'x'), normalize(centerY,'y'));
            }
        }
        return true;
    }

    //normlizing values to match FG requirments.
    private double normalize(double val, char axis) {
        double roundingError = 0.15;
        switch (axis) {
            case 'x': {
                if(val > centerX) {
                    return (val - centerX) / (centerX - radiusHat) - roundingError;
                } else if (val < centerX) {
                    return (val - centerX) / (centerX - radiusHat) + roundingError;
                } else {
                    return (val - centerX) / (centerX - radiusHat);
                }

            }
            case 'y': {
                if(val > centerY) {
                    return (val - centerY) / (centerY - radiusHat) - roundingError/2;
                } else if (val < centerY) {
                    return (val - centerY) / (centerY - radiusHat) + roundingError*1.4 ;
                } else {
                    return (val - centerY) / (centerY - radiusHat);
                }

            }
            default:
                return val;
        }
    }

    public void setOnJoystickChangeListener(OnJoystickChangeListener l) {
        mOnJoystickChangeListener = l;
    }

    public interface OnJoystickChangeListener {
        void onChange(double x, double y);
    }
}
