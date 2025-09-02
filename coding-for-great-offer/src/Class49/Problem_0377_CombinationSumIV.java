package Class49;

import java.util.Arrays;

//给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。请你从 nums 中找出并返回总和为 target 的元素组合的个数。
//题目数据保证答案符合 32 位整数范围。
//leetcode题目：https://leetcode.com/problems/combination-sum-iv
public class Problem_0377_CombinationSumIV {

    public static int ways(int rest, int[] nums) {
        if (rest < 0) {
            return 0;
        }
        if (rest == 0) {
            return 1;
        }
        int ways = 0;
        for (int num : nums) {
            ways += ways(rest - num, nums);
        }
        return ways;
    }

    public static int[] dp = new int[1001];

    public static int combinationSum41(int[] nums, int target) {
        Arrays.fill(dp, 0, target + 1, -1);
        return process1(nums, target);
    }

    public static int process1(int[] nums, int rest) {
        if (rest < 0) {
            return 0;
        }
        if (dp[rest] != -1) {
            return dp[rest];
        }
        int ans = 0;
        if (rest == 0) {
            ans = 1;
        } else {
            for (int num : nums) {
                ans += process1(nums, rest - num);
            }
        }
        dp[rest] = ans;
        return ans;
    }

    public static int combinationSun42(int[] nums, int target) {
        Arrays.sort(nums);
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int rest = 1; rest <= target; rest++) {
            for (int i = 0; i < nums.length && nums[i] <= rest; i++) {
                dp[rest] += dp[rest - nums[i]];
            }
        }
        return dp[target];
    }
}
