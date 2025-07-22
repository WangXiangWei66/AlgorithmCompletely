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

    // 在线查询最优解 -> 树链剖分
    // 空间复杂度O(N), 支持在线查询，单次查询时间复杂度O(logN)
    // 如果有M次查询，时间复杂度O(N + M * logN)
    //功能：为每个查询计算两个节点的LCA，并返回结果数组
    public static int[] query3(int[] father, int[][] queries) {
        TreeChain tc = new TreeChain(father);//完成树的预处理
        int M = queries.length;//获取查询的数量M
        int[] ans = new int[M];//用于存储每个查询的LCA结果
        for (int i = 0; i < M; i++) {
            if (queries[i][0] == queries[i][1]) {
                ans[i] = queries[i][0];//节点本身便是LCA
            } else {
                ans[i] = tc.lca(queries[i][0], queries[i][1]);
            }
        }
        return ans;
    }

    public static class TreeChain {
        private int n;  // 节点总数
        private int h;  // 根节点
        private int[][] tree; // 树的邻接表表示
        private int[] fa; // 父节点数组
        private int[] dep; // 深度数组
        private int[] son; // 重儿子数组
        private int[] siz; // 子树大小数组
        private int[] top; // 链顶节点数组

        public TreeChain(int[] father) {
            initTree(father);
            dfs1(h, 0);// 第一次DFS，计算子树大小、深度、父节点、重儿子
            dfs2(h, h); // 第二次DFS，划分链，确定链顶节点
        }

        private void initTree(int[] father) {
            n = father.length + 1;//计算节点的总数
            tree = new int[n][];
            fa = new int[n];
            dep = new int[n];
            son = new int[n];
            siz = new int[n];
            top = new int[n--]; // 调整n为实际节点数（0-based）
            int[] cnum = new int[n];// 记录每个节点的子节点数量
            // 寻找根节点并统计子节点数量
            for (int i = 0; i < n; i++) {
                if (father[i] == i) {
                    h = i + 1;
                } else {
                    cnum[father[i]]++;
                }
            }
            //将邻接表初始化
            tree[0] = new int[0];
            for (int i = 0; i < n; i++) {
                tree[i + 1] = new int[cnum[i]];// 为每个节点分配子节点数组空间
            }
            //将邻接表填充
            for (int i = 0; i < n; i++) {
                if (i + 1 != h) {
                    // 将节点i+1添加到其父节点的子节点列表中
                    tree[father[i] + 1][--cnum[father[i]]] = i + 1;
                }
            }
        }

        private void dfs1(int u, int f) {
            fa[u] = f;
            dep[u] = dep[f] + 1;
            siz[u] = 1;//子树的大小初始化为1
            int maxSize = -1;//用于寻找重儿子
            //对所有子节点遍历
            for (int v : tree[u]) {
                dfs1(v, u);
                siz[u] += siz[v];//更新子树的大小
                // 寻找重儿子（子树最大的子节点）
                if (siz[v] > maxSize) {
                    maxSize = siz[v];
                    son[u] = v;
                }
            }
        }

        private void dfs2(int u, int t) {
            top[u] = t; // 记录当前节点所在链的链顶
            // 如果有重儿子，优先处理重儿子，保证重链的连续性
            if (son[u] != 0) {
                dfs2(son[u], t);//重儿子与当前节点在同一链上
                for (int v : tree[u]) {
                    if (v != son[u]) {
                        dfs2(v, v);//重儿子自身作为轻链的链顶
                    }
                }
            }
        }

        public int lca(int a, int b) {
            a++;
            b++;// 转换为1-based索引
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {
                    a = fa[top[a]];
                } else {
                    b = fa[top[b]];
                }
            }
            // 当两个节点在同一链上时，深度较小的节点是LCA
            return (dep[a] < dep[b] ? a : b) - 1;
        }
    }

    public static int[] generateFatherArray(int N) {
        int[] order = new int[N];//创建o~N-1的有序数组
        for (int i = 0; i < N; i++) {
            order[i] = i;
        }
        //  Fisher-Yates洗牌算法，随机打乱数组
        for (int i = N - 1; i >= 0; i--) {
            swap(order, i, (int) (Math.random() * (i + 1)));
        }
        //生成父节点数组
        int[] ans = new int[N];
        // 第一个元素作为根节点，父节点指向自己
        ans[order[0]] = order[0];
        // 为每个节点分配随机父节点（必须是已出现过的节点）
        for (int i = 1; i < N; i++) {
            // 从0到i-1之间随机选一个索引，对应order中的节点作为父节点
            ans[order[i]] = order[(int) (Math.random() * i)];
        }
        return ans;
    }

    public static int[][] generateQueries(int M, int N) {
        //创建M个查询的二维数组
        int[][] ans = new int[M][2];
        // 为每个查询生成两个随机节点
        for (int i = 0; i < M; i++) {
            ans[i][0] = (int) (Math.random() * N);
            ans[i][1] = (int) (Math.random() * N);
        }
        return ans;
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static boolean equal(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int N = 1000;
        int M = 200;
        int testTime = 50000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int size = (int) (Math.random() * N) + 1;
            int ques = (int) (Math.random() * M) + 1;
            int[] father = generateFatherArray(size);
            int[][] queries = generateQueries(ques, size);
            int[] ans1 = query1(father, queries);
            int[] ans2 = query2(father, queries);
            int[] ans3 = query3(father, queries);
            if (!equal(ans1, ans2) || !equal(ans1, ans3)) {
                System.out.println("Oops");
                break;
            }
        }
        System.out.println("test finish");
    }
}
