package org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools;

import android.content.Context;
import android.text.Editable;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/13
 * desc    ： 用于用户名和密码格式检测的工具类
 * version ： 1.0
 */
public class FormatChecking {

    // 判断长度是否符合要求
    public boolean checkLength(Context context, Editable editable) {
        // 匹配汉字和中文标点符号的正则表达式
        String chinesePattern = "[\\u4e00-\\u9fff\\p{P}]+";
        boolean flag = false;
        // 统计字符数，汉字算一个字符
        int length = editable.length();
        String str = "";
        // 统计字符长度，汉字和中文字符算两个字符长度
        int count = 0;
        for (int i = 0; i < length; i++) {
            str += editable.charAt(i);
            // 判断是否为汉字
            if (Pattern.matches(chinesePattern, str)) {
                count += 2;
                str = "";
            } else {
                count++;
                str = "";
            }
        }

        if (count == 0) {
            Toast.makeText(context, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
        } else if (count < 6) {
            Toast.makeText(context, "用户名和密码长度不能小于6！", Toast.LENGTH_SHORT).show();
        } else if (count > 20) {
            Toast.makeText(context, "用户名和密码长度不能大于20！", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
        }

        return flag;
    }

    // 判断用户名字符是否合法
    public boolean checkUsernameChar(Context context, Editable editable) {
        // 允许数字、大小写字母和汉字
        String usernamePattern = "[0-9a-zA-Z\\u4e00-\\u9fff]+";

        if ((Pattern.matches(usernamePattern, editable.toString()))) {
            return true;
        } else {
            Toast.makeText(context, "用户名不允许出现非法字符！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // 判断密码字符是否合法
    public boolean checkPasswordChar(Context context, Editable editable) {
        // 允许数字、大小写字母和标点符号
        String passwordPattern = "[0-9a-zA-Z\\u4e00-\\u9fff\\p{P}]+";
        if ((Pattern.matches(passwordPattern, editable.toString()))) {
            return true;
        } else {
            Toast.makeText(context, "密码不允许出现非法字符！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
