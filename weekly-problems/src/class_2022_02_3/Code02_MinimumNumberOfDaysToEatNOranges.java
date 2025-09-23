package class_2022_02_3;

import java.util.HashMap;

//厨房里总共有 n个橘子，你决定每一天选择如下方式之一吃这些橘子
//1）吃掉一个橘子。
//2）如果剩余橘子数 n能被 2 整除，那么你可以吃掉 n/2 个橘子。
//3）如果剩余橘子数n能被 3 整除，那么你可以吃掉 2*(n/3) 个橘子。
//每天你只能从以上 3 种方案中选择一种方案。
//请你返回吃掉所有 n个橘子的最少天数。
//leetcode链接: https://leetcode.com/problems/minimum-number-of-days-to-eat-n-oranges/
public class Code02_MinimumNumberOfDaysToEatNOranges {

    public static HashMap<Integer, Integer> dp = new HashMap<>();

    public static int minDays(int n) {
        if (n <= 1) {
            return 1;
        }
        if (dp.containsKey(n)) {
            return dp.get(n);
        }
        int ans = Math.min(n % 2 + 1 + minDays(n / 2), n % 3 + 1 + minDays(n / 3));
        dp.put(n, ans);
        return ans;
    }
}
