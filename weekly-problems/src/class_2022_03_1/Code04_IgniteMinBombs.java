package class_2022_03_1;

import java.util.ArrayList;
import java.util.TreeMap;

//在一个地图上有若干个炸弹，每个炸弹会呈现十字型引爆
//每个炸弹都有其当量值，这个值决定了这个炸弹的爆炸半径
//如果一个炸弹被引爆时，有其它炸弹在其爆炸半径内，
//那么其它炸弹也会爆炸
//请问使地图上所有炸弹爆炸所需的最少人为引爆次数。
// 例如：
// 0,0,0,0,0
// 0,0,0,1,0
// 0,0,0,0,0
// 上图中val为1的单元是一个炸弹，人为引爆后地图变成下面的样子：
// 0, 0, 0,-1, 0
// 0, 0,-1,-1,-1
// 0, 0, 0,-1, 0
// 题目并没有给数据量，面经题目的通病
public class Code04_IgniteMinBombs {

    public static class StronglyConnectedComponents {
        public ArrayList<ArrayList<Integer>> nexts;
        public int n;
        public int[] stack;
        public int stackSize;
        public int[] dfn;
        public int[] low;
        public int cnt;
        public int[] scc;
        public int sccn;

        // 请保证点的编号从1开始，不从0开始
        // 注意：
        // 如果edges里有0、1、2...n这些点，那么容器edges的大小为n+1
        // 但是0点是弃而不用的，所以1..n才是有效的点，所以有效大小是n
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

    public static int[][] randomMatrix(int n, int m, int v) {
        int[][] map = new int[n][n];
        for (int i = 0; i < m; i++) {
            map[(int) (Math.random() * n)][(int) (Math.random() * n)] = (int) (Math.random() * v) + 1;
        }
        return map;
    }

