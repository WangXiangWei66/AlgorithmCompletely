package Class32;

//统计所有小于非负整数 n 的质数的数量。
//示例 1：
//输入：n = 10
//输出：4
//解释：小于 10 的质数一共有 4 个, 它们是 2, 3, 5, 7 。
//示例 2：
//输入：n = 0
//输出：0
//示例 3：
//输入：n = 1
//输出：0
//提示：
//0 <= n <= 5 * 10^6
//Leetcode题目 : https://leetcode.com/problems/count-primes/
public class Problem_0204_CountPrimes {

    public static int countPrimes(int n) {
        if (n < 3) {
            return 0;
        }
        boolean[] f = new boolean[n];//标记是否为非质数
        int count = n / 2;//除了2之外，所有的质数都是奇数
        //只处理奇数，步长为2
        for (int i = 3; i * i < n; i += 2) {
            if (f[i]) {
                continue;
            }
            //标记当前质数的奇数倍数（偶数倍数已经被提前排除了）为非质数
            for (int j = i * i; j < n; j += 2 * i) {
                if (!f[j]) {
                    --count;
                    f[j] = true;
                }
            }
        }
        return count;
    }
}
