package Class30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

//路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。
//同一个节点在一条路径序列中至多出现一次.该路径至少包含一个节点,且不一定经过根节点。
//路径和 是路径中各节点值的总和。
//给你一个二叉树的根节点 root ，返回其最大路径和。
//进阶：
//如果返回最大路径和上的所有节点，该怎么做？
//Leetcode题目 : https://leetcode.com/problems/binary-tree-maximum-path-sum/
public class Problem_0124_BinaryTreeMaximumPathSum {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    //计算二叉树的最大路径和
    //时间复杂度为O(N)
    //空间复杂度为O(h)
    public static int maxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return process(root).maxPathSum;
    }

    //存储每个节点的两个关键信息
    public static class Info {
        public int maxPathSum;//以当前节点为根的子树中的最大路径和
        public int maxPathSumFromHead;//从当前节点触发向下延申的最大路径和

        public Info(int path, int head) {
            maxPathSum = path;
            maxPathSumFromHead = head;
        }
    }

    public static Info process(TreeNode x) {
        if (x == null) {
            return null;
        }
        Info leftInfo = process(x.left);
        Info rightInfo = process(x.right);
        //计算从当前节点出发向下延申的最大路径和
        int maxPathSumFromHead = x.val;
        //如果左子树有有效信息，考虑加上左子树的路径和
        if (leftInfo != null) {
            maxPathSumFromHead = Math.max(maxPathSumFromHead, x.val + leftInfo.maxPathSumFromHead);
        }
        //如果右子树上有有效信息，考虑加上右子树的路径和
        if (rightInfo != null) {
            maxPathSumFromHead = Math.max(maxPathSumFromHead, x.val + rightInfo.maxPathSumFromHead);
        }
        //计算以当前节点为根子树的最大路径和
        int maxPathSum = x.val;
        // 比较左子树中的最大路径和
        if (leftInfo != null) {
            maxPathSum = Math.max(maxPathSum, leftInfo.maxPathSum);
        }
        // 比较右子树中的最大路径和
        if (rightInfo != null) {
            maxPathSum = Math.max(maxPathSum, rightInfo.maxPathSum);
        }
        //比较从当前节点出发的最大路径和
        maxPathSum = Math.max(maxPathSumFromHead, maxPathSum);
        // 考虑当前节点连接左右子树形成的路径和（如果左右子树的路径和都为正）
        if (leftInfo != null && rightInfo != null && leftInfo.maxPathSumFromHead > 0 && rightInfo.maxPathSumFromHead > 0) {
            maxPathSum = Math.max(maxPathSum, leftInfo.maxPathSumFromHead + rightInfo.maxPathSumFromHead + x.val);
        }
        //返回当前节点的信息
        return new Info(maxPathSum, maxPathSumFromHead);
    }

    public static List<TreeNode> getMaxSumPath(TreeNode head) {
        List<TreeNode> ans = new ArrayList<>();//存储最终的最大路径节点
        if (head != null) {
            Data data = f(head);//计算最大路径和的关键信息
            HashMap<TreeNode, TreeNode> fmap = new HashMap<>();//存储节点的父节点映射
            fmap.put(head, head);//根节点的父节点设为自身
            fatherMap(head, fmap);//构建父节点映射表
            fillPath(fmap, data.from, data.to, ans);//根据路径端点填充完整路径
        }
        return ans;
    }

    public static class Data {
        public int maxAllSum;//整棵树的最大路径和
        public TreeNode from;//最大路径的起点
        public TreeNode to;//最大路径的终点
        public int maxHeadSum;//从当前节点出发向下延申的最大路径和
        public TreeNode end;//上述路径的终点（用于拼接路径）

        public Data(int a, TreeNode b, TreeNode c, int d, TreeNode e) {
            maxAllSum = a;
            from = b;
            to = c;
            maxHeadSum = d;
            end = e;
        }
    }

    public static Data f(TreeNode x) {
        if (x == null) {
            return null;
        }
        Data l = f(x.left);
        Data r = f(x.right);
        //计算：从当前节点x出发向下延伸的最大路径和（maxHeadSum）及终点（end）
        int maxHeadSum = x.val;
        TreeNode end = x;
        //拼接左子树的路径
        if (l != null && l.maxHeadSum > 0 && (r == null || l.maxHeadSum > r.maxHeadSum)) {
            maxHeadSum += l.maxHeadSum;
            end = l.end;
        }
        //拼接右子树的路径
        if (r != null && r.maxHeadSum > 0 && (l == null || r.maxHeadSum > l.maxHeadSum)) {
            maxHeadSum += r.maxHeadSum;
            end = r.end;
        }
        //计算以x为根的子树中，全局最大路径和及路径端点
        int maxAllSum = Integer.MIN_VALUE;
        TreeNode from = null;
        TreeNode to = null;
        //最大路径和在左树中
        if (l != null) {
            maxAllSum = l.maxAllSum;
            from = l.from;
            to = l.to;
        }
        //如果右子树更大
        if (r != null && r.maxAllSum > maxAllSum) {
            maxAllSum = r.maxAllSum;
            from = r.from;
            to = r.to;
        }
        //最大路径和经过节点x，连接左右子树
        int p3 = x.val + (l != null && l.maxHeadSum > 0 ? l.maxHeadSum : 0) + (r != null && r.maxHeadSum > 0 ? r.maxHeadSum : 0);
        if (p3 > maxAllSum) {
            maxAllSum = p3;
            //找到左右端点
            from = (l != null && l.maxHeadSum > 0) ? l.end : x;
            to = (r != null && r.maxHeadSum > 0) ? r.end : x;
        }
        return new Data(maxAllSum, from, to, maxHeadSum, end);
    }

    public static void fatherMap(TreeNode h, HashMap<TreeNode, TreeNode> map) {
        if (h.left == null && h.right == null) {
            return;
        }
        if (h.left != null) {
            map.put(h.left, h);
            fatherMap(h.left, map);//递归处理左子树
        }
        if (h.right != null) {
            map.put(h.right, h);
            fatherMap(h.right, map);//递归处理右子树
        }
    }

    //fmap（父节点映射）、路径起点（data.from）和终点（data.to），将完整路径的节点按顺序填充到 ans 列表中。
    public static void fillPath(HashMap<TreeNode, TreeNode> fmap, TreeNode a, TreeNode b, List<TreeNode> ans) {
        if (a == b) {
            ans.add(a);//起点与终点相同，直接添加该节点
        } else {
            //收集起点a到根节点的所有节点（即寻找LCA)
            HashSet<TreeNode> ap = new HashSet<>();
            TreeNode cur = a;
            //根节点的父节点是自身，终止循环
            while (cur != fmap.get(cur)) {
                ap.add(cur);
                cur = fmap.get(cur);
            }
            ap.add(cur);//将根节点加进去
            //寻找a和b的最近公共祖先
            cur = b;
            TreeNode lca = null;
            while (lca == null) {
                if (ap.contains(cur)) {
                    lca = cur;//第一个在a的路径中出现的节点即为LCA
                } else {
                    cur = fmap.get(cur);
                }
            }
            //从a回溯到LCA
            while (a != lca) {
                ans.add(a);
                a = fmap.get(a);
            }
            ans.add(lca);
            //从b回溯到LCA，反向加入路径
            ArrayList<TreeNode> right = new ArrayList<>();
            while (b != lca) {
                right.add(b);//从b到LCA的节点暂存到right列表
                b = fmap.get(b);
            }
            for (int i = right.size() - 1; i >= 0; i--) {
                ans.add(right.get(i));
            }
        }
    }

    public static void main(String[] args) {
        TreeNode head = new TreeNode(4);
        head.left = new TreeNode(-7);
        head.right = new TreeNode(-5);
        head.left.left = new TreeNode(9);
        head.left.right = new TreeNode(9);
        head.right.left = new TreeNode(4);
        head.right.right = new TreeNode(3);

        List<TreeNode> maxPath = getMaxSumPath(head);
        for (TreeNode n : maxPath) {
            System.out.println(n.val);
        }
    }
}
