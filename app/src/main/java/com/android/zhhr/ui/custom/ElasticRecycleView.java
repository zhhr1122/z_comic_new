package com.android.zhhr.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by 皓然 on 2017/8/15.
 */

public class ElasticRecycleView extends RecyclerView{
    private float y;
    private Rect normal = new Rect();
    private boolean canMove;
    public ElasticRecycleView(Context context) {
        this(context, null);
    }
    public ElasticRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElasticRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("ElasticRecycleView","newState="+newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.d("ElasticRecycleView","dx="+dx+",dy="+dy);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
       /* if (!canScrollVertically(-1)&&getChildAt(0).getScrollY() == 0){
            commOnTouchEvent(e);
        }*/
        return super.onTouchEvent(e);
    }

    protected void onFinishInflate() {

    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int deltaY = 0;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                /*if(!canScrollVertically(-1)&&getChildAt(0).getScrollY() == 0){

                }*/
                animation(deltaY);
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                deltaY = (int) Math.sqrt(Math.abs(nowY - preY)*2) ;
                // 滚动
                // scrollBy(0, deltaY);

                y = nowY;
                /*if (!canScrollVertically(-1)) {*/
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(this.getLeft(), this.getTop(),
                                this.getRight(), this.getBottom());
                        return;
                    }
                    if (nowY > preY) {
                        this.layout(this.getLeft(), this.getTop() + deltaY, this.getRight(),
                                this.getBottom() + deltaY);

                    } /*else if(nowY < preY){
                        //Log.d("zhhr1122","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        this.layout(this.getLeft(), this.getTop() - deltaY, this.getRight(),
                                this.getBottom() - deltaY);
                    }*/
                    // 移动布局
                /*}*/
                break;
            default:
                animation(deltaY);
                break;
        }
    }

    public void animation(int deltaY) {
        // 开启移动动画
      /*  TranslateAnimation ta = new TranslateAnimation(0, 0, this.getTop()+deltaY, 240+deltaY);
        ta.setDuration(300);
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        this.startAnimation(ta);
        // 设置回到正常的布局位置*/
        this.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public boolean isNeedMove(){
        int aa[] = ((StaggeredGridLayoutManager)getLayoutManager()).findFirstVisibleItemPositions(null);
    //达到这个条件就说明滑到了顶部
        return getChildAt(0).getScrollY() == 0 && aa[0] == 0;
    }
}
