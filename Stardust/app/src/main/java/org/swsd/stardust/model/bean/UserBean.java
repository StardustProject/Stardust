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

    //服务器返回的user的ID
    private int userId;
    private String userName ;
    private String email;
    private String tel;
    private String avatarPath;
    private String recentMood;
    private String token;
    private String tokenTime;
    private String qiniuToken;
    private String qiniuTime;


    private long registerTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getTokenTime(){
        return tokenTime;
    }

    public void setTokenTime(String tokenTime){
        this.tokenTime = tokenTime;
    }

    public String getQiniuToken() {
        return qiniuToken;
    }

    public void setQiniuToken(String qiniuToken) {
        this.qiniuToken = qiniuToken;
    }

    public String getQiniuTime(){
        return qiniuTime;
    }

    public void setQiniuTime(String qiniuTime){
        this.qiniuTime = qiniuTime;
    }
}
