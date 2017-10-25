package com.lib_im.pro.im.manager.notify;

import java.util.Map;

/**
 * Created by songgx on 2017/8/29.
 * 创建业务通知管理器
 */

public interface PushManager {
    /**
     * 是否开启震动提醒
     * @param _vibrate
     */
    void setVibrate(boolean _vibrate);

    /**
     * 是否开启铃声提醒
     * @param _bell
     */
    void setBell(boolean _bell);

    /**
     * 设置提醒数据
     * @param appName
     * @param iconId
     * @param action
     */
    void setNotifyLink(String appName, int iconId, String action, Class<?> peddingClass);


    /**
     * 播放业务推送通知
     * @param
     */
    void playChatMessage(Map<String,String> map,String textContent);

    /**
     * @descript 取消通知栏
     *
     */
    void cancelNotation();
}
