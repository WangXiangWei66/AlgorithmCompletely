package class_2022_10_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//来自Leetcode周赛
//魔物占领了若干据点，这些据点被若干条道路相连接，
//roads[i] = [x, y] 表示编号 x、y 的两个据点通过一条道路连接。
//现在勇者要将按照以下原则将这些据点逐一夺回：
//在开始的时候，勇者可以花费资源先夺回一些据点，
//初始夺回第 j 个据点所需消耗的资源数量为 cost[j]
//接下来，勇者在不消耗资源情况下，
//每次可以夺回一个和「已夺回据点」相连接的魔物据点，
//并对其进行夺回
//为了防止魔物暴动，勇者在每一次夺回据点后（包括花费资源夺回据点后），
//需要保证剩余的所有魔物据点之间是相连通的（不经过「已夺回据点」）。
//请返回勇者夺回所有据点需要消耗的最少资源数量。
//输入保证初始所有据点都是连通的，且不存在重边和自环
//测试链接 : https://leetcode.cn/problems/s5kipK/
public class Code02_CaptureStrongHold {
    //cost[i]:初始夺回第 i 个据点所需消耗的资源数量
    //roads[i] = [x, y] 表示编号 x、y 的两个据点通过一条道路连接
    public static long minimumCost(int[] cost, int[][] roads) {
        int n = cost.length;
        if (n == 1) {
            return cost[0];
        }
        int m = roads.length;
        // 构建点双连通分量结构
        DoubleConnectedComponents dc = new DoubleConnectedComponents(n, m, roads);
        long ans = 0;
        // 整个图是一个双连通分量
        if (dc.dcc.size() == 1) {
            ans = Integer.MAX_VALUE;
            for (int num : cost) {
                //选成本最小的据点初始夺回
                ans = Math.min(ans, num);
            }
        } else { // 不只一个点双连通分量
            ArrayList<Integer> arr = new ArrayList<>();
            for (List<Integer> set : dc.dcc) {
                //分量内割点的数量
                int cutCnt = 0;
                //分量内非割点的最小成本
                int curCost = Integer.MAX_VALUE;
                for (int nodes : set) {
                    if (dc.cut[nodes]) {
                        cutCnt++;
                    } else {
                        curCost = Math.min(curCost, cost[nodes]);
                    }
                }
                if (cutCnt == 1) {
                    arr.add(curCost);
                }
            }
            arr.sort((a, b) -> a - b);
            for (int i = 0; i < arr.size() - 1; i++) {
                ans += arr.get(i);
            }
        }
        return ans;
    }

    public static class DoubleConnectedComponents {
        // 链式前向星存储图
        public int[] head; // head[u]表示u的第一条边的索引
        public int[] next; // next[i]表示第i条边的下一条边索引
        public int[] to;   // to[i]表示第i条边指向的节点

        public int[] dfn;       // 节点发现时间
        public int[] low;       // 节点能到达的最早发现时间（点双连通分量专用）
        public int[] stack;     // 栈：存储当前DFS路径的节点
        public List<List<Integer>> dcc; // 所有点双连通分量
        public boolean[] cut;   // 标记节点是否为割点
        public static int edgeCnt; // 边计数器
        public static int dfnCnt;  // 发现时间计数器
        public static int top;      // 栈顶指针
        public static int root;     // DFS根节点

        // 构造方法：初始化并构建点双连通分量
        public DoubleConnectedComponents(int n, int m, int[][] roads) {
            init(n, m);        // 初始化变量
            createGraph(roads); // 构建图（链式前向星）
            creatDcc(n);       // 计算点双连通分量和割点
        }

        private void init(int n, int m) {
            head = new int[n];
            Arrays.fill(head, -1); // 初始化为-1，表示无邻接边
            next = new int[m << 1]; // m<<1等价于2*m，存储所有边的下一条边索引
            to = new int[m << 1];   // 存储每条边指向的节点
            dfn = new int[n];       // 初始化为0（未访问）
            low = new int[n];       // 初始化为0
            stack = new int[n];     // 栈：存储DFS路径节点
            dcc = new ArrayList<>();// 存储所有点双连通分量
            cut = new boolean[n];   // 初始化为false（非割点）
            edgeCnt = 0;            // 边计数器初始化为0
            dfnCnt = 0;             // 发现时间计数器初始化为0
            top = 0;                // 栈顶指针初始化为0
            root = 0;               // DFS根节点初始化为0
        }

        private void createGraph(int[][] roads) {
            for (int[] edges : roads) {
                add(edges[0], edges[1]); // 添加u→v的边
                add(edges[1], edges[0]); // 添加v→u的边（无向图）
            }
        }

        private void add(int u, int v) {
            to[edgeCnt] = v;       // 第edgeCnt条边指向v
            next[edgeCnt] = head[u];// 下一条边为当前u的第一条边
            head[u] = edgeCnt++;   // 更新u的第一条边为当前边
        }

        private void creatDcc(int n) {
            for (int i = 0; i < n; i++) {
                //未访问的节点作为根节点
                if (dfn[i] == 0) {
                    root = i;
                    tarjan(i);
                }
            }
        }

        private void tarjan(int x) {
            dfn[x] = low[x] = ++dfnCnt; // 初始化发现时间和最早可达时间
            stack[top++] = x;           // 节点入栈

            int flag = 0; // 根节点的子节点计数（判断是否为割点）
            if (x == root && head[x] == -1) { // 根节点无邻接边（孤立节点）
                dcc.add(new ArrayList<>());
                dcc.get(dcc.size() - 1).add(x); // 孤立节点作为一个分量
            } else {
                for (int i = head[x]; i >= 0; i = next[i]) { // 遍历x的所有邻接边
                    int y = to[i]; // 邻接节点y
                    if (dfn[y] == 0) { // y未访问（树边）
                        tarjan(y);
                        if (low[y] >= dfn[x]) { // y无法到达x的祖先，x是割点，形成点双分量
                            flag++;
                            if (x != root || flag > 1) { // 根节点需至少2个子节点才是割点
                                cut[x] = true; // 标记x为割点
                            }
                            // 弹出栈中节点，形成当前点双分量
                            List<Integer> curAns = new ArrayList<>();
                            for (int z = stack[--top]; z != y; z = stack[--top]) {
                                curAns.add(z);
                            }
                            curAns.add(y); // 加入y
                            curAns.add(x); // 加入x（割点属于多个分量）
                            dcc.add(curAns); // 保存分量
                        }
                        low[x] = Math.min(low[x], low[y]); // 更新x的最早可达时间
                    } else { // y已访问（回边）
                        low[x] = Math.min(low[x], dfn[y]); // 更新x的最早可达时间
                    }
                }
            }
        }

    }
}
