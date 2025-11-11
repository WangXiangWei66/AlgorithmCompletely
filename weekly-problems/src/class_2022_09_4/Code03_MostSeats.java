package class_2022_09_4;

import java.util.Arrays;

//来自华为
//给定一个二维数组map，代表一个餐厅，其中只有0、1两种值
//map[i][j] == 0 表示(i,j)位置是空座
//map[i][j] == 1 表示(i,j)位置坐了人
//根据防疫要求，任何人的上、下、左、右，四个相邻的方向都不能再坐人
//但是为了餐厅利用的最大化，也许还能在不违反防疫要求的情况下，继续安排人吃饭
//请返回还能安排的最大人数
//如果一开始的状况已经不合法，直接返回-1
//比如:
//1 0 0 0
//0 0 0 1
//不违反防疫要求的情况下，这个餐厅最多还能安排2人，如下所示，X是新安排的人
//1 0 X 0
//0 X 0 1
//再比如:
//1 0 0 0 0 1
//0 0 0 0 0 0
//0 1 0 0 0 1
//0 0 0 0 0 0
//不违反防疫要求的情况下，这个餐厅最多还能安排7人，如下所示，X是新安排的人
//1 0 0 X 0 1
//0 0 X 0 X 0
//0 1 0 X 0 1
//X 0 X 0 X 0
//数据范围 : 1 <= 矩阵的行、列 <= 20
public class Code03_MostSeats {

