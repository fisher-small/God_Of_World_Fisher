package com.yutiandi.qqclient.service;/**
 * @author 于于于于于
 * @date 2024/9/1820:18QQProject
 */

import com.yutiandi.qqclient.service.ClientServiceThread;
import com.yutiandi.qqcommon.Message;
import com.yutiandi.qqcommon.MessageType;
import com.yutiandi.qqcommon.User;
import com.yutiandi.qqserver.service.ManageServerThread;
import com.yutiandi.qqserver.service.QQserviceThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * <h3>QQProject</h3>
 * <p>客户端服务层</p>
 **/
public class UserClientService {
    private User u = new User();

    private Message message = new Message();


    //根据Id和password判断用户是否合法
    public boolean checkUser(String userId, String password,String flag) {
        u.setPassword(password);
        u.setUserId(userId);
        u.setFlag(flag);
        if (flag == "1") {
            //创建socket对象发送u数据到服务端
            try {

                Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(u);//发送u对象

                //从服务端接收message对象数据
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Message message = (Message) ois.readObject();
                while (message.getMesType().equals(MessageType.MESSAGE_OFF_MES)) {
                    System.out.println("\n" + message.getSender() + "给你留下一条离线留言:\n" + message.getContent());
                    message = (Message) ois.readObject();
                }
                if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
                    //登录成功.
                    //创建一个和服务器保持通信的线程
                    ClientServiceThread clientServiceThread = new ClientServiceThread(socket);
                    System.out.println("登录成功");
                    clientServiceThread.start();
                    //把线程放在集合中管理
                    ManageClientThread.addServiceThread(u.getUserId(), clientServiceThread);

                    return true;
                } else {
                    System.out.println("登录失败");
                    return false;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }else if (flag=="2"){
            try {

                Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(u);//发送u对象

                //从服务端接收message对象数据
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Message message = (Message) ois.readObject();
                if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
                    //登录成功.
                    //创建一个和服务器保持通信的线程
                    ClientServiceThread clientServiceThread = new ClientServiceThread(socket);
                    System.out.println("登录成功");
                    clientServiceThread.start();
                    //把线程放在集合中管理
                    ManageClientThread.addServiceThread(u.getUserId(), clientServiceThread);

                    return true;
                } else {
                    System.out.println("登录失败");
                    return false;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }




    public void onlineFriendList(){

        message.setSender(u.getUserId());
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);

        //发送给服务器

        try {
            ClientServiceThread thread = ManageClientThread.getThread(u.getUserId());
            Socket socket = thread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("拉取结束");
    }

    public void OverClientThread(){
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());
        try {
            ClientServiceThread thread = ManageClientThread.getThread(u.getUserId());
            Socket socket = thread.getSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("用户: "+u.getUserId()+"请求下机");
    }
    public void privateChat(String content,String getterId){
        new ChatManage().privateChat(content,u.getUserId(),getterId);
    }
    public void groupChat(String content){
        new ChatManage().groupChat(content,u.getUserId());
    }
    public void sendFile(String src, String dest, String getterId){
        new FileClientService().sendFile(src,dest, u.getUserId(), getterId);
    }
}
