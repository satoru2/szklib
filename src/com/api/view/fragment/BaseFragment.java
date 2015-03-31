package com.api.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 全てのフラグメントの基底クラス<br>
 * これを継承するFragmentは必ずstaticメソッドでインスタンスを作成する必要がある。<br>
 *
 */
abstract public class BaseFragment extends Fragment{

    public static String TAG = "BaseFragment";

    /***************************************************************************
     * フィールド変数
     ***************************************************************************/

    /**
     * Attach されている Activity<br>
     */
    protected Activity activity;

    /***************************************************************************
     * コアライフサイクルメソッド
     ***************************************************************************/

    /**
     * Activityに関連付けされた際に一度だけ呼ばれる。
     */
    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        Log.d(TAG,"Fragment-onAttach");
    }

    /**
     * Fragmentの初期化処理を行う。<br>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Fragment-onCreate");

    }

    /**
     * Fragmentに関連付けるViewを作成し、returnする。
     * FragmentのUIが描画されるタイミングでよびだされる。
     * FragmentのレイアウトのRootになっているViewをここでinflateする。
     */
    @Override
    abstract public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState);

    /**
     * 親となるActivityの「onCreate」の終了を知らせる。<br>
     */
    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        Log.d(TAG,"Fragment-onActivityCreated");
    }

    /**
     * Activityの「onStart」に基づき開始される。<br>
     */
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"Fragment-onStart");
    }

    /**
     * Activityの「onResume」に基づき開始される。<br>
     */
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"Fragment-onResume");
    }

    /**
     * Activityが「onPause」になったり、Fragmentが変更更新され、<br>
     * 操作を受け付けなくなった場合に呼び出される。<br>
     */
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"Fragment-onPause");
    }

    /**
     * フォアグラウンドで無くなった場合に呼び出される。<br>
     */
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"Fragment-onStop");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG,"Fragment-onDestroyView");
    }

    /**
     * Fragmentが殺される最後に呼び出される。<br>
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"Fragment-onDestroy");
    }

    /**
     * Activityの関連付けから外された時に呼び出される。<br>
     */
    @Override
    public void onDetach(){
        super.onDetach();
        Log.d(TAG,"Fragment-onDetach");
    }

    /***************************************************************************
     * abstract メソッド
     ***************************************************************************/
    /***************************************************************************
     * protected メソッド
     ***************************************************************************/

}
