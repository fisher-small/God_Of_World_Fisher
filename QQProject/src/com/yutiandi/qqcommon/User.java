package com.yutiandi.qqcommon; /**
 * @author 于于于于于
 * @date 2024/9/1812:50QQProject
 */

import java.io.Serializable;

/**
 * <h3>QQProject</h3>
 * <p>客户信息</p>
 **/
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String password;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String flag;
    public User(String userId,String password){
        this.userId = userId;
        this.password = password;
    }
    public User () {}
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
