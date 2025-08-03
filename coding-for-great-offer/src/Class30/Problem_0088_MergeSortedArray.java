package Class30;

//给你两个有序整数数组nums1 和 nums2，请你将 nums2 合并到nums1中，使 nums1 成为一个有序数组。
//初始化nums1 和 nums2 的元素数量分别为m 和 n 。你可以假设nums1 的空间大小等于m + n，这样它就有足够的空间保存来自 nums2 的元素。
//示例 1：
//输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
//输出：[1,2,2,3,5,6]
//示例 2：
//输入：nums1 = [1], m = 1, nums2 = [], n = 0
//输出：[1]
//Leetcode题目 : https://leetcode.com/problems/merge-sorted-array/
public class Problem_0088_MergeSortedArray {
    //采用从后往前合并的策略，避免了额外空间的使用
    //本算法的时间复杂度为O(m+n),时间复杂度为O(1)
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int index = nums1.length;//指向了nums1的当前需要填充的位置
        //如果两个数组中还有未处理的元素
        while (m > 0 && n > 0) {
            //比较两个数组中当前最大的未处理元素
            if (nums1[m - 1] >= nums2[n - 1]) {
                //将较大的元素放在nums1的末尾位置
                nums1[--index] = nums1[--m];
            } else {
                nums1[--index] = nums2[--n];
            }
        }
        while (m > 0) {
            nums1[--index] = nums1[--m];
        }
        while (n > 0) {
            nums1[--index] = nums2[--n];
        }
    }
}
