package com.demo.contact;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * description: 通讯录搜索栏
 * created by kalu on 2018/4/9 10:15
 */
public class ContactBar extends View {

    private final List<String> mLetterList = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#");
    private final Paint mPaint = new Paint();

    private float mTextSize;
    private int mTextColor = Color.BLACK;
    private int mTextColorChoose = Color.GREEN;

    private int mColorCircle = Color.RED;

    private int mBgColorNormal = Color.TRANSPARENT;
    private int mBgColorPress = Color.YELLOW;

    private float mTextWidth = 10;
    private float mItemHeight = 10;

    private float mColorRadius = 25;
    private float mHintWidth = 100;
    private float mHintHeight = 100;
    private float mTouchY = 10;
    private float mTouchX = -1;

    private float mHintTextSize = 25;
    private float mHintRadius = 5;

    private int mSelectPosition = -1;
    private boolean isTouchLetter = false;

    private final int minY = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    /****************************************/

    private OnBarChangeListener listener;

    /**********************************************************************************************/

    public ContactBar(Context context) {
        this(context, null);
    }

    public ContactBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContactBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ContactBar);

            mTextColor = a.getColor(R.styleable.ContactBar_cb_text_color_normal, mTextColor);
            mTextColorChoose = a.getColor(R.styleable.ContactBar_cb_text_color_select, mTextColorChoose);
            mTextSize = a.getDimension(R.styleable.ContactBar_cb_text_size_normal, mTextSize);
            mTextWidth = a.getDimension(R.styleable.ContactBar_cb_text_width, mTextWidth);
            mItemHeight = a.getDimension(R.styleable.ContactBar_cb_text_height, mItemHeight);
            mHintWidth = a.getDimension(R.styleable.ContactBar_cb_hint_width, mHintWidth);
            mHintHeight = a.getDimension(R.styleable.ContactBar_cb_hint_height, mHintHeight);
            mColorCircle = a.getColor(R.styleable.ContactBar_cb_circle_color, mColorCircle);
            mColorRadius = a.getDimension(R.styleable.ContactBar_cb_circle_radius, mColorRadius);
            mHintTextSize = a.getDimension(R.styleable.ContactBar_cb_hint_text_size, mHintTextSize);
            mHintRadius = a.getDimension(R.styleable.ContactBar_cb_hint_radius, mHintRadius);
            mBgColorNormal = a.getColor(R.styleable.ContactBar_cb_text_bg_color_normal, mBgColorNormal);
            mBgColorPress = a.getColor(R.styleable.ContactBar_cb_text_bg_color_press, mBgColorPress);
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();

        // 1.初始化画笔
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextAlign(Paint.Align.CENTER);

        // 字体高度误差
        final float fontTemp = (mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top) / 4;

        // 单个文字高度
        final int letterCount = mLetterList.size();
        final int height = getHeight();

        float itemHeight = height / letterCount;
        float itemHeightBegin = 0;
        float itemHeightEnd = height;

        if (itemHeight > mItemHeight) {
            itemHeight = mItemHeight;
            float temp = (height - itemHeight * letterCount) / 2;
            itemHeightBegin += temp;
            itemHeightEnd -= temp;
        } else {
            mItemHeight = itemHeight;
        }

        // 按压背景变色
        if (mBgColorPress != Color.TRANSPARENT) {
            final boolean isPress = (isTouchLetter && mTouchY > itemHeightBegin && mTouchY < itemHeightEnd);
            mPaint.setColor(isPress ? mBgColorPress : mBgColorNormal);
            canvas.drawRect(getWidth() - mTextWidth, itemHeightBegin, getRight(), itemHeightEnd, mPaint);
        }

        // 每一个的中间位置X
        final float itemCenterX = getWidth() - mTextWidth / 2;
        final float itemHeightHalf = itemHeight / 2;

        // 2.画文字
        for (int i = 0; i < mLetterList.size(); i++) {

            final String str = mLetterList.get(i);
            if (TextUtils.isEmpty(str)) continue;

            // 每一个的中间位置Y
            final float itemCenterY = itemHeight * i + itemHeightHalf + itemHeightBegin;
            final float itemTopY = itemHeight * i + itemHeightBegin;
            final float itemBottomY = itemHeight * (i + 1) + itemHeightBegin;

            final boolean selecte = (mTouchY > itemTopY && mTouchY < itemBottomY);
            mSelectPosition = selecte ? i : mSelectPosition;

            // 1. 字母背景
            boolean ok1 = (selecte || (mSelectPosition == -1 && 0 == i));
            if (ok1) {
                mPaint.setColor(mColorCircle);
                canvas.drawCircle(itemCenterX, itemCenterY, mColorRadius, mPaint);
            }
            // 2. 字母变色
            mPaint.setTextSize(mTextSize);
            mPaint.setFakeBoldText(ok1);
            mPaint.setColor(ok1 ? mTextColorChoose : mTextColor);
            canvas.drawText(str, itemCenterX, itemCenterY + fontTemp, mPaint);
            final float centenHintX = mHintWidth / 2;
            // 3. 提示背景, 提示文字
            if (isTouchLetter && selecte) {
                mPaint.setColor(mColorCircle);
                canvas.drawCircle(centenHintX, itemCenterY, mHintRadius, mPaint);
                mPaint.setTextSize(mHintTextSize);
                mPaint.setFakeBoldText(true);
                mPaint.setColor(isTouchLetter && selecte ? mTextColorChoose : Color.TRANSPARENT);
                canvas.drawText(str, centenHintX, itemCenterY + 2 * fontTemp, mPaint);
            }
            if (null != listener && selecte && isTouchLetter) {
                listener.onBarChange(str);
            }
        }
        canvas.restore();
    }

    /**********************************************************************************************/

    private float mDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        mTouchX = event.getX();
        mTouchY = event.getY();
        isTouchLetter = (mTouchX > getWidth() - mTextWidth && mTouchX <= getWidth());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mTouchY - mDownY) > minY) {
                    mDownY = mTouchY;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouchLetter = false;
                invalidate();
                break;
        }
        return isTouchLetter ? true : super.dispatchTouchEvent(event);
    }

    /************************************************/

    public void scrollLetter(String positionStr) {
        if (isTouchLetter) return;

        int index = mLetterList.indexOf(positionStr);
        if (index < 0 || index == mSelectPosition) return;

        mSelectPosition = index;
        invalidate();
    }

    public List<String> getLetter() {
        return mLetterList;
    }

    public void setLetter(List<String> letters) {

        if (letters.isEmpty()) return;

        mLetterList.clear();
        mLetterList.addAll(letters);
        invalidate();
    }

    public void setOnBarChangeListener(OnBarChangeListener mListener) {
        this.listener = mListener;
    }

    public interface OnBarChangeListener {

        void onBarChange(String letter);
    }
}