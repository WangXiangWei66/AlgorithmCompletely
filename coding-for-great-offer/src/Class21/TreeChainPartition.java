package Class21;

import java.util.HashMap;

//树链剖分专题
//给定数组father，大小为N，表示一共有N个节点
//father[i] = j 表示点i的父亲是点j， father表示的树一定是一棵树而不是森林
//给定数组values，大小为N，values[i]=v表示节点i的权值是v
//实现如下4个方法，保证4个方法都很快！
//1)让某个子树所有节点值加上v，入参：int head, int v
//2)查询某个子树所有节点值的累加和，入参：int head
//3)在树上从a到b的整条链上所有加上v，入参：int a, int b, int v
//4)查询在树上从a到b的整条链上所有节点值的累加和，入参：int a, int b
public class TreeChainPartition {
    //数链部分的数据结构，用于高效处理树上的路径查询和子树操作
    public static class TreeChain {
        private int tim;//DFS时间戳，用来记录节点访问顺序
        private int n;//节点总数
        private int h;//树根节点编号
        private int[][] tree;//邻接表存储树结构
        private int[] val;//存储节点原始权重(平移后，原0节点对应val[1])
        private int[] fa;//父节点数组
        private int[] dep; //深度数组
        private int[] son;// 重儿子数组（son[i]=0表示i没有儿子）
        private int[] siz;//子树大小数组
        private int[] top;//所在重链的顶端节点
        private int[] dfn;// DFS序数组（节点在DFS中的编号）
        private int[] tnw;// 转换后的节点权重（按DFS序排列）
        private SegmentTree seg;// 线段树，用于区间操作

        //构造函数
        public TreeChain(int[] father, int[] values) {
            initTree(father, values);// 初始化树结构
            dfs1(h, 0);// 第一次DFS：处理父节点、深度、重儿子、子树大小
            dfs2(h, h);// 第二次DFS：处理重链顶端、DFS序、转换权重
            seg = new SegmentTree(tnw);//初始化线段树
            seg.build(1, n, 1); // 构建线段树
        }

        //初始化树结构
        private void initTree(int[] father, int[] values) {
            tim = 0;
            n = father.length + 1;// 注意：这里假设father数组长度为n-1（0~n-2）// 注意：这里假设father数组长度为n-1（0~n-2）
            tree = new int[n][];//邻接表存储树结构
            val = new int[n];//存节点原始权重
            fa = new int[n];
            dep = new int[n];
            son = new int[n];
            siz = new int[n];
            top = new int[n];//所在的重链顶端
            dfn = new int[n];//节点在dfs序编号
            tnw = new int[n--];//转化后的热权重
            int[] cnum = new int[n];//记录每个节点的子节点数量
            //将原始节点的权重由0平移到val数组中从1开始存储
            for (int i = 0; i < n; i++) {
                val[i + 1] = values[i];
            }
            //下面开始构建树的邻接表表示，将父节点father转化为便于遍历的树形结构
            for (int i = 0; i < n; i++) {
                if (father[i] == i) {
                    h = i + 1;//寻找根节点——加1是因为节点的编号是从1开始的（父节点是自身的节点）
                } else {
                    cnum[father[i]]++;//如果不是根节点，他的父节点数量会加1
                }
            }
            tree[0] = new int[0];//无实际意义的空节点
            for (int i = 0; i < n; i++) {
                //tree[i]存储节点i的所有子节点
                tree[i + 1] = new int[cnum[i]];//为每个节点分配子节点数组
            }
            //填充邻接表
            for (int i = 0; i < n; i++) {
                if (i + 1 != h) {//将根节点直接跳过
                    tree[father[i] + 1][--cnum[father[i]]] = i + 1;
                }
            }
        }

        //用于预处理节点的父节点信息、深度、子树大小和重儿子（子树最大的子节点）
        private void dfs1(int u, int f) {
            fa[u] = f;
            dep[u] = dep[f] + 1;
            siz[u] = 1;//初始化u的子树大小为1（仅包含自身）
            int maxSize = -1;//最大的子树大小
            //遍历u的所有子节点v
            for (int v : tree[u]) {
                dfs1(v, u);
                siz[u] += siz[v];//更新u的子树大小
                //如果v的子树更大，则更新u的重儿子为v
                if (siz[v] > maxSize) {
                    maxSize = siz[v];
                    son[u] = v;
                }
            }
        }

        //生成dfs序、标记重链顶端，并将节点权重按DFS序重新排列
        //u：当前处理的节点
        //t：当前重链的顶端节点
        private void dfs2(int u, int t) {
            dfn[u] = ++tim;//记录每个节点在DFS中的访问顺序
            top[u] = t;//记录每个节点所在重链的顶端节点
            tnw[tim] = val[u];//按DFS序重新排列节点权重，便于线段树处理
            if (son[u] != 0) {
                dfs2(son[u], t);
                //处理轻儿子
                for (int v : tree[u]) {
                    if (v != son[u]) {
                        dfs2(v, v);//轻儿子的重链顶端为自身
                    }
                }
            }
        }

        //head为头的子树上，所有节点值+value
        //因为节点经过平移，所以head(原始节点) - > head(平移节点)
        public void addSubtree(int head, int value) {
            head++; //原始点编号 - > 平移编号
            //平移编号 - > dfs编号 dfn[head]
            //子树的所有节点在 DFS 序中的区间为 [dfn[u], dfn[u] + siz[u] - 1]。将这个区间上的值都加上value
            seg.add(dfn[head], dfn[head] + siz[head] - 1, value, 1, n, 1);
        }

        public int querySubtree(int head) {
            head++;
            return seg.query(dfn[head], dfn[head] + siz[head] - 1, 1, n, 1);
        }

        public void addChain(int a, int b, int v) {
            a++;
            b++;
            //如果两个节点不在同一条链上
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {//选择深度较大的重链顶端
                    seg.add(dfn[top[a]], dfn[a], v, 1, n, 1);//处理a所在的重链区间
                    a = fa[top[a]]; //跳到重链顶端的父节点
                } else {
                    seg.add(dfn[top[b]], dfn[b], v, 1, n, 1);
                    b = fa[top[b]];
                }
            }
            //如果两个节点在同一条重链上，处理剩余区间
            //判断哪个更深来决定区间的两个端点
            if (dep[a] > dep[b]) {
                seg.add(dfn[b], dfn[a], v, 1, n, 1);
            } else {
                seg.add(dfn[a], dfn[b], v, 1, n, 1);
            }
        }

