package Class30;


public class Code02_MinDepth {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int x) {
            val = x;
        }
    }

    public static int minDepth1(TreeNode head) {
        if (head == null) {
            return 0;
        }

        return p(head);
    }
    //计算X为头的子树的最小深度
    public static int p(TreeNode x) {
        if (x.left == null && x.right == null) {
            return 1;
        }
        //去二叉树的左右递归去拿深度值
        int leftH = Integer.MAX_VALUE;
        if (x.left != null) {
            leftH = p(x.left);
        }
        int rightH = Integer.MAX_VALUE;
        if (x.right != null) {
            rightH = p(x.right);
        }
        return 1 + Math.min(leftH, rightH);
    }

    public static int minDepth2(TreeNode head) {
        if (head == null) {
            return 0;
        }
        TreeNode cur = head;
        TreeNode mostRight = null;
        int curLevel = 0;
        int minHeight = Integer.MAX_VALUE;
        while (cur != null) {
            mostRight = cur.left;
            if (mostRight != null) {
                //记录当前左子树右边界数量为1
                int rightBoardSize = 1;
                //找到最右节点，沿途记录右边界节点的数量
                while (mostRight.right != null && mostRight.right != cur) {
                    rightBoardSize++;
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    curLevel++;
                    mostRight.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    if (mostRight.left == null) {
                        minHeight = Math.min(minHeight, curLevel);
                    }
                    curLevel -= rightBoardSize;
                    mostRight.right = null;
                }
            } else {
                //移到下一层了
                curLevel++;
            }
            cur = cur.right;
        }
    //该部分已经成功处理了回指行为
        int finalRight = 1;
        cur = head;
        while (cur.right != null) {
            finalRight++;
            cur = cur.right;
        }
        if (cur.left == null && cur.right == null) {
            minHeight = Math.min(minHeight, finalRight);
        }
        return minHeight;
    }
}
