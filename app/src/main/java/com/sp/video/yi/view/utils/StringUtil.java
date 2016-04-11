package com.sp.video.yi.view.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2015/2/5.
 */
public class StringUtil {
    /**
     * 添加url地址后缀
     * @param rawUrl
     * @param width
     * @param height
     * @return
     */
    public static String formatUrlBySize(String rawUrl, int width, int height) {
        String url = "";
        url = rawUrl+"!t"+width+"x"+height+".jpg";
        return url;
    }
    /**
     * 文本过长时，限制字数，结尾用指定文本替换。支持中英混排。
     *
     * @param str     input string
     * @param toCount 中文占2个字符 ，限制toCount/2个中文
     * @param more    结尾替换字符，如“……” ," XXX "
     * @return
     * @throws Exception
     *
     *example  限定12
    超bbb屌的伟哥  13 => 超bbb屌的伟...
    超级屌的伟哥b  13 => 超级屌的伟哥...
    超级屌的伟哥   12 => 超级屌的伟哥
    超bbbb的伟哥   12 => 超bbbb的伟哥
    超bbbbb的伟哥  13 => 超bbbbb的伟...
    abc1234567890  13 => abc123456789...
    ab1234567890   12 => ab1234567890

     *
     */
    public static String getLimitLengthString(String str, int toCount, String more) throws Exception {
        int reInt = 0;
        String reStr = "";
        if (str == null) return "";
        char[] tempChar = str.toCharArray();
        int kk = 0;
        for ( kk = 0;(kk < tempChar.length && toCount > reInt); kk++) {
            String s1 = str.valueOf(tempChar[kk]);
            byte[] b = s1.getBytes("GBK");
            reInt += b.length;
            if(reInt <= toCount){
                reStr += tempChar[kk];
            }
        }
        if ((toCount == reInt && kk < tempChar.length ) || (toCount == reInt - 1))
            reStr += more;
        return reStr;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param str
     * @return
     */
    public static String subZeroAndDot(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.indexOf(".") > 0) {
            str = str.replaceAll("0+?$", "");//去掉多余的0
            str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return str;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为数值字串
     * <p/>
     * <br>
     * Created 2014-11-12 下午8:30:52
     *
     * @param str
     * @return
     * @author
     */
    public static boolean isDigitsOnly(String str) {
        if (null == str) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将字串解析成整数
     *
     * @param data
     * @param def
     * @return
     */
    public static int parseStringToInt(String data, int def) {
        if (StringUtil.isBlank(data))
            return def;
        if (StringUtil.isDigitsOnly(data)) {
            return Integer.valueOf(data);
        }
        return def;
    }


}