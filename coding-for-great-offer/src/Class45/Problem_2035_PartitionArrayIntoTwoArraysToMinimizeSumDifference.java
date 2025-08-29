package Class45;

import java.util.HashMap;
import java.util.TreeSet;

//给你一个长度为 2 * n的整数数组。你需要将nums分成两个长度为n的数组，分别求出两个数组的和，并 最小化两个数组和之差的绝对值。nums中每个元素都需要放入两个数组之一。
//请你返回最小的数组和之差。
//数据范围：
//1 <= n <= 15
//nums.length == 2 * n
//-10^7 <= nums[i] <= 10^7
//leetcode题目：https://leetcode.com/problems/partition-array-into-two-arrays-to-minimize-sum-difference/
public class Problem_2035_PartitionArrayIntoTwoArraysToMinimizeSumDifference {
    //问题转化为了：找到长度为n的子数组，使pickSum尽可能接近sum/2
    public static int minimumDifference(int[] arr) {
        int size = arr.length;
        int half = size >> 1;
        //TreeSet：存储"选中k个元素时，所有可能的和"
        HashMap<Integer, TreeSet<Integer>> lMap = new HashMap<>();
        process(arr, 0, half, 0, 0, lMap);
        HashMap<Integer, TreeSet<Integer>> rMap = new HashMap<>();
        process(arr, half, size, 0, 0, rMap);
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        //遍历左半的所有组合，匹配右半的最优组合
        int ans = Integer.MAX_VALUE;
        for (int leftNum : lMap.keySet()) {
            for (int leftSum : lMap.get(leftNum)) {//左半数组选数的所有可能和
                Integer rightSum = rMap.get(half - leftNum).floor((sum >> 1) - leftSum);
                if (rightSum != null) {
                    int pickSum = leftSum + rightSum;
                    int restSum = sum - pickSum;
                    ans = Math.min(ans, restSum - pickSum);
                }
            }
        }
        return ans;
    }

    //index：当前正在处理的元素索引
    //pick：已经选中的元素的个数
    //sum：已选中的元素的总和
    public static void process(int[] arr, int index, int end, int pick, int sum, HashMap<Integer, TreeSet<Integer>> map) {
        if (index == end) {
            if (!map.containsKey(pick)) {
                map.put(pick, new TreeSet<>());
            }
            map.get(pick).add(sum);
        } else {
            process(arr, index + 1, end, pick, sum, map);
            process(arr, index + 1, end, pick + 1, sum + arr[index], map);
        }
    }
}
