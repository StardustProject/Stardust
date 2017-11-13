package org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools;

import android.content.Context;
import android.text.Editable;
import android.widget.Toast;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/13
 * desc    ： 用于用户名和密码格式检测的工具类
 * version ： 1.0
 */
public class FormatChecking {
    // 判断长度
    public boolean checkLength(Context context, Editable editable) {
        boolean flag = false;
        // 统计字符数，汉字算一个字符
        int length = editable.length();
        // 统计字符长度，汉字算两个字符长度
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (editable.charAt(i) < 256) {
                count++;
            } else {
                count += 2;
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
        boolean flag = false;
        // 统计字符数，汉字算一个字符
        int length = editable.length();
        char c;
        int i;
        for (i = 0; i < length; i++) {
            c = editable.charAt(i);
            // 允许数字、大小写字母和汉字
            if ((c <= '9' && c >= '0') || (c <= 'z' && c >= 'a')
                    || (c <= 'Z' && c >= 'A') || c > 128) {
                continue;

            } else {
                Toast.makeText(context, "用户名不允许出现非法字符！", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (i == length) {
            flag = true;
        }
        return flag;
    }

    // 判断密码字符是否合法，缺标点符号的判断
    // …………………………………………………………
    // ………………………………………………………………
    public boolean checkPasswordChar(Context context, Editable editable) {
        boolean flag = false;
        //统计字符数，汉字算一个字符
        int length = editable.length();
        char c;
        int i;
        for (i = 0; i < length; i++) {
            c = editable.charAt(i);
            // 允许数字、大小写字母和标点符号
            if ((c <= '9' && c >= '0') || (c <= 'z' && c >= 'a') || (c <= 'A' && c >= 'Z')) {
                continue;

            } else {
                Toast.makeText(context, "密码不允许出现非法字符！", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (i == length) {
            flag = true;
        }
        return flag;
    }
}
