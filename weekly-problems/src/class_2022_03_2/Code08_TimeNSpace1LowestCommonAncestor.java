package class_2022_03_2;

//如何时间复杂度O(N)，额外空间复杂度O(1)，解决最低公共祖先问题?
//测试链接 : https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
public class Code08_TimeNSpace1LowestCommonAncestor {
    //本代码采用了Morris遍历
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }
    //判断leftAim是否以head为根的左子树上
    public static boolean findLeftAim(TreeNode head, TreeNode leftAim) {
        TreeNode tail = reverseEdge(head);
        TreeNode cur = tail;
        boolean ans = false;
        while (cur != null) {
            if (cur == leftAim) {
                ans = true;
            }
            cur = cur.right;
        }
        reverseEdge(tail);
        return ans;
    }

    public static TreeNode reverseEdge(TreeNode from) {
        TreeNode pre = null;
        TreeNode next = null;
        while (from != null) {
            next = from.right;
            from.right = pre;
            pre = from;
            from = next;
        }
        return pre;//最终返回反转后的头节点
    }
    //使用Morris遍历，寻找o1，o2首次出现的节点
    public static TreeNode findFirst(TreeNode head, TreeNode o1, TreeNode o2) {
        if (head == null) {
            return null;
        }
        TreeNode cur = head;
        TreeNode mostRight = null;
        TreeNode first = null;//记录第一个遇到的o1或o2
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    //记录第一个遇到的目标节点
                    if (first == null && (cur == o1 || cur == o2)) {
                        first = cur;
                    }
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                }
            } else {
                //没有左子树
                if (first == null && (cur == o1 || cur == o2)) {
                    first = cur;
                }
            }
            cur = cur.right;//移动到右子树或者回退到父节点
        }
        return first;
    }

    public static TreeNode lowestCommonAncestor(TreeNode head, TreeNode o1, TreeNode o2) {
        //o1是o2的祖先
        if (findFirst(o1.left, o1, o2) != null || findFirst(o1.right, o1, o2) != null) {
            return o1;
        }
        //o2是o1的 祖先
        if (findFirst(o2.left, o1, o2) != null || findFirst(o2.right, o1, o2) != null) {
            return o2;
        }
        //两者互不为祖先
        TreeNode leftAim = findFirst(head, o1, o2);//寻找两者先出现的节点
        TreeNode cur = head;
        TreeNode mostRight = null;
        TreeNode ans = null;
        //再次使用Morris遍历
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    mostRight.right = null;
                    //leftAim是否在当前节点的左子树上
                    if (findLeftAim(cur.left, leftAim)) {
                        //判断另一个节点是否在leftAim的右子树上
                        if (ans == null && findFirst(leftAim.right, o1, o2) != null) {
                            ans = leftAim;
                        }
                        leftAim = cur;
                    }
                }
            }
            cur = cur.right;
        }
        return ans != null ? ans : (findFirst(leftAim.right, o1, o2) != null ? leftAim : head);
    }
}
