package Class36;

//来自哈喽单车(Leetcode原题)
//Alice 和 Bob 两个人轮流玩一个游戏，Alice 先手。
//一开始，有 n个石子堆在一起。每个人轮流操作，正在操作的玩家可以从石子堆里拿走 任意非零 平方数个石子。
//如果石子堆里没有石子了，则无法操作的玩家输掉游戏。
//给你正整数n，且已知两个人都采取最优策略。如果 Alice 会赢得比赛，那么返回True，否则返回False。
//leetcode原题 : https://leetcode.com/problems/stone-game-iv/
public class Code10_StoneGameIV {

    public static boolean winnerSquareGame1(int n) {
        if (n == 0) {
            return false;
        }
        for (int i = 1; i * i <= n; i++) {
            if (!winnerSquareGame1(n - i * i)) {
                return true;
            }
        }
        return false;
    }

    //时间复杂度为O(n * sqrt(n))
    //空间复杂度为(N)
    public static boolean winnerSquareGame2(int n) {
        //dp[i]:有i个石子时，当前玩家是否会赢
        //0:未计算
        //1：能赢
        //2：不能赢
        int[] dp = new int[n + 1];
        dp[0] = -1;
        return process2(n, dp);
    }

    public static boolean process2(int n, int[] dp) {
        if (dp[n] != 0) {
            return dp[n] == 1 ? true : false;
        }
        boolean ans = false;
        for (int i = 1; i * i <= n; i++) {
            if (!process2(n - i * i, dp)) {
                ans = true;
                break;
            }
        }
        dp[n] = ans ? 1 : -1;
        return ans;
    }


    public static boolean winnerSquareGame3(int n) {
        boolean[] dp = new boolean[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                if (!dp[i - j * j]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        int n = 100;
        System.out.println(winnerSquareGame3(n));
    }
}
