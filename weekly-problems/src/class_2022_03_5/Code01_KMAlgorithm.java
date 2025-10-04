package class_2022_03_5;
//km算法流程详解、代码实现，求解最大权重匹配
/*
KM 算法（Kuhn-Munkres 算法）是专为解决二分图最大权重匹配问题设计的高效算法，其核心思路是通过构建 “相等子图” 和调整 “顶标” 来逐步找到最优匹配。以下是其实现思路的详细解析：

一、核心概念铺垫
二分图：节点分为两个不相交集合（左侧集合 U、右侧集合 V），所有边仅存在于 U 和 V 之间。
完美匹配：每个节点都被匹配，且每个节点仅匹配一次。
顶标：为每个节点分配一个数值（左侧节点为lx[i]，右侧节点为ly[j]），需满足对任意边(i,j)有 lx[i] + ly[j] ≥ weight(i,j)（权重）。
相等子图：由所有满足 lx[i] + ly[j] = weight(i,j) 的边构成的子图。
关键性质：若相等子图中存在完美匹配，则该匹配就是原问题的最大权重匹配。

二、算法核心流程
KM 算法通过 “初始化顶标→寻找增广路径→调整顶标” 的循环，逐步扩大相等子图，直到找到完美匹配。
1. 初始化顶标
左侧节点顶标 lx[i]：设为该节点连接的最大边权重（确保 lx[i] + ly[j] ≥ weight(i,j)）。
右侧节点顶标 ly[j]：初始化为 0。
目的：构建初始相等子图（包含所有最大权重边）。
2. 为每个左侧节点寻找匹配
对左侧每个节点 u，通过深度优先搜索（DFS） 在相等子图中寻找增广路径：
增广路径：从未匹配的左侧节点出发，交替经过未匹配边和已匹配边，最终到达未匹配的右侧节点的路径。
匹配逻辑：
若找到增广路径，沿路径更新匹配关系（翻转匹配状态）。
若未找到，记录 “松弛量”（右侧节点加入相等子图所需的最小顶标调整值），进入顶标调整阶段。
3. 调整顶标（核心步骤）
当无法找到增广路径时，通过调整顶标扩大相等子图：
计算调整量 d：取未加入交错树（DFS 中未访问）的右侧节点的最小松弛量。
更新顶标：
左侧节点在交错树中：lx[i] -= d（降低顶标，让更多边进入相等子图）。
右侧节点在交错树中：ly[j] += d（提高顶标，维持 lx[i] + ly[j] ≥ weight(i,j) 条件）。
调整后，原相等子图的边仍保留，且新增部分边进入相等子图。
4. 循环直至完美匹配
重复 “寻找增广路径→调整顶标” 的过程，直到所有左侧节点都找到匹配（形成完美匹配）。此时，所有匹配边的权重和即为最大权重和（等于所有顶标之和）。

三、关键数据结构
match[j]：记录右侧节点 j 匹配的左侧节点（-1 表示未匹配）。
lx[i]/ly[j]：左侧 / 右侧节点的顶标数组。
x[i]/y[j]：标记节点是否在当前交错树中（DFS 过程中使用）。
slack[j]：右侧节点 j 的松弛量（lx[i] + ly[j] - weight(i,j) 的最小值）。

四、示例流程
假设二分图左侧节点 U = {u1, u2}，右侧节点 V = {v1, v2}，权重矩阵为：
plaintext
|   | v1 | v2 |
| u1| 3  | 5  |
| u2| 2  | 4  |
初始化顶标：lx = [5,4]（u1 的最大边是 5，u2 的最大边是 4），ly = [0,0]。
为 u1 找匹配：
相等子图包含边 (u1,v2)（5+0=5），匹配 u1→v2，match[1] = 0。
为 u2 找匹配：
相等子图包含边 (u2,v2)（4+0=4），但 v2 已匹配。
DFS 尝试调整 u2 的匹配，未找到路径，计算松弛量 slack[v1] = 4+0-2=2。
调整顶标：d=2，lx = [5-2=3,4-2=2]，ly = [0,0+2=2]。
重新为 u2 找匹配：
新相等子图包含 (u2,v1)（2+0=2），匹配 u2→v1，形成完美匹配。
结果：总权重为 (u1→v2:5) + (u2→v1:2) = 7（等于顶标和 3+2 + 0+2 =7）。

五、时间复杂度
对每个左侧节点，最多调整 O(N) 次顶标，每次调整和 DFS 的复杂度为 O(N²)。
整体时间复杂度为 O(N³)，适合处理中等规模（N < 1000）的二分图问题。
KM 算法通过顶标和相等子图的巧妙设计，高效地将 “最大权重匹配” 问题转化为 “完美匹配” 问题，避免了回溯法的指数级复杂度，是解决二分图优化问题的经典方案。
 */
import java.util.Arrays;

