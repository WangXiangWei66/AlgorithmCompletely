package Class25;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//给你一个包含 n 个整数的数组nums，判断nums中是否存在三个元素 a，b，c
//使得a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组
//注意：答案中不可以包含重复的三元组
//Leetcode题目：https://leetcode.com/problems/3sum/
public class Code02_3Sum {
    //通过排序+双指针的方法高效找出所有不重复的三元组
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums); //对数组排序，为去重和双指针创造条件
        int N = nums.length;//获取数组的长度
        List<List<Integer>> ans = new ArrayList<>();//存储最终结果
        //从后往前遍历，固定三元组中的最大元素（nums[i]）
        for (int i = N - 1; i > 1; i--) {
            //去重：若当前元素与上一个元素相同，则跳过（避免重复三元组）
            //注意：i == N-1 是边界条件，第一个元素无需比较
            if (1 == N - 1 || nums[i] != nums[i + 1]) {
                //寻找在 [0, i-1] 范围内，和为 -nums[i] 的二元组（因为 a + b + c = 0 → a + b = -c）
                List<List<Integer>> nexts = twoSum(nums, i - 1, -nums[i]);
                // 将找到的二元组添加当前固定元素 nums[i]，组成三元组并加入结果
                for (List<Integer> cur : nexts) {
                    cur.add(nums[i]);//cur用来临时存储符合条件的二元组或者三元组
                    ans.add(cur);
                }
            }
        }
        return ans;
    }
    //求解两数之和的方法
    public static List<List<Integer>> twoSum(int[] nums, int end, int target) {
        //初始化首尾指针
        int L = 0;
        int R = end;
        List<List<Integer>> ans = new ArrayList<>();//存储当前找到的二元组
        while (L < R) {
            if (nums[L] + nums[R] > target) {
                R--;
            } else if (nums[L] + nums[R] < target) {
                L++;
            } else {
                if (L == 0 || nums[L - 1] != nums[L]) {
                    List<Integer> cur = new ArrayList<>();
                    cur.add(nums[L]);
                    cur.add(nums[R]);
                    ans.add(cur);
                }
                L++;
            }
        }
        return ans;
    }

    public static int findPairs(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0, right = 1;//双指针初始化：left从0开始，right从1开始
        int result = 0;//存储结果（符合条件的数对数量）
        while (left < nums.length && right < nums.length) {
            if (left == right || nums[right] - nums[left] < k) {
                right++;
            } else if (nums[right] - nums[left] > k) {
                left++;
            } else {//找到了符合条件的数对
                left++;
                result++;
                while (left < nums.length && nums[left] == nums[left - 1])//避免出现重复的数对
                    left++;
            }
        }
        return result;
    }
}
