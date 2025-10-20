package class_2022_06_3;

//给你两个整数m 和n，分别表示一块矩形木块的高和宽。
//同时给你一个二维整数数组prices，其中prices[i] = [hi, wi, pricei]
//表示你可以以pricei元的价格卖一块高为hi宽为wi的矩形木块。
//每一次操作中，你必须按下述方式之一执行切割操作，以得到两块更小的矩形木块：
//沿垂直方向按高度 完全 切割木块，或
//沿水平方向按宽度 完全 切割木块
//在将一块木块切成若干小木块后，你可以根据 prices卖木块。
//你可以卖多块同样尺寸的木块。
//你不需要将所有小木块都卖出去。
//你 不能旋转切好后木块的高和宽。
//请你返回切割一块大小为m x n 的木块后，能得到的最多钱数。
//注意你可以切割木块任意次。
//测试链接 : https://leetcode.cn/problems/selling-pieces-of-wood/
public class Code02_SellingPiecesOfWood {

    public static int zuo(int m, int n, int[][] values) {
        if (m == 0 || n == 0) {
            return 0;
        }
        int ans = values[m][n];
        for (int split = 1; split < m; split++) {
            int up = zuo(split, n, values);
            int down = zuo(m - split, n, values);
            ans = Math.max(ans, up + down);
        }
        for (int split = 1; split < n; split++) {
            int left = zuo(m, split, values);
            int right = zuo(m, n - split, values);
            ans = Math.max(ans, left + right);
        }
        return ans;
    }

    public static long sellingWood1(int m, int n, int[][] prices) {
        //存储不同尺寸木块的最高售价
        long[][] values = new long[m + 1][n + 1];
        for (int[] p : prices) {
            values[p[0]][p[1]] = Math.max(values[p[0]][p[1]], p[2]);
        }
        return f1(m, n, values);
    }

    public static long f1(int m, int n, long[][] values) {
        if (m == 0 || n == 0) {
            return 0;
        }
        long ans = values[m][n];
        for (int split = 1; split < m; split++) {
            ans = Math.max(ans, f1(split, n, values) + f1(m - split, n, values));
        }
        for (int split = 1; split < n; split++) {
            ans = Math.max(ans, f1(m, split, values) + f1(m, n - split, values));
        }
        return ans;
    }

    public static long sellingWood2(int m, int n, int[][] prices) {
        long[][] values = new long[m + 1][n + 1];
        for (int[] p : prices) {
            values[p[0]][p[1]] = Math.max(values[p[0]][p[1]], p[2]);
        }
        long[][] dp = new long[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = -1;
            }
        }
        return f2(m, n, values, dp);
    }

    public static long f2(int m, int n, long[][] values, long[][] dp) {
        if (m == 0 || n == 0) {
            return 0;
        }
        if (dp[m][n] != -1) {
            return dp[m][n];
        }
        long ans = values[m][n];
        for (int split = 1; split < m; split++) {
            ans = Math.max(ans, f2(split, n, values, dp) + f2(m - split, n, values, dp));
        }
        for (int split = 1; split < n; split++) {
            ans = Math.max(ans, f2(m, split, values, dp) + f2(m, n - split, values, dp));
        }
        dp[m][n] = ans;
        return ans;
    }

    // 严格位置依赖的动态规划版本 + 优化
    // 优化1 : 递归的形式，改成迭代形式
    // 优化2 : prices中的单块收益直接填入dp表即可，如果有更好的分割方案，更新掉
    // 优化3 : 分割只需要枚举一半即可，对称性
    public static long sellingWood3(int m, int n, int[][] prices) {
        long[][] dp = new long[m + 1][n + 1];
        for (int[] p : prices) {
            dp[p[0]][p[1]] = p[2];
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = 1; k <= (j >> 1); k++) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[i][j - k]);
                }
                for (int k = 1; k <= (i >> 1); k++) {
                    dp[i][j] = Math.max(dp[i][j], dp[k][j] + dp[i - k][j]);
                }
            }
        }
        return dp[m][n];
    }
}
