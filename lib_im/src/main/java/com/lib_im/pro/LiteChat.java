package com.lib_im.pro;

import com.lib_im.pro.api.IMRequestManager;
import com.lib_im.pro.db.Cache;
import com.lib_im.pro.im.client.ChatClient;
import com.lib_im.pro.retrofit.config.RetrofitClient;
import com.lib_im.pro.retrofit.upload.UpLoadManager;

/**
 * Created by songgx on 2017/9/21.
 * 静态顶级类
 */

public class LiteChat {

    /**
     * 聊天模块静态顶级类
     */
    public static ChatClient chatClient;

    /**
     * 数据库表缓存模块
     */
    public static Cache chatCache;

    /**
     * 网络请求客户端
     */
    public static RetrofitClient retrofitClient;
    /**
     * 文件上传管理类
     */
    public static UpLoadManager upLoadManager;
    /**
     * 接口服务请求
     */
    public static IMRequestManager imRequestManager;
}
