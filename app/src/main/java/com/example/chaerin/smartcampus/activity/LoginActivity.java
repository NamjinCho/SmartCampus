package com.example.chaerin.smartcampus.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.chaerin.smartcampus.R;

/**
 * Created by ChaeRin on 2016-07-03.
 * 로그인
 */
public class LoginActivity extends Activity {
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnLinkToAlterpassword;
    private EditText inputStudentId;
    private EditText inputPassword;
    private Switch auto_login;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
   // private ProgressBar mRegistrationProgressBar;
    private String token;
    public SharedPreferences sf;
    String[] campus = {
            "글로벌 캠퍼스", "메디컬 캠퍼스"
    };
    Spinner s1; //campus spinner
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

/**
 * TODO : 수정하기
                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);

                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    token = intent.getStringExtra("token");
                    Log.d("토큰 = ", token);
                }
*/
            }
        };
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        s1 = (Spinner) findViewById(R.id.login_campus);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, campus);
        s1.setAdapter(adapter); //캠퍼스 선택하는 스피너 정의.

        inputStudentId = (EditText) findViewById(R.id.stu_id);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        auto_login = (Switch) findViewById(R.id.auto_login);
        btnLinkToAlterpassword = (Button)findViewById(R.id.btnLinkToAlterPassword);
        sf=getSharedPreferences("smart",MODE_PRIVATE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("ID",inputStudentId.getText().toString());
                editor.commit();
                startActivity(new Intent(LoginActivity.this,SplashActivity.class));
            }
        });
    }
    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
