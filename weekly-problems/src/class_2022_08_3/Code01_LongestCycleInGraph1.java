package class_2022_08_3;

import java.util.ArrayList;

//给你一个 n个节点的 有向图，节点编号为0到n - 1，
//其中每个节点至多有一条出边。
//图用一个大小为 n下标从0开始的数组edges表示，
//节点 i到节点edges[i]之间有一条有向边。
//如果节点i没有出边，那么edges[i] == -1。
//请你返回图中的 最长环，如果没有任何环，请返回 -1。
//一个环指的是起点和终点是 同一个节点的路径。
// 用强联通分量
//测试链接 : https://leetcode.cn/problems/longest-cycle-in-a-graph/
public class Code01_LongestCycleInGraph1 {

    public static class StronglyConnectedComponents {
        public ArrayList<ArrayList<Integer>> nexts;//图的邻接表
        public int n;//节点总数
        public int stackSize;
        public int cnt;//时间戳(节点首次被访问的顺序)
        public int sccn;//强连通分量的个数
        public int[] stack;
        public int[] dfn;//节点i首次被访问的时间戳
        public int[] low;//节点i能回溯到的最早时间戳
        public int[] scc;//节点i所属的强连通分量编号

        public StronglyConnectedComponents(ArrayList<ArrayList<Integer>> edges) {
            nexts = edges;
            init();
            scc();//查找强连通分量
        }

        private void init() {
            n = nexts.size();
            stackSize = 0;
            cnt = 0;
            sccn = 0;
            stack = new int[n];
            dfn = new int[n];//0表示未访问
            low = new int[n];
            scc = new int[n];
        }

        private void scc() {
            for (int i = 0; i < n; i++) {
                if (dfn[i] == 0) {
                    tarjan(i);
                }
            }
        }

        private void tarjan(int p) {
            low[p] = dfn[p] = ++cnt;
            stack[stackSize++] = p;
            for (int q : nexts.get(p)) {
                if (dfn[q] == 0) {
                    tarjan(q);
                }
                if (scc[q] == 0) {
                    low[p] = Math.min(low[p], low[q]);
                }
            }
            if (low[p] == dfn[p]) {
                sccn++;
                int top = 0;
                //弹节点,直到弹出p
                do {
                    top = stack[--stackSize];
                    scc[top] = sccn;
                } while (top != p);
            }
        }

        public int[] getScc() {
            return scc;
        }
        //返回强连通分量的总个数
        public int getSccn() {
            return sccn;
        }
    }

    public static int longestCycle(int[] edges) {
        int n = edges.length;
        ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < n; i++) {
            if (edges[i] != -1) {
                graph.get(i).add(edges[i]);
            }
        }
        StronglyConnectedComponents connectedComponents = new StronglyConnectedComponents(graph);
        int m = connectedComponents.getSccn() + 1;
        //统计每个强连通分量的节点数量
        int[] cnt = new int[m];
        //每个节点所属的强连通分量编号
        int[] scc = connectedComponents.getScc();
        for (int i = 0; i < n; i++) {
            cnt[scc[i]]++;
        }
        int ans = -1;
        for (int count : cnt) {
            ans = Math.max(ans, count > 1 ? count : -1);
        }
        return ans;
    }
}
