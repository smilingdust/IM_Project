package com.lib_im.pro.im.client;

import android.content.Context;
import android.util.Log;

import com.lib_im.pro.LiteChat;
import com.lib_im.pro.db.BaseDao;
import com.lib_im.pro.db.DataBaseHelper;
import com.lib_im.pro.entity.GroupContact;
import com.lib_im.pro.entity.UserInfo;
import com.lib_im.pro.im.config.ChatCode;
import com.lib_im.pro.im.config.XmppTool;
import com.lib_im.pro.im.listener.OnLoginListener;
import com.lib_im.pro.im.manager.connect.ConnectionManager;
import com.lib_im.pro.im.manager.connect.IMConnectionManager;
import com.lib_im.pro.im.manager.contact.ContactManager;
import com.lib_im.pro.im.manager.contact.IMContactManger;
import com.lib_im.pro.im.manager.group.GroupContactManager;
import com.lib_im.pro.im.manager.group.IMGroupContactManger;
import com.lib_im.pro.im.manager.message.ChatMsgManager;
import com.lib_im.pro.im.manager.message.IMChatMsgManager;
import com.lib_im.pro.im.manager.message.IMSessionManager;
import com.lib_im.pro.im.manager.message.SessionManager;
import com.lib_im.pro.im.manager.notify.IMNotifyManager;
import com.lib_im.pro.im.manager.notify.IMPushManager;
import com.lib_im.pro.im.manager.notify.NotifyManager;
import com.lib_im.pro.im.manager.notify.PushManager;
import com.lib_im.pro.rx.SimpleListObserver;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.reactivex.annotations.NonNull;
import retrofit2.Retrofit;

/**
 * Created by songgx on 2017/9/21.
 * 聊天客户端
 */

public class IMChatClient implements ChatClient<Context> {

    private static final String TAG = "IMChatClient";
    private Context mContext;
    private AbstractXMPPConnection connection;
    private HashMap<String, String> configMap = new HashMap<>();

    /**
     * 连接管理
     */
    private IMConnectionManager connectManager;

    /**
     * 联系人管理
     */
    private IMContactManger contactManger;
    /**
     * 聊天管理器、消息接收管理器
     */
    private IMChatMsgManager chatManager;

    /**
     * 会话管理器
     */
    private IMSessionManager sessionManager;

    /**
     * 群组通讯录管理器
     */
    private IMGroupContactManger groupContactManager;

    private IMNotifyManager notifyManager;

    /**
     * 业务通知管理器
     * @param context
     */
    private IMPushManager pushManager;

    @Override
    public void init(Context context) {
        this.mContext = context;
        //TODO 初始化每个管理器对象
        //----创建模块
        connectManager = new IMConnectionManager(mContext);
        chatManager = new IMChatMsgManager(mContext);
        contactManger = new IMContactManger(mContext);
        notifyManager = new IMNotifyManager(mContext);
        pushManager=new IMPushManager(mContext);
        sessionManager = new IMSessionManager(mContext);
        groupContactManager = new IMGroupContactManger(mContext);
    }

    /**
     * 初始化聊天模块相关
     */
    @Override
    public void initChatAbout() {
        //TODO 初始化每个管理器需要的聊天模块
        connectManager.initIm();
        chatManager.initIm();
        contactManger.initIm();
        notifyManager.initIm();
        sessionManager.initIm();
        groupContactManager.initIm();
    }

    /**
     * 获取上下文对象
     *
     * @return mContext
     */
    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void login(String userName, String passWord, OnLoginListener onLoginListener) {
        UserInfo userInfo= LiteChat.chatCache.readObject(ChatCode.KEY_USER_INFO,UserInfo.class);
        setConfig(ChatCode.KEY_USER_NICK_NAME,userInfo.getNickName());
        setConfig(ChatCode.KEY_USER_ID, userName);
        setConfig(ChatCode.KEY_USER_NAME, userName);
        setConfig(ChatCode.KEY_USER_PASS, passWord);
        initManager();
        loginXmpp(userName,passWord,onLoginListener);
    }

