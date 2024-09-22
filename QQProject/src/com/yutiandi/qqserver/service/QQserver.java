package com.yutiandi.qqserver.service;/**
 * @author 于于于于于
 * @date 2024/9/2014:45QQProject
 */

import com.yutiandi.qqclient.service.ManageClientThread;
import com.yutiandi.qqcommon.Message;
import com.yutiandi.qqcommon.MessageType;
import com.yutiandi.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>QQProject</h3>
 * <p>服务端服务层</p>
 **/
public class QQserver {
    private ServerSocket ss = null;
    private Socket socket;
    private boolean loop = true;

    private static ConcurrentHashMap<String, ArrayList<Message>>offChat = new ConcurrentHashMap<>();

    //创建一个集合模拟数据库
    private static ConcurrentHashMap<String ,User> validUser = new ConcurrentHashMap<>();
    static{
        System.out.println("静态代码块调用了");
        validUser.put("100",new User("100","123456"));
        validUser.put("200",new User("200","1234567"));
        validUser.put("300",new User("300","12345678"));
        validUser.put("400",new User("400","123456789"));
        validUser.put("卡特琳娜",new User("卡特琳娜","123"));
        validUser.put("永恩",new User("永恩","1234"));
        validUser.put("阿兹尔",new User("阿兹尔","12345"));
    }



    public static void setOffChat(String userId, Message message) {
        ArrayList<Message> messages = offChat.get(userId);
        if (messages == null) {
            messages = new ArrayList<>();
            offChat.put(userId, messages);
        }
        messages.add(message);
    }

    //验证用户是否有效合法
    private boolean checkUser(String userId,String password){
        User user = validUser.get(userId);
        if(user==null){
            return false;
        }
        if(!user.getPassword().equals(password)){
            return false;
        }
        return true;
    }
    public QQserver() {
        try {
            System.out.println("服务端在9999端口监听...");
            new Thread(new SendNews()).start();
            ss = new ServerSocket(9999);
            while (loop) {
                socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                User u = (User)ois.readObject();
                Message message = new Message();
                //验证
                if (u.getFlag().equals("1")) {
                    if (checkUser(u.getUserId(), u.getPassword())) {

                        if (!(offChat.get(u.getUserId()) == null)) {
                            ArrayList<Message> messages = offChat.get(u.getUserId());
                            if (!messages.isEmpty()) {
                                for (Message mes : messages) {
                                    oos.writeObject(mes);
                                }

                            }
                        }
                        message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                        //将Message信息返回给客户端
                        oos.writeObject(message);
                        //开启通信线程
                        QQserviceThread thread = new QQserviceThread(socket, u.getUserId());
                        thread.start();
                        //把线程放在集合中管理
                        ManageServerThread.addServiceThread(u.getUserId(), thread);

                    } else {
                        //登录失败
                        System.out.println("接收用户id" + u.getUserId() + "\t用户密码" + u.getPassword() + "\t验证不通过");
                        message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                        oos.writeObject(message);
                        socket.close();
                    }
                }else if (u.getFlag().equals("2")){
                        System.out.println("添加用户: " + u.getUserId());
                        validUser.put(u.getUserId(),new User(u.getUserId(),u.getPassword()));
                        validUser.forEach((userId, user) -> {
                            System.out.println("User ID: " + userId + ", User: " + user);
                        });
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将Message信息返回给客户端
                    oos.writeObject(message);
                    //开启通信线程
                    QQserviceThread thread = new QQserviceThread(socket, u.getUserId());
                    thread.start();
                    //把线程放在集合中管理
                    ManageServerThread.addServiceThread(u.getUserId(), thread);

                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
