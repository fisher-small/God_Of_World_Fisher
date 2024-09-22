package com.yutiandi.qqcommon;/**
 * @author 于于于于于
 * @date 2024/9/1813:14QQProject
 */

/**
 * <h3>QQProject</h3>
 * <p>表示消息类型</p>
 **/
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1";//表示登录成功
    String MESSAGE_LOGIN_FAIL = "2";//表示登录失败
    String MESSAGE_COMM_MES = "3";//普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4";//要求返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6";//客户端请求退出
    String MESSAGE_GROUP_MES = "7";//群体消息
    String MESSAGE_FILE = "8";//发送文件
    String MESSAGE_NEWS = "9";//服务器推送新闻
    String MESSAGE_OFF_MES = "0";
}
