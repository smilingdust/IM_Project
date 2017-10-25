package com.lib_im.pro.ui.chat;

import com.lib_im.pro.entity.ChatMessage;
import com.lib_im.pro.entity.FileUpEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by songgx on 2017/9/27.
 * 定义聊天模块数据处理模块
 */

public interface ChatHandle {

    void getHistoryMessage(String mChatUserID,boolean isRoom);

    void startRecord(String filePath, float seconds);

    void initSendMessageConfig(final ChatMessage _msg);

    void sendMessage(ChatMessage _msg);

    void sendSingleOpenFireMessageAndRefresh(final ChatMessage _msg);

    void sendRoomOpenFireMessageAndRefresh(final ChatMessage _msg);

    void repeatSendMessage(ChatMessage chatMessage);

    void getRoomRecord(List<ChatMessage> chatMessages, List<ChatMessage> mMessagesList);

    void getChatRecorder(List<ChatMessage> chatMessages, List<ChatMessage> mMessagesList);

    void UpLoadFile(String url, boolean isImage, Map<String, String> params, final LinkedList<FileUpEntity> files);

    void pullDownRefresh(List<ChatMessage> mMessagesList);
}
