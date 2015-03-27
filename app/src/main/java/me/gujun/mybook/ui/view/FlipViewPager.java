package me.gujun.mybook.ui.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

/**
 * Custom flip pager view.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-20 14:59:59
 */
public class FlipViewPager extends ViewGroup {
    private static final String TAG = FlipViewPager.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final int DEFAULT_FLING_VELOCITY = 10;

    private View mPrevPage, mCurrPage, mNextPage;

    private FlipAdapter mAdapter;

    private VelocityTracker mVelocityTracker;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId;

    /**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;

    /**
     * Position of the last motion event.
     */
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;

    private int mIndex;

    public FlipViewPager(Context context) {
        this(context, null);
    }

    public FlipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIndex = 0;
    }

    public void setAdapter(FlipAdapter adapter) {
        removeAllViews();
        mAdapter = adapter;

        mPrevPage = adapter.getView(null, mIndex - 1);
        addView(mPrevPage, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mCurrPage = adapter.getView(null, mIndex);
        addView(mCurrPage, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mNextPage = adapter.getView(null, mIndex + 1);
        addView(mNextPage, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public boolean pagePrev() {
        if (mIndex > 0) {
            removeView(mNextPage);
            mNextPage = mAdapter.getView(null, mIndex - 2);
            addView(mNextPage, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            mIndex--;

            View tempView = mPrevPage;
            mPrevPage = mNextPage;
            mNextPage = mCurrPage;
            mCurrPage = tempView;
            return true;
        }
        return false;
    }

    public boolean pageNext() {
        removeView(mPrevPage);
        mPrevPage = mAdapter.getView(null, mIndex + 2);
        addView(mPrevPage, -1, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mIndex++;

        View tempView = mPrevPage;
        mPrevPage = mCurrPage;
        mCurrPage = mNextPage;
        mNextPage = tempView;
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mLastMotionX = ev.getX();
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(ev);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(500);
                int initialVelocity = (int) mVelocityTracker.getXVelocity();
                int dx = (int) (ev.getX() - mLastMotionX);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * This method JUST determines whether we want to intercept the motion.
     * If we return true, onMotionEvent will be called and we do the actual
     * scrolling there.
     */
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        final int action = event.getAction() & MotionEventCompat.ACTION_MASK;

        // Always take care of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the drag.
            if (DEBUG) Log.d(TAG, "Intercept done!");
            mIsBeingDragged = false;
            mIsUnableToDrag = false;
            mActivePointerId = INVALID_POINTER;
            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            return false;
        }

        // Nothing more to do here if we have decided whether or not we are dragging.
        if (action != MotionEvent.ACTION_DOWN) {
            if (mIsBeingDragged) {
                if (DEBUG) Log.v(TAG, "Intercept returning true!");
                return true;
            }
            if (mIsUnableToDrag) {
                if (DEBUG) Log.v(TAG, "Intercept returning false!");
                return false;
            }
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                // mIsBeingDragged == false, otherwise the shortcut would have caught it.
                // Check whether the user have moved far enough from his original down touch.

                // Locally do absolute value. mLastMotionY is set to the y value of the down event.
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(event, activePointerId);
                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float dx = x - mLastMotionX;
                final float xDiff = Math.abs(dx);
                final float y = MotionEventCompat.getY(event, pointerIndex);
                final float dy = y - mLastMotionY;
                final float yDiff = Math.abs(dy);
                if (DEBUG) Log.d(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);

                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                }
            }
            case MotionEvent.ACTION_DOWN: {

            }
        }

        return super.onInterceptHoverEvent(event);
    }


    private int getClientWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    /*protected boolean canScroll(View v, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight()&&
                        )
            }
        }
    }*/

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (mAdapter == null) {
            return false;
        }

        final int width = getClientWidth();
        final int scrollX = getScrollX();
        if (direction < 0) { // scrolling left
            return scrollX > width;
        } else if (direction > 0) { // scrolling right
            return scrollX < width;
        } else {
            return false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, our internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        // Children are just made to fill our space.
        int childWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int childHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                if (DEBUG) Log.v(TAG, "Measuringg #" + i + " " + child
                        + ": " + childWidthMeasureSpec);

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = 0;
        final int parentRight = r - l;
        final int parentTop = 0;
        final int parentBottom = b - t;

        int childLeft = -parentRight;
        int childTop = parentTop;
        int childWidth;
        int childHeight;

        for (int i = 0; i < 3; i++) {
            final View child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                if (DEBUG) Log.v(TAG, "Positioning #" + i + " " + child
                        + ":" + childLeft + "," + childTop + " " + child.getMeasuredWidth()
                        + "x" + child.getMeasuredHeight());
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                childLeft += childWidth;
            }
        }
    }

    public interface FlipAdapter {
        View getView(View convertView, int position);
    }
}