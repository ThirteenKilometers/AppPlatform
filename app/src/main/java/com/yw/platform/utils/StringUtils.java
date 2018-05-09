package com.yw.platform.utils;

import android.widget.Toast;

import com.yw.platform.R;
import com.yw.platform.global.Constants;
import com.yw.platform.global.MyApplication;

public class StringUtils {
    /**
     * 判空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判空并显示Toast
     */
    public static boolean isEmpty2Toast(CharSequence str, int resId) {
        if (str == null || str.length() == 0) {
            Toast.makeText(MyApplication.getInstance(), resId, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 正则检测手机号
     *
     * @return true: 正确
     */
    public static boolean phoneNumberMatch(String phone) {
        if (phone.matches(Constants.PHONE_MATCH)) {
            return true;
        } else {
            Toast.makeText(MyApplication.getInstance(), R.string.phone_match_error, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //去掉虚拟应用资源xml里每个value的引号
    public static String rmBothSideQuotation(String resValue) {
        String quotation = "\"";
        if (resValue.startsWith(quotation) && resValue.endsWith(quotation)) {
            resValue = resValue.substring(1, resValue.length() - 1);
        }
        return resValue;
    }

}
