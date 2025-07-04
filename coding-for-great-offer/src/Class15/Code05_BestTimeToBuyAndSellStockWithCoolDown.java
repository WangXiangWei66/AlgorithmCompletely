package Class15;

public class Code05_BestTimeToBuyAndSellStockWithCoolDown {

    public static int maxProfit(int[] prices) {
        return process1(prices, false, 0, 0);
    }

    //buy == false : 目前没有，而且当前没有购买行为
    //buy == true : 已经买了，买入价buyPrices，待卖出
    //index当前处理的天数
    //buyPrices：买入股票的价格
    public static int process1(int[] prices, boolean buy, int index, int buyPrices) {
        if (index >= prices.length) {
            return 0;
        }
        if (buy) {
            int noSell = process1(prices, true, index + 1, buyPrices);
            //prices[index] - buyPrices：为获得的利润
            int yesSell = prices[index] - buyPrices + process1(prices, false, index + 2, 0);
            return Math.max(noSell, yesSell);
        } else {
            int noBuy = process1(prices, false, index + 1, 0);
            int yesBuy = process1(prices, true, index + 1, prices[index]);
            return Math.max(noBuy, yesBuy);
        }
    }

    public static int maxProfit2(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }
        int N = prices.length;
        //buy[i]:在0...i范围上，最后一次操作是buy动作，最后一次操作可能在i位置，也可能在这之前
        int[] buy = new int[N];
        //sell[i]:在0...i上做无限次交易，最后是卖的情况下，获得最好收益
        int[] sell = new int[N];
        buy[1] = Math.max(-prices[0], -prices[1]);
        sell[1] = Math.max(0, prices[1] - prices[0]);
        for (int i = 2; i < N; i++) {
            //之前交易获得的最大收益-最后buy的收购价格
            buy[i] = Math.max(buy[i - 1], sell[i - 2] - prices[i]);
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i]);
        }
        return sell[N - 1];
    }

    //可用空间压缩，用有限几个变量去推
    public static int maxProfit3(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }
        int buy1 = Math.max(-prices[0], -prices[1]);
        int sell1 = Math.max(0, prices[1] - prices[0]);
        int sell2 = 0;
        for (int i = 2; i < prices.length; i++) {
            int tmp = sell1;
            sell1 = Math.max(sell1, buy1 + prices[i]);
            buy1 = Math.max(buy1, sell2 - prices[i]);
            sell2 = tmp;
        }
        return sell1;
    }
}
