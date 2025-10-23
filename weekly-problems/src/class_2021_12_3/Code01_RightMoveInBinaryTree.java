package class_2021_12_3;

//来自TME2022校园招聘后台开发/运营开发/业务运维/应用开发笔试
//给定一棵二叉树的头节点head，和一个正数k。从最底层开始，每一层右移k位，然后往上走过每一层，进行树的调整
//返回调整之后树的头节点
//具体详情请参加如下的测试页面
//测试链接 : https://www.nowcoder.com/test/33701596/summary
//本题目为该试卷第1题
public class Code01_RightMoveInBinaryTree {

    public static class TreeNode {
        int val;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }
    //存储层序遍历的节点序列
    public static TreeNode[] queue = new TreeNode[300000];
    //每一层最后一个节点在队列中的索引
    public static int[] ends = new int[50];

    //二叉树按层先移动，再重新构建关系
    //层序遍历记录节点 + 从下往上调整父子关系
    public static TreeNode cyclicshiftTree(TreeNode root, int k) {
        //记录队列头尾指针
        int l = 0;
        int r = 0;
        queue[r++] = root;
        int level = 0;//当前层数
        //将所有节点按层放入queue，并记录每层的结束位置
        while (l != r) {
            ends[level] = r;//当前层的结束位置
            //遍历当前层的所有节点
            while (l < ends[level]) {
                TreeNode cur = queue[l++];
                //将当前节点的左右子节点入队
                if (cur != null) {
                    queue[r++] = cur.left;
                    queue[r++] = cur.right;
                }
            }
            level++;//处理完毕当前层后，进入下一层
        }
        //从最底层开始往上遍历
        for (int i = level - 1; i > 0; i--) {
            //当前层的下一层的左右边界
            int downLeft = ends[i - 1];//下一层的起始索引
            int downRight = ends[i] - 1;//下一层的结束索引
            //下一层需要右移的有效位数
            int downRightSize = k % (downRight - downLeft + 1);
            //确定右移后，下一层的起始索引
            int downIndex = downRightSize == 0 ? downLeft : (downRight - downRightSize + 1);
            int curLeft = i - 2 >= 0 ? ends[i - 2] : 0;//获取当前层父节点的起始索引
            int curRight = ends[i - 1] - 1;
            for (int j = curLeft; j <= curRight; j++) {
                if (queue[j] != null) {//仅处理非空父节点
                    queue[j].left = queue[downIndex];
                    downIndex = nextIndex(downIndex, downLeft, downRight);
                    queue[j].right = queue[downIndex];
                    downIndex = nextIndex(downIndex, downLeft, downRight);
                }
            }
        }
        return root;
    }

    public static int nextIndex(int i, int l, int r) {
        //如果到达了右边界，则返回到左边界位置
        return i == r ? l : i + 1;
    }
}
