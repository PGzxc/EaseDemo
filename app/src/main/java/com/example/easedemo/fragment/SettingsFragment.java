package com.example.easedemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.easedemo.LoginActivity;
import com.example.easedemo.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;


/**
 * 设置
 */

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.btn_logout).setOnClickListener(v -> EMClient.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String error) {
            }
        }));
    }
}
