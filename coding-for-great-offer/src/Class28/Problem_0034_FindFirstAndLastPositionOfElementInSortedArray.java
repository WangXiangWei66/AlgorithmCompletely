package Class28;
//二分查找的时间复杂度为O(logN)
//给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
//如果数组中不存在目标值 target，返回[-1, -1]。
//要求：设计并实现时间复杂度为O(log n)的算法
//Leetcode题目：https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
public class Problem_0034_FindFirstAndLastPositionOfElementInSortedArray {

    public static int[] searchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }
        int L = lessMostRight(nums, target) + 1;//调用lessMostRight方法找到小于target的最大元素索引，加1后得到目标值可能的起始位置L
        if (L == nums.length || nums[L] != target) {
            return new int[]{-1, -1};
        }
        return new int[]{L, lessMostRight(nums, target + 1)};//寻找target最后出现的索引
    }

    //在有序数组中找到小于num的最大元素索引
    public static int lessMostRight(int[] arr, int num) {
        int L = 0;
        int R = arr.length - 1;
        int M = 0;//存储中间位置的计算结果
        int ans = -1;//记录小于num的最大元素索引
        while (L <= R) {
            M = L + ((R - L) >> 1);
            if (arr[M] < num) {
                ans = M;
                L = M + 1;
            } else {
                R = M - 1;
            }
        }
        return ans;
    }
}
