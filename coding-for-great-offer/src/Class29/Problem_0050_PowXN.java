package Class29;

//实现 pow(x, n) ，即计算 x 的 n 次幂函数
//Leetcode题目：https://leetcode.com/problems/powx-n/
public class Problem_0050_PowXN {
    //a是底数
    //n是指数
    public static int pow(int a, int n) {
        int ans = 1;
        int t = a;//临时变量t为底数 a，用于计算幂的中间结果
        while (n != 0) {
            if ((n & 1) != 0) {
                ans *= t;
            }
            t *= t;
            n >>= 1;
        }
        return ans;
    }

    public static double myPow(double x, int n) {
        if (n == 0) {
            return 1D;//任何数的0次方都是1
        }
        int pow = Math.abs(n == Integer.MIN_VALUE ? n + 1 : n);
        double t = x;
        double ans = 1D;
        while (pow != 0) {
            if ((pow & 1) != 0) {
                ans *= t;
            }
            pow >>= 1;
            t = t * t;
        }
        if (n == Integer.MIN_VALUE) {
            ans *= x;
        }
        //如果是负数，则返回倒数
        return n < 0 ? (1D / ans) : ans;
    }
}
