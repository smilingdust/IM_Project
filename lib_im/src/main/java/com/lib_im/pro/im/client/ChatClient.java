package com.lib_im.pro.im.client;
import com.lib_im.pro.im.listener.OnLoginListener;
import com.lib_im.pro.im.manager.connect.ConnectionManager;
import com.lib_im.pro.im.manager.contact.ContactManager;
import com.lib_im.pro.im.manager.group.GroupContactManager;
import com.lib_im.pro.im.manager.message.ChatMsgManager;
import com.lib_im.pro.im.manager.message.SessionManager;
import com.lib_im.pro.im.manager.notify.NotifyManager;
import com.lib_im.pro.im.manager.notify.PushManager;

/**
 * Created by songgx on 2017/9/1.
 * 聊天客户端
 */

public interface ChatClient<T> {
    /**
     * 初始化加载
     */
    void init(T context);

    /**
     * 初始化聊天相关对象
     */
    void initChatAbout();

    /**
     * 获取上下文对象
     * @return t
     */
    T getContext();

    /**
     * 登录
     * @param userName
     * @param passWord
     * @param onLoginListener
     */
    void login(String userName, String passWord, OnLoginListener onLoginListener);
    /**
     * 登录方法
     * @param userName 用户名
     * @param passWord 密码
     * @param onLoginListener 登录接口
     */
    void loginXmpp(String userName, String passWord, OnLoginListener onLoginListener);

    /**
     * 自动登录
     */
    void autoLogin();

    /**
     * 登出
     */
    void logout();

    /**
     * 是否登录
     */
    boolean isLogin();

    /**
     * 保存配置
     */
    void setConfig(String key,String value);
    /**
     * 提取配置
     */
    String getConfig(String key);

    /**
     * 聊天管理器、消息接收管理器
     *
     * @return
     */
     ChatMsgManager getChatManger();


    /**
     * 连接管理器
     *
     * @return
     */
    ConnectionManager getConnectManager();

    /**
     * 聊天、群组聊天 回话管理器
     *
     * @return
     */
    SessionManager getSessionManager();

    /**
     * 获取群组通讯录
     *
     * @return
     */
    GroupContactManager getGroupContactManager();


    /**
     * 通知管理器,负责未读消息、群组消息提醒
     *
     * @return
     */
    NotifyManager getNotifyManager();

    /**
     * 联系人管理器，负责联系人的获取更新，删除，添加
     */
   ContactManager getContactManager();

    /**
     * 业务推送通知管理器
     * @return
     */
    PushManager getPushManager();


    /**
     * @descript 加入聊天室
     */
    void joinGroupRoom();

    /**
     * 初始化管理器
     */
    void initManager();

    /**
     * 设置用户在线状态
     */
    void setOnLine();

    /**
     * 设置xmpp IP,端口号，服务器名称
     */
    void setOpenfireServer(String host,int port,String serverName);

}
