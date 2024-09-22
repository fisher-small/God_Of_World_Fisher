package com.yutiandi.qqclient.service;/**
 * @author 于于于于于
 * @date 2024/9/2015:19QQProject
 */

import com.yutiandi.qqserver.service.QQserviceThread;

import java.util.HashMap;

/**
 * <h3>QQProject</h3>
 * <p>管理服务端线程</p>
 **/
public class ManageClientThread {
    private static HashMap<String, ClientServiceThread> hm = new HashMap<>();

    //添加线程对象到hm集合
    public static void addServiceThread(String userId,ClientServiceThread thread){
        hm.put(userId , thread);
    }
    public static ClientServiceThread getThread(String userId){
        return hm.get(userId);
    }
}
