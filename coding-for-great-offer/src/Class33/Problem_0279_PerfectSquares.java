package Class33;

//给定正整数n，找到若干个完全平方数（比如1, 4, 9, 16, ...）使得它们的和等于 n。你需要让组成和的完全平方数的个数最少。
//给你一个整数 n ，返回和为 n 的完全平方数的 最少数量 。
//完全平方数 是一个整数，其值等于另一个整数的平方；换句话说，其值等于一个整数自乘的积。例如，1、4、9 和 16 都是完全平方数，而 3 和 11 不是。
//示例1：
//输入：n = 12
//输出：3
//解释：12 = 4 + 4 + 4
//示例 2：
//输入：n = 13
//输出：2
//解释：13 = 4 + 9
//Leetcode题目 : https://leetcode.com/problems/perfect-squares/
public class Problem_0279_PerfectSquares {

    public static int numSquares1(int n) {
        //最坏的情况是n个数相加，从2开始尝试各个平方数
        int res = n, num = 2;
        while (num * num <= n) {
            //a：n中包含的num^2的个数
            //b：使用num^2后剩余的数值，拿着这个数值去递归
            int a = n / (num * num), b = n % (num * num);
            res = Math.min(res, a + numSquares1(b));
            //尝试下一个更大的平方数
            num++;
        }
        return res;
    }

    //如果n是 4 的倍数，那么n的最少平方数数量与n/4相同
    //如果n除以 8 余 7，那么n最少需要 4 个平方数之和
    //一个数能表示为 1 个平方数，当且仅当它本身是完全平方数
    //排除以上情况后，检查是否能表示为 2 个平方数之和
    //如果都不能，则一定能表示为 3 个平方数之和
    public static int numSquares2(int n) {
        int rest = n;
        //如果n是4的倍数，那么他的最少平方数 数量和n/4相同
        while (rest % 4 == 0) {
            rest /= 4;
        }
        //如果一个数除8余7，那么他最少需要4个平方数的和
        if (rest % 8 == 7) {
            return 4;
        }
        int f = (int) Math.sqrt(n);//检查本身是否为平方数
        if (f * f == n) {
            return 1;
        }
        //检查是否可以表示为两个平方数的和
        for (int first = 1; first * first <= n; first++) {
            int second = (int) Math.sqrt(n - first * first);
            if (first * first + second * second == n) {
                return 2;
            }
        }
        //否则一定表示为了三个平方数的和
        return 3;
    }

    // 数学解
    // 1）四平方和定理
    // 2）任何数消掉4的因子，结论不变
    public static int numSquares3(int n) {
        while (n % 4 == 0) {
            n /= 4;
        }
        if (n % 8 == 7) {
            return 4;
        }
        for (int a = 0; a * a <= n; ++a) {
            int b = (int) Math.sqrt(n - a * a);
            if (a * a + b * b == n) {
                return (a > 0 & b > 0) ? 2 : 1;
            }
        }
        return 3;
    }

    public static void main(String[] args) {
        for (int i = 1; i < 1000; i++) {
            System.out.println(i + "," + numSquares1(i));
        }
    }
}
