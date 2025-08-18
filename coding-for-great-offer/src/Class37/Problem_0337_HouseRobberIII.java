package Class37;
//在二叉树结构中，每个节点代表一户人家
//节点的值表示该户的财产。
//要求不能抢劫相邻的两户（即如果抢劫了父节点，就不能抢劫子节点，反之亦然），需要找到能抢劫到的最大财产总和。
public class Problem_0337_HouseRobberIII {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }
    //计算能抢到的最大财产和
    public static int rob(TreeNode root) {
        Info info = process(root);
        return Math.max(info.no, info.yes);
    }


    public static class Info {
        public int no;//不抢劫当前节点时，以当前节点为根的最大收益
        public int yes;//抢劫当前节点时，以当前节点为根的最大收益

        public Info(int n, int y) {
            no = n;
            yes = y;
        }
    }

    public static Info process(TreeNode x) {
        if (x == null) {
            return new Info(0, 0);
        }
        Info leftInfo = process(x.left);
        Info rightInfo = process(x.right);
        int no = Math.max(leftInfo.no, leftInfo.yes) + Math.max(rightInfo.no, rightInfo.yes);
        int yes = x.val + leftInfo.no + leftInfo.yes;
        return new Info(no, yes);
    }
}
