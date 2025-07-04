package Class14;

public class Code04_CompleteTreeNodeNumber {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    public static int countNodes(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return bs(head, 1, mostLeftLevel(head, 1));
    }
    //当前来到node节点，node节点在level层，总共有h层
    //返回node为头的子树中，有多少个节点
    public static int bs(TreeNode node, int Level, int h) {
        if (Level == h) {
            return 1;
        }
        //找根右树的最左节点
        if (mostLeftLevel(node.right, Level + 1) == h) {
            //到最后一层了，说明左树为满
            return (1 << (h - Level)) + bs(node.right, Level + 1, h);
        } else {
            //否则计算右树，他有h - 1 层
            return (1 << (h - Level - 1)) + bs(node.left, Level + 1, h);
        }
    }
    //如果node在第level层，求以node为头的子树中，最大高度为多少
    public static int mostLeftLevel(TreeNode node, int level) {
        while (node != null) {
            level++;
            node = node.left;
        }
        return level - 1;
    }
}
