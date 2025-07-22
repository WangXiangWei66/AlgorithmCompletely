package Class23;

import java.util.HashSet;

//给定数组father大小为N，表示一共有N个节点
//father[i] = j 表示点i的父亲是点j， father表示的树一定是一棵树而不是森林
//queries是二维数组，大小为M*2，每一个长度为2的数组都表示一条查询
//[4,9], 表示想查询4和9之间的最低公共祖先（LCA）…
//[3,7], 表示想查询3和7之间的最低公共祖先…
//tree和queries里面的所有值，都一定在0~N-1之间
//返回一个数组ans，大小为M，ans[i]表示第i条查询的答案
public class Code01_LCATarjanAndTreeChainPartition {

    //暴力方法
    public static int[] query1(int[] father, int[][] queries) {
        int M = queries.length;// 获取查询的数量
        int[] ans = new int[M];// 创建存储结果的数组
        HashSet<Integer> path = new HashSet<>(); // 用于记录路径上的节点
        //遍历每个查询（每次处理一组[a,b]节点对
        for (int i = 0; i < M; i++) {
            //记录第一个节点到根的路径
            int jump = queries[i][0];// 取出当前查询的第一个节点 a
            // 从节点 a 向上遍历到根节点，记录沿途所有节点
            while (father[jump] != jump) {//如果还没有到达根节点
                path.add(jump);//将当前节点加入路径集合
                jump = father[jump];//将父节点往上移动
            }
            path.add(jump);//最后将根节点也加入路径集合
            jump = queries[i][1];//取出当前节点的第二个节点b
            // 从节点 b 向上遍历，寻找第一个出现在 path 集合中的节点
            while (!path.contains(jump)) {
                jump = father[jump];
            }
            ans[i] = jump; //找到的第一个共同节点就是 a 和 b 的最近公共祖先
            path.clear();// 清空路径集合，为下一个查询做准备
        }
        return ans;
    }

    //本代码采用了Tarjan离线算法（通过深度优先搜索+并查集实现）
    //Tarjan离线算法的核心：
    //1.先将所有查询存储起来（离线处理）
    //2.对树进行一次DFS遍历，在回溯时通过并查集合并节点
    //3.当遍历到某个节点时，处理与该节点相关的所有查询，利用并查集找到已访问的LCA
    //这种方式能在 O (N + M α(N)) 的时间复杂度内完成所有查询（N 为节点数，M 为查询数，α 为并查集的反阿克曼函数，近似常数）。
    public static int[] query2(int[] father, int[][] queries) {
        int N = father.length;// 节点总数（father数组长度即节点数）
        int M = queries.length;// 查询总数
        int[] help = new int[N]; // 辅助数组，用于临时计数
        int h = 0;// 记录根节点（父节点是自身的节点）
        //构建树的邻接表
        // 第一步：找到根节点，并统计每个节点的子节点数量
        for (int i = 0; i < N; i++) {
            if (father[i] == i) {
                h = i;
            } else {
                help[father[i]]++;
            }
        }
        // 第二步：初始化每个节点的子节点数组（mt = 子节点表）
        int[][] mt = new int[N][]; //mt:存储每个节点的子节点，便于DFS遍历
        for (int i = 0; i < N; i++) {
            mt[i] = new int[help[i]];// 每个节点的子节点数组长度=子节点数量
        }
        // 第三步：填充子节点表（将子节点按父节点归类）
        for (int i = 0; i < N; i++) {
            if (i != h) {//根节点没有父节点，跳过
                mt[father[i]][--help[father[i]]] = i;// 将i添加到其父节点的子节点数组中
            }
        }
        //构建查询表
        // 第一步：统计每个节点参与的查询次数
        for (int i = 0; i < M; i++) {
            if (queries[i][0] != queries[i][1]) {//将节点与自身的查询跳过
                help[queries[i][0]]++;//节点a参与的查询数+1
                help[queries[i][1]]++;//节点b参与的查询数+1
            }
        }
        // 第二步：初始化查询表和索引表
        int[][] mq = new int[N][]; // mq[u] 存储与u相关的所有查询节点v（即查询(u,v)）
        int[][] mi = new int[N][]; // mi[u] 存储与u相关的查询在结果数组中的索引
        for (int i = 0; i < N; i++) {
            mq[i] = new int[help[i]]; // 长度=节点i参与的查询数
            mi[i] = new int[help[i]];
        }
        // 第三步：填充查询表和索引表
        for (int i = 0; i < M; i++) {
            if (queries[i][0] != queries[i][1]) {
                mq[queries[i][0]][--help[queries[i][0]]] = queries[i][1];
                mi[queries[i][0]][help[queries[i][0]]] = i;
                mq[queries[i][1]][--help[queries[i][1]]] = queries[i][0];
                mi[queries[i][1]][help[queries[i][1]]] = i;
            }
        }
        //处理查询并但会结果
        int[] ans = new int[M];
        UnionFind uf = new UnionFind(N); // 并查集，用于合并节点和查找LCA
        // 执行Tarjan算法的核心过程（DFS遍历+并查集操作）
        process(h, mt, mq, mi, uf, ans);
        // 处理特殊情况：节点与自身的查询，LCA就是该节点本身
        for (int i = 0; i < M; i++) {
            if (queries[i][0] == queries[i][1]) {
                ans[i] = queries[i][0];
            }
        }
        return ans;
    }

