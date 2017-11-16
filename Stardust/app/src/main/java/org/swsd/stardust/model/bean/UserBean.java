package org.swsd.stardust.model.bean;


import org.litepal.crud.DataSupport;


/**
 * author     :  张昭锡
 * time       :  2017/11/02
 * description:  数据库用户表的JavaBean文件
 * version:   :  1.0
 */
public class UserBean extends DataSupport{

    private int id;
    private String userName;
    private String email;
    private String tel;
    private String avatarPath;
    private String recentMood;
    private String token;


    private long registerTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getRecentMood() {
        return recentMood;
    }

    public void setRecentMood(String recentMood) {
        this.recentMood = recentMood;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
