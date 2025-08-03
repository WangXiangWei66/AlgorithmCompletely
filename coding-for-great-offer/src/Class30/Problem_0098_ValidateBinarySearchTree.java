package Class30;
//给定一个二叉树，判断其是否是一个有效的二叉搜索树。
//二叉搜索树的核心性质：中序遍历的结果为严格递增序列
public class Problem_0098_ValidateBinarySearchTree {
    //本代码使用了“Morris”中序遍历算法，在不使用递归栈的情况下，验证一颗二叉树是否为有效的二叉搜索树
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        TreeNode cur = root;
        TreeNode mostRight = null;//morris遍历中，寻找左子树的最右节点
        Integer pre = null;//中序遍历中前一个节点的val值
        boolean ans = true;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                //寻找cur左子树的最右节点
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            }
            if (pre != null && pre >= cur.val) {
                ans = false;
            }
            pre = cur.val;
            cur = cur.right;
        }
        return ans;
    }
}
