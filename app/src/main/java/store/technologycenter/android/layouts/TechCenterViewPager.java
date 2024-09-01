package store.technologycenter.android.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class TechCenterViewPager extends ViewPager {
    public TechCenterViewPager(@NonNull Context context) {
        super(context);
    }

    public TechCenterViewPager(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        } catch (IllegalArgumentException ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        try {
            return super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException ex) {
            //ex.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            //e.printStackTrace();
            return true;
        }
    }
}