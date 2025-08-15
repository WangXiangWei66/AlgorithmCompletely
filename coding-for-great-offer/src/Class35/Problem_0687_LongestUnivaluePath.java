package Class35;

//给定一个二叉树，找到最长的路径，这个路径中的每个节点具有相同值。 这条路径可以经过也可以不经过根节点。
//注意：两个节点之间的路径长度由它们之间的边数表示。
//注意: 给定的二叉树不超过10000个结点。 树的高度不超过1000。
//Leetcode题目 : https://leetcode.com/problems/longest-univalue-path/
public class Problem_0687_LongestUnivaluePath {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    public static int longestUniValuePath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        //减1是因为路径中路径的定义长度为边数
        return process(root).max - 1;
    }

    public static class Info {
        public int len;//路径必须以x出发，且只能往下走
        public int max;

        public Info(int l, int m) {
            len = l;
            max = m;
        }
    }

    private static Info process(TreeNode x) {
        if (x == null) {
            return new Info(0, 0);
        }
        TreeNode l = x.left;
        TreeNode r = x.right;

        Info linfo = process(l);
        Info rinfo = process(r);
        //计算以当前节点为起点的最长同值路径的长度
        int len = 1;
        if (l != null && l.val == x.val) {
            len = linfo.len + 1;
        }
        if (r != null && r.val == x.val) {
            len = Math.max(len, rinfo.len + 1);
        }
        int max = Math.max(Math.max(linfo.max, rinfo.max), len);
        if (l != null && r != null && l.val == x.val && r.val == x.val) {
            max = Math.max(max, linfo.len + rinfo.len + 1);//形成穿过当前节点的路径
        }
        return new Info(len, max);
    }
}
