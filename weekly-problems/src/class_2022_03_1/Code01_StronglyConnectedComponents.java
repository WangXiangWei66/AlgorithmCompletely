package class_2022_03_1;

import java.util.ArrayList;

//强连通分量的原理
//1，理解什么是dfn序号
//2，理解什么是low序号
//3，理解算法流程中，节点的三种状态：未遍历、遍历了未结算、遍历了已结算
//4，理解什么是scc序号
//tarjan(cur)算法求强连通分量流程：
//1，遍历到一个“未遍历”状态的节点cur
//2，给cur节点dfn编号和low编号，初始时low(cur) = dfn(cur)
//3，将cur标记为“遍历了未结算”状态，并放入栈中
//4，对于cur的每个孩子q，做如下两步操作：
//1）如果q未遍历，执行tarjen(u)；否则，不执行tarjan(u)
//2）步骤1)后，q就遍历过了，如果是“遍历未结算”，令low(cur) = min ( low(cur), low(q))；
//如果是“已结算”，什么也不做
//5，如果经历完步骤4，依然发现low(cur) = dfn(cur)，
//说明cur是一个强连通分量的头节点，分配给cur一个scc号，
//并去结算cur及其cur之下的点(利用栈)
//强连通分量代码解析
public class Code01_StronglyConnectedComponents {

    public static class StronglyConnectedComponents {
        public ArrayList<ArrayList<Integer>> nexts;//邻接表：存储图的边信息
        public int n;//图中顶点数量（顶点编号从1开始）
        public int[] stack;//暂存已经遍历但是未结算的顶点
        public int stackSize;//栈的当前大小
        public int[] dfn;//顶点i的发现时间
        public int[] low;//顶点i能回溯到最早发现时间的顶点编号 --  判断是否可以形成环
        public int cnt;//计数器，用于分配dfn的序号
        public int[] scc;//顶点i所属的强连通分量编号
        public int sccn;//强联通分量的总数量

        public StronglyConnectedComponents(ArrayList<ArrayList<Integer>> edges) {
            nexts = edges;
            init();//初始化成员变量
            scc();//执行强连通分量求解
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
            n--;//实际的顶点数量
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
            //遍历p的所有临界点q
            for (int q : nexts.get(p)) {
                if (dfn[q] == 0) {
                    tarjan(q);
                }
                //q已遍历但是未结算
                if (scc[q] == 0) {
                    low[p] = Math.min(low[p], low[q]);
                }
            }
            //p是否未强连通分量的根节点
            if (low[p] == dfn[p]) {
                sccn++;
                int top = 0;
                //从栈中弹出所有的顶点，直到p被弹出
                do {
                    top = stack[--stackSize];
                    scc[top] = sccn;
                } while (top != p);//已知弹，直到根被弹出
            }
        }

        public int[] getScc() {
            return scc;//获取强联通分量编号
        }

        public int getSccn() {
            return sccn;//强连通分量的总数量
        }

        public ArrayList<ArrayList<Integer>> getShortGraph() {
            //将每个强联通分量视为一个顶点，保留分量间的边
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
}
