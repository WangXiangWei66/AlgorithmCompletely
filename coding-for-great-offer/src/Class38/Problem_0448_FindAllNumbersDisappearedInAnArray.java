package Class38;

import java.util.ArrayList;
import java.util.List;

//给你一个含 n 个整数的数组 nums ，其中 nums[i] 在区间 [1, n] 内。请你找出所有在 [1, n] 范围内但没有出现在 nums 中的数字，并以数组的形式返回结果。
//示例 1：
//输入：nums = [4,3,2,7,8,2,3,1]
//输出：[5,6]
//示例 2：
//输入：nums = [1,1]
//输出：[2]
//提示：
//n == nums.length
//1 <= n <= 10^5
//1 <= nums[i] <= n
//进阶：你能在不使用额外空间且时间复杂度为 O(n) 的情况下解决这个问题吗? 你可以假定返回的数组不算在额外空间内。
//Leetcode题目 : https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
public class Problem_0448_FindAllNumbersDisappearedInAnArray {

    public static List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return ans;
        }
        int N = nums.length;
        //将每个数字放在对应的索引位置
        for (int i = 0; i < N; i++) {
            walk(nums, i);
        }
        //检查所有的位置，找到未匹配的数字
        for (int i = 0; i < N; i++) {
            if (nums[i] != i + 1) {
                ans.add(i + 1);
            }
        }
        return ans;
    }

    public static void walk(int[] nums, int i) {
        while (nums[i] != i + 1) {
            int nexti = nums[i] - 1;//计算当前数字应该去得位置
            if (nums[nexti] == nexti + 1) {
                break;
            }
            swap(nums, i, nexti);
        }
    }

    public static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
