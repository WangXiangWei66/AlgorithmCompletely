package Class44;

import java.util.HashMap;

//给定一个正整数数组arr，和正数k
//如果arr的某个子数组中含有k种不同的数，称这个子数组为有效子数组
//返回arr中有效子数组的数量
//leetcode题目 : https://leetcode.com/problems/subarrays-with-k-different-integers/
public class Problem_0992_SubarraysWithKDifferentIntegers {

    //直接计算 “恰好包含 k 种不同整数的子数组” 较复杂，可通过补集转化简化：
    //设 f(k) = 数组中 “最多包含 k 种不同整数” 的子数组数量；
    //则 “恰好包含 k 种不同整数” 的子数组数量 = f(k) - f(k-1)；
    public static int subArraysWithKDistinct1(int[] nums, int k) {
        int n = nums.length;
        int[] lessCounts = new int[n + 1];//最多k种窗口计算
        int[] equalCounts = new int[n + 1];//最多k-1种窗口计算
        //左指针，两个窗口的左边界
        int lessLeft = 0;
        int equalLeft = 0;
        //窗口中不同整数的数量
        int lessKinds = 0;
        int equalKinds = 0;
        int ans = 0;
        //右指针实时遍历数组，两个窗口共享右边界
        for (int r = 0; r < n; r++) {
            //首次出现，则种类+1
            if (lessCounts[nums[r]] == 0) {
                lessKinds++;
            }
            if (equalCounts[nums[r]] == 0) {
                equalKinds++;
            }
            //计数加1
            lessCounts[nums[r]]++;
            equalCounts[nums[r]]++;
            while (lessKinds == k) {
                if (lessCounts[nums[lessLeft]] == 1) {
                    lessKinds--;
                }
                lessCounts[nums[lessLeft++]]--;//左指针的元素计数减1，左指针右移
            }
            while (equalKinds > k) {
                if (equalCounts[nums[equalLeft]] == 1) {
                    equalKinds--;
                }
                equalCounts[nums[equalLeft++]]--;
            }
            ans += lessLeft - equalLeft;
        }
        return ans;
    }

    public static int subArraysWithKDistinct2(int[] arr, int k) {
        return numsMostK(arr, k) - numsMostK(arr, k - 1);
    }
    //k，剩余的不同种类数还剩几个
    public static int numsMostK(int[] arr, int k) {
        int i = 0, res = 0;
        //key = 数组元素，value = 该元素在窗口在窗口内的出现次数
        HashMap<Integer, Integer> count = new HashMap<>();
        for (int j = 0; j < arr.length; ++j) {
            if (count.getOrDefault(arr[j], 0) == 0) {
                k--;
            }
            count.put(arr[j], count.getOrDefault(arr[j], 0) + 1);
            //收缩窗口左边界
            while (k < 0) {
                count.put(arr[i], count.get(arr[i]) - 1);
                if (count.get(arr[i]) == 0) {
                    k++;
                }
                i++;
            }
            res += j - i + 1;
        }
        return res;
    }
}
