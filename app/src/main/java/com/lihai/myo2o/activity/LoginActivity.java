package com.lihai.myo2o.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lihai.common.base.BaseActivity;
import com.lihai.myo2o.R;
import com.lihai.myo2o.fragment.MineFragment;
import com.lihai.request.utils.RequestUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiHai on 2017/3/23.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    TextView register_text, findPassword_text;//快速注册    找回密码
    EditText loginName,password;   //登录账号   密码
    Button login_btn;  //登录按钮



    @Override
    protected int setLayout() {
        return R.layout.layout_login_activity;
    }

    @Override
    protected void initView() {



        loginName = (EditText) findViewById(R.id.loginName);
        password = (EditText) findViewById(R.id.password);
        login_btn = (Button) findViewById(R.id.log_in);
        login_btn.setOnClickListener(this);

        register_text = (TextView) findViewById(R.id.register);
        register_text.setOnClickListener(this);

        findPassword_text = (TextView) findViewById(R.id.findPassword);
        findPassword_text.setOnClickListener(this);






    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.register:   //快速注册
                Intent intentRegister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.findPassword:    //找回密码
                Intent intentFindPassword = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intentFindPassword);
                break;
            case R.id.log_in:   //登录
                loginRequest();
                break;
        }

    }



    public void loginRequest(){

        String login_name = loginName.getText().toString();  //15915789358
        String login_password = password.getText().toString();  //123456
        //登录
        Map<String , String> loginParam = new HashMap<String ,String>();
        loginParam.put("phone",login_name);
        loginParam.put("password",login_password);
        String URL = "http://testwxys.rhy.com/out/login";
        RequestUtil.post(URL,loginParam, null, false, new RequestUtil.MyCallBack() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                int status = Integer.parseInt(result.get("status") + "");
                if (status == 1){   //登录成功

                      finish();

                }else {
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}
