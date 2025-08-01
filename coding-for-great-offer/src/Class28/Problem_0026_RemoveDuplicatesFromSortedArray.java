package Class28;

//给你一个有序数组 nums ，请你原地删除重复出现的元素，使每个元素只出现一次 ，返回删除后数组的新长度。
//不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1)额外空间的条件下完成。
//示例 1：
//输入：nums = [1,1,2]
//输出：2, nums = [1,2,_]
//解释：函数应该返回新的长度 2 ，并且原数组 nums 的前两个元素被修改为 1, 2 。不需要考虑数组中超出新长度后面的元素。
//示例 2：
//输入：nums = [0,0,1,1,1,2,2,3,3,4]
//输出：5, nums = [0,1,2,3,4,_,_,_,_,_]
//解释：函数应该返回新的长度 5 ， 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4 。不需要考虑数组中超出新长度后面的元素。
//Leetcode题目：https://leetcode.com/problems/remove-duplicates-from-sorted-array/
public class Problem_0026_RemoveDuplicatesFromSortedArray {

    public static int removeDuplicates(int[] nums) {
        if (nums == null) {
            return 0;
        }
        if (nums.length < 2) {
            return nums.length;
        }
        int done = 0;//记录已经处理好的（去重后）元素的最后索引
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[done]) {
                nums[++done] = nums[i];
            }
        }
        return done + 1;//因为索引下标是从0开始的
    }
}
