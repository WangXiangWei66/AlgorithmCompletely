package class_2022_02_4;

import java.util.Arrays;

//给你一个整数数组 arr
//请你将该数组分隔为长度最多为 k 的一些（连续）子数组
//分隔完成后，每个子数组的中的所有值都会变为该子数组中的最大值。
//返回将数组分隔变换后能够得到的元素最大和
//注意：
//原数组和分隔后的数组对应顺序应当一致，
//也就是说，你只能选择分隔数组的位置而不能调整数组中的顺序。
//leetcode链接 : https://leetcode.com/problems/partition-array-for-maximum-sum/
public class Code03_PartitionArrayForMaximumSum {

    public static int maxSumAfterPartitioning1(int[] arr, int k) {
        //dp[i]:存储数组从0到i位置的子数组的最大分割和
        int[] dp = new int[arr.length];//存储原来已经计算过的结果
        Arrays.fill(dp, -1);
        //从数组的最后一个元素开始计算
        return process1(arr, k, arr.length - 1, dp);
    }

    //index：当前已经处理到的数组的位置
    public static int process1(int[] arr, int k, int index, int[] dp) {
        if (index == -1) {
            return 0;
        }
        if (dp[index] != -1) {
            return dp[index];
        }
        int max = Integer.MIN_VALUE;//当前子数组的最大值
        int ans = Integer.MIN_VALUE;//当前子数组的最大和
        //j是用来保证当前子数组的长度不会超过k
        for (int i = index, j = 1; i >= 0 && j <= k; i--, j++) {
            max = Math.max(max, arr[i]);
            ans = Math.max(ans, process1(arr, k, i - 1, dp) + (index - i + 1) * max);
        }
        dp[index] = ans;
        return ans;
    }

    public static int maxSumAfterPartitioning2(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        //dp[i]:存储前i+1个元素
        int[] dp = new int[n];
        dp[0] = arr[0];
        for (int i = 1; i < n; i++) {
            dp[i] = arr[i] + dp[i - 1];
            int max = arr[i];
            for (int j = i - 1; j >= 0 && (i - j + 1) <= k; j--) {
                max = Math.max(max, arr[j]);
                dp[i] = Math.max(dp[i], max * (i - j + 1) + (j - 1 >= 0 ? dp[j - 1] : 0));
            }
        }
        return dp[n - 1];
    }
}
