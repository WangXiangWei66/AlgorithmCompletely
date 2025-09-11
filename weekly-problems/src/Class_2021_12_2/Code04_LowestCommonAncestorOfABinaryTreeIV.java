package Class_2021_12_2;

import java.util.HashSet;

//已知一棵二叉树上所有的值都不一样，
//给定这棵二叉树的头节点head，
//给定一个整型数组arr，arr里放着不同的值，每个值一定在树上
//返回数组里所有值的最低公共祖先
//Leetcode链接 : https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iv/
public class Code04_LowestCommonAncestorOfABinaryTreeIV {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode[] nodes) {
        HashSet<Integer> set = new HashSet<>();
        for (TreeNode node : nodes) {
            set.add(node.val);
        }
        return process(root, set, set.size()).find;
    }

    public static class Info {
        public TreeNode find;  //是否找到
        public int remove;//本节点及其子树包含的目标节点数量

        public Info(TreeNode f, int r) {
            find = f;
            remove = r;
        }
    }

    public static Info process(TreeNode x, HashSet<Integer> set, int all) {
        if (x == null) {
            return new Info(null, 0);
        }
        Info left = process(x.left, set, all);
        if (left.find != null) {
            return left;
        }
        Info right = process(x.right, set, all);
        if (right.find != null) {
            return right;
        }
        //检查当前节点是否为目标节点之1
        int cur = set.contains(x.val) ? 1 : 0;
        set.remove(x.val);
        if (left.remove + right.remove + cur == all) {
            return new Info(x, all);
        } else {
            //返回已经累积的，并继续往上查
            return new Info(null, left.remove + right.remove + cur);
        }
    }
}
