package org.swsd.stardust.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author     :  骆景钊
 * time       :  2017/11/16
 * description:  服务器返回代码判断
 * version:   :  1.0
 */

public class ErrorCodeJudgment {

    public static String networkJudge = "200";

    //传入一个json格式的字符串，返回一个字符串
    public static String errorCodeJudge(String responseData) {
        String judgeStr = null;
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String error_code = jsonObject.getString("error_code");
            switch (error_code){

                //服务器成功返回用户请求的数据
                case "200" : judgeStr = "Ok"; break;

                //用户新建或修改数据成功
                case "201" : judgeStr = "Create"; break;

                //表示一个请求已经进入后台排队（异步任务）
                case "202" : judgeStr = "Accepted"; break;

                //用户删除数据成功
                case "204" : judgeStr = "No Content"; break;

                //用户发出的请求有错误
                case "400" : judgeStr = "Invalid Request"; break;

                //表示用户没有权限（令牌、用户名、密码错误）
                case "401" : judgeStr = "Unauthorized"; break;

                //表示用户得到授权（与401错误相对），但是访问是被禁止的
                case "403" : judgeStr = "Forbidden"; break;

                //用户发出的请求针对的是不存在的记录，服务器没有进行操作
                case "404" : judgeStr = "Not Found"; break;

                //用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式)
                case "406" : judgeStr = "Not Acceptable"; break;

                //用户请求的资源被永久删除，且不会再得到的
                case "410" : judgeStr = "Gone"; break;

                //当创建一个对象时，发生一个验证错误
                case "422" : judgeStr = "Unprocesable entity"; break;

                //服务器发生错误，用户将无法判断发出的请求是否成功
                case "500" : judgeStr = "Internal Server Error"; break;

                default: judgeStr = null; break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return judgeStr;
    }

    //网络状态判断
    public static void networkJudgment(String ErrorCode){
        networkJudge = ErrorCode;
    }
}
