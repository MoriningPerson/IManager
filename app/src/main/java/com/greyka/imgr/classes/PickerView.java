package com.greyka.imgr.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.greyka.imgr.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PickerView extends View {


    private final VelocityTracker myVelocityTracker = VelocityTracker.obtain();
    private float mSpeed = 0;
    private float mSlowDownRate = 0.95f;
    private float fastSlideSpeed = 200;
    private int textColor = 0XFFFFFFFF;

    private long lastOnSeletedChangeTime = 0;
    private int onSelectedChangeInterval = 50;

    public static final String TAG = "PickerView";
    /**
     * text之间间距和minTextSize之比
     */
    public static final float MARGIN_ALPHA = 2.3f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 6;

    private List<String> mDataList;
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint;

    private float mMaxTextSize = 30;
    private float mMinTextSize = 15;
    private float mMaxTextSizeRate = 4;
    private float mMinTextSizeRate =2;


    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 100;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;


    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if(Math.abs(mSpeed) <= 20){
                mSpeed = 0;
            }
            else{
                Log.d("speed",mSpeed+"b");
                mSpeed *= mSlowDownRate;
                Log.d("speed",mSpeed+"a");
                doMove(mSpeed * (float)0.1);
                return;
            }
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            invalidate();
        }

    };

    public PickerView(Context context) {
        super(context);
        init();
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
        Log.d("select","select");
    }

    public void setData(List<String> datas) {
        mDataList = datas;
        mCurrentSelected = datas.size() / 2;
        invalidate();
    }

    /**
     * 选择选中的item的index
     *
     * @param selected
     */
    public void setSelected(int selected) {
        mCurrentSelected = selected;
        int distance = mDataList.size() / 2 - mCurrentSelected;
        if (distance < 0)
            for (int i = 0; i < -distance; i++) {
                moveHeadToTail();
                mCurrentSelected--;
            }
        else if (distance > 0)
            for (int i = 0; i < distance; i++) {
                moveTailToHead();
                mCurrentSelected++;
            }
        invalidate();
    }

    /**
     * 选择选中的内容
     *
     * @param mSelectItem
     */
    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++)
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
    }

    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / mMaxTextSizeRate;
        mMinTextSize = mMaxTextSize / mMinTextSizeRate;
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<String>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setColor(textColor);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        if(mDataList.size() > 0)
        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }

    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type
                * mMoveLen);
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                (float) (mViewWidth / 2.0), baseline, mPaint);
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }
    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {
        Log.d("domove","domoveeeeee");
        mMoveLen += (event.getY() - mLastDownY);
        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            Log.d("domove","domove");
            // 往下滑超过离开距离
            if(mSelectListener != null && (System.currentTimeMillis() > lastOnSeletedChangeTime + onSelectedChangeInterval)) {
                mSelectListener.onSelectedChange();
                lastOnSeletedChangeTime = System.currentTimeMillis();
            }
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            if(mSelectListener != null && (System.currentTimeMillis() > lastOnSeletedChangeTime + onSelectedChangeInterval)) {
                mSelectListener.onSelectedChange();
                lastOnSeletedChangeTime = System.currentTimeMillis();
            }
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }
        mLastDownY = event.getY();
        myVelocityTracker.addMovement(event);
        myVelocityTracker.computeCurrentVelocity(100);
        invalidate();
    }
    private void doMove(float moveY) {

        mMoveLen += moveY;
        Log.d("move",moveY+"");
        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            if(mSelectListener != null && (System.currentTimeMillis() > lastOnSeletedChangeTime + onSelectedChangeInterval)) {
                mSelectListener.onSelectedChange();
                lastOnSeletedChangeTime = System.currentTimeMillis();
            }
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            if(mSelectListener != null && (System.currentTimeMillis() > lastOnSeletedChangeTime + onSelectedChangeInterval)) {
                mSelectListener.onSelectedChange();
                lastOnSeletedChangeTime = System.currentTimeMillis();
            }
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }
        invalidate();
    }
    private void doUp(MotionEvent event) {

        mSpeed = myVelocityTracker.getYVelocity();
        Log.d("mspeed",mSpeed+"");
        if(Math.abs(mSpeed) < fastSlideSpeed){
            mSpeed = 0;
        }

        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;
        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            Log.d("a","aaaaa");
            handler.sendMessage(handler.obtainMessage());
        }

    }

    public interface onSelectListener {
        void onSelect(String text);
        void onSelectedChange();
    }
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PickerView, 0, 0);
        fastSlideSpeed = typeArray.getFloat(R.styleable.PickerView_fastSlideSpeed,200);
        mSlowDownRate = typeArray.getFloat(R.styleable.PickerView_slowDownRate,0.96f);
        mMaxTextAlpha = typeArray.getFloat(R.styleable.PickerView_maxTextAlpha, 255);
        mMinTextAlpha = typeArray.getFloat(R.styleable.PickerView_minTextAlpha,100);
        mMaxTextSizeRate = typeArray.getFloat(R.styleable.PickerView_maxTextSizeRate,30);
        mMinTextSizeRate = typeArray.getFloat(R.styleable.PickerView_minTextSizeRate,15);
        textColor = typeArray.getColor(R.styleable.PickerView_textColor,0xFF000000);
        onSelectedChangeInterval = typeArray.getInt(R.styleable.PickerView_onSelectedChangeInterval,50);
    }
}