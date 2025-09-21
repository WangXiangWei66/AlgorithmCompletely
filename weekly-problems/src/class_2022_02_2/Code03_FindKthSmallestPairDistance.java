package class_2022_02_2;

import java.util.Arrays;

//给定一个整数数组，返回所有数对之间的第 k 个最小距离
//一对 (A, B) 的距离被定义为 A 和 B 之间的绝对差值
//leetcode链接 : https://leetcode.com/problems/find-k-th-smallest-pair-distance/
public class Code03_FindKthSmallestPairDistance {

    public static int smallestDistancePair(int[] nums, int k) {
        int n = nums.length;
        Arrays.sort(nums);
        int l = 0;//最小的可能距离
        int r = nums[n - 1] - nums[0];//最大的可能距离
        int ans = 0;
        while (l <= r) {
            int dis = l + ((r - l) >> 1);
            int cnt = f(nums, dis);//计算当前距离小于等于dis的数组对量
            if (cnt >= k) {
                ans = dis;
                r = dis - 1;
            } else {
                l = dis + 1;
            }
        }
        return ans;
    }

    public static int f(int[] arr, int dis) {
        int cnt = 0;
        for (int l = 0, r = 0; l < arr.length; l++) {
            while (r < arr.length && arr[r] <= arr[l] + dis) {
                r++;
            }
            cnt += r - l - 1;
        }
        return cnt;
    }
}
