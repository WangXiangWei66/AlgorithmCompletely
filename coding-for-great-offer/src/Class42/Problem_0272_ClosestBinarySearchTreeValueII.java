package Class42;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

//给定一棵搜索二叉树的头节点root，搜索二叉树默认是没有重复值的，给定一个double类型的数target，给定一个正数k
//请返回在这棵二叉树里最接近target的k个值，作为链表返回
//要求：如果二叉树的节点个数为n，而k相比于n较小的话，实现时间复杂度低于O(n)的方法
//leetcode题目 : https://leetcode.com/problems/closest-binary-search-tree-value-ii/
public class Problem_0272_ClosestBinarySearchTreeValueII {
    //搜索二叉树：左小右大
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static List<Integer> closestKValues(TreeNode root, double target, int k) {
        List<Integer> ret = new LinkedList<>();
        Stack<TreeNode> moreTops = new Stack<>();//栈顶是“≥target”的最小节点
        Stack<TreeNode> lessTops = new Stack<>();//栈顶是“≤target”的最大节点
        getMoreTops(root, target, moreTops);
        getLessTops(root, target, lessTops);
        if (!moreTops.isEmpty() && !lessTops.isEmpty() && moreTops.peek().val == lessTops.peek().val) {
            getPredecessor(lessTops);//弹出一个重复节点
        }
        while (k-- > 0) {
            if (moreTops.isEmpty()) {
                ret.add(getPredecessor(lessTops));
            } else if (lessTops.isEmpty()) {
                ret.add(getSuccessor(moreTops));
            } else {
                //寻找栈顶更小的
                double diffs = Math.abs((double) moreTops.peek().val - target);
                double diffp = Math.abs((double) lessTops.peek().val - target);
                if (diffs < diffp) {
                    ret.add(getSuccessor(moreTops));
                } else {
                    ret.add(getPredecessor(lessTops));
                }
            }
        }
        return ret;
    }
    //栈顶是大于target的最小节点
    public static void getMoreTops(TreeNode root, double target, Stack<TreeNode> moreTops) {
        while (root != null) {
            if (root.val == target) {
                moreTops.push(root);
                break;
            } else if (root.val > target) {
                moreTops.push(root);
                root = root.left;
            } else {
                root = root.right;
            }
        }
    }
    //栈顶是小于target的最大节点
    public static void getLessTops(TreeNode root, double target, Stack<TreeNode> lessTops) {
        while (root != null) {
            if (root.val == target) {
                lessTops.push(root);
                break;
            } else if (root.val < target) {
                lessTops.push(root);
                root = root.right;
            } else {
                root = root.left;
            }
        }
    }
    //从lessTops栈中弹出当前栈顶，并更新栈，使其下一次弹出的是“当前节点的前驱节点”
    public static int getPredecessor(Stack<TreeNode> lessTops) {
        TreeNode cur = lessTops.pop();
        int ret = cur.val;
        cur = cur.left;
        //当前节点的前驱节点是左子树的最右节点
        while (cur != null) {
            lessTops.push(cur);
            cur = cur.right;
        }
        return ret;
    }

    public static int getSuccessor(Stack<TreeNode> moreTops) {
        TreeNode cur = moreTops.pop();
        int ret = cur.val;
        cur = cur.right;
        while (cur != null) {
            moreTops.push(cur);
            cur = cur.left;
        }
        return ret;
    }
}
