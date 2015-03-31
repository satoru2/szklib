package com.api.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;

import com.api.API.log.APILog;

/**
 * 全てのActivityのベース
 * 
 */
public abstract class BaseActivity extends Activity {

    /**
     * Resources
     */
    protected Resources resources;

    /**
     * refreshFlg
     */
    protected boolean refreshFlg;

    /**************************************************************************************
     * ライフサイクルメソッド
     * 
     **************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        APILog.d(this, "onCreate()");

        super.onCreate(savedInstanceState);

        // リソースを取得
        this.resources = getResources();

        // レイアウトセット
        setContentView(this.getContentView());
        // ビュー初期化
        this.initView();
        // イベント登録
        this.initEventListener();

        // フラグメントをコードから追加する場合
        SparseArray<Fragment> fragments = this.getFragments();
        if (fragments != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (int i = 0; i < fragments.size(); i++) {
                int targetContainerId = fragments.keyAt(i);
                fragmentTransaction.add(targetContainerId, fragments.get(targetContainerId));

            }
            fragmentTransaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        APILog.d(this, "onCreateOptionsMenu()");
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        APILog.d(this, "onStart()");

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        APILog.d(this, "onRestart()");

    }

    @Override
    protected void onResume() {
        super.onResume();

        APILog.d(this, "onResume()");

    }

    @Override
    protected void onPause() {
        super.onPause();

        APILog.d(this, "onPause()");

        // refresh が呼ばれた時はここで ActivityScopeSession を削除する
        if (refreshFlg) {

            APILog.d(this, "onPause()", String.format("refreshFlg = %s", refreshFlg ? "True" : "False"));

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        APILog.d(this, "onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        APILog.d(this, "onDestroy()");

    }

    /**************************************************************************************
     * Fragment
     * 
     **************************************************************************************/

    /**************************************************************************************
     * アブストラクトメソッド
     * 
     **************************************************************************************/
    /** 各アクティビティのビュー初期化 */
    abstract protected void initView();

    /** 各アクティビティのイベント初期化 */
    abstract protected void initEventListener();

    /** 各アクティビティのlayout初期化 */
    abstract protected int getContentView();

    /**************************************************************************************
     * fragment 関係
     * 
     **************************************************************************************/
    /**
     * コードからフラグメントを実装する画面ではこれを呼ぶ<br>
     * onCreate から initEventListener() の後にコールされる<br>
     * 
     * @return
     */
    protected SparseArray<Fragment> getFragments() {
        return null;
    }

    /**************************************************************************************
     * ダイアログ関係
     * 
     **************************************************************************************/

    /**************************************************************************************
     * Intent関係
     * 
     **************************************************************************************/

    /**
     * 現在のアクティビティのスタックを残さず遷移<br>
     * 
     * @param context 遷移元<br>
     * @param next 遷移先<br>
     */
    protected void startActivityWithFinish(Context context, Class<? extends BaseActivity> next) {

        APILog.d(this, "startActivityWithFinish()", "Start");

        Intent intent = new Intent(context, next);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        APILog.d(this, "startActivityWithFinish()", "End");

    }

    /**
     * 自画面再表示<br>
     * 
     * @param context Activity<br>
     */
    protected void refresh(Context context) {

        APILog.d(this, "refresh()", "Start");

        startActivity(new Intent(context, context.getClass()));
        finish();

        APILog.d(this, "refresh()", "End");

    }
}