    public static int mostSeats1(int[][] map) {
        int n = map.length;
        int m = map[0].length;
        //将每行的状态转化为二进制数组
        int[] arr = new int[n];
        //遍历当前行的每一列
        for (int row = 0; row < n; row++) {
            int status = 0;
            //i是二进制的偏移,这里保证了矩阵列和二进位的意义映射关系
            for (int col = 0, i = m - 1; col < m; col++, i--) {
                //当前位置是1,检查上下左右是否有1,只检查左和上,因为右下还没有开始操作
                if (map[row][col] == 1) {
                    if (row > 0 && map[row - 1][col] == 1) {
                        return -1;
                    }
                    if (col > 0 && map[row][col - 1] == 1) {
                        return -1;
                    }
                }
                //构建当前行的压缩状态
                status |= map[row][col] << i;
            }
            arr[row] = status;
        }
        //dp[row][pre] 表示「处理到第 row 行，上一行的座位占用状态为 pre」时，后续能安排的最大人数（pre 是二进制状态，标记上一行哪些列有人)
        int[][] dp = new int[n][1 << m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], -2);
        }
        //从第0行开始,上一行为0
        int ans = process1(arr, 0, 0, m, dp);
        return ans == -1 ? 0 : ans;
    }

    //处理当前行,衔接上下行状态
    //row:当前处理的行号
    //pre:上一行的最终座位状态
    //m:矩阵的列数
    public static int process1(int[] arr, int row, int pre, int m, int[][] dp) {
        if (row == arr.length) {
            return 0;
        }
        if (dp[row][pre] != -2) {
            return dp[row][pre];
        }
        int cur = arr[row];
        int ans = 0;
        if ((cur & pre) != 0) {
            ans = -1;
        } else {
            //递归DFS枚举当前行的所有合法安排方式，求最大人数
            ans = dfs(arr, row, m - 1, pre, cur, m, dp);
        }
        dp[row][pre] = ans;
        return ans;
    }

    //枚举当前行的列安排,决策是否坐人
    //seats:当前行的已决策列的状态
    public static int dfs(int[] arr, int row, int col, int pre, int seats, int m, int[][] dp) {
        //当前行的所有列都已经遍历完了
        if (col == -1) {
            return process1(arr, row + 1, seats, m, dp);
        } else {
            //当前列不安排人,直接遍历到前一列
            int p1 = dfs(arr, row, col - 1, pre, seats, m, dp);
            int p2 = 0;
            //上一行同列无人,当前列是空地,右侧列无人,左侧列无人
            if ((pre & (1 << col)) == 0 && (seats & (1 << col)) == 0
                    && (col == m - 1 || (seats & (1 << (col + 1))) == 0)
                    && (col == 0 || (seats & (1 << (col - 1))) == 0)) {
                // 满足约束：在col列安排人，更新seats（将col位置1），递归遍历前一列
                int next2 = dfs(arr, row, col - 1, pre, seats | (1 << col), m, dp);
                if (next2 != -1) {
                    p2 = 1 + next2;
                }
            }
            return Math.max(p1, p2);
        }
    }

    public static int mostSeats2(int[][] map) {
        int n = map.length;
        int m = map[0].length;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            int status = 0;
            for (int j = 0; j < m; j++) {
                if (map[i][j] == 1) {
                    if (i > 0 && map[i - 1][j] == 1) {
                        return -1;
                    }
                    if (j > 0 && map[i][j - 1] == 1) {
                        return -1;
                    }
                }
                status |= map[i][j] << j;
            }
            arr[i] = status;
        }
        int s = 1 << m;
        //dp[i];i][status]:处理到第i行第j列,当前行的已决策状态为status时的最大安排人数
        int[][][] dp = new int[n][m][s];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < s; k++) {
                    dp[i][j][k] = -2;
                }
            }
        }
        //从第0行第0列开始,当前行决策状态为0
        int ans = process2(arr, n, m, 0, 0, 0, dp);
        return ans == -1 ? 0 : ans;
    }

    public static int process2(int[] arr, int n, int m, int i, int j, int status, int[][][] dp) {
        if (j == m) {
            return process2(arr, n, m, i + 1, 0, status, dp);
        }
        //当前行遍历完了
        if (i == n) {
            return 0;
        }
        if (dp[i][j][status] != -2) {
            return dp[i][j][status];
        }
        //获取当前位置的4个状态
        int left = status(status, j - 1, m);
        int up = status(status, j, m);
        int cur = status(arr[i], j, m);
        int right = status(arr[i], j + 1, m);
        //检查是否发生了冲突
        if (up == 1 && cur == 1) {
            return -1;
        }
        int p1 = -1;
        //当前列原始有人,必须标记为有人
        if (cur == 1) {
            p1 = process2(arr, n, m, i, j + 1, status | (1 << j), dp);
        } else {
            //当前列是有人的,通过异或运算强制把她设为0
            p1 = process2(arr, n, m, i, j + 1, (status | (1 << j)) ^ (1 << j), dp);
        }
        int p2 = -1;
        if (left == 0 && up == 0 && cur == 0 && right == 0) {
            int next2 = process2(arr, n, m, i, j + 1, status | (1 << j), dp);
            if (next2 != -1) {
                p2 = 1 + next2;
            }
        }
        int ans = Math.max(p1, p2);
        dp[i][j][status] = ans;
        return ans;
    }

    //获取某状态下第i列是否有人
    public static int status(int status, int i, int m) {
        return (i < 0 || i == m || (status & (1 << i)) == 0) ? 0 : 1;
    }

    public static int[][] randomMatrix(int n, int m, double oneP) {
        int[][] ans = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] = Math.random() < oneP ? 1 : 0;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 10;
        int M = 10;
        // 产生1的概率
        double oneP = 0.15;
        int testTimes = 10000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 1;
            int m = (int) (Math.random() * M) + 1;
            int[][] map = randomMatrix(n, m, oneP);
            int ans1 = mostSeats1(map);
            int ans2 = mostSeats2(map);
            if (ans1 != ans2) {
                for (int[] arr : map) {
                    for (int num : arr) {
                        System.out.print(num + " ");
                    }
                    System.out.println();
                }
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("测试结束");

        int n = 20;
        int[][] map = new int[n][n];
        System.out.println("最大规模 : " + n + " * " + n);
        long start = System.currentTimeMillis();
        mostSeats2(map);
        long end = System.currentTimeMillis();
        System.out.println("运行时间 : " + (end - start) + " 毫秒");
    }
}
