package cn.mapscloud.www.mydevelop;

import android.app.Application;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by wangyongcan on 2017/8/27.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("main", "MyApplication:" + "MyApplication");

        //初始化讯飞语音配置对象
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=599e41d7");
    }
}
