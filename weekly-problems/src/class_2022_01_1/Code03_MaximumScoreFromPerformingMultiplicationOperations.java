package class_2022_01_1;

//给你两个长度分别 n 和 m 的整数数组 nums 和 multipliers ，其中 n >= m ，
//数组下标 从 1 开始 计数。
//初始时，你的分数为 0 。
//你需要执行恰好 m 步操作。在第 i 步操作（从 1 开始 计数）中，需要：
//选择数组 nums 开头处或者末尾处 的整数 x 。
//你获得 multipliers[i] * x 分，并累加到你的分数中。
//将 x 从数组 nums 中移除。
//在执行 m 步操作后，返回 最大分数。
//leetcode链接 : https://leetcode.com/problems/maximum-score-from-performing-multiplication-operations/
public class Code03_MaximumScoreFromPerformingMultiplicationOperations {
    //left和right是当前数组A的左右边界
    public static int wang(int[] A, int[] B, int left, int right) {
        //已经移除的左右侧元素的数量
        int leftAlready = left;
        int rightAlready = A.length - right - 1;
        int i = leftAlready + rightAlready;
        if (i == B.length) {
            return 0;
        }
        int p1 = A[left] * B[i] + wang(A, B, left + 1, right);
        int p2 = A[right] * B[i] + wang(A, B, left, right - 1);
        return Math.max(p1, p2);
    }

    public static int maximumScore1(int[] A, int[] B) {
        if (A == null || A.length == 0 || B == null || B.length == 0 || A.length < B.length) {
            return 0;
        }
        return process1(A, B, 0, A.length - 1);
    }

    public static int process1(int[] A, int[] B, int L, int R) {
        int indexB = L + A.length - R - 1;//当前已经使用的乘数的索引
        if (indexB == B.length) {
            return 0;
        } else {
            int p1 = A[L] * B[indexB] + process1(A, B, L + 1, R);
            int p2 = A[R] * B[indexB] + process1(A, B, L, R - 1);
            return Math.max(p1, p2);
        }
    }

    public static int maximumScore2(int[] A, int[] B) {
        if (A == null || A.length < 0 || B == null || B.length < 0 || A.length < B.length) {
            return 0;
        }
        int N = A.length;
        int M = B.length;
        //dp[L][j]:表示从左侧取L个元素，从右侧取j个元素的最大得分
        int[][] dp = new int[M + 1][M + 1];
        for (int L = M - 1; L >= 0; L--) {
            for (int j = L + 1; j <= M; j++) {
                int R = N - M + j - 1;//当前右边界的实际索引
                int indexB = L + N - R - 1;//计算当前乘数的索引
                dp[L][j] = Math.max(A[L] * B[indexB] + dp[L + 1][j], A[R] * B[indexB] + dp[L][j - 1]);
            }
        }
        return dp[0][M];
    }
}
