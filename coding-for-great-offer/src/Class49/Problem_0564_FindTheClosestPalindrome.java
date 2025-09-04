package Class49;

//给定一个整数 n ，你需要找到与它最近的回文数（不包括自身）。
//“最近的”定义为两个整数差的绝对值最小。
//示例 1:
//输入: "123"
//输出: "121"注意:
//n 是由字符串表示的正整数，其长度不超过18。
//如果有多个结果，返回最小的那个。
//leetcode题目：https://leetcode.com/problems/find-the-closest-palindrome/
public class Problem_0564_FindTheClosestPalindrome {

    public static String nearestPalindromic(String n) {
        Long num = Long.valueOf(n);
        Long raw = getRawPalindrome(n);//获得基于原数的回文数
        Long big = raw > num ? raw : getBigPalindrome(raw);
        Long small = raw < num ? raw : getSmallPalindrome(raw);
        //看看哪个离得更近
        return String.valueOf(big - num >= num - small ? small : big);
    }

    public static Long getRawPalindrome(String n) {
        char[] chs = n.toCharArray();
        int len = chs.length;
        //将前半部分镜像到后半部分，来生成回文数
        for (int i = 0; i < len / 2; i++) {
            chs[len - 1 - i] = chs[i];
        }
        return Long.valueOf(String.valueOf(chs));
    }

    public static Long getBigPalindrome(Long raw) {
        char[] chs = String.valueOf(raw).toCharArray();
        char[] res = new char[chs.length + 1];
        res[0] = '0';
        for (int i = 0; i < chs.length; i++) {
            res[i + 1] = chs[i];
        }
        //从中间位置开始+1，处理进位
        int size = chs.length;
        for (int j = (size - 1) / 2 + 1; j >= 0; j--) {
            if (++res[j] > '9') {
                res[j] = '0';
            } else {
                break;
            }
        }
        //处理最高位进位后的镜像
        int offset = res[0] == '1' ? 1 : 0;
        size = res.length;
        for (int i = size - 1; i >= (size + offset) / 2; i--) {
            res[i] = res[size - i - offset];
        }
        return Long.valueOf(String.valueOf(res));
    }

    public static Long getSmallPalindrome(Long raw) {
        char[] chs = String.valueOf(raw).toCharArray();
        char[] res = new char[chs.length];
        int size = res.length;
        for (int i = 0; i < size; i++) {
            res[i] = chs[i];
        }
        //处理借位情况
        for (int j = (size - 1) / 2; j >= 0; j--) {
            if (--res[j] < '0') {
                res[j] = '9';
            } else {
                break;
            }
        }
        if (res[0] == '0') {
            res = new char[size - 1];
            for (int i = 0; i < res.length; i++) {
                res[i] = '9';
            }
            return size == 1 ? 0 : Long.parseLong(String.valueOf(res));//解析为Long类型
        }
        for (int k = 0; k < size; k++) {
            res[size - 1 - k] = res[k];
        }
        return Long.valueOf(String.valueOf(res));
    }
}