    //本题假设矩阵为方阵
    public static int minBombs1(int[][] map) {
        int n = map.length;
        int m = 0;
        //统计地图上炸弹的总数
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m += map[i][j] == 0 ? 0 : 1;
            }
        }
        //存储所有的炸弹坐标
        int[][] bombs = new int[m][2];
        m = 0;//重置索引，重新遍历地图记录炸弹的位置
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] != 0) {
                    bombs[m][0] = i;
                    bombs[m++][1] = j;
                }
            }
        }
        //生成炸弹的索引数组
        int[] arr = new int[m];
        for (int i = 0; i < m; i++) {
            arr[i] = i;
        }
        //调用递归方法来枚举所有的引爆顺序
        return process1(arr, 0, bombs, map);
    }

    public static int process1(int[] arr, int index, int[][] bombs, int[][] map) {
        int ans = Integer.MAX_VALUE;
        //生成了一个完整的引爆顺序
        if (index == arr.length) {
            ans = orderIgnite(arr, bombs, map);//模拟该引爆过程
        } else {
            //使用了全排列的思想
            for (int i = index; i < arr.length; i++) {
                swap(arr, index, i);
                ans = Math.min(ans, process1(arr, index + 1, bombs, map));
                swap(arr, index, i);
            }
        }
        return ans;
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    //模拟特定顺序的引爆过程
    public static int orderIgnite(int[] arr, int[][] bombs, int[][] map) {
        int[][] copy = copyMap(map);
        int ans = 0;//认为引爆次数的计数器
        for (int i : arr) {
            int row = bombs[i][0];
            int col = bombs[i][1];
            if (copy[row][col] != -1) {
                ans++;
                //触发这次爆炸引起的连锁爆炸
                burn(copy, row, col, copy[row][col]);
            }
        }
        return ans;
    }

    //复制一份地图
    public static int[][] copyMap(int[][] map) {
        int n = map.length;
        int[][] ans = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ans[i][j] = map[i][j];
            }
        }
        return ans;
    }

    public static void burn(int[][] map, int i, int j, int v) {
        //v为当前的最大距离当量
        map[i][j] = -1;//将当前位置标记为已爆炸
        ArrayList<int[]> queue = new ArrayList<>();//存储被当前爆炸触发的其他炸弹
        //处理上方向的爆炸
        for (int row = i - 1, cnt = 1; row >= 0 && cnt <= v; row--, cnt++) {
            if (map[row][j] > 0) {
                queue.add(new int[]{row, j, map[row][j]});
            }
            map[row][j] = -1;//标记为已被炸弹覆盖
        }
        //处理下方向的炸弹
        for (int row = i + 1, cnt = 1; row < map.length && cnt <= v; row++, cnt++) {
            if (map[row][j] > 0) {
                queue.add(new int[]{row, j, map[row][j]});
            }
            map[row][j] = -1;
        }
        //处理左方向的
        for (int col = j - 1, cnt = 1; col >= 0 && cnt <= v; col--, cnt++) {
            if (map[i][col] > 0) {
                queue.add(new int[]{i, col, map[i][col]});
            }
            map[i][col] = -1;
        }
        //处理右方向的
        for (int col = j + 1, cnt = 1; col < map.length && cnt <= v; col++, cnt++) {
            if (map[i][col] > 0) {
                queue.add(new int[]{i, col, map[i][col]});
            }
            map[i][col] = -1;
        }
        //递归触发当前爆炸覆盖的炸弹
        for (int[] next : queue) {
            burn(map, next[0], next[1], next[2]);
        }
    }

    public static int minBombs2(int[][] map) {
        int n = map.length;
        //每行每列的炸弹用TreeMap存储
        ArrayList<TreeMap<Integer, Integer>> rowTreeMaps = new ArrayList<>();
        ArrayList<TreeMap<Integer, Integer>> colTreeMaps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rowTreeMaps.add(new TreeMap<>());
            colTreeMaps.add(new TreeMap<>());
        }
        //分配炸弹的编号
        int m = 0;//炸弹的总数
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //有炸弹
                if (map[i][j] != 0) {
                    m++;
                    rowTreeMaps.get(i).put(j, m);
                    colTreeMaps.get(j).put(i, m);
                }
            }
        }
        //构造引爆关系图的邻接表
        ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
        for (int i = 0; i <= m; i++) {
            edges.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] != 0) {
                    TreeMap<Integer, Integer> rowTreeMap = rowTreeMaps.get(i);
                    TreeMap<Integer, Integer> colTreeMap = colTreeMaps.get(j);
                    int from = rowTreeMap.get(j);//当前炸弹A的编号
                    int col = j - 1;
                    //左侧存在炸弹，并且列距小与引爆的最大半径
                    while (rowTreeMap.floorKey(col) != null && j - rowTreeMap.floorKey(col) <= map[i][j]) {
                        col = rowTreeMap.floorKey(col);//被触发的炸弹的编号
                        edges.get(from).add(rowTreeMap.get(col));
                        col--;//继续往左边找
                    }
                    col = j + 1;
                    while (rowTreeMap.ceilingKey(col) != null && rowTreeMap.ceilingKey(col) - j <= map[i][j]) {
                        col = rowTreeMap.ceilingKey(col);
                        edges.get(from).add(rowTreeMap.get(col));
                        col++;
                    }
                    int row = i - 1;
                    while (colTreeMap.floorKey(row) != null && i - colTreeMap.floorKey(row) <= map[i][j]) {
                        row = colTreeMap.floorKey(row);
                        edges.get(from).add(colTreeMap.get(row));
                        row--;
                    }
                    row = i + 1;
                    while (colTreeMap.ceilingKey(row) != null && colTreeMap.ceilingKey(row) - i <= map[i][j]) {
                        row = colTreeMap.ceilingKey(row);
                        edges.get(from).add(colTreeMap.get(row));
                        row++;
                    }
                }
            }
        }
        //求解引爆关系图的强连通分量
        StronglyConnectedComponents scc = new StronglyConnectedComponents(edges);
        int sccn = scc.getSccn();
        //构建压缩后的DAG
        int[] in = new int[sccn + 1];
        int[] out = new int[sccn + 1];
        ArrayList<ArrayList<Integer>> dag = scc.getShortGraph();
        for (int i = 1; i <= sccn; i++) {
            for (int j : dag.get(i)) {
                out[i]++;
                in[j]++;
            }
        }
        //统计入度为0的节点数
        int zeroIn = 0;
        for (int i = 1; i <= sccn; i++) {
            if (in[i] == 0) {
                zeroIn++;
            }
        }
        return zeroIn;//返回的即为最小的需要人为引爆的次数
    }
    public static void main(String[] args) {
        int n = 8;
        int m = 8;
        int v = 5;
        int testTime = 1000;
        System.out.println("测试开始");
        for (int k = 0; k < testTime; k++) {
            int[][] map = randomMatrix(n, m, v);
            int ans1 = minBombs1(map);
            int ans2 = minBombs2(map);
            if (ans1 != ans2) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        System.out.print(map[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println("出错了!");
            }

        }
        System.out.println("测试结束");
    }
}
