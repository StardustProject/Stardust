package org.swsd.stardust.presenter.ButtonNavigationBarPresenter.tools;

import android.content.Context;
import android.text.Editable;
import android.widget.Toast;
import java.util.regex.Pattern;

/**
 * author  ： 胡俊钦
 * time    ： 2017/11/13
 * desc    ： 一些通用的函数
 * version ： 1.0
 */
public class CommonFunctions {

    public boolean check(Context context, Editable username, Editable password) {
        boolean flag = false;
        // 检查用户名长度
        switch (checkLength(username)) {
            case 1:
                flag = false;
                Toast.makeText(context, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                flag = false;
                Toast.makeText(context, "用户名长度不能小于6！", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                flag = false;
                Toast.makeText(context, "用户名长度不能大于20！", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                flag = true;
                break;
            default:
        }

        if (flag) {
            // 检查用户名是否存在非法字符
            if (checkUsernameChar(username)) {
                flag = true;
            } else {
                flag = false;
                Toast.makeText(context, "用户名格式不正确！", Toast.LENGTH_SHORT).show();
            }
        }

        if (flag) {
            // 检查密码长度
            switch (checkLength(password)) {
                case 1:
                    flag = false;
                    Toast.makeText(context, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    flag = false;
                    Toast.makeText(context, "密码长度不能小于6！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    flag = false;
                    Toast.makeText(context, "密码长度不能大于20！", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    flag = true;
                    break;
                default:
            }
        }

        if (flag) {
            // 检查密码是否存在非法字符
            if (checkPasswordChar(password)) {
                flag = true;
            } else {
                flag = false;
                Toast.makeText(context, "密码不允许出现非法字符！", Toast.LENGTH_SHORT).show();
            }
        }
        return flag;
    }

    // 判断长度是否符合要求
    public int checkLength(Editable editable) {
        // 统计字符数，双字节字符只算一个字符
        int length = editable.length();
        // 统计字符长度，双字节字符算两个字符长度
        int count = 0;
        for (int i = 0; i < length; i++) {
            // 判断是否为双字节字符
            if (editable.charAt(i) > 0x80) {
                count += 2;
            } else {
                count++;
            }
        }

        if (count == 0) {
            return 1;

        } else if (count < 6) {
            return 2;

        } else if (count > 20) {
            return 3;

        } else {
            return 0;
        }
    }

    // 判断用户名字符是否合法
    public boolean checkUsernameChar(Editable editable) {
        // 用户名应为6-20位的字母、数字、下划线、中文组合，且以字母或中文作为第一个字符
        String usernamePattern = "^[a-zA-Z\\u4e00-\\u9fa5][\\w_\\u4e00-\\u9fa5]*$";

        if ((Pattern.matches(usernamePattern, editable.toString()))) {
            return true;
        } else {
            return false;
        }
    }

    // 判断密码字符是否合法
    public boolean checkPasswordChar(Editable editable) {
        // 密码应为6-20位的字母、数字、符号~!@#$%^&*()_=+|,.?:;'"{}[]-/\组合
        String passwordPattern = "[\\w~!@#$%\\^&*()_=+|,.?:;'\"{\\}\\[\\]\\-/\\\\]+";
        if ((Pattern.matches(passwordPattern, editable.toString()))) {
            return true;
        } else {
            return false;
        }
    }
}
