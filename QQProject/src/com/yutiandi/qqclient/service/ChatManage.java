package com.yutiandi.qqclient.service;/**
 * @author 于于于于于
 * @date 2024/9/2111:33QQProject
 */

import com.yutiandi.qqcommon.Message;
import com.yutiandi.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * <h3>QQProject</h3>
 * <p>管理用户聊天的类</p>
 **/
public class ChatManage {
    public void privateChat(String content,String senderId, String getterId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);//普通消息类型
        message.setGetter(getterId);
        message.setSender(senderId);
        message.setContent(content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void groupChat(String content,String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GROUP_MES);//群发消息类型
        message.setSender(senderId);
        message.setContent(content);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