    public static void process(int head, int[][] mt, int[][] mq, int[][] mi, UnionFind uf, int[] ans) {
        // 第一步：递归遍历所有子节点（深度优先）
        for (int next : mt[head]) {
            process(next, mt, mq, mi, uf, ans);//递归处理子节点
            uf.union(head, next);//回溯时将子节点合并到当前节点
            uf.setTag(head, head);  // 将当前节点标记为合并集合的代表（LCA标记）
        }
        // 第二步：处理与当前节点相关的所有查询
        int[] q = mq[head];
        int[] i = mi[head];
        for (int k = 0; k < q.length; k++) {
            int tag = uf.getTag(q[k]);//已访问的最近祖先
            if (tag != -1) {
                ans[i[k]] = tag;
            }
        }
    }

    public static class UnionFind {
        private int[] f;  // 父节点数组：f[i]表示节点i的父节点
        private int[] s;  // 大小数组：s[i]表示以i为根的集合的大小（仅根节点有效）
        private int[] t;  // 标记数组：t[i]表示以i为根的集合的标记（仅根节点有效）
        private int[] h;  // 临时数组：用于路径压缩时存储中间节点

        public UnionFind(int N) {
            f = new int[N];
            s = new int[N];
            t = new int[N];
            h = new int[N];
            for (int i = 0; i < N; i++) {
                f[i] = i;
                s[i] = i;
                t[i] = -1;//初始时所有节点都是无标记
            }
        }

        private int find(int i) {
            int j = 0;
            //找到根节点，并记录沿途的所有节点
            while (i != f[i]) {
                h[j++] = i;
                i = f[i];
            }
            while (j > 0) {
                h[--j] = i;//将沿途的节点都指向根节点
            }
            return i;
        }

        public void union(int i, int j) {
            int fi = find(i);
            int fj = find(j);
            if (fi != fj) {
                int si = s[fi];
                int sj = s[fj];
                int m = si >= sj ? fi : fj;
                int l = m == fi ? fj : fi;
                f[l] = m;// 小集合的根节点指向大集合的根节点
                s[m] += s[l];//更新新集合的大小
            }
        }
        //设计集合的标记
        public void setTag(int i, int tag) {
            t[find(i)] = tag;// 找到i所在集合的根节点，将根节点的标记设为tag
        }
        //获取集合的标记
        public int getTag(int i) {
            return t[find(i)];
        }
    }
}
