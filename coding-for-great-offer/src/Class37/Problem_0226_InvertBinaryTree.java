package Class37;

//翻转一棵二叉树
//任何节点的左右两个孩子交换
//Leetcode题目 : https://leetcode.com/problems/invert-binary-tree/
public class Problem_0226_InvertBinaryTree {

    public class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    public static TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        TreeNode left = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(left);
        return root;
    }
}
