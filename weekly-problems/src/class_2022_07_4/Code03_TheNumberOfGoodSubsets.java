package class_2022_07_4;

import java.util.Arrays;

//给你一个整数数组nums。如果nums的一个子集中，
//所有元素的乘积可以表示为一个或多个 互不相同的质数 的乘积，那么我们称它为好子集。
//比方说，如果nums = [1, 2, 3, 4]：
//[2, 3]，[1, 2, 3]和[1, 3]是 好子集，乘积分别为6 = 2*3，6 = 2*3和3 = 3。
//[1, 4] 和[4]不是 好子集，因为乘积分别为4 = 2*2 和4 = 2*2。
//请你返回 nums中不同的好子集的数目对109 + 7取余的结果。
//nums中的 子集是通过删除 nums中一些（可能一个都不删除，也可能全部都删除）
//元素后剩余元素组成的数组。
//如果两个子集删除的下标不同，那么它们被视为不同的子集。
//测试链接 : https://leetcode.cn/problems/the-number-of-good-subsets/
public class Code03_TheNumberOfGoodSubsets {

    public static int[] primes = {
            0,
            0,
            1,
            2,
            0,
            4,
            3,
            8,
            0,
            0,
            5,
            16, 0, 32, 9, 6, 0, 64, 0, 128, 0, 10, 17, 256, 0, 0, 33, 0, 0,
            512,
            7
    };

    public static int[] counts = new int[31];//1到30每个数字在nums中出现的次数
    public static int[] status = new int[1 << 10];//每个质因数组合的子集数
    public static int mod = 1000000007;

    public static int numberOfGoodSubsets(int[] nums) {
        Arrays.fill(counts, 0);
        Arrays.fill(status, 0);
        for (int num : nums) {
            counts[num]++;
        }
        status[0] = 1;//空集的计数为0
        //处理数字1的组合
        for (int i = 0; i < counts[1]; i++) {
            status[0] = (status[0] << 1) % mod;
        }
        for (int i = 2; i <= 30; i++) {
            int curPrimesStatus = primes[i];//当前数字的质因数状态码
            if (curPrimesStatus != 0 && counts[i] != 0) {
                //遍历所有可能的质因数组合
                for (int from = 0; from < (1 << 10); from++) {
                    if ((from & curPrimesStatus) == 0) {
                        int to = from | curPrimesStatus;
                        status[to] = (int) (((long) status[to] + ((long) status[from] * counts[i])) % mod);
                    }
                }
            }
        }
        int ans = 0;
        for (int s = 1; s < (1 << 10); s++) {
            ans = (ans + status[s]) % mod;
        }
        return ans;
    }
}
