package Class15;

public class Code04_BestTimeToBuyAndSellStockIV {

    public static int allTrans(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            ans += Math.max(prices[i] - prices[i - 1], 0);
        }
        return ans;
    }

    public static int maxProfit2(int K, int[] arr) {
        if (arr == null || arr.length == 0 || K < 1) {
            return 0;
        }
        int N = arr.length;
        if (K > N / 2) {
            return allTrans(arr);
        }
        //从左往右+业务限制模型
        //dp[i][j]:智只能在arr[0...i]上交易，交易次数不能超过J次，返回最大交易次数
        //本表整体从上往下填写，然后从左往右
        //本表的第一行和第一列都是0
        int[][] dp = new int[N][K + 1];
        //大循环从左往右
        for (int j = 1; j <= K; j++) {
            //p1是不进行新交易的最大利润
            int p1 = dp[0][j];
            //参与交易时，当天的最大收益
            int best = Math.max(dp[1][j - 1] - arr[1], dp[0][j - 1] - arr[0]);
            dp[1][j] = Math.max(p1, best + arr[1]);
            for (int i = 2; i < N; i++) {
                //i位置不参与交易
                p1 = dp[i - 1][j];
                int newP = dp[i][j - 1] - arr[i];
                best = Math.max(newP, best);
                dp[i][j] = Math.max(p1, best + arr[i]);
            }
        }
        return dp[N - 1][K];
    }

    public static int maxProfit(int K, int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int N = prices.length;
        if (K >= N / 2) {
            return allTrans(prices);
        }
        int[][] dp = new int[K + 1][N];
        int ans = 0;
        for (int tran = 1; tran <= K; tran++) {
            //前 tran-1 次交易在第 0 天的最大利润
            int pre = dp[tran][0];
            //在第 0 天买入后的剩余利润
            int best = pre - prices[0];
            for (int index = 1; index < N; index++) {
                //前 tran-1 次交易在第 index 天的最大利润
                pre = dp[tran - 1][index];
                //交易和不交易两种情况
                dp[tran][index] = Math.max(dp[tran][index - 1],prices[index] + best);
                //更新最佳买入点
                best = Math.max(best, pre - prices[index]);
                ans = Math.max(dp[tran][index], ans);
            }
        }
        return ans;
    }
}
