package org.swsd.stardust.util;

import org.litepal.crud.DataSupport;
import org.swsd.stardust.model.bean.UserBean;

import java.util.List;

/**
 * author     :  骆景钊
 * time       :  2017/11/13
 * description:  登录判断
 * version:   :  1.0
 */

public class LoginActivityJudgment {


    public static Boolean loginActivityJudgment(){

        List<UserBean> userBeanList = DataSupport.findAll(UserBean.class);

        if(userBeanList.size() == 0){
            return true;
        }else{
            return false;
        }
    }
}
