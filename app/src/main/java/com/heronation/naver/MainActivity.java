package com.heronation.naver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import static android.os.Build.ID;

public class MainActivity extends AppCompatActivity {
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeNaverAPI();
    }

    private void InitializeNaverAPI() {
        final OAuthLogin mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                this,
                "Ec32AXdnypaBwF9iWGlh",
                "30AN_mH5cg",
                "Heronation"
        );

        // 네이버 로그인 버튼 리스너 등록
        OAuthLoginButton naverLoginButton = (OAuthLoginButton) findViewById(R.id.button_naverlogin);

        naverLoginButton.setOAuthLoginHandler(new OAuthLoginHandler() {

            @Override

            public void run(boolean b) {
                if (!flag) {
                    if (b) {
                        final String token = mOAuthLoginModule.getAccessToken(MainActivity.this);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                            if (!flag) {
                                String response = mOAuthLoginModule.requestApi(MainActivity.this, token, "https://openapi.naver.com/v1/nid/me");
                                try {
                                    JSONObject json = new JSONObject(response);
                                    Log.i("result : ", response);
                                    // response 객체에서 원하는 값 얻어오기
                                    String email = json.getJSONObject("response").getString("email");
                                    // 액티비티 이동 등 원하는 함수 호출

                                    flag = true;
                                    Log.i("flag : ", flag + "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                            }
//                            else {
//                                Log.i("asdfsadfsdaf....", "  ");
//                                mOAuthLoginModule.logout(MainActivity.this);
//                                Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
//                            }
                            }
                        }).start();
                        Toast.makeText(getApplicationContext(), "success login", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } else {
                    mOAuthLoginModule.logout(MainActivity.this);
                    Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
            }

        });

    }

}
