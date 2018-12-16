package com.bankle.bazierdemo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.bankle.bazierdemo.R;

public class BazierWaveView extends View {
    private static final String TAG = "BazierView";
    private Paint paintForward = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintBackward = new Paint(Paint.ANTI_ALIAS_FLAG);

    Path pathBackward = new Path();
    Path pathForward = new Path();

    Point[] pointBackward = new Point[9];

    Point[] pointForward = new Point[9];
    public BazierWaveView(Context context) {
        this(context, null);
    }

    public BazierWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BazierWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintForward.setStrokeWidth(10);
        paintForward.setColor(getResources().getColor(R.color.white));
        paintForward.setStyle(Paint.Style.FILL);
        paintBackward.setStrokeWidth(10);
        paintBackward.setColor(getResources().getColor(R.color.white_alpha_half));
        paintBackward.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final int width = getWidth();
        final int span = width / 4;

        for (int i = 0; i < pointBackward.length; i++) {
            pointBackward[i] = new Point();
            pointBackward[i].x = span * i;

            if (i % 2 == 0) {
                pointBackward[i].y = 300;
            }else {

                if (i % 4 == 1) {
                    pointBackward[i].y = 250;
                }

                if (i % 4 == 3) {
                    pointBackward[i].y = 350;
                }
            }

        }

        for (int i = 0; i < pointForward.length; i++) {
            pointForward[i] = new Point(pointBackward[i]);
            pointForward[i].x -= width;
        }

        Log.v(TAG, "onSizeChanged width "+ width);


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(Integer.MAX_VALUE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                Log.i(TAG, "bankle onAnimationUpdate: " +animatedFraction);

                if (pointBackward[0].x <= -width) {
                    for (int i = 0; i < pointBackward.length; i++) {
                        pointBackward[i].x += width;
                    }
                }

                for (int i = 0; i < pointBackward.length; i++) {
                    pointBackward[i].x -= 1;
                }


                if (pointForward[0].x >= 0) {
                    for (int i = 0; i < pointForward.length; i++) {
                        pointForward[i].x -= width;
                    }
                }
                for (int i = 0; i < pointForward.length; i++) {
                    pointForward[i].x += 2;
                }

                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pathBackward.reset();

        pathBackward.moveTo(pointBackward[0].x, pointBackward[0].y);
        for (int i = 0; i < pointBackward.length/2; i++) {
            pathBackward.quadTo(pointBackward[2*i+1].x, pointBackward[2*i+1].y, pointBackward[2*i+2].x, pointBackward[2*1 +2].y);
        }

        pathBackward.lineTo(pointBackward[8].x, getHeight());
        pathBackward.lineTo(pointBackward[0].x, getHeight());
        pathBackward.close();

        pathForward.reset();
        pathForward.moveTo(pointForward[0].x, pointForward[0].y);
        for (int i = 0; i < pointForward.length/2; i++) {
            pathForward.quadTo(pointForward[2*i+1].x, pointForward[2*i+1].y, pointForward[2*i+2].x, pointForward[2*1 +2].y);
        }

        pathForward.lineTo(pointForward[8].x, getHeight());
        pathForward.lineTo(pointForward[0].x, getHeight());

        pathForward.close();

        canvas.drawPath(pathBackward, paintBackward);
        canvas.drawPath(pathForward, paintForward);
    }
}
