package com.example.easedemo.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.easedemo.LoginActivity;
import com.hyphenate.EMError;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;

import java.util.Locale;

/**
 * 处理错误码
 */

public class HandleError {
    private Activity mContext;

    public  HandleError(Activity context) {
        this.mContext = context;
    }

    public void handleError(HyphenateException e) {
        mContext.runOnUiThread(() -> {
            /**
             * 关于错误码可以参考官方api详细说明
             * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
             */
            int errorCode = e.getErrorCode();
            String message = e.getMessage();
            Logger.d(String.format(Locale.CHINA, "sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
            switch (errorCode) {
                // 网络错误
                case EMError.NETWORK_ERROR:
                    Toast.makeText(mContext, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                    break;
                // 用户已存在
                case EMError.USER_ALREADY_EXIST:
                    Toast.makeText(mContext, "用户已存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                    break;
                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                case EMError.USER_ILLEGAL_ARGUMENT:
                    Toast.makeText(mContext, "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                    break;
                // 服务器未知错误
                case EMError.SERVER_UNKNOWN_ERROR:
                    Toast.makeText(mContext, "服务器未知错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                    break;
                case EMError.USER_REG_FAILED:
                    Toast.makeText(mContext, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(mContext, "ml_sign_up_failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }
}
