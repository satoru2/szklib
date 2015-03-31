// reference by http://y-anz-m.blogspot.jp/2012/10/how-to-create-looping-viewpager.html
package com.api.view.swipe;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SwipeableSelecterView extends LinearLayout {

    private static final String TAG = SwipeableSelecterView.class
            .getSimpleName();
    // listener
    private OnClickSelecterView onClickSelectListener;
    // view Positon
    public final static int ZERO = 0;
    public final static int FIRST = 1;
    public final static int SECOND = 2;
    public final static int THIRD = 3;
    public final static int FORTH = 4;
    public final static int FIFTH = 5;
    public final static int SIXTH = 6;
    public final static int SEVENTH = 7;
    public final static int EIGHTH = 8;

    // ページャーアダプター
    private SelecterPagerAdapter mPagerAdapter;
    // ビューページャー
    private ViewPager mViewPager;

    /**
     * @Deprecated
     * @param context
     */
    public SwipeableSelecterView(Context context) {
        this(context, null);
    }

    /**
     * @Deprecated
     * @param context
     * @param attrs
     */
    public SwipeableSelecterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int pageNum = 0;
        final String NAME_SPACE = "http://ikuzus.android.util.swipe.view.SwipeableSelecterView";
        if (attrs != null) {
            // TODO 属性指定
            pageNum = attrs.getAttributeIntValue(NAME_SPACE, "pageNum", 0);
            // mPagerAdapter.setPageNum(pageNum);
        }
        // ページアダプター
        mPagerAdapter = new SelecterPagerAdapter(context);

        // ビューページャー
        mViewPager = new MyViewPager(context);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(mPagerAdapter);

        // 表示
        addView(mViewPager, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * Set Listener
     * @param listener
     */
    public void setOnClickSelectListener (OnClickSelecterView listener) {
        this.onClickSelectListener = listener;
    }
    
    private class SelecterPagerAdapter extends PagerAdapter implements
            ViewPager.OnPageChangeListener, OnClickSelecterView {

        Context mContext;

        /**
         * from 0 to 2
         */
        int focusedPosition = 0;

        /**
         * from 0 to PAGE_NUM -1
         */
        int contentPosition = 0;

        /**
         * number of pages (3x)
         */
        int PAGE_NUM = 9;

        public SelecterPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, String.format("start # instantiateItem(%s)", position));

            // 今のポジション計算してー
            int newPosition = calcContentPosition(position);
            // Viewを生成
            SelecterView view = createContent(newPosition);
            // 表示
            container.addView(view, 0);
            // 今のViewにポジションをセット
            view.setTag(position);
            // ポジションかえすよ
            return Integer.valueOf(position);
        }

        public SelecterView createContent(int position) {
            Log.d(TAG, String.format("start # createContent(%s)", position));
            final SelecterView view = new SelecterView(mContext);
            view.setText("hogehoge");
            view.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    int number = (Integer) view.getTag();
                    onClickSelecter(number);
                }
                
            });
            return view;
        }

        public void updateContent(SelecterView view, int position) {
            Log.d(TAG, "updateContent position " + String.valueOf(position));
            Log.d(TAG,
                    String.format("updateContent view Tag [%d]",
                            (Integer) view.getTag()));
            int newPosition = position;
            SelecterView selecter = view;
            if (newPosition == 0) {
                selecter.setBackgroundColor(Color.BLACK);
            } else if (newPosition == 1) {
                selecter.setBackgroundColor(Color.YELLOW);
            } else if (newPosition == 2) {
                selecter.setBackgroundColor(Color.GREEN);
            } else if (newPosition == 3) {
                selecter.setBackgroundColor(Color.BLUE);
            } else if (newPosition == 4) {
                selecter.setBackgroundColor(Color.DKGRAY);
            } else if (newPosition == 5) {
                selecter.setBackgroundColor(Color.RED);
            } else if (newPosition == 6) {
                selecter.setBackgroundColor(Color.MAGENTA);
            } else if (newPosition == 7) {
                selecter.setBackgroundColor(Color.CYAN);
            } else if (newPosition == 8) {
                selecter.setBackgroundColor(Color.LTGRAY);
            } else if (newPosition == 9) {
                selecter.setBackgroundColor(Color.WHITE);
            }
        }

        /**
         * 移動中に発生する。
         */
        @Override
        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
        }

        /**
         * 現在表示しているViewの番号
         * 
         */
        @Override
        public void onPageSelected(int position) {
            focusedPosition = position;
            Log.d(TAG, String.format("focusedPosition[%s]", focusedPosition));
        }

        /**
         * ViewPagerの状態の検知
         * 
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                handlePageScrollStateChangedToIdle();
            }
        }

        private void handlePageScrollStateChangedToIdle() {
            Log.d(TAG, "handlePageScrollStateChangedToIdle " + focusedPosition);
            switch (focusedPosition) {
            case 0:
                // scroll to previous page
                if ((--contentPosition) < 0) {
                    contentPosition = PAGE_NUM - 1;
                }
                updateContents(0);
                break;

            case 2:
                // scroll to next page
                contentPosition = (++contentPosition) % PAGE_NUM;
                updateContents(2);
                break;
            default:
                break;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        /**
         * update specified view's content
         * 
         * @param index
         *            0 or 2
         */
        void updateContents(int index) {
            int count = mViewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                SelecterView v = (SelecterView) mViewPager.getChildAt(i);
                if (isViewFromObject(v, Integer.valueOf(index))) {
                    final int newPosition = calcContentPosition(index);
                    updateContent(v, newPosition);
                    break;
                }
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e(TAG, "destroyItem");
            int count = mViewPager.getChildCount();
            int index = (Integer) object;
            for (int i = 0; i < count; i++) {
                View v = mViewPager.getChildAt(i);
                if (isViewFromObject(v, Integer.valueOf(index))) {
                    container.removeView(v);
                    break;
                }
            }
        }

        /**
         * get position of content
         * 
         * @param position
         *            from 0 to 2
         * @return content position (from 0 to PAGE_NUM - 1)
         */
        private int calcContentPosition(int position) {
            int offset = position - 1;
            int newPosition = contentPosition + offset;
            if (newPosition < 0) {
                newPosition = PAGE_NUM - 1;
            } else {
                newPosition = newPosition % PAGE_NUM;
            }
            return newPosition;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            int index = (Integer) view.getTag();
            int position = (Integer) object;
            int newPosition = (position + contentPosition) % 3;
            return newPosition == index;
        }

        @Override
        public void onClickSelecter(int number) {
            if (onClickSelectListener != null) {
                onClickSelectListener.onClickSelecter(number);
            }
        }
    }
}