        public int queryChain(int a, int b) {
            a++;
            b++;
            int ans = 0;
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {
                    ans += seg.query(dfn[top[a]], dfn[a], 1, n, 1);
                    a = fa[top[a]];
                } else {
                    ans += seg.query(dfn[top[b]], dfn[b], 1, n, 1);
                    b = fa[top[b]];
                }
            }
            if (dep[a] > dep[b]) {
                ans += seg.query(dfn[b], dfn[a], 1, n, 1);
            } else {
                ans += seg.query(dfn[a], dfn[b], 1, n, 1);
            }
            return ans;
        }
    }

    public static class SegmentTree {
        private int MAXN;        // 原始数组的最大长度
        private int[] arr;       // 原始数组（存储节点权重）
        private int[] sum;       // 线段树数组（存储区间和）
        private int[] lazy;      // 懒标记数组（用于区间更新优化）

        public SegmentTree(int[] origin) {
            MAXN = origin.length;
            arr = origin;
            sum = new int[MAXN << 2];//每个节点sum[rt]存储区间和
            lazy = new int[MAXN << 2];//懒标记数组
        }

        private void pushUp(int rt) {
            //总区间的和为左右子节点区间和
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        //rt：当前节点编号
        //ln 与 rn 为左右子树的节点数量
        //向下延迟懒标记，并把当前的懒标记清空
        private void pushDown(int rt, int ln, int rn) {
            if (lazy[rt] != 0) {
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }

        public void build(int l, int r, int rt) {
            if (l == r) {
                sum[rt] = arr[l]; // 叶子节点的和等于原始数组对应位置的值
                return;
            }
            int mid = (l + r) >> 1;
            build(l, mid, rt << 1);
            build(mid + 1, r, rt << 1 | 1);
            pushUp(rt);
        }

        public void add(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                sum[rt] += C * (r - l + 1); // 整个区间的和增加 C×区间长度
                lazy[rt] += C; // 记录懒标记（延迟更新子节点）
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            if (L <= mid) {
                add(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                add(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        public int query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            int ans = 0;
            if (L <= mid) {
                ans += query(L, R, l, mid, rt << 1);
            }
            if (R > mid) {
                ans += query(L, R, mid + 1, r, rt << 1 | 1);
            }
            return ans;
        }
    }

    //为了测试，这个结构是暴力但正确的方法
    public static class Right {
        private int n;
        private int[][] tree;
        private int[] fa;
        private int[] val;//节点权重数组
        private HashMap<Integer, Integer> path;//记录路径，辅助路径操作

        public Right(int[] father, int[] value) {
            n = father.length;
            tree = new int[n][];
            fa = new int[n];
            val = new int[n];
            //复制父节点和权重信息
            for (int i = 0; i < n; i++) {
                fa[i] = father[i];
                val[i] = value[i];
            }
            // 统计每个节点的子节点数量（用于初始化邻接表）
            int[] help = new int[n];
            int h = 0;//根节点（父节点是自身的节点）
            for (int i = 0; i < n; i++) {
                if (father[i] == i) {
                    h = i;//找到了根节点
                } else {
                    help[father[i]]++;//统计父节点的子节点数量
                }
            }
            // 初始化邻接表（为每个节点的子节点数组分配空间）
            for (int i = 0; i < n; i++) {
                tree[i] = new int[help[i]];
            }
            //填充邻接表（记录每个节点的子节点）
            for (int i = 0; i < n; i++) {
                if (i != h) {
                    // 将节点i加入其父节点的子节点列表
                    tree[father[i]][--help[father[i]]] = i;
                }
            }
            path = new HashMap<>();//初始化路径压缩器
        }

        public void addSubtree(int head, int value) {
            val[head] += value;
            for (int next : tree[head]) {
                addSubtree(next, value);
            }
        }

        public int querySubtree(int head) {
            int ans = val[head];//先累加当前节点的权重
            for (int next : tree[head]) {
                ans += querySubtree(next);//递归累加所有子节点的权重
            }
            return ans;
        }

        public void addChain(int a, int b, int v) {
            path.clear();// 清空路径记录
            // 第一步：记录从a到根的路径（key是节点，value是父节点到该节点的“下一个节点”）
            path.put(a, null);
            while (a != fa[a]) {
                path.put(fa[a], a);
                a = fa[a];
            }
            while (!path.containsKey(b)) {//b如果不在路径上
                val[b] += v;//将当前节点更新
                b = fa[b];//将父节点向上移动
            }
            // 第三步：更新b到a的路径（此时b是两条路径的交点）
            val[b] += v;//先更新交点
            while (path.get(b) != null) {
                b = path.get(b);
                val[b] += v;
            }
        }

        public int queryChain(int a, int b) {
            path.clear();
            path.put(a, null);
            while (a != fa[a]) {
                path.put(fa[a], a);
                a = fa[a];
            }
            int ans = 0;
            //从b向上移动到与a路径的交点，累加权重
            while (!path.containsKey(b)) {
                ans += val[b];
                b = fa[b];
            }
            //从交点沿a的路径向下移动到a，累加权重
            ans += val[b];
            while (path.get(b) != null) {
                b = path.get(b);
                ans += val[b];
            }
            return ans;
        }
    }

    //for test
    // 随机生成N个节点树，可能是多叉树，并且一定不是森林
    // 输入参数N要大于0
    //生成（父节点数组）
    public static int[] generateFatherArray(int N) {
        //生成0~N-1的随机排列（打乱节点顺序）
        int[] order = new int[N];
        //先按照顺序初始化
        for (int i = 0; i < N; i++) {
            order[i] = i;
        }
        //随机打乱
        for (int i = N - 1; i >= 0; i--) {
            swap(order, i, (int) (Math.random() * (i + 1)));
        }
        //基于随机排列生成父节点数组
        int[] ans = new int[N];
        // 第一个节点作为根节点（父节点是自身）
        ans[order[0]] = order[0];
        // 为剩余节点分配父节点（父节点是之前出现过的节点）
        for (int i = 1; i < N; i++) {
            //从里面随机挑选节点来做父节点
            ans[order[i]] = order[(int) (Math.random() * i)];
        }
        return ans;
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static int[] generateValueArray(int N, int V) {
        int[] ans = new int[N];
        for (int i = 0; i < N; i++) {
            ans[i] = (int) (Math.random() * V) + 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 50000;//树的节点数
        int V = 100000;//节点权重范围
        // 1. 生成随机树结构和节点权重
        int[] father = generateFatherArray(N);
        int[] values = generateValueArray(N, V);
        // 2. 初始化两种实现（树链剖分和朴素方法）
        TreeChain tc = new TreeChain(father, values);
        Right right = new Right(father, values);
        int testTime = 10000000;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            double decision = Math.random();
            if (decision < 0.25) {//子树加值测试
                int head = (int) (Math.random() * N);
                int value = (int) (Math.random() * V);
                tc.addSubtree(head, value);
                right.addSubtree(head, value);
            } else if (decision < 0.5) {//子树查询
                int head = (int) (Math.random() * N);
                if (tc.querySubtree(head) != right.querySubtree(head)) {
                    System.out.println("Oops");
                }
            } else if (decision < 0.75) {//路径加值
                int a = (int) (Math.random() * N);
                int b = (int) (Math.random() * N);
                int value = (int) (Math.random() * V);
                tc.addChain(a, b, value);
                right.addChain(a, b, value);
            } else {//路径查询
                int a = (int) (Math.random() * N);
                int b = (int) (Math.random() * N);
                if (tc.queryChain(a, b) != right.queryChain(a, b)) {
                    System.out.println("Ops");
                }
            }
        }
        System.out.println("test finish");
    }
}
