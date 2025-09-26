package class_2022_03_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;//更方便读取数字输入
import java.util.ArrayList;

//N个学校之间有单向的网络，每个学校得到一套软件后，
//可以通过单向网络向周边的学校传输
//问题1：
//初始至少需要向多少个学校发放软件，使得网络内所有的学校最终都能得到软件
//问题2：
//至少需要添加几条传输线路(边)，使任意向一个学校发放软件后，
//经过若干次传送，网络内所有的学校最终都能得到软件
//2 <= N <= 1000

// 从题意中抽象出的算法模型, 给定一个有向图，求：
// 1) 至少要选几个顶点，才能做到从这些顶点出发，可以到达全部顶点
// 2) 至少要加多少条边，才能使得从任何一个顶点出发，都能到达全部顶点
// 测试链接 : http://poj.org/problem?id=1236
public class Code02_NetworkOfSchools {

    public static class StronglyConnectedComponents {
        public ArrayList<ArrayList<Integer>> nexts;//原图的邻接表
        public int n;//顶点数量
        public int[] stack;//Tarjan算法中用于存储当前路径的栈
        public int stackSize;//栈的大小
        public int[] dfn;
        public int[] low;//顶点能到达的最早发现时间的顶点
        public int cnt;//时间戳计数器
        public int[] scc;//每个顶点所属的scc编号
        public int sccn;

        public StronglyConnectedComponents(ArrayList<ArrayList<Integer>> edges) {
            nexts = edges;
            init();
            scc();
        }

        private void init() {
            n = nexts.size();
            stack = new int[n];
            stackSize = 0;
            dfn = new int[n];
            low = new int[n];
            cnt = 0;
            scc = new int[n];
            sccn = 0;
            n--;//调整为实际的顶点数
        }

        private void scc() {
            for (int i = 1; i <= n; i++) {
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
                //邻接顶点在当前的栈中
                if (scc[q] == 0) {
                    low[p] = Math.min(low[p], low[q]);
                }
            }
            if (low[p] == dfn[p]) {
                sccn++;
                int top = 0;
                do {
                    top = stack[--stackSize];
                    scc[top] = sccn;
                } while (top != p);
            }
        }

        public int[] getScc() {
            return scc;
        }

        public int getSccn() {
            return sccn;
        }
        //使得每个SCC都作为一个顶点
        public ArrayList<ArrayList<Integer>> getShortGraph() {
            ArrayList<ArrayList<Integer>> shortGraph = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i <= sccn; i++) {
                shortGraph.add(new ArrayList<Integer>());
            }
            for (int u = 1; u <= n; u++) {
                for (int v : nexts.get(u)) {
                    if (scc[u] != scc[v]) {
                        shortGraph.get(scc[u]).add(scc[v]);
                    }
                }
            }
            return shortGraph;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            ArrayList<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i <= n; i++) {
                edges.add(new ArrayList<Integer>());
            }
            //读取每个学校的接收列表
            for (int from = 1; from <= n; from++) {
                do {
                    in.nextToken();
                    int to = (int) in.nval;
                    if (to == 0) {
                        break;
                    } else {
                        edges.get(from).add(to);
                    }
                } while (true);
            }
            //求解强连通分量
            StronglyConnectedComponents scc = new StronglyConnectedComponents(edges);
            int sccn = scc.getSccn();
            //计算压缩后的DAG中每个节点的入度和出度
            int[] inDegrees = new int[sccn + 1];
            int[] outDegrees = new int[sccn + 1];
            ArrayList<ArrayList<Integer>> dag = scc.getShortGraph();
            for (int i = 1; i <= sccn; i++) {
                for (int j : dag.get(i)) {
                    outDegrees[i]++;
                    inDegrees[j]++;
                }
            }
            //统计入度和出度为0的点
            int zeroIn = 0;
            int zeroOut = 0;
            for (int i = 1; i <= sccn; i++) {
                if (inDegrees[i] == 0) {
                    zeroIn++;
                }
                if (outDegrees[i] == 0) {
                    zeroOut++;
                }
            }
            out.println(zeroIn);//问题1的答案
            out.println(sccn == 1 ? 0 : Math.max(zeroIn, zeroOut));
            out.flush();

        }

    }
}
