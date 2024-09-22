package com.yutiandi.qqserver.service;/**
 * @author 于于于于于
 * @date 2024/9/2015:04QQProject
 */

import com.yutiandi.qqcommon.Message;
import com.yutiandi.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <h3>QQProject</h3>
 * <p>服务端通信客户端线程</p>
 **/
public class QQserviceThread extends Thread{
    private Socket socket;
    private String userId;
    private boolean loop = true;
    public QQserviceThread(Socket socket,String userId){
        this.socket = socket;
        this.userId = userId;
    }
    @Override
    public void run(){
        while(loop){
            System.out.println("服务端和客户端保持通信...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Message o = (Message) ois.readObject();

                //如果读取到的是 客户要拉取在线用户列表
                if(o.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(o.getSender()+"需要拉取用户列表");
                    String onlineUser = ManageServerThread.getOnlineUser();
                    Message message = new Message();
                    message.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message.setContent(onlineUser);
                    message.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerThread.getThread(o.getSender()).getSocket().getOutputStream());
                    oos.writeObject(message);
                    continue;
                }

                //如果读取到的是 客户端要关闭
                if(o.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    Message message = new Message();
                    message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
                    message.setGetter(o.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerThread.getThread(o.getSender()).getSocket().getOutputStream());
                    oos.writeObject(message);
                    ManageServerThread.removeServerThread(o.getSender());
                    loop = false;
                    this.socket.close();
                    System.out.println("用户: "+o.getSender()+"已下机成功!!!");
                    continue;
                }
                if (o.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //转发给接收客户端
                    QQserviceThread thread = ManageServerThread.getThread(o.getGetter());
                    if (thread == null) {
                        System.out.println("当前用户已经离线!!!");
                        System.out.println("自动转成留言");
                        o.setMesType(MessageType.MESSAGE_OFF_MES);
                        QQserver.setOffChat(o.getGetter(),o);
                        Message m = new Message();
                        m.setContent("\n当前用户处于离线状态...自动留言...");
                        m.setMesType(MessageType.MESSAGE_COMM_MES);
                        m.setGetter(o.getSender());
                        ObjectOutputStream oos1 = new ObjectOutputStream(ManageServerThread.getThread(m.getGetter()).getSocket().getOutputStream());
                        m.setSender("服务器 ");
                        oos1.writeObject(m);
                    } else {
                        ObjectOutputStream oos1 = new ObjectOutputStream(thread.getSocket().getOutputStream());
                        oos1.writeObject(o);
                    }
                    continue;
                }
                if (o.getMesType().equals(MessageType.MESSAGE_GROUP_MES)){
                    //获取hm集合
                    HashMap<String,QQserviceThread>hm = ManageServerThread.getHm();
                    Iterator<String>iterator = hm.keySet().iterator();
                    while(iterator.hasNext()){
                        String onlineUserId = iterator.next().toString();
                        if(!onlineUserId.equals(o.getSender())){
                            new ObjectOutputStream(hm.get(onlineUserId).getSocket().getOutputStream()).writeObject(o);
                        }
                    }
                }
                if (o.getMesType().equals(MessageType.MESSAGE_FILE)){
                    QQserviceThread thread = ManageServerThread.getThread(o.getGetter());
                    if (thread == null) {
                        System.out.println("当前用户已经离线!!!");
                        o.setContent("\n当前用户处于离线状态...");
                        ObjectOutputStream oos1 = new ObjectOutputStream(ManageServerThread.getThread(o.getSender()).getSocket().getOutputStream());
                        o.setSender("服务器 ");
                        oos1.writeObject(o);
                    } else {
                        ObjectOutputStream oos1 = new ObjectOutputStream(thread.getSocket().getOutputStream());
                        oos1.writeObject(o);
                    }
                    continue;
                }
                System.out.println("别急别急功能还没实现~~~");

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public Socket getSocket(){
        return this.socket;
    }
}
