package class_2022_09_1;

//来自微软面试
//给定一个长度为n的二维数组graph，代表一张图
//graph[i] = {a,b,c,d} 表示i讨厌(a,b,c,d)，讨厌关系为双向的
//一共有n个人，编号0~n-1
//讨厌的人不能一起开会
//返回所有人能不能分成两组开会
//测试链接 : https://leetcode.cn/problems/is-graph-bipartite/
public class Code05_IsGraphBipartite {

    public static class UnionFind {
        private int[] f;  // 父节点数组：f[i]表示i的父节点（用于查找根节点）
        private int[] s;  // 集合大小数组：s[i]表示以i为根的集合大小（用于合并优化）
        private int[] h;  // 路径压缩临时数组：存储查找路径上的节点（优化查找效率）

        public UnionFind(int n) {
            f = new int[n];
            s = new int[n];
            h = new int[n];
            for (int i = 0; i < n; i++) {
                f[i] = i;//初始化每个节点的父节点为自己
                s[i] = 1;
            }
        }

        private int find(int i) {
            int hi = 0;
            //沿途寻找根节点,并记录路径的数到h数组
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

    public boolean isBipartite(int[][] graph) {
        int n = graph.length;//总节点数
        UnionFind uf = new UnionFind(n);
        //合并每个节点的所有讨厌对象
        for (int[] neighbors : graph) {
            for (int i = 1; i < neighbors.length; i++) {
                uf.union(neighbors[i - 1], neighbors[i]);
            }
        }
        //检查矛盾
        for (int i = 0; i < n; i++) {
            for (int j : graph[i]) {
                if (uf.same(i, j))
                    return false;
            }
        }
        return true;
    }
}
