package Class48;

//对于给定的整数 n, 如果n的k（k>=2）进制数的所有数位全为1，则称k（k>=2）是 n 的一个好进制。
//以字符串的形式给出 n, 以字符串的形式返回 n 的最小好进制。
//示例 1：
//输入："13"
//输出："3"
//解释：13 的 3 进制是 111。
//示例 2：
//输入："4681"
//输出："8"
//解释：4681 的 8 进制是 11111。
//示例 3：
//输入："1000000000000000000"
//输出："999999999999999999"
//解释：1000000000000000000 的 999999999999999999 进制是 11。
//leetcode题目：https://leetcode.com/problems/smallest-good-base/
public class Problem_0483_SmallestGoodBase {

    public static String smallestGoodBase(String n) {
        long num = Long.valueOf(n);//将输入的字符串转化为长整数、
        //m = (int) (Math.log(num + 1) / Math.log(2))：这是m的最大值，用到了换底公式
        for (int m = (int) (Math.log(num + 1) / Math.log(2)); m > 2; m--) {
            //拿到左右边界
            //基于等比数列公式推导 k 的范围：
            //由 n = (k^m - 1)/(k-1) > k^(m-1)（分子分母同除 k-1，忽略低次项）→ k < n^(1/(m-1))；
            //由 n = (k^m - 1)/(k-1) < k^m（分母 k-1 > 1）→ k > n^(1/m)；
            //因此 k 的有效范围是 [n^(1/m), n^(1/(m-1))]，左右边界用 Math.pow 估算后转为 long，右边界加 1 避免精度丢失。
            long l = (long) (Math.pow(num, 1.0 / m));
            long r = (long) (Math.pow(num, 1.0 / (m - 1))) + 1L;
            while (l <= r) {
                long k = l + ((r - l) >> 1);
                long sum = 0L;//存储等比数列求和结果
                long base = 1L;
                for (int i = 0; i < m && sum <= num; i++) {
                    sum += base;
                    base *= k;
                }
                if (sum < num) {
                    l = k + 1;
                } else if (sum > num) {
                    r = k - 1;
                } else {
                    return String.valueOf(k);
                }
            }
        }
        return String.valueOf(num - 1);//最终处理为m=2的情况
    }
}
