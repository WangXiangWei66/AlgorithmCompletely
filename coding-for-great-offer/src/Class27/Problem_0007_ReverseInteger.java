package Class27;

//给你一个32位的有符号整数x，返回将x中的数字部分反转后的结果，如果反转后整数超过 32 位的有符号整数的范围，就返回0
//假设环境不允许存储 64 位整数（有符号或无符号）
//Leetcode题目：https://leetcode.com/problems/reverse-integer/

public class Problem_0007_ReverseInteger {

    public static int reverse(int x) {
        boolean neg = ((x >>> 31) & 1) == 1;//判断x是正数还是负数
        x = neg ? x : -x;//将x统一转化为负数来处理
        //后续溢出判断
        int m = Integer.MIN_VALUE / 10;//-214748364
        int o = Integer.MIN_VALUE % 10;//-8
        int res = 0;
        while (x != 0) {//循环处理x的每一位数字，直到变成0
            if (res < m || (res == m && x % 10 < o)) {
                return 0;//反转后会溢出
            }
            res = res * 10 + x % 10;
            x /= 10;
        }
        return neg ? res : Math.abs(res);
    }
}
