package Class32;

//给你一个整数数组 nums，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
//Leetcode题目 : https://leetcode.com/problems/maximum-product-subarray/
public class Problem_0152_MaximumProductSubarray {
    //时间复杂度为O(n),额外空间复杂度为O(1)
    public static double max(double[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        //记录前一个位置的最大最小乘积
        double preMax = arr[0];
        double preMin = arr[0];//维护最小值是为了考虑到负负得正的情况
        double ans = arr[0];
        for (int i = 1; i < n; i++) {
            double p1 = arr[i];//从当前元素开始的子数组的值
            double p2 = arr[i] * preMax;
            double p3 = arr[i] * preMin;
            double curMax = Math.max(Math.max(p1, p2), p3);
            double curMin = Math.min(Math.min(p1, p2), p3);
            ans = Math.max(ans, curMax);
            preMax = curMax;
            preMin = curMin;
        }
        return ans;
    }

    public static int maxProduct(int[] nums) {
        int ans = nums[0];
        int min = nums[0];
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int curMin = Math.min(nums[i], Math.min(min * nums[i], max * nums[i]));
            int curMax = Math.max(nums[i], Math.max(min * nums[i], max * nums[i]));
            min = curMin;
            max = curMax;
            ans = Math.max(ans, max);
        }
        return ans;
    }
}
