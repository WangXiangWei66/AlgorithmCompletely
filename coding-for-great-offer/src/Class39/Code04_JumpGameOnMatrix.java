package Class39;

//来自京东
//给定一个二维数组matrix，matrix[i][j] = k代表:
//从(i,j)位置可以随意往右跳<=k步，或者从(i,j)位置可以随意往下跳<=k步
//如果matrix[i][j] = 0，代表来到(i,j)位置必须停止
//返回从matrix左上角到右下角，至少要跳几次
//已知matrix中行数n <= 5000, 列数m <= 5000
//matrix中的值 <= 1000
public class Code04_JumpGameOnMatrix {

    public static int jump1(int[][] map) {
        return process(map, 0, 0);
    }

    public static int process(int[][] map, int row, int col) {
        if (row == map.length - 1 && col == map[0].length - 1) {
            return 0;
        }
        if (map[row][col] == 0) {
            return Integer.MAX_VALUE;
        }
        int next = Integer.MAX_VALUE;//记录下一步的最小步数
        //跳跃距离不能超过当前位置允许的最大值
        for (int down = row + 1; down < map.length && (down - row) <= map[row][col]; down++) {
            next = Math.min(next, process(map, down, col));
        }

        for (int right = col + 1; right < map[0].length && (right - col) <= map[row][col]; right++) {
            next = Math.min(next, process(map, row, right));
        }

        return next != Integer.MAX_VALUE ? (next + 1) : next;
    }


    public static class SegmentTree {
        private int[] min;
        private int[] change;//是否有需要更新操作的值
        private boolean[] update;

        public SegmentTree(int size) {
            int N = size + 1;//保证索引从1开始
            //线段树通常余姚4倍于数组的空间
            min = new int[N << 2];
            change = new int[N << 2];
            update = new boolean[N << 2];
            //将所有位置的最小值设为系统最大
            Update(1, size, Integer.MAX_VALUE, 1, size, 1);
        }

        private void pushUp(int rt) {
            min[rt] = Math.min(min[rt << 1], min[rt << 1 | 1]);
        }

        private void pushDown(int rt, int ln, int rn) {
            if (update[rt]) {
                update[rt << 1] = true;
                update[rt << 1 | 1] = true;
                change[rt << 1] = change[rt];
                change[rt << 1 | 1] = change[rt];
                min[rt << 1] = change[rt];
                min[rt << 1 | 1] = change[rt];
                update[rt] = false;
            }
        }

        public void Update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                update[rt] = true;
                change[rt] = C;
                min[rt] = C;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            if (L <= mid) {
                Update(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                Update(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        public int query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return min[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            int left = Integer.MAX_VALUE;
            int right = Integer.MAX_VALUE;
            if (L <= mid) {
                left = query(L, R, l, mid, rt << 1);
            }
            if (R > mid) {
                right = query(L, R, mid + 1, r, rt << 1 | 1);
            }
            return Math.min(left, right);
        }
    }

    public static int jump2(int[][] arr) {
        int n = arr.length;
        int m = arr[0].length;
        //将矩阵的0~based索引转化为1~based索引
        int[][] map = new int[n + 1][m + 1];
        for (int a = 0, b = 1; a < n; a++, b++) {
            for (int c = 0, d = 1; c < m; c++, d++) {
                map[b][d] = arr[a][c];
            }
        }
        //每行对应一个线段树，用于查询当前行右侧的最小步数
        SegmentTree[] rowTrees = new SegmentTree[n + 1];
        for (int i = 1; i <= n; i++) {
            rowTrees[i] = new SegmentTree(m);
        }
        SegmentTree[] colTrees = new SegmentTree[m + 1];
        for (int i = 1; i <= m; i++) {
            colTrees[i] = new SegmentTree(n);
        }
        //(m,m):单点
        rowTrees[n].Update(m, m, 0, 1, m, 1);
        colTrees[m].Update(n, n, 0, 1, n, 1);
        for (int col = m - 1; col >= 1; col--) {
            if (map[n][col] != 0) {//当前位置可以跳跃
                int left = col + 1;
                int right = Math.min(col + map[n][col], m);
                int next = rowTrees[n].query(left, right, 1, m, 1);
                if (next != Integer.MAX_VALUE) {//存在可达路径
                    rowTrees[n].Update(col, col, next + 1, 1, m, 1);
                    colTrees[col].Update(n, n, next + 1, 1, n, 1);
                }
            }
        }
        for (int row = n - 1; row >= 1; row--) {
            if (map[row][m] != 0) {
                int up = row + 1;
                int down = Math.min(row + map[row][m], n);
                int next = colTrees[m].query(up, down, 1, n, 1);
                if (next != Integer.MAX_VALUE) {
                    rowTrees[row].Update(m, m, next + 1, 1, m, 1);
                    colTrees[m].Update(row, row, next + 1, 1, n, 1);
                }
            }
        }
        //从右下到左上填充剩余区域
        for (int row = n - 1; row >= 1; row--) {
            for (int col = m - 1; col >= 1; col--) {
                if (map[row][col] != 0) {
                    //向右跳跃最小步数
                    int left = col + 1;
                    int right = Math.min(col + map[row][col], m);
                    int next1 = rowTrees[row].query(left, right, 1, m, 1);
                    //向下跳跃最小步数
                    int up = row + 1;
                    int down = Math.min(row + map[row][col], n);
                    int next2 = colTrees[col].query(up, down, 1, n, 1);
                    int next = Math.min(next2, next1);
                    if (next != Integer.MAX_VALUE) {
                        rowTrees[row].Update(col, col, next + 1, 1, m, 1);
                        colTrees[col].Update(row, row, next + 1, 1, n, 1);
                    }
                }
            }
        }
        return rowTrees[1].query(1, 1, 1, m, 1);
    }

    public static int[][] randomMatrix(int n, int m, int v) {
        int[][] ans = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] = (int) (Math.random() * v);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println("线段树展示开始");
        int N = 100;
        SegmentTree st = new SegmentTree(N);
        System.out.println(st.query(8, 19, 1, N, 1));
        st.Update(6, 14, 56, 1, N, 1);
        System.out.println(st.query(8, 19, 1, N, 1));
        System.out.println("线段树展示结束");

        int len = 10;
        int value = 8;
        int testTime = 1000;
        System.out.println("对数器测试开始");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * len) + 1;
            int m = (int) (Math.random() * len) + 1;
            int[][] map = randomMatrix(n, m, value);
            int ans1 = jump1(map);
            int ans2 = jump2(map);
            if (ans1 != ans2) {
                System.out.println("Oops");
                break;
            }
        }
        System.out.println("对数器测试结束");

    }
}
