package Class52;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//给定一个数组arr，arr[i] = j，表示来到i位置需要承受j的代价。给定一个正数jump，表示任何位置都可以随意往右跳<=jump步。
//要求你从1位置开始，跳到数组最后位置n，在保证最小代价的前提下，返回字典序最小的路径
//leetcode题目：https://leetcode.com/problems/coin-path/
public class Problem_0656_CoinPath {

    public static int minCost(int[] arr, int jump) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        if (arr[0] == -1 || arr[n - 1] == -1) {
            return -1;
        }
        int[] dp = new int[n];
        dp[0] = arr[0];
        for (int i = 1; i < n; i++) {
            dp[i] = Integer.MAX_VALUE;
            if (arr[i] == -1) {
                for (int pre = Math.max(0, i - jump); pre < i; pre++) {
                    if (dp[pre] != -1) {
                        dp[i] = Math.min(dp[i], dp[pre] + arr[i]);
                    }
                }
            }
            dp[i] = dp[i] == Integer.MAX_VALUE ? -1 : dp[i];
        }
        return dp[n - 1];
    }

    public static List<Integer> cheapestJump(int[] arr, int jump) {
        int n = arr.length;
        int[] best = new int[n];//到达每个位置的最小代价
        int[] last = new int[n];//存储路径中每个位置的 前一个位置
        int[] size = new int[n];//到达每个位置的路径长度
        Arrays.fill(best, Integer.MAX_VALUE);
        Arrays.fill(last, -1);//各个位置初始时都没有前序位置
        best[0] = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] != -1) {
                for (int j = Math.max(0, i - jump); j < i; j++) {
                    if (arr[j] != -1) {
                        int cur = best[j] + arr[i];
                        if (cur < best[i] || (cur == best[i] && size[i] - 1 < size[j])) {
                            best[i] = cur;
                            last[i] = j;
                            size[i] = size[j] + 1;
                        }
                    }
                }
            }
        }
        List<Integer> path = new LinkedList<>();
        for (int cur = n - 1; cur >= 0; cur = last[cur]) {
            path.add(0, cur + 1);//将当前位置添加到路径的最前面
        }
        return path.get(0) != 1 ? new LinkedList<>() : path;
    }
}
