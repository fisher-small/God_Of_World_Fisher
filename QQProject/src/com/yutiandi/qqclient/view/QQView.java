package com.yutiandi.qqclient.view;/**
 * @author 于于于于于
 * @date 2024/9/1814:43QQProject
 */

import com.yutiandi.qqclient.service.ChatManage;
import com.yutiandi.qqcommon.User;
import com.yutiandi.qqclient.service.UserClientService;
import com.yutiandi.qqclient.util.Utility;
import com.yutiandi.qqserver.service.QQserver;

import java.io.IOException;
import java.net.Socket;

/**
 * <h3>QQProject</h3>
 * <p>客户端的菜单界面</p>
 **/
public class QQView {
    private boolean loop = true;
    private Socket socket;
    private String key ="";
    public static void main(String []args){
        new QQView().mainMenu();
    }
    private int i = 0;
    private void mainMenu(){
        while(loop){
            System.out.println("=========欢迎登录网络通信系统=========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 2 注册系统");
            System.out.println("\t\t 9 退出系统");
            UserClientService userClientService = new UserClientService();
            key = Utility.readString(1);
            switch (key){
                case"1":
                    System.out.println("登录系统");
                    System.out.println("请输入用户号: ");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密码: ");
                    String password = Utility.readString(50);
                    //验证是否正确

                    User user = new User(userId,password);


                    //如果正确进入二级菜单
                    if( userClientService.checkUser(userId,password,"1")){
                        System.out.println("===================欢迎(用户"+userId+")====================");
                        while(loop){
                            System.out.println("\n===========网络通信系统二级菜单(用户 "+userId+")================");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择: ");
                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    //System.out.println("显示用户列表");
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("群发消息");
                                    System.out.println("请输入你想群发的消息: ");
                                    String content1 = Utility.readString(100);
                                    userClientService.groupChat(content1);
                                    break;
                                case "3":
                                    System.out.println("私聊消息");
                                    System.out.println("请你输入你想通信的用户id(在线): ");
                                    String getterId = Utility.readString(50);
                                    System.out.println("发送你想说的话(100字以内): ");
                                    String content = Utility.readString(100);
                                    userClientService.privateChat(content,getterId);
                                    break;
                                case "4":
                                    System.out.println("发送文件");
                                    System.out.println("请输入你想把文件发送给的用户id(在线):");
                                    getterId = Utility.readString(50);
                                    System.out.println("请输入发送文件的路径(形式 d:\\xx.jpg)");
                                    String src = Utility.readString(100);
                                    System.out.println("请输入把文件发送到对应的路径(形式 d:\\yy.jpg)");
                                    String dest = Utility.readString(100);
                                    userClientService.sendFile(src,dest,getterId);
                                    break;
                                case "9":
                                    userClientService.OverClientThread();
                                    System.out.println("系统退出了偶~~");
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("好好输入别为难大家~");
                            }
                        }
                    }else {
                        System.out.println("已失败"+(++i)+"次,还剩"+(5-i)+"次机会");
                    }
                    break;
                case "2":
                    System.out.println("欢迎来到注册界面~~~");
                    System.out.println("请输入想注册的用户号: ");
                    String registUserId = Utility.readString(50);
                    System.out.println("请输入要注册的密码: ");
                    String registPassword = Utility.readString(50);
                    System.out.println("请再次输入要注册的密码: ");
                    String registPasswordAgain = Utility.readString(50);
                    if (registPassword.equals(registPasswordAgain)){
                        //判断是注册
                        userClientService.checkUser(registUserId,registPassword,"2");
                        System.out.println("注册成功~快登陆试试看吧!");
                    }else {
                        System.out.println("两次密码输入不同不合法!!!");
                    }
                    break;
                case "9":
                    System.out.println("退出系统");
                    loop = false;
                    break;
                default:
                    System.out.println("好好输入不要为难大家~");
            }
            if(i>=5){
                break;
            }
        }

    }
}
