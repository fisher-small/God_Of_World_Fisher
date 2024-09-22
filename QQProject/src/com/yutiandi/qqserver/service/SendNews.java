package com.yutiandi.qqserver.service;

import com.yutiandi.qqclient.util.Utility;
import com.yutiandi.qqcommon.Message;
import com.yutiandi.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class SendNews implements Runnable{
    @Override
    public void run(){
        //为了可以推送多次新闻，使用while
        while (true) {
            System.out.println("请输入要推送的新闻/信息[输入exit表示退出推送服务]");
            String news = Utility.readString(100);

            if (news.equals("exit")) {
                break;
            }

            //构建一个群发消息
            Message message = new Message();
            message.setSender("服务器");
            message.setMesType(MessageType.MESSAGE_NEWS);
            message.setContent(news);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人说：" + news);

            //利用迭代器，遍历当前所有的通信线程，得到socket，并发送message
            HashMap<String, QQserviceThread> hm = ManageServerThread.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onLineUserId = iterator.next().toString();
                 QQserviceThread thread = hm.get(onLineUserId);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}