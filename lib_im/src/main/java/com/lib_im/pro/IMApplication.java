package com.lib_im.pro;

import android.app.Application;

import com.lib_im.pro.api.IMListRequest;
import com.lib_im.pro.db.TableCache;
import com.lib_im.pro.im.client.IMChatClient;
import com.lib_im.pro.im.manager.notify.NotifyManager;
import com.lib_im.pro.retrofit.config.CommonRetrofit;
import com.lib_im.pro.ui.chat.ChatActivity;
import com.lib_im.pro.utils.Utils;

import java.util.HashMap;

/**
 * Created by songgx on 2017/9/28.
 * 单独调试
 */

public class IMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //工具类初始化
        Utils.init(this);
        //聊天模块初始化
        LiteChat.chatClient =new IMChatClient();
        LiteChat.chatClient.init(this);
        LiteChat.chatClient.setOpenfireServer("192.168.253.7",5222,"127.0.0.1");
        LiteChat.chatCache =new TableCache(this);

        //聊天消息推送
        NotifyManager notifyManager = LiteChat.chatClient.getNotifyManager();
        if (notifyManager != null) {
            String _appName = getString(R.string.app_name);
            notifyManager.setNotifyLink(_appName, android.R.mipmap.sym_def_app_icon, "", ChatActivity.class);
            notifyManager.setBell(Boolean.TRUE);
            notifyManager.setVibrate(Boolean.FALSE);
        }
        //网络请求
        LiteChat.retrofitClient=new CommonRetrofit(10*1000,"http://192.168.253.7:8089/api/",new HashMap<String, String>());
        LiteChat.imRequestManager=new IMListRequest();//初始化为返回list结构请求对象

    }
}
