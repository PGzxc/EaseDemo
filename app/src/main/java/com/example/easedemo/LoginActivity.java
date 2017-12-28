package com.example.easedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.easedemo.utils.HandleError;
import com.example.easedemo.utils.RxPermisionUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登录注册
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EMClient.getInstance().isLoggedInBefore()) {
            //enter to main activity directly if you logged in before.
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        RxPermisionUtils.reqPermision(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_register, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register://注册
                register();
                break;
            case R.id.btn_login://登录
                login();
                break;
        }
    }

    private void login() {
        Observable.just(etUsername.getText().toString().trim(), etPassword.getText().toString().trim())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.d("login==onError");
                    }

                    @Override
                    public void onNext(String str) {
                        Logger.d("login==onNext");
                        if (str.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "登录时：用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        EMClient.getInstance().login(etUsername.getText().toString(), etPassword.getText().toString(), new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String error) {
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "login failed" + error, Toast.LENGTH_LONG).show());
                            }
                        });
                    }
                });

    }

    private void register() {
        Observable.just(etUsername.getText().toString().trim(), etPassword.getText().toString().trim())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("register==onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.d("register==onError");
                    }

                    @Override
                    public void onNext(String str) {
                        if (str.isEmpty()) {
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "注册时：用户名或密码不能为空", Toast.LENGTH_SHORT).show());
                            return;
                        }
                        try {
                            String username = etUsername.getText().toString().trim();
                            String password = etPassword.getText().toString().trim();
                            Logger.d("username==" + username + "password==" + password);
                            EMClient.getInstance().createAccount(username, password);
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_LONG).show());
                        } catch (final HyphenateException e) {
                            e.printStackTrace();
                            HandleError handleError = new HandleError(LoginActivity.this);
                            handleError.handleError(e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
