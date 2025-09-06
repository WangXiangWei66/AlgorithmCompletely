package Class51;

//珂珂喜欢吃香蕉。这里有N堆香蕉，第 i 堆中有piles[i]根香蕉。警卫已经离开了，将在H小时后回来。
//珂珂可以决定她吃香蕉的速度K（单位：根/小时）。每个小时，她将会选择一堆香蕉，从中吃掉 K 根。如果这堆香蕉少于 K 根，她将吃掉这堆的所有香蕉，然后这一小时内不会再吃更多的香蕉。
//珂珂喜欢慢慢吃，但仍然想在警卫回来前吃掉所有的香蕉。
//返回她可以在 H 小时内吃掉所有香蕉的最小速度 K（K 为整数）。
//示例 1：
//输入: piles = [3,6,7,11], H = 8
//输出: 4
//示例2：
//输入: piles = [30,11,23,4,20], H = 5
//输出: 30
//示例3：
//输入: piles = [30,11,23,4,20], H = 6
//输出: 23
//leetcode题目：https://leetcode.com/problems/koko-eating-bananas/
public class Problem_0875_KokoEatingBananas {

    public static int minEatingSpeed(int[] piles, int h) {
        //最大最小可能速度
        int L = 1;
        int R = 0;
        for (int pile : piles) {
            R = Math.max(R, pile);
        }
        int ans = 0;
        int M = 0;
        while (L <= R) {
            M = L + ((R - L) >> 1);
            //检查当前速度M是否能在H小时内吃完所有香蕉
            if (hours(piles, M) <= h) {
                ans = M;
                R = M - 1;
            } else {
                L = M + 1;
            }
        }
        return ans;
    }

    public static long hours(int[] piles, int speed) {
        long ans = 0;
        int offset = speed - 1;//快速计算向上取整的技巧
        for (int pile : piles) {
            ans += (pile + offset) / speed;//(pile + offset) / speed 等价于“pile / speed 的向上取整”
        }
        return ans;
    }
}
