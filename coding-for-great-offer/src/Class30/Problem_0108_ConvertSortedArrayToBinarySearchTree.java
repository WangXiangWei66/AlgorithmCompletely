package Class30;

//给你一个整数数组 nums ，其中元素已经按 升序 排列，请你将其转换为一棵 高度平衡 二叉搜索树。
//高度平衡二叉树是一棵满足每个节点的左右两个子树的高度差的绝对值不超过1的二叉树
//Leetcode题目 : https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/
public class Problem_0108_ConvertSortedArrayToBinarySearchTree {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    //时间复杂度为O(n),空间复杂度为O(logn)
    public TreeNode sortedArrayToBST(int[] nums) {
        return process(nums, 0, nums.length - 1);
    }

    //将数组[L,R]范围转化为平衡二叉搜索树
    public static TreeNode process(int[] nums, int L, int R) {
        if (L > R) {
            return null;
        }
        if (L == R) {
            return new TreeNode(nums[L]);//直接只有一个元素时，直接创建节点返回
        }
        //使用中间元素作为根节点来保证左右子树的节点数量尽可能的均衡
        int M = (L + R) / 2;
        TreeNode head = new TreeNode(nums[M]);
        head.left = process(nums, L, M - 1);
        head.right = process(nums, M + 1, R);
        return head;
    }
}
