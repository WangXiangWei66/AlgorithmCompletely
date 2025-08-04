package Class30;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//给定一个二叉树，返回其节点值的锯齿形层序遍历。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
//Leetcode题目 : https://leetcode.com/problems/symmetric-tree/

//本代码使用双端队列来实现层序遍历
//时间复杂度为O(n)
public class Problem_0103_BinaryTreeZigzagLevelOrderTraversal {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        //创建结果列表，用于存储每一层的节点值
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        //使用双向链表作为双端队列，用于层序遍历过程中存储节点
        LinkedList<TreeNode> deque = new LinkedList<>();
        deque.add(root);
        int size = 0;//记录当前层的节点数量
        boolean isHead = true;//遍历当前层的遍历方向，true标识从左往右，false标识从右往左
        while (!deque.isEmpty()) {
            size = deque.size();
            List<Integer> curLevel = new ArrayList<>();//存储当前层的节点值
            for (int i = 0; i < size; i++) {
                TreeNode cur = isHead ? deque.pollFirst() : deque.pollLast();//判断队列取节点的方向
                curLevel.add(cur.val);
                if (isHead) {
                    if (cur.left != null) {
                        deque.addLast(cur.left);
                    }
                    if (cur.right != null) {
                        deque.addLast(cur.right);
                    }
                } else {
                    if (cur.right != null) {
                        deque.addFirst(cur.right);
                    }
                    if (cur.left != null) {
                        deque.addFirst(cur.left);
                    }
                }
            }
            ans.add(curLevel);//加入结果列表
            isHead = !isHead;//到下一层开始切换遍历的方向
        }
        return ans;
    }
}
