package Class34;

//给定一个包含n + 1 个整数的数组nums ，其数字都在 1 到 n之间（包括 1 和 n），可知至少存在一个重复的整数。
//假设 nums 只有 一个重复的整数 ，找出 这个重复的数 。
//你设计的解决方案必须不修改数组 nums 且只用常量级 O(1) 的额外空间。
//Leetcode题目 : https://leetcode.com/problems/find-the-duplicate-number/
public class Problem_0287_FindTheDuplicateNumber {

    public static int findDuplicate(int[] nums) {
        if (nums == null || nums.length < 2) {
            return -1;
        }
        int slow = nums[0];//慢指针从数组的第一个元素开始
        int fast = nums[nums[0]];//块指针，比慢指针多走一步
        //寻找环中的相遇点
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[nums[fast]];
        }
        fast = 0;//快慢指针相遇后，重置快指针为0
        //开始快慢指针都走一步，直到相遇
        while (slow != fast) {
            fast = nums[fast];
            slow = nums[slow];
        }
        return slow;
    }
}