    /**
     * 登录
     *
     * @param userName        用户名
     * @param passWord        密码
     * @param onLoginListener 登录回调接口
     */
    @Override
    public void loginXmpp(String userName, String passWord, OnLoginListener onLoginListener) {
        connection = XmppTool.getInstance().getConnection();
        if (connection != null) {
            try {
                if (connection.isConnected()) {// 首先判断是否还连接着服务器，需要先断开
                    try {
                        connection.disconnect();
                    } catch (Exception e) {
                        Log.d(TAG, "conn.disconnect() failed: " + e);
                    }
                }
                connection.connect();
                if (!connection.isConnected()) {
                    Log.d(TAG, "SMACK connect failed without exception!");
                }
                if (!connection.isAuthenticated()) {
                    connection.login(userName, passWord);
                }
                if (onLoginListener != null) {
                    //添加connectionLisenter监听
                    initChatAbout();
                    setOnLine();
                    onLoginListener.OnLoginSuccess();
                }
            } catch (XMPPException e) {
                e.printStackTrace();
                if (onLoginListener != null) {
                    onLoginListener.OnLoginFailed(e.getMessage());
                }
            } catch (SmackException e) {
                e.printStackTrace();
                if (onLoginListener != null) {
                    onLoginListener.OnLoginFailed(e.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (onLoginListener != null) {
                    onLoginListener.OnLoginFailed(e.getMessage());
                }
            }
        } else {
            if (onLoginListener != null) {
                onLoginListener.OnLoginFailed(ChatCode.CHAT_UN_CONNECT);
            }
        }
    }

    /**
     * 服务器如果是连接状态，则可执行自动登录，不再执行login操作
     */
    @Override
    public void autoLogin() {
        connectManager.initIm();
        initManager();
    }

    /**
     * 登出
     */
    @Override
    public void logout() {
        try {
            configMap.clear();
            LiteChat.chatCache.deleteAllValue();
            connectManager.removeXmppConnectListener();
            chatManager.removeChatAboutListener();
            connection.disconnect(new Presence(Presence.Type.unavailable));
            connection = null;
            ChatCode.conMap.clear();
            notifyManager.cancelNotation();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        LiteChat.chatClient.getChatManger().removeRoomChatMap();
    }

    @Override
    public boolean isLogin() {
        boolean isLoginIn = false;
        if (connection != null) {
            boolean isConnect = connection.isConnected();
            isLoginIn = isConnect && connection.isAuthenticated();
        }
        return isLoginIn;
    }

    @Override
    public void setConfig(String key, String value) {
        configMap.put(key, value);
    }

    @Override
    public String getConfig(String key) {
        return configMap.get(key);
    }

    @Override
    public ChatMsgManager getChatManger() {
        return chatManager;
    }

    @Override
    public ConnectionManager getConnectManager() {
        return connectManager;
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public GroupContactManager getGroupContactManager() {
        return groupContactManager;
    }

    @Override
    public NotifyManager getNotifyManager() {
        return notifyManager;
    }

    @Override
    public ContactManager getContactManager() {
        return contactManger;
    }

    @Override
    public PushManager getPushManager() {
        return pushManager;
    }

    /**
     * 加入聊天室，加入群组
     */
    @Override
    public void joinGroupRoom() {
        final String nickName = getConfig(ChatCode.KEY_USER_NICK_NAME);
        final String userId = getConfig(ChatCode.KEY_USER_ID);
        /**获取聊天室信息,将userName作为加入房间后自己的昵称*/
        //TODO 逻辑原理是先从网络获取到群组列表，然后通过与本地数据匹配进行更新操作，更新完成进行加入聊天室操作
        LiteChat.imRequestManager.getListInstance().queryGroupContact("1").subscribe(new SimpleListObserver<GroupContact>() {
            @Override
            public void onNext(@NonNull List<GroupContact> groupContacts) {
                if (groupContacts != null) {
                    BaseDao<GroupContact> dao = new BaseDao<>(DataBaseHelper.getInstance(mContext), GroupContact.class);
                    for (GroupContact groupContact : groupContacts) {
                        groupContact.setAccount(userId);
                        GroupContact table = groupContactManager.getGroupContact(groupContact.getGroupID());
                        if (table != null) {
                            dao.delete(table);
                        }
                        dao.add(groupContact);
                        chatManager.initMultiRoom(groupContact.getGroupJid(), nickName);
                    }
//                    chatManager.readOfflineMessage();
                    //setOnLine();
                    groupContactManager.loadRoomHistoryMsg(groupContacts);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //setOnLine();
            }
        });

    }

    /**
     * 初始化管理器
     */
    @Override
    public void initManager() {
        String _user = getConfig(ChatCode.KEY_USER_NAME);
        notifyManager.init();
        chatManager.init();
        contactManger.init();
        groupContactManager.init();
        sessionManager.init();

        chatManager.setCurrentUser(_user);
        sessionManager.setCurrentUser(_user);
        groupContactManager.setCurrentUser(_user);
        contactManger.setCurrentUser(_user);
    }

    /**
     * 设置在线
     */
    @Override
    public void setOnLine() {
        try {
            Presence presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.chat);//设置为"可聊天"以区分状态
            XmppTool.getInstance().getConnection().sendStanza(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置openfire参数
     * @param host
     * @param port
     * @param serverName
     */
    @Override
    public void setOpenfireServer(String host, int port, String serverName) {
        ChatCode.XMPP_SERVER=host;
        ChatCode.XMPP_PORT=port;
        ChatCode.XMPP_SERVER_NAME=serverName;
    }
}
