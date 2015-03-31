package com.api.view.swipe;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.api.util.ArrayUtil;
import com.api.util.ArrayUtil.Direction;

/**
 * updateContent内での処理は 子ビューの中身のみの変更では無限スワイプしないよ ViewPager のonLayoutは呼ばれないからな
 * ViewPager 自身の子ビューの変化(ViewPager 自身の再描画)をしないと夢幻にゃならん ex) <br>
 * <code>
 * × Class extends TextView {
 *      public void updateContent(View v, int newPosition){
 *          setText(String.valueOf(newPosition));
 *      }
 *       </code><br>
 * <code>
 * ○ Class extends LinearLayout {
 *         public void updateContent(View int newPosition){
 *              removeView(v);
 *              TextView text = (TextView) v;
 *              text.setText(String.valueOf(newPosition));
 *               addView(text,new Linearlayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
 *          }
 *      }
 * </code>
 */
public abstract class CirculationSwipeAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    public static String TAG = CirculationSwipeAdapter.class.getSimpleName();
    /**
     * 定数
     */
    /** 前 */
    protected final static int PREV = 0;
    /** 次 */
    protected final static int NEXT = 2;

    /**
     * 変数
     */

    /**
     * from 0 to PAGE_NUM -1
     */
    private int contentPosition;

    /**
     * number of pages (3x)
     */
    private int PAGE_NUM;

    /**
     * Context
     */
    private Context mContext;

    /**
     * translation of swipe 0 or 2 0 : previous 2 : next
     */
    private int translation;

    /**
     * array of position
     */
    private int[] contentAry;

    /**
     * array of View
     */
    private HashMap<Integer, View> mViews;

    /**
     * ViewPager
     */
    private ViewPager mViewPager;
    /**
     * Tag of OverFlow
     */
    private int overFlow;

    /**
     * コンストラクタ
     * 
     * @param context
     */
    @SuppressLint("UseSparseArrays")
    public CirculationSwipeAdapter(Context context) {
        // 初期化
        mContext = context;
        // View の数
        PAGE_NUM = getPagerChildCount();
        // 現在ポジション
        contentPosition = 0;
        // 移動方向
        translation = -1;
        // View の overFlow 管理
        overFlow = -1;
        // View Tag 保持
        contentAry = new int[] { 0, 1, 2 };
        // View 保持
        mViews = new HashMap<Integer, View>();
        // ViewPager
        mViewPager = new CirculateViewPager(context);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(1);

        addViewPager(mViewPager);
    }

    // *******************************************************************************************
    // アクセッサ 系 メソッド
    // *******************************************************************************************
    /**
     * 
     * @return 正面のタグを返すよ
     */
    protected int getFocusedPosition() {
        return contentAry[1];
    }

    /**
     * 正面のView返すよ
     */
    protected View getFocusedView() {
        View view = mViews.get(getFocusedPosition());
        // 初回用
        if (view == null) {
            Log.w(TAG, "getFocusedView view == null");
        }
        return view;
    }

    // *******************************************************************************************
    // 子クラスで利用 メソッド
    // *******************************************************************************************
    protected void onPageScrolle(int position, float positionOffset, int positionOffsetPixels) {
    }

    // *******************************************************************************************
    // abstract メソッド
    // *******************************************************************************************
    /**
     * View を生成するよ
     * 
     * @param newPosition
     * @return View getPagerChildCountで指定した回数呼ばれるよ
     */
    abstract public View createContent(Context mContext, int newPosition);

    /**
     * updateContent
     * 
     * @param v
     * @param newPosition
     */
    abstract public void updateContent(View v, int newPosition);

    /**
     * 捲るViewの数
     * 
     * @return
     */
    abstract public int getPagerChildCount();

    /**
     * ここでViewGroupにセット<br>
     * <code>
     * viewGroup.addView(viewPager, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
     * </code>
     * 
     * @param viewPager
     */
    abstract public void addViewPager(ViewPager viewPager);

    // *******************************************************************************************
    // いじくらない系 メソッド
    // *******************************************************************************************
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // 今のポジション計算してー
        int newPosition = calcContentPosition(position);
        // Viewを生成
        View view = createContent(mContext, newPosition);
        // 表示
        container.addView(view, 0);
        // 今のViewにポジションをセット
        view.setTag(position);
        // Viewを保持
        mViews.put(position, view);
        // ポジションかえすよ
        return Integer.valueOf(position);
    }

    /**
     * 現在表示しているViewの番号
     * 
     */
    @Override
    public void onPageSelected(int position) {
        translation = position;
    }

    /**
     * 移動中に発生する。
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onPageScrolle(position, positionOffset, positionOffsetPixels);
    }

    /**
     * ViewPagerの状態の検知
     * 
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            handlePageScrollStateChangedToIdle();
        } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {

        } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

        } else if (state == ViewPager.SCREEN_STATE_OFF) {

        } else if (state == ViewPager.SCREEN_STATE_ON) {

        }
    }

    /**
     * 捲った時の処理
     */
    public void handlePageScrollStateChangedToIdle() {

        // contentPosition 更新
        switch (translation) {
        case PREV:
            // scroll to previous page
            if ((--contentPosition) < 0) {
                contentPosition = PAGE_NUM - 1;
            }
            // 正面View 更新
            updateFocusedPosition(translation);
            // View 更新
            updateContents(translation);
            break;
        case NEXT:
            // scroll to next page
            contentPosition = (++contentPosition) % PAGE_NUM;
            // 正面View 更新
            updateFocusedPosition(translation);
            // View 更新
            updateContents(translation);
            break;
        default:
            break;
        }
    }

    /**
     * 捲った時のアップデート
     * 
     * @param v
     * @param newPosition
     */
    public void updateContent(View view, int newPosition, boolean isInitialize) {
        if (isInitialize) {
            InitializeContentAry();
        }
        updateContent(view, newPosition);
    }

    private void InitializeContentAry() {
        for (int i = 0; i < contentAry.length; i++) {
            contentAry[i] = (Integer) mViewPager.getChildAt(i).getTag();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        int index = (Integer) view.getTag();
        int position = (Integer) object;
        int newPosition = (position + contentPosition) % 3;
        return newPosition == index;
    }

    /**
     * update specified view's content
     * 
     * @param index 0 or 2
     */
    void updateContents(int trans) {
        final int newPosition = calcContentPosition(trans);
        updateContent(mViews.get(overFlow), newPosition, false);
    }

    /**
     * get position of content
     * 
     * @param position from 0 to 2
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

    /**
     * 正面View保持
     * 
     * @param trans
     */
    private void updateFocusedPosition(int trans) {

        // update contentAry
        if (trans == NEXT) {
            // 左あふれ
            overFlow = ArrayUtil.circulationShift(contentAry, Direction.LEFT);
        } else if (trans == PREV) {
            // 右あふれ
            overFlow = ArrayUtil.circulationShift(contentAry, Direction.RIGHT);
        } else {
            Log.w(TAG, "unKnown Translation");
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.w(TAG, "destroyItem");
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

    @Override
    public int getCount() {
        return 3;
    }

    private class CirculateViewPager extends ViewPager {

        public CirculateViewPager(Context context) {
            this(context, null);
        }

        public CirculateViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);

            if (getCurrentItem() != 1) {
                setCurrentItem(1, false);
            }
        }
    }
}
