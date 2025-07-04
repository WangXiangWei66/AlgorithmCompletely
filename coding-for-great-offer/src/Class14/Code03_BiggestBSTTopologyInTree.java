package Class14;

import java.io.*;

//通过后序遍历的方式，递归计算每个节点作为头节点的最大拓扑结构的节点数
public class Code03_BiggestBSTTopologyInTree {

    public static int MAXN = 200001;

    public static int[][] tree = new int[MAXN][3];
    //record数组记录以节点 i 为根的最大 BST 拓扑大小
    public static int[] record = new int[MAXN];

    public static void main(String[] args) throws IOException {
        //BufferedReader：高效读取输入流
        //StreamTokenizer：解析输入标记（数字、字符串等）
        //PrintWriter：高效输出结果
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            for (int i = 1; i <= n; i++) {
                tree[i][0] = 0;
                tree[i][1] = 0;
                tree[i][2] = 0;
                record[i] = 0;
            }
            in.nextToken();
            int h = (int) in.nval;
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                int c = (int) in.nval;
                in.nextToken();
                int l = (int) in.nval;
                in.nextToken();
                int r = (int) in.nval;
                tree[l][0] = c;
                tree[l][1] = c;
                tree[c][1] = l;
                tree[c][2] = r;
            }
            out.println(maxBSTTopology(h));
            out.flush();
        }
    }

    //t:代表树
    // t[i][0] :代表i节点的父节点
    //t[i][1]代表i节点的左孩子
    //t[i][2]代表i节点的右孩子
    //m[i]代表以h为头的整棵树中，最大BST的拓扑结大小
    public static int maxBSTTopology(int h) {
        if (h == 0) {
            return 0;
        }
        int l = tree[h][1];
        int r = tree[h][2];
        //递归处理左右子树的最大子拓扑结构的大小
        int p1 = maxBSTTopology(l);
        int p2 = maxBSTTopology(r);
        //从左孩子开始，沿右路径向下查找
        int tmp = l;
        while (tmp < h && record[tmp] != 0) {
            tmp = tree[tmp][2];
        }
        //从左子树的总贡献中减去这个违规子树的大小
        record[l] -= record[tmp];
        //从右孩子开始，沿左路径向下查找
        tmp = r;
        while (tmp > h && record[tmp] != 0) {
            tmp = tree[tmp][1];
        }
        //从右子树的总贡献中减去这个违规子树的大小
        record[r] -= record[tmp];
        record[h] = record[l] + record[r] + 1;
        return Math.max(Math.max(p1, p2), record[h]);
    }
}
