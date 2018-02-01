package com.android.zhhr.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.android.zhhr.R;

/***
 * 下拉回弹的ScrollView
 * @author 皓然
 *
 */
public class ElasticScrollView extends ScrollView {
    ImageView mLoadingTop;

    public ElasticScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }



    public ElasticScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }



    private ElasticScrollView scrollViewListener = null;


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
    }

    // 拖动的距离 size = 4 的意思 只允许拖动屏幕的1/4
    private static final int size = 3;
    private ViewGroup inner;
    private float y;
    private Rect normal = new Rect();

    //获取到ScrollView内部的子View,并赋值给inner
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = (ViewGroup) getChildAt(0);
            if(inner.getChildCount()!=1){
                mLoadingTop = (ImageView) inner.findViewById(R.id.iv_loading_top);
                initAnimation();
            }
        }
        setOverScrollMode(OVER_SCROLL_NEVER);//取消5.0效果
    }

    //初始化动画
    private void initAnimation() {
        mLoadingTop.setImageResource(R.drawable.loading_top);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingTop.getDrawable();
        animationDrawable.start();
    }

    //重写滑动方法
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                int deltaY;
                deltaY = (int) Math.sqrt(Math.abs(nowY - preY)*2) ;
                // 滚动
                // scrollBy(0, deltaY);

                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                        return;
                    }
                    if (nowY > preY) {
                        //Log.d("zhhr","deltaY="+deltaY);
                        //Log.d("zhhr1122","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        inner.layout(inner.getLeft(), inner.getTop() + deltaY, inner.getRight(),
                                inner.getBottom() + deltaY);
                    } else if(nowY < preY){
                        //Log.d("zhhr1122","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        inner.layout(inner.getLeft(), inner.getTop() - deltaY, inner.getRight(),
                                inner.getBottom() - deltaY);
                    }
                    // 移动布局
                }
                break;
            default:
                break;
        }
    }

    // 开启动画移动

    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),0);
        ta.setDuration(300);
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == offset;
    }
}
