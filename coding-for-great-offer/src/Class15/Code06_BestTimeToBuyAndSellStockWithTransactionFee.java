package Class15;

public class Code06_BestTimeToBuyAndSellStockWithTransactionFee {

    public static int maxProfit(int[] arr, int fee) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int N = arr.length;
        int bestBuy = -arr[0] - fee;
        //bestSell为收入
        int bestSell = 0;
        for (int i = 1; i < N; i++) {
            int curBuy = bestSell - arr[i] - fee;
            int curSell = bestBuy + arr[i];
            bestBuy = Math.max(bestBuy, curBuy);
            bestSell = Math.max(bestSell, curSell);
        }
        return bestSell;
    }
}
