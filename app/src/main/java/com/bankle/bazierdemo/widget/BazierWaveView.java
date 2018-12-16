package com.bankle.bazierdemo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.bankle.bazierdemo.R;

public class BazierWaveView extends View {
    private static final String TAG = "BazierView";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);

    Path path = new Path();
    Path path2 = new Path();
    Rect outLine = new Rect();

    Point[] points = new Point[9];

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
        paint.setStrokeWidth(10);
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStyle(Paint.Style.FILL);
        paint2.setStrokeWidth(10);
        paint2.setColor(getResources().getColor(R.color.white_alpha_half));
        paint2.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final int width = getWidth();
        final int span = width / 4;

        for (int i = 0; i < points.length; i++) {
            points[i] = new Point();
            points[i].x = span * i;

            if (i % 4 == 0) {
                points[i].y = 300;
            }

            if (i % 4 == 1) {
                points[i].y = 250;
            }

            if (i % 4 == 2) {
                points[i].y = 300;
            }

            if (i % 4 == 3) {
                points[i].y = 350;
            }

        }

        for (int i = 0; i < pointForward.length; i++) {
            pointForward[i] = new Point(points[i]);
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

                if (points[0].x <= -width) {
                    for (int i = 0; i < points.length; i++) {
                        points[i].x += width;
                    }
                }

                for (int i = 0; i < points.length; i++) {
                    points[i].x -= 1;
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

        path.reset();

        path.moveTo(points[0].x, points[0].y);

        path.quadTo(points[1].x, points[1].y, points[2].x, points[2].y);
        path.quadTo(points[3].x, points[3].y, points[4].x, points[4].y);
        path.quadTo(points[5].x, points[5].y, points[6].x, points[6].y);
        path.quadTo(points[7].x, points[7].y, points[8].x, points[8].y);

        path.lineTo(points[8].x, getHeight());
        path.lineTo(points[0].x, getHeight());
        path.close();

        path2.reset();
        path2.moveTo(pointForward[0].x, pointForward[0].y);
        path2.quadTo(pointForward[1].x, pointForward[1].y, pointForward[2].x, pointForward[2].y);
        path2.quadTo(pointForward[3].x, pointForward[3].y, pointForward[4].x, pointForward[4].y);
        path2.quadTo(pointForward[5].x, pointForward[5].y, pointForward[6].x, pointForward[6].y);
        path2.quadTo(pointForward[7].x, pointForward[7].y, pointForward[8].x, pointForward[8].y);

        path2.lineTo(pointForward[8].x, getHeight());
        path2.lineTo(pointForward[0].x, getHeight());

        path2.close();

        canvas.drawPath(path, paint2);
        canvas.drawPath(path2, paint);
    }
}
