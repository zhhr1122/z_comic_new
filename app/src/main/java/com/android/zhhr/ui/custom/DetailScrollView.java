package com.android.zhhr.ui.custom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.zhhr.utils.DisplayUtil;

/***
 * 下拉回弹的ScrollView
 * @author 皓然
 *
 */
public class DetailScrollView extends ScrollView {

    private RelativeLayout inner;
    private RelativeLayout mMoveView;
    private View mLoadingTop;
    private View mLoadingBottom;
    //private View mListView;
    private View mTopView;
    private View mDetailView;
    private float y;
    private Rect normal = new Rect();
    private int height = 1;//一开始不为0
    private boolean isRefresh;
    private boolean isAction_UP;
    private RefreshListener listener;
    private ScaleTopListener scalelistener;
    public static final int SHOW_ACTIONBAR_TITLE = 3;
    public static final int SHOW_DETAIL_TAB = 2;
    public static final int SHOW_CHAPER_TAB= 1;
    public static final int SHOW_NONE = 0;

    //private View mLoadingTop;
    public DetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }


    public DetailScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        height = inner.getMeasuredHeight()-mLoadingBottom.getMeasuredHeight()-DisplayUtil.getScreenHeight(getContext())+DisplayUtil.dip2px(getContext(),60);
        if(y>=height){
            this.scrollTo(0,height);
        }

        if(y>0&&y<mLoadingTop.getHeight()){
            float alpha = 1-((float) y)/mLoadingTop.getHeight();
            mLoadingTop.setAlpha(alpha);
            scalelistener.isBlurTransform(alpha);
        }else if(getScrollY()>=mLoadingTop.getHeight()){
            mLoadingTop.setAlpha(0);
            scalelistener.isBlurTransform(0);
        }else{
            mLoadingTop.setAlpha(1.0f);
            scalelistener.isBlurTransform(1.0f);
        }
        if(y>=mDetailView.getHeight()+DisplayUtil.dip2px(getContext(),170)){
            scalelistener.isShowTab(SHOW_DETAIL_TAB);
        }else if(y>=DisplayUtil.dip2px(getContext(),220)){
            scalelistener.isShowTab(SHOW_CHAPER_TAB);
        }else if(y>DisplayUtil.dip2px(getContext(),135)){
            scalelistener.isShowTab(SHOW_ACTIONBAR_TITLE);
        }else if(y>0){
            scalelistener.isShowTab(SHOW_NONE);
        }
        //Log.d("zhhr","y="+y);
        /*if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }*/
    }
    //获取到ScrollView内部的子View,并赋值给inner
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = (RelativeLayout) getChildAt(0);
        }
        mMoveView = (RelativeLayout) inner.getChildAt(0);
        mTopView = inner.getChildAt(1);
        mLoadingTop = mMoveView.getChildAt(0);
        mDetailView = mMoveView.getChildAt(1);
        //mListView = mMoveView.getChildAt(2);
        mLoadingBottom = mMoveView.getChildAt(3);
        setOverScrollMode(OVER_SCROLL_NEVER);//取消5.0效果
    }

    //重写滑动方法
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            if(isRefresh&&listener!=null){
                return super.onTouchEvent(ev);
            }
            commOnTouchEvent(ev);
            //防止拉取过度的问题
            if(mMoveView.getTop()>DisplayUtil.dip2px(getContext(),140)&&getScrollY()==0||mMoveView.getBottom()<inner.getMeasuredHeight()&&getScrollY() ==inner.getMeasuredHeight()-mLoadingBottom.getMeasuredHeight()-DisplayUtil.getScreenHeight(getContext())+DisplayUtil.dip2px(getContext(),60)){
                return false;
            }
            return super.onTouchEvent(ev);
        }
    }

    public void commOnTouchEvent(MotionEvent ev) {
        //Log.d("zhhrheight","mLoadingTop = "+mLoadingTop.getMeasuredHeight()+",mLoadingBottom = "+mLoadingBottom.getMeasuredHeight()+",innerHeight="+inner.getMeasuredHeight()+",mListView="+mListView.getMeasuredHeight());
        //Log.d("zhhrheight","getBottomStatusHeight"+ DisplayUtil.getBottomStatusHeight(getContext())+",getScreenHeight="+DisplayUtil.getScreenHeight(getContext())+",getHeight="+getHeight());
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                //Log.d("zhr", "MotionEvent.ACTION_DOWN  y=" + y);
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    if(mMoveView.getTop()>DisplayUtil.dip2px(getContext(),245)&&listener!=null){
                        RefreshAnimation();
                        //animation();
                        isRefresh = true;
                        listener.onRefresh();
                    }else{
                        //Log.v("zhhr112233", "getScrollY="+getScrollY()+",height="+height);
                        if(getScrollY()==height&&listener!=null){
                            listener.onLoadMore();
                        }
                        animation();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //判断向上滑动
                final float preY = y;
                float nowY = ev.getY();
                //Log.d("zhr", "MotionEvent.ACTION_MOVE  nowY=" + nowY + ";preY=" + preY);
                int deltaY;
                deltaY = (int) Math.sqrt(Math.abs(nowY - preY)*2) ;
                // 滚动
                // scrollBy(0, deltaY);

                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove(nowY)) {
                    //Log.d("zhr", "isNeedMove");
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(mMoveView.getLeft(), mMoveView.getTop(),mMoveView.getRight(), mMoveView.getBottom());
                        return;
                    }
                    if (nowY > preY) {
                        //Log.d("zhhr","deltaY="+deltaY);
                        //Log.d("zhhrtest","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        mMoveView.layout(mMoveView.getLeft(), mMoveView.getTop() + deltaY, mMoveView.getRight(),
                                mMoveView.getBottom() + deltaY);
                        if(getScrollY() == 0 && nowY > mTopView.getMeasuredHeight()){
                            scalelistener.isScale(mMoveView.getTop() + deltaY+DisplayUtil.dip2px(getContext(),60));
                        }
                    } else if(nowY < preY){
                        if(getScrollY() == 0 && nowY > mTopView.getMeasuredHeight()){
                            scalelistener.isScale(mMoveView.getTop() + deltaY+DisplayUtil.dip2px(getContext(),60));
                        }
                        //Log.d("zhhr1122","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        mMoveView.layout(mMoveView.getLeft(), mMoveView.getTop() - deltaY, mMoveView.getRight(),
                                mMoveView.getBottom() - deltaY);
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
        TranslateAnimation ta = new TranslateAnimation(0, 0, mMoveView.getTop()-DisplayUtil.dip2px(getContext(),140), normal.top-DisplayUtil.dip2px(getContext(),140));
       // Log.d("zhhr112233", "inner.getTop()=" + inner.getTop() + ",normal.top=" + normal.top);
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        Log.d("DetailScrollView", "animation"+"getScrollY="+getScrollY()+"height="+height);
        if(getScrollY() < height){
            Log.d("DetailScrollView", "isFinished()"+"getScrollY="+getScrollY()+"height="+height);
            scalelistener.isFinished();
        }
        mMoveView.startAnimation(ta);
        // 设置回到正常的布局位置  
        mMoveView.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public void RefreshAnimation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, mMoveView.getTop()-DisplayUtil.dip2px(getContext(),240), normal.top-DisplayUtil.dip2px(getContext(),200));
        // Log.d("zhhr112233", "inner.getTop()=" + inner.getTop() + ",normal.top=" + normal.top);
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        mMoveView.startAnimation(ta);
        // 设置回到正常的布局位置
        mMoveView.layout(normal.left, normal.top+200, normal.right, normal.bottom+200);
        //normal.setEmpty();
    }

    // 是否需要开启动画  
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局  
    public boolean isNeedMove(float nowY) {
        int scrollY = getScrollY();
        return scrollY == 0 && nowY > mTopView.getMeasuredHeight() || scrollY == height;
    }

    public interface ScrollViewListener {
        void onScrollChanged(DetailScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    /**
     * 设置刷新状态
     * @param isRefresh
     */
    public void setRefreshing(boolean isRefresh){
        if(this.isRefresh!=isRefresh){
            this.isRefresh = isRefresh;
            if(isRefresh == false){
                animation();
            }
        }
    }

    public boolean isRefreshing(){
        return isRefresh;
    }

    public interface RefreshListener{
        void onRefresh();
        void onRefreshFinish();
        void onLoadMore();
    }

    public interface ScaleTopListener{
        void isScale(float y);
        void isFinished();
        void isBlurTransform(float y);
        void isShowTab(int a);
    }

    public void setScaleTopListener(ScaleTopListener scalelistener) {
        this.scalelistener = scalelistener;
    }
    public void ScrollToPosition(int position){
        if(position==0){
            position =1;
        }
        ScrollTo(0,DisplayUtil.dip2px(getContext(),170+(position-1)*60)+mDetailView.getHeight());
    }

    public void ScrollTo(int x,int y) {

        ObjectAnimator xTranslate = ObjectAnimator.ofInt(this, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(this, "scrollY", y);
        Interpolator in = new DecelerateInterpolator();
        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(200l);
        animators.setInterpolator(in);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }

    public void setRefreshListener(RefreshListener listener){
        this.listener = listener;
    }

    /**
     * 强制设置内层VIEW的高度，防止刷新较慢导致显示不全
     */
    public void setInnerHeight(){
        if(inner!=null){
            int height = mMoveView.getHeight()+DisplayUtil.dip2px(getContext(),140);
            inner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        }
    }

}
