package Class37;

//给你二叉树的根结点 root ，请你将它展开为一个单链表：
//展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
//展开后的单链表应该与二叉树 先序遍历 顺序相同。
//Leetcode题目 : https://leetcode.com/problems/flatten-binary-tree-to-linked-list/
public class Problem_0114_FlattenBinaryTreeToLinkedList {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int value) {
            val = value;
        }
    }

    //时间复杂度为O(N),空间复杂度为O(h)
    public static void flatten1(TreeNode root) {
        process(root);
    }

    public static class Info {
        public TreeNode head;//子树展开为链表后的头
        public TreeNode tail;//子树展开后链表的尾

        public Info(TreeNode h, TreeNode t) {
            head = h;
            tail = t;
        }
    }

    public static Info process(TreeNode head) {
        if (head == null) {
            return null;
        }
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);
        head.left = null;
        head.right = leftInfo == null ? null : leftInfo.head;
        TreeNode tail = leftInfo == null ? head : leftInfo.tail;
        tail.right = rightInfo == null ? null : rightInfo.head;
        tail = rightInfo == null ? tail : rightInfo.tail;
        return new Info(head, tail);
    }
    //时间复杂度为O(N),空间复杂度为O(1)
    public static void flatten2(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode pre = null;//记录前一个处理的节点
        TreeNode cur = root;//当前处理的节点
        TreeNode mostRight = null;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    if (pre != null) {
                        pre.left = cur;//记录先序顺序
                    }
                    pre = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            } else {//当前节点没有左子树
                if (pre != null) {
                    pre.left = cur;
                }
                pre = cur;
            }
            cur = cur.right;
        }
        cur = root;
        TreeNode next = null;
        while (cur != null) {
            next = cur.left;
            cur.left = null;
            cur.right = next;
            cur = next;
        }
    }
}
