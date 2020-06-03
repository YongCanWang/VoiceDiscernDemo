package cn.mapscloud.www.mydevelop;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.sunflower.FlowerCollector;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.mapscloud.www.mydevelop.utils.ApkInstaller;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bt_start;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语记安装助手类
//    ApkInstaller mInstaller;

    private boolean mTranslateEnable = false;
    private TextView tv_text;
    private EditText et_text;

    // 语记安装助手类
    private ApkInstaller mInstaller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_start = (Button) findViewById(R.id.bt_start);
        tv_text = (TextView) findViewById(R.id.tv_Text);
        et_text = (EditText) findViewById(R.id.et_text);

        //设置监听事件
        bt_start.setOnClickListener(this);


    }


    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.e("main", "onClick");

        //初始化initDialog
//        initDialog();
        //设置参数
//        setParam();

//        initSpeech();

        init();

    }

    private void init() {
// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(MainActivity.this, mInitListener);

//        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
//                Activity.MODE_PRIVATE);

        mInstaller = new ApkInstaller(MainActivity.this);

        mEngineType = SpeechConstant.TYPE_LOCAL;       //本地
        mEngineType = SpeechConstant.TYPE_CLOUD;    //云端

        // 移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(MainActivity.this, "iat_recognize");
//        mResultText.setText(null);// 清空显示内容
        mIatResults.clear();

        //设置参数
        setParam();

        mIatDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String resultJson = "]";

                if (!b) {
                    resultJson += recognizerResult.getResultString() + ",";

                } else {
                    resultJson += recognizerResult.getResultString() + "]";

                }

                if (b) {
                    Log.e("main", "11111");
                    //解析语音识别后返回的json格式的结果
                    Gson gson = new Gson();
                    List<DictationResult> resultList = gson.fromJson(resultJson,
                            new TypeToken<List<DictationResult>>() {
                            }.getType());
                    Log.e("main", "22222");
                    String result = "";
                    for (int i = 0; i < resultList.size() - 1; i++) {
                        result += resultList.get(i).toString();
                    }

                    Log.e("main", "result:" + result);
                    tv_text.setText(result);
                    //获取焦点
                    tv_text.requestFocus();
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        mIatDialog.show();

    }



    private void initSpeech() {
        //1. 创建SpeechRecognizer对象，第二个参数： 本地识别时传 InitListener
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer( this, null); //语音识别器
        //2. 设置听写参数，详见《 MSC Reference Manual》 SpeechConstant类
        mIat.setParameter(SpeechConstant. DOMAIN, "iat" );// 短信和日常用语： iat (默认)
        mIat.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mIat.setParameter(SpeechConstant. ACCENT, "mandarin" );// 设置普通话
        //3. 开始听写
        mIat.startListening( mRecoListener);

    }

    RecognizerListener mRecoListener = new RecognizerListener(){


        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        //开始录音时回掉
        @Override
        public void onBeginOfSpeech() {

        }

        //结束录音时回掉
        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String resultJson = "]";

            if (!b) {
                resultJson += recognizerResult.getResultString() + ",";

            } else {
                resultJson += recognizerResult.getResultString() + "]";

            }

            if (b) {
                Log.e("main", "11111");
                //解析语音识别后返回的json格式的结果
                Gson gson = new Gson();
                List<DictationResult> resultList = gson.fromJson(resultJson,
                        new TypeToken<List<DictationResult>>() {
                        }.getType());
                Log.e("main", "22222");
                String result = "";
                for (int i = 0; i < resultList.size() - 1; i++) {
                    result += resultList.get(i).toString();
                }

                Log.e("main", "result:" + result);
                tv_text.setText(result);
                //获取焦点
                tv_text.requestFocus();
        }
        }
        @Override
        public void onError(SpeechError speechError) {
            Log. e("main", "error.getPlainDescription(true)==" + speechError.getPlainDescription(true ));

        }

        //扩展用接口
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    /**
     * 初始化initDialog对象
     */
    private void initDialog() {

        //初始化对象
        //创建语音听写对象
//        mIat = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);


        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(MainActivity.this, mInitListener);


        //设置监听
        mIatDialog.setListener(new RecognizerDialogListener() {
            //创建一个字符串用于存放数据
            String resultJson = "[";

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                Log.e("main", "onResult: onResult");
                if (!b) {
                    resultJson += recognizerResult.getResultString() + ",";

                } else {
                    resultJson += recognizerResult.getResultString() + "]";

                }

                if (b) {
                    Log.e("main", "11111");
                    //解析语音识别后返回的json格式的结果
                    Gson gson = new Gson();
                    List<DictationResult> resultList = gson.fromJson(resultJson,
                            new TypeToken<List<DictationResult>>() {
                            }.getType());
                    Log.e("main", "22222");
                    String result = "";
                    for (int i = 0; i < resultList.size() - 1; i++) {
                        result += resultList.get(i).toString();
                    }

                    Log.e("main", "result:" + result);
                    tv_text.setText(result);
                    //获取焦点
                    tv_text.requestFocus();
                    //将光标定位到文字最后，以便修改
//                    et_text.setSelection(result.length());
                }
            }

            @Override
            public void onError(SpeechError speechError) {

                Log.e("main", "onResult: onError");
                speechError.getPlainDescription(true);
            }
        });
        //开始听写，需将sdk中的assets文件下的文件夹拷入项目的assets文件夹下（没有的话自己新建）
        mIatDialog.show();

    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(MainActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * 设置参数
     */
    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

//        this.mTranslateEnable = mSharedPreferences.getBoolean( this.getString(R.string.pref_key_translate), false );
//        if( mTranslateEnable ){
//            mIat.setParameter( SpeechConstant.ASR_SCH, "1" );
//            mIat.setParameter( SpeechConstant.ADD_CAP, "translate" );
//            mIat.setParameter( SpeechConstant.TRS_SRC, "its" );
//        }

//        String lag = mSharedPreferences.getString("iat_language_preference",
//                "mandarin");
//        if (lag.equals("en_us")) {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
//            mIat.setParameter(SpeechConstant.ACCENT, null);
//
//            if( mTranslateEnable ){
//                mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
//                mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
//            }
//        } else {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//            // 设置语言区域
//            mIat.setParameter(SpeechConstant.ACCENT, lag);
//
//            if( mTranslateEnable ){
//                mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
//                mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
//            }
//        }
//
//        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
//
//        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
//
//        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
//
//        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
//        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");

    }

}
