package Class_2021_12_1;

//给定一棵树的头节点head，原本是一棵正常的树
//现在，在树上多加了一条冗余的边
//请找到这条冗余的边并返回
//Leetcode链接 : https://leetcode.com/problems/redundant-connection-ii/
public class Code03_RedundantConnectionII {

    public static int[] findRedundantDirectedConnection(int[][] edges) {
        int N = edges.length;
        UnionFind uf = new UnionFind(N);//用来检测环
        int[] pre = new int[N + 1];//记录每个节点的父节点
        int[] first = null;//记录第一次出现的冲突边
        int[] second = null;//记录第二次出现的冲突边
        int[] circle = null;//记录形成环的边
        for (int i = 0; i < N; i++) {
            int from = edges[i][0];
            int to = edges[i][1];
            //to节点已经有了父节点
            if (pre[to] != 0) {
                first = new int[]{pre[to], to};
                second = edges[i];
            } else {
                pre[to] = from;//记录父节点
                if (uf.same(from, to)) {
                    circle = edges[i];
                } else {
                    uf.union(from, to);
                }
            }
        }
        return first != null ? (circle != null ? first : second) : circle;
    }

    public static class UnionFind {
        private int[] f;
        private int[] s;//集合大小数组
        private int[] h;//临时数组，路径压缩记录路径

        public UnionFind(int N) {
            f = new int[N + 1];
            s = new int[N + 1];
            h = new int[N + 1];
            for (int i = 0; i <= N; i++) {
                f[i] = i;
                s[i] = 1;
            }
        }

        private int find(int i) {
            int hi = 0;
            while (i != f[i]) {
                h[hi++] = i;
                i = f[i];
            }
            while (hi > 0) {
                f[h[--hi]] = i;
            }
            return i;
        }

        public boolean same(int i, int j) {
            return find(i) == find(j);
        }

        public void union(int i, int j) {
            int fi = find(i);
            int fj = find(j);
            if (fi != fj) {
                if (s[fi] >= s[fj]) {
                    f[fj] = fi;
                    s[fi] = s[fi] + s[fj];
                } else {
                    f[fi] = fj;
                    s[fj] = s[fi] + s[fj];
                }
            }
        }
    }
}
