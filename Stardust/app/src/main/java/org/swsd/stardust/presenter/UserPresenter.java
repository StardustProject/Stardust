package org.swsd.stardust.presenter;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.NoteBean;
import org.swsd.stardust.model.bean.UserBean;

import java.util.Date;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/14
 * desc    ： 用户的presenter层
 * version ： 1.0
 */
public class UserPresenter extends DataSupport {
    public UserBean toGetUserInfo() {
        UserBean userBean;
        userBean = DataSupport.findLast(UserBean.class);
        if (userBean == null) {
            userBean = useDefaultData();
        }
        return userBean;
    }

    // 默认数据，防止数据库没有数据时导致程序崩溃
    public UserBean useDefaultData() {
        UserBean userBean = new UserBean();

        // 设置默认用户id
        userBean.setId(0);
        // 设置默认用户名
        userBean.setUserName("Username");
        // 获取当前时间作为默认注册时间值
        Date date = new Date();
        Long time = date.getTime();
        userBean.setRegisterTime(time);
        userBean.save();
        return userBean;
    }

    public int toGetStarNum(){
        int num=0;
        num=DataSupport.count(NoteBean.class);
        return num;
    }
}
