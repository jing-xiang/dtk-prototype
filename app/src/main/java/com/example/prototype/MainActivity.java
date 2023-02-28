package com.example.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private View crossView;
    private View circleView;
    private PointF crossLocation = new PointF();
    private PointF circleLocation = new PointF();
    private boolean isAnimating = false;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crossView = findViewById(R.id.cross_view);
        crossView.setVisibility(View.GONE);

        circleView = findViewById(R.id.circle_view);
        circleView.setVisibility(View.GONE);

        mHandler = new Handler();

        Button randomTapButton = findViewById(R.id.random_tap_button);
        randomTapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                float x = random.nextFloat() * v.getWidth();
                float y = random.nextFloat() * v.getHeight();
                MotionEvent event = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
                dispatchTouchEvent(event);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            crossLocation.set(x, y);
            crossView.setX(x);
            crossView.setY(y);
            crossView.setVisibility(View.VISIBLE);
            if (circleView.getVisibility() == View.GONE) {
                circleLocation.set(x, y);
                circleView.setX(x);
                circleView.setY(y);
                circleView.setVisibility(View.VISIBLE);
            }
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            crossLocation.set(x, y);
            crossView.setX(x);
            crossView.setY(y);
            if (!isAnimating) {
                animateCircleToCrossLocation();
            }
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            crossView.setVisibility(View.GONE);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void animateCircleToCrossLocation() {
        isAnimating = true;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PointF circleDelta = new PointF(crossLocation.x - circleLocation.x, crossLocation.y - circleLocation.y);
                float distance = (float) Math.sqrt(circleDelta.x * circleDelta.x + circleDelta.y * circleDelta.y);
                float speed = 5.0f;
                if (distance > speed) {
                    circleDelta.x = circleDelta.x / distance * speed;
                    circleDelta.y = circleDelta.y / distance * speed;
                }
                circleLocation.offset(circleDelta.x, circleDelta.y);
                circleView.setX(circleLocation.x);
                circleView.setY(circleLocation.y);
                if (distance > 1.0f) {
                    mHandler.postDelayed(this, 16);
                } else {
                    isAnimating = false;
                }
            }
        }, 16);
    }
}
