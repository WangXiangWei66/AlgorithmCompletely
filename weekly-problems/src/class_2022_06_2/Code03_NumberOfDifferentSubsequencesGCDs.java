package class_2022_06_2;

//给你一个由正整数组成的数组 nums 。
//数字序列的 最大公约数 定义为序列中所有整数的共有约数中的最大整数。
//例如，序列 [4,6,16] 的最大公约数是 2 。
//数组的一个 子序列 本质是一个序列，可以通过删除数组中的某些元素（或者不删除）得到。
//例如，[2,5,10] 是 [1,2,1,2,4,1,5,10] 的一个子序列。
//计算并返回 nums 的所有 非空 子序列中 不同 最大公约数的 数目 。
//测试链接 : https://leetcode.com/problems/number-of-different-subsequences-gcds/
public class Code03_NumberOfDifferentSubsequencesGCDs {

    public static int countDifferentSubsequenceGCDs(int[] nums) {
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            max = Math.max(max, num);
        }
        boolean[] set = new boolean[max + 1];
        for (int num : nums) {
            set[num] = true;
        }
        //计算所有可能成为gcd的数字
        int ans = 0;
        for (int a = 1; a <= max; a++) {
            int g = a;
            for (; g <= max; g += a) {
                //找到了第一个
                if (set[g]) {
                    break;
                }
            }
            for (int b = g; b <= max; b += a) {
                if (set[b]) {
                    g = gcd(g, b);
                    if (g == a) {
                        ans++;
                        break;
                    }
                }
            }
        }
        return ans;
    }

    public static int gcd(int m, int n) {
        return n == 0 ? m : gcd(n, m % n);
    }
}
