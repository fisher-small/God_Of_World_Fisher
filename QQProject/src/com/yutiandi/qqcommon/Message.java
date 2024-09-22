package com.yutiandi.qqcommon;/**
 * @author 于于于于于
 * @date 2024/9/1813:07QQProject
 */

import java.io.Serializable;

/**
 * <h3>QQProject</h3>
 * <p>消息发送</p>
 **/
public class Message implements Serializable {
    private String sender;
    private String getter;
    private String content;//消息内容
    private String sendTime;//发送时间
    private String mesType;//消息类型
    private static final long serialVersionUID = 1l;

    private byte[] fileBytes;
    private int filelen = 0;
    private String dest;//文件传输目标地址
    private String src;//源文件路径

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFilelen() {
        return filelen;
    }

    public void setFilelen(int filelen) {
        this.filelen = filelen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Message(String sender, String getter, String content, String sendTime, String mesType){
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.sendTime = sendTime;
        this.mesType = mesType;
    }
    public Message(){}

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
