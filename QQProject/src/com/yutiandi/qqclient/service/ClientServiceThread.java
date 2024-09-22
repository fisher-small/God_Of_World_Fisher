package com.yutiandi.qqclient.service;/**
 * @author 于于于于于
 * @date 2024/9/1820:38QQProject
 */

import com.yutiandi.qqcommon.Message;
import com.yutiandi.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * <h3>QQProject</h3>
 * <p>客户端服务层线程</p>
 **/
public class ClientServiceThread extends Thread{
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ClientServiceThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务端通信,因此要用while循环
        while(true){

            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务端没有发送Message对象,线程会阻塞在这里
                Message message = (Message) ois.readObject();

                //如果读到的是拉取用户列表
                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    //取出在线列表信息,并显示
                    //规定

                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("======当前在线用户列表======");
                    for (int i = 0;i<onlineUsers.length;i++){
                        System.out.println("用户: "+onlineUsers[i]);
                    }
                }
                if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    //用户要下机
                    socket.close();
                    break;
                }
                if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    //用户发信息
                    System.out.println("\n"+message.getSender()+"说: "+message.getContent());
                }
                if (message.getMesType().equals(MessageType.MESSAGE_GROUP_MES)){
                    //群发消息
                    System.out.println("\n"+message.getSender()+"说: "+message.getContent());
                }
                if (message.getMesType().equals(MessageType.MESSAGE_FILE)){
                    System.out.println("\n"+message.getSender()+"发送文件"+message.getSrc()+"给"+message.getGetter()+"到目录"+message.getDest());
                    //取出message的文件字节数组,通过文件输出流写出到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("保存文件成功~~~");
                }
                if (message.getMesType().equals(MessageType.MESSAGE_NEWS)){
                    System.out.println("\n"+"服务器大人说: "+message.getContent());
                }


            } catch (IOException | ClassNotFoundException e) {
                System.out.println("出bug了");
                throw new RuntimeException(e);
            }
        }

    }
}