// 注意本文件中，graph不是邻接矩阵的含义，而是一个二部图
// 在长度为N的邻接矩阵matrix中，所有的点有N个，matrix[i][j]表示点i到点j的距离或者权重
// 而在二部图graph中，所有的点有2*N个，行所对应的点有N个，列所对应的点有N个
// 而且认为，行所对应的点之间是没有路径的，列所对应的点之间也是没有路径的！
public class Code01_KMAlgorithm {

    public static int right(int[][] graph) {
        int N = graph.length;
        int[] to = new int[N];//标记右侧节点是否已被匹配
        for (int i = 0; i < N; i++) {
            to[i] = 1;//1标识可用，0标识已匹配
        }
        return process(0, to, graph);//返回最终的最大权重和
    }
    //from：当前需要匹配的左侧节点索引
    public static int process(int from, int[] to, int[][] graph) {
        if (from == graph.length) {
            return 0;
        }
        int ans = 0;
        for (int i = 0; i < to.length; i++) {
            if (to[i] == 1) {
                to[i]=0;
                ans = Math.max(ans, graph[from][i] + process(from + 1, to, graph));
                to[i] = 1;
            }
        }
        return ans;
    }

    public static int km(int[][] graph) {
        int N = graph.length;//获取二分图的规模，左右都为N
        int[] match = new int[N];//右侧节点匹配的左侧节点
        int[] lx = new int[N];//左侧节点的顶标，用于构建相等子图
        int[] ly = new int[N];//右侧节点的顶标
        boolean[] x = new boolean[N];//左侧节点是否在交错树中
        boolean[] y = new boolean[N];//右侧节点是否在交错树中
        //松弛量：顶标和-边权重
        int[] slack = new int[N];//右侧节点的最小松弛量（用于顶标调整）
        int invalid = Integer.MAX_VALUE;
        //初始化顶标
        for (int i = 0; i < N; i++) {
            match[i] = -1;//所有右侧节点都未匹配
            lx[i] = -invalid;
            for (int j = 0; j < N; j++) {
                lx[i] = Math.max(lx[i], graph[i][j]);//左侧节点的顶标初始化为与他相连边的最大权重
            }
            ly[i] = 0;//右侧节点的顶标设为0
        }
        //为每个左侧节点寻找匹配
        for (int from = 0; from < N; from++) {
            for (int i = 0; i < N; i++) {
                slack[i] = invalid;
            }
            //重置交错树标记
            Arrays.fill(x, false);
            Arrays.fill(y, false);
            while (!dfs(from, x, y, lx, ly, match, slack, graph)) {
                //计算顶标调整量d
                int d = invalid;
                for (int i = 0; i < N; i++) {
                    if (!y[i] && slack[i] < d) {
                        d = slack[i];
                    }
                }
                //左侧在交错树上的顶标-d
                for (int i = 0; i < N; i++) {
                    if (x[i]) {
                        lx[i] = lx[i] - d;
                    }
                    //右侧在交错树上的+d
                    if (y[i]) {
                        ly[i] = ly[i] + d;
                    }
                }
                Arrays.fill(x, false);
                Arrays.fill(y, false);
            }
        }
        int ans = 0;
        for (int i = 0; i < N; i++) {
            ans += (lx[i] + ly[i]);
        }
        return ans;
    }
    // from: 当前要匹配的左侧节点
    // x, y: 标记节点是否在交错树中
    // lx, ly: 左右侧节点的顶标
    // match: 右侧节点的匹配状态
    // slack: 右侧节点的松弛量
    // map: 二部图的权重矩阵
    // 返回值: 当前左侧节点是否能找到匹配
    public static boolean dfs(int from, boolean[] x, boolean[] y, int[] lx, int[] ly, int[] match, int[] slack, int[][] map) {
        int N = map.length;
        x[from] = true;
        for (int to = 0; to < N; to++) {
            //仅处理未加入交错树的节点
            if (!y[to]) {
                int d = lx[from] + ly[to] - map[from][to];
                //边不是相等子图
                if (d != 0) {
                    slack[to] = Math.min(slack[to], d);
                } else {
                    y[to] = true;//将右侧节点加入交错树
                    if (match[to] == -1 || dfs(match[to], x, y, lx, ly, match, slack, map)) {
                        match[to] = from;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //生成了N阶无向带权图
    public static int[][] randomGraph(int N, int V) {
        int[][] graph = new int[N][N];
        //遍历上三角形
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                int num = (int) (Math.random() * V);
                graph[i][j] = num;
                graph[j][i] = num;
            }
        }
        return graph;
    }

    // 为了测试
    public static void main(String[] args) {
        int N = 10;
        int V = 20;
        int testTime = 100;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[][] graph = randomGraph(N, V);
            int ans1 = right(graph);
            int ans2 = km(graph);
            if (ans1 != ans2) {
                System.out.println("Oops!");
                for (int r = 0; r < graph.length; r++) {
                    for (int c = 0; c < graph.length; c++) {
                        System.out.print(graph[r][c] + " ");
                    }
                    System.out.println();
                }
                System.out.println(ans1);
                System.out.println(ans2);
            }
        }
        System.out.println("测试结束");
    }
}
