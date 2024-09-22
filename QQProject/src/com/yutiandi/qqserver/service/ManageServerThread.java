package com.yutiandi.qqserver.service;/**
 * @author 于于于于于
 * @date 2024/9/2015:19QQProject
 */

import java.util.HashMap;
import java.util.Iterator;

/**
 * <h3>QQProject</h3>
 * <p>管理服务端线程</p>
 **/
public class ManageServerThread {
    private static HashMap<String,QQserviceThread> hm = new HashMap<>();

    //添加线程对象到hm集合
    public static void addServiceThread(String userId,QQserviceThread thread){
        hm.put(userId , thread);
    }
    public static QQserviceThread getThread(String userId){
        return hm.get(userId);
    }
    public static String getOnlineUser(){
       //集合遍历,遍历hm集合
        String onlineList = "";
        Iterator<String>iterator = hm.keySet().iterator();
        while(iterator.hasNext()){
            onlineList+=iterator.next().toString()+" ";
        }
        return onlineList;
    }
    public static void removeServerThread(String userId){
        hm.remove(userId);
    }
    //返回hm
    public static HashMap<String,QQserviceThread>getHm(){
        return hm;
    }
}
