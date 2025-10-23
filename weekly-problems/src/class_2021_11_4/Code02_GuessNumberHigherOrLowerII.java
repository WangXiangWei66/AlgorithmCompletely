package class_2021_11_4;

//我们正在玩一个猜数游戏，游戏规则如下：
//我从1到 n 之间选择一个数字。
//你来猜我选了哪个数字。
//如果你猜到正确的数字，就会 赢得游戏 。
//如果你猜错了，那么我会告诉你，我选的数字比你的 更大或者更小 ，并且你需要继续猜数。
//每当你猜了数字 x 并且猜错了的时候，你需要支付金额为 x 的现金。
//如果你花光了钱，就会 输掉游戏 。
//给你一个特定的数字 n ，返回能够 确保你获胜 的最小现金数，不管我选择那个数字 。
//Leetcode链接 : https://leetcode.com/problems/guess-number-higher-or-lower-ii/
public class Code02_GuessNumberHigherOrLowerII {

    public static int minGold(int n) {
        return zuo(1, n);
    }

    public static int zuo(int L, int R) {
        if (L == R) {
            return 0;
        }
        if (L == R - 1) {
            return L;
        }
        //单独算是因为猜错后的搜索方向是确定的
        int min = Math.min(L + zuo(L + 1, R), +zuo(L, R - 1) + R);
        for (int i = L + 1; i < R; i++) {
            min = Math.min(min, i + Math.max(zuo(L, i - 1), zuo(i + 1, R)));
        }
        return min;
    }

    public static int minGold2(int n) {
        //dp[L][R]:在[L...R]范围内确保获胜的最小现金
        int[][] dp = new int[n + 1][n + 1];
        for (int i = 1; i < n; i++) {
            dp[i][i + 1] = i;
        }
        for (int L = n - 2; L >= 1; L--) {
            for (int R = L + 2; R <= n; R++) {
                int min = Math.min(L + dp[L + 1][R], R + dp[L][R - 1]);
                for (int i = L + 1; i < R; i++) {
                    min = Math.min(min, i + Math.max(dp[L][i - 1], dp[i + 1][R]));
                }
                dp[L][R] = min;
            }
        }
        return dp[1][n];
    }

    public static int getMoneyAmount1(int n) {
        return process1(1, n);
    }

    public static int process1(int L, int R) {
        if (L == R) {
            return 0;
        }
        if (L == R - 1) {
            return L;
        }
        int ans = Math.min(L + process1(L + 1, R), R + process1(L, R - 1));

        for (int M = L + 1; M < R; M++) {
            ans = Math.min(ans, M + Math.max(process1(L, M - 1), process1(M + 1, R)));
        }
        return ans;
    }

    public static int getMoneyAmount2(int n) {
        int[][] dp = new int[n + 1][n + 1];
        for (int i = 1; i < n; i++) {
            dp[i][i + 1] = i;
        }
        for (int L = n - 2; L >= 1; L--) {
            for (int R = L + 2; R <= n; R++) {
                dp[L][R] = Math.min(L + dp[L + 1][R], R + dp[L][R - 1]);
                for (int M = L + 1; M < R; M++) {
                    dp[L][R] = Math.min(dp[L][R], M + Math.max(dp[L][M - 1], dp[M + 1][R]));
                }
            }
        }
        return dp[1][n];
    }
}
