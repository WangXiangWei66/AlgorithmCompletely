package Class15;

public class Code03_BestTimeToBuyAndSellStockIII {

    //核心思路：动态规划 + 状态压缩。通过维护四个关键变量，在一次遍历中完成状态转移，最终得到最大利润。
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        //最终结果，最大利润
        int ans = 0;
        //完成一次交易后，再买一次的最大收益
        int doneOnceMinusBuyMax = -prices[0];
        //完成一次交易的最大收益
        int doneOnceMax = 0;
        //记录遍历到当前位置的最低价格
        int min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            //记录最低股票价格
            min = Math.min(min, prices[i]);
            //doneOnceMinusBuyMax + prices[i]：完成一次交易后再买入，然后在当前位置卖出的总收益。
            ans = Math.max(ans, doneOnceMinusBuyMax + prices[i]);
            //prices[i] - min：在当前位置卖出，在最低点买入
            doneOnceMax = Math.max(doneOnceMax, prices[i] - min);
            doneOnceMinusBuyMax = Math.max(doneOnceMinusBuyMax, doneOnceMax - prices[i]);
        }
        return ans;
    }
}
