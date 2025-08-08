package Class32;

//给定一个整数 n，返回 n! 结果尾数中零的数量。
//示例 1:
//输入: 3
//输出: 0
//解释:3! = 6, 尾数中没有零。
//示例2:
//输入: 5
//输出: 1
//解释:5! = 120, 尾数中有 1 个零.
//说明: 你算法的时间复杂度应为O(logn)。
//Leetcode题目 : https://leetcode.com/problems/factorial-trailing-zeroes/
public class Problem_0172_FactorialTrailingZeroes {
    //阶乘末尾的零来自于乘法中的因子 10，而 10 = 2 × 5
    //在阶乘计算中，因子 2 的数量总是多于因子 5 的数量
    //因此零的数量由因子 5 的数量决定。
    public static int trailingZeroes(int n) {
        int ans = 0;
        while (n != 0) {
            n /= 5;
            ans += n;
        }
        return ans;
    }
}
