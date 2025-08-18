package Class37;

//在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。
//Leetcode题目 : https://leetcode.com/problems/maximal-square/
public class Problem_0221_MaximalSquare {
    //时间复杂度为O(N * M)
    //空间复杂度为O(N * M)
    public static int maximalSquare(char[][] m) {
        if (m == null || m.length == 0 || m[0].length == 0) {
            return 0;
        }
        int N = m.length;
        int M = m[0].length;
        //dp[i][j]:以矩阵中(i,j)位置为右下角的最大正方向的边长
        int[][] dp = new int[N + 1][N + 1];
        int max = 0;
        //第一列无法往左扩展
        for (int i = 0; i < N; i++) {
            if (m[i][0] == '1') {
                dp[i][0] = 1;
                max = 1;
            }
        }
        //第一行无法往上扩展
        for (int j = 1; j < M; j++) {
            if (m[0][j] == '1') {
                dp[0][j] = 1;
                max = 1;
            }
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (m[i][j] == '1') {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                    max = Math.max(max, dp[i][j]);
                }
            }
        }
        return max * max;
    }
}
