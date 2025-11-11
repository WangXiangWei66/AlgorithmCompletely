package class_2022_09_4;

//来自华为
//一个n*n的二维数组中，只有0和1两种值
//当你决定在某个位置操作一次
//那么该位置的行和列整体都会变成1，不管之前是什么状态
//返回让所有值全变成1，最少的操作次数
//1 < n < 10，没错！原题就是说n < 10, 不会到10！最多到9！
public class Code02_SetAllOneMinTimes {

    public static int setOneMinTimes1(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        //所有可能的操作组合数
        int limit = 1 << (n * m);
        int ans = Integer.MAX_VALUE;
        for (int status = 0; status < limit; status++) {
            //当前操作组合是否能让矩阵全1
            if (ok(status, matrix, n, m)) {
                ans = Math.min(ans, hammingWeight(status));
            }
        }
        return ans;
    }

    public static boolean ok(int status, int[][] matrix, int n, int m) {
        //辅助矩阵,模拟操作后的结果
        int[][] help = new int[n][m];
        int limit = n * m;
        for (int i = 0; i < limit; i++) {
            if ((status & (1 << i)) != 0) {
                //获取行列号
                int row = i / m;
                int col = i % m;
                for (int j = 0; j < n; j++) {
                    help[j][col] = 1;
                }
                for (int j = 0; j < m; j++) {
                    help[row][j] = 1;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (help[i][j] == 0 && matrix[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    //计算二进制中1的个数
    public static int hammingWeight(int n) {
        // 分治思想：每次将相邻2位的1的个数相加，最终得到总个数
        n = (n & 0x55555555) + ((n >>> 1) & 0x55555555); // 2位一组求和
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333); // 4位一组求和
        n = (n & 0x0f0f0f0f) + ((n >>> 4) & 0x0f0f0f0f); // 8位一组求和
        n = (n & 0x00ff00ff) + ((n >>> 8) & 0x00ff00ff); // 16位一组求和
        n = (n & 0x0000ffff) + ((n >>> 16) & 0x0000ffff); // 32位一组求和
        return n; // 最终结果是n的二进制中1的个数
    }

    public static int setOneMinTimes2(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        //将每行的原始状态压缩为二进制数(arr[i]表示第i行的1的位置)
        int[] arr = new int[n];//每个元素对应1行
        for (int i = 0; i < n; i++) {
            //初始化该行的二进制状态
            int status = 0;
            // 第i行的二进制状态（j位为1表示matrix[i][j]=1）
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] == 1) {
                    status |= 1 << j;
                }
            }
            //记录第i行的原始1的状态
            arr[i] = status;
        }
        // DP维度定义：dp[已操作的行][已操作的列][当前遍历行r][当前遍历列c] = 最少操作次数
        // 行操作状态：1<<n 种（每行是否被操作，二进制位表示）
        // 列操作状态：1<<m 种（每列是否被操作，二进制位表示）
        // 当前位置：r（0~n-1）、c（0~m-1）
        int[][][][] dp = new int[1 << n][1 << m][n][m];
        for (int a = 0; a < (1 << n); a++) {
            for (int b = 0; b < (1 << m); b++) {
                for (int c = 0; c < n; c++) {
                    for (int d = 0; d < m; d++) {
                        dp[a][b][c][d] = -1;
                    }
                }
            }
        }
        //从左上角开始遍历
        return process2(arr, n, m, 0, 0, 0, 0, dp);
    }

    public static int process2(int[] arr, int n, int m, int row, int col, int r, int c, int[][][][] dp) {
        if (r == n) {
            for (int i = 0; i < n; i++) {
                //第i行未被操作,操作完后为0
                if ((row & (1 << i)) == 0 && (arr[i] | col) != (1 << m) - 1) {
                    return Integer.MAX_VALUE;
                }
            }
            return 0;
        }
        //当前行遍历完了,进入下一行的第0列
        if (c == m) {
            return process2(arr, n, m, row, col, r + 1, 0, dp);
        }
        if (dp[row][col][r][c] != -1) {
            return dp[row][col][r][c];
        }
        int p1 = process2(arr, n, m, row, col, r, c + 1, dp);
        int p2 = Integer.MAX_VALUE;
        int next2 = process2(arr, n, m, row | (1 << r), col | (1 << c), r, c + 1, dp);
        if (next2 != Integer.MAX_VALUE) {
            p2 = 1 + next2;
        }
        int ans = Math.min(p1, p2);
        dp[row][col][r][c] = ans;
        return ans;
    }

    public static int setOneMinTimes3(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            int status = 0;
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] == 1) {
                    status |= 1 << j;
                }
            }
            arr[i] = status;
        }
        //用二进制位表示每行是否被操作
        int[][][][] dp = new int[1 << n][1 << m][n][m];
        for (int a = 0; a < (1 << n); a++) {
            for (int b = 0; b < (1 << m); b++) {
                for (int c = 0; c < n; c++) {
                    for (int d = 0; d < m; d++) {
                        dp[a][b][c][d] = -1;
                    }
                }
            }
        }
        return process3(arr, n, m, 0, 0, 0, 0, dp);
    }

    public static int process3(int[] arr, int n, int m, int row, int col, int r, int c, int[][][][] dp) {
        if (r == n) {
            for (int i = 0; i < n; i++) {
                if ((row & (1 << i)) == 0 && (arr[i] | col) != (1 << m) - 1) {
                    return Integer.MAX_VALUE;
                }
            }
            return 0;
        }
        if (c == m) {
            return process3(arr, n, m, row, col, r + 1, 0, dp);
        }
        if (dp[row][col][r][c] != -1) {
            return dp[row][col][r][c];
        }
        int p1 = process3(arr, n, m, row, col, r, c + 1, dp);
        int p2 = Integer.MAX_VALUE;
        int next2 = process3(arr, n, m, row | (1 << r), col | (1 << c), r + 1, 0, dp);
        if (next2 != Integer.MAX_VALUE) {
            p2 = 1 + next2;
        }
        int ans = Math.min(p1, p2);
        dp[row][col][r][c] = ans;
        return ans;
    }

    public static int[][] randomMatrix(int n, int m, double p0) {
        int[][] ans = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] = Math.random() < p0 ? 0 : 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 3;
        int testTimes = 5000;
        System.out.println("功能测试开始");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 1;
            int m = (int) (Math.random() * N) + 1;
            double p0 = Math.random();
            int[][] matrix = randomMatrix(n, m, p0);
            int ans1 = setOneMinTimes1(matrix);
            int ans2 = setOneMinTimes2(matrix);
            int ans3 = setOneMinTimes3(matrix);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("出错了！");
            }
        }
        System.out.println("功能测试结束");

        int[][] matrix = randomMatrix(9, 9, 0.9);
        long start = System.currentTimeMillis();
        setOneMinTimes2(matrix);
        long end = System.currentTimeMillis();
        System.out.println("最极限的数据下的运行时间 : " + (end - start) + "毫秒");
    }
}
