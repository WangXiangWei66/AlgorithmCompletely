package Class29;

//一个机器人位于一个 m x n网格的左上角
//机器人每次只能向下或者向右移动一步
//机器人试图达到网格的右下角
//问总共有多少条不同的路径？
//Leetcode题目：https://leetcode.com/problems/unique-paths/
public class Problem_0062_UniquePaths {
    //机器人从左上角到右下角，必须向右移动n-1次，向下移动m-1次，总移动次数为m+n-2次。
    //不同路径的数量等价于：从m+n-2次移动中选择n-1次向右移动（或m-1次向下移动）的组合数，即：
    //C(m+n-2, n-1) = (m+n-2)! / [(n-1)! * (m-1)!]
    public static int uniquePaths(int m, int n) {
        int right = n - 1;//计算总共需要向右移动的步数
        int all = m + n - 2;//计算需要总共移动的步数
        //两个变量用来计算组合数的分子和分母
        long o1 = 1;
        long o2 = 1;
        //循环计算组合数：从总步数中选择向右步数 的组合数
        //循环变量i从right+1遍历到all（分子部分）
        //循环变量j从 1 遍历到all-right（分母部分，即(m-1)）
        for (int i = right + 1, j = 1; i <= all; i++, j++) {
            o1 *= i;//累积分子
            o2 *= j;//累积分母
            long gcd = gcd(o1, o2);//计算当前分子分母的最大公约数
            //分子分母同时除以最大公约数，减少数值大小，避免溢出
            o1 /= gcd;
            o2 /= gcd;
        }
        return (int) o1;
    }

    public static long gcd(long m, long n) {
        return n == 0 ? m : gcd(n, m % n);
    }
}
