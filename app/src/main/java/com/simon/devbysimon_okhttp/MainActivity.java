package com.simon.devbysimon_okhttp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.et_password)
    EditText et_password;

    private String baseUrl = "http://192.168.31.134:8080/WEB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

    @OnClick({R.id.bt_get, R.id.bt_post, R.id.bt_upload_file, R.id.bt_download_file})
    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.bt_get:
                getMethod();
                break;
            case R.id.bt_post:
                 postMethods();
                break;
            case R.id.bt_upload_file:
                // uploadUndefinedFile();
                uploadFiles();
                break;
            case R.id.bt_download_file:
                downLoadFile();
                break;
        }
    }

    // Get
    private void getMethod() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        String url = baseUrl + "/login?username=" + username + "&password=" + password;
        OkHttpUtils.getInstance().get(url, new OkHttpUtils.RequestCallBack<String>() {
            @Override
            public void requestSuccess(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }

            @Override
            public void requestFail(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }
        });


    }

    // Post
    private void postMethod() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        String url = baseUrl + "/login";
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        OkHttpUtils.getInstance().post(url, map, new OkHttpUtils.RequestCallBack<String>() {
            @Override
            public void requestSuccess(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }

            @Override
            public void requestFail(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }
        });
    }

    private void postMethods() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("member_id", "0");
        params.put("member_logo", "http://resource.cinfor.com.cn/images/703e8c97a3cd195b04f5c889219a3f3e.png");
        params.put("member_name", "Simon");
        params.put("member_sex", "1");
        params.put("member_birth", "1561478400000");// 秒级时间戳
        OkHttpUtils.getInstance().post("http://test.cinfor.com.cn/App/v1_0/User/editMember", params, new OkHttpUtils.RequestCallBack<String>() {
            @Override
            public void requestSuccess(final String obj) {
                Log.i("Simon","post = "+obj);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }

            @Override
            public void requestFail(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }
        });
    }

    // Image
    private void uploadUndefinedFile() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + "mine.png");
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在!", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = baseUrl + "/uploadUndefinedFiles";

        OkHttpUtils.getInstance().uploadUndefinedFile(url, file, new OkHttpUtils.RequestCallBack<String>() {
            @Override
            public void requestSuccess(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }

            @Override
            public void requestFail(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }
        });
    }

    // Upload File
    private void uploadFile() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + "mine.png");
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在!", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = baseUrl + "/uploadFiles";

        Map<String, Object> map = new HashMap<>();
        map.put("address", "USA");
        map.put("fileData", file);

        OkHttpUtils.getInstance().uploadFile(url, map, new OkHttpUtils.RequestCallBack<String>() {
            @Override
            public void requestSuccess(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }

            @Override
            public void requestFail(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }
        });
    }
    private void uploadFiles() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + "mine.png");
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在!", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://test.cinfor.com.cn/App/v1_0/Common/uploadImg";

        Map<String, Object> map = new HashMap<>();
        map.put("token", "33b3df74fefc9640c490c8c42d504f54");
        map.put("img", file);

        OkHttpUtils.getInstance().uploadFile(url, map, new OkHttpUtils.RequestCallBack<String>() {
            @Override
            public void requestSuccess(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }

            @Override
            public void requestFail(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }
        });
    }

    // Download File
    private void downLoadFile() {
        String url = "http://192.168.31.134:8080/files/1561300442112.jpg";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + "mine123.png";

        OkHttpUtils.getInstance().downloadFile(url, filePath, new OkHttpUtils.RequestCallBack<String>() {
            @Override
            public void requestSuccess(final String obj) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText(obj);
                    }
                });
            }

            @Override
            public void requestFail(String obj) {

            }
        });
    }

}
