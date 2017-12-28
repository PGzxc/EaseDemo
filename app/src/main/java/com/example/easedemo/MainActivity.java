package com.example.easedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.easedemo.fragment.SettingsFragment;
import com.example.easedemo.utils.RxPermisionUtils;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_conversation)
    Button btnConversation;
    @BindView(R.id.unread_msg_number)
    TextView unreadMsgNumber;
    @BindView(R.id.btn_container_conversation)
    RelativeLayout btnContainerConversation;
    @BindView(R.id.btn_address_list)
    Button btnAddressList;
    @BindView(R.id.btn_container_address_list)
    RelativeLayout btnContainerAddressList;
    @BindView(R.id.btn_setting)
    Button btnSetting;
    @BindView(R.id.btn_container_setting)
    RelativeLayout btnContainerSetting;
    @BindView(R.id.main_bottom)
    LinearLayout mainBottom;
    @BindView(R.id.fragment_container)
    RelativeLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化操作
     */
    private Button[] mTabs;
    private EaseConversationListFragment conversationListFragment;
    private EaseContactListFragment contactListFragment;
    private SettingsFragment settingFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;

    private void initData() {
        mTabs = new Button[3];
        mTabs[0] = btnConversation;
        mTabs[1] = btnAddressList;
        mTabs[2] = btnSetting;
        // set first tab as selected
        mTabs[0].setSelected(true);

        conversationListFragment = new EaseConversationListFragment();
        contactListFragment = new EaseContactListFragment();
        settingFragment = new SettingsFragment();

        //获取联系人列表，设置到界面
        contactListFragment.setContactsMap(getContacts());

        //设置点击事件
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });
        fragments = new Fragment[]{conversationListFragment, contactListFragment, settingFragment};
        // add and show first fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
                .add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(conversationListFragment)
                .commit();
    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.btn_conversation, R.id.btn_address_list, R.id.btn_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation://聊天记录
                index = 0;
                break;
            case R.id.btn_address_list://好友列表
                index = 1;
                break;
            case R.id.btn_setting://设置界面
                index = 2;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab as selected.
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    /**
     * prepared users, password is "123456"
     * you can use these user to test
     * @return
     */
    private Map<String, EaseUser> getContacts() {
        Map<String, EaseUser> contacts = new HashMap<>();
        for(int i = 1; i <= 9; i++){
            EaseUser user = new EaseUser("user" + i);
            contacts.put("user" + i, user);
        }
        return contacts;
    }
}
