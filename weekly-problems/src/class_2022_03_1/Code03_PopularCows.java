package class_2022_03_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;

//A -> B，表示A认为B是红人
//A -> B -> C，表示A认为B是红人，B认为C是红人，
//规定“认为”关系有传递性，所以A也认为C是红人
//给定一张有向图，方式是给定M个有序对(A, B)
//(A, B)表示A认为B是红人，该关系具有传递性
//给定的有序对中可能包含(A, B)和(B, C)，但不包含(A,C)
//求被其他所有人认为是红人的总数
public class Code03_PopularCows {

    public static class StrongConnectedComponents {
        public ArrayList<ArrayList<Integer>> nexts;
        public int n;
        public int[] stack;
        public int stackSize;
        public int[] dfn;
        public int[] low;
        public int cnt;
        public int[] scc;
        public int sccn;

        public StrongConnectedComponents(ArrayList<ArrayList<Integer>> edges) {
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
            n--;
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

        public ArrayList<ArrayList<Integer>> getShortGraph() {
            ArrayList<ArrayList<Integer>> shortGraph = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i <= sccn; i++) {
                shortGraph.add(new ArrayList<>());
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
            int n = (int) in.nval;//读取顶点的数量
            in.nextToken();
            int m = (int) in.nval;//获取边的数量
            ArrayList<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i <= n; i++) {
                edges.add(new ArrayList<Integer>());
            }
            //读取边信息并获取邻接表
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int from = (int) in.nval;
                in.nextToken();
                int to = (int) in.nval;
                edges.get(from).add(to);//添加有向边
            }
            StrongConnectedComponents connectedComponents = new StrongConnectedComponents(edges);
            int sccn = connectedComponents.getSccn();
            int ans = 0;
            if (sccn == 1) {
                ans = n;
            } else {
                ArrayList<ArrayList<Integer>> dag = connectedComponents.getShortGraph();
                int zeroOut = 0;
                int outScc = 0;//出度为0的SCC编号
                for (int i = 1; i <= sccn; i++) {
                    if (dag.get(i).size() == 0) {
                        zeroOut++;
                        outScc = i;
                    }
                }
                if (zeroOut > 1) {
                    ans = 0;
                } else {
                    int[] scc = connectedComponents.getScc();
                    for (int i = 1; i <= n; i++) {
                        if (scc[i] == outScc) {
                            ans++;
                        }
                    }
                }
            }
            out.println(ans);
            out.flush();
        }
    }
}
