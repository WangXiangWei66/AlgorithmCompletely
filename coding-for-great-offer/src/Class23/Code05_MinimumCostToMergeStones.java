package Class23;

import java.security.PublicKey;

//有 N 堆石头排成一排，第 i 堆中有stones[i]块石头。每次移动（move）需要将连续的K堆石头合并为一堆，而这个移动的成本为这K堆石头的总数。
//找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1 。
//Leetcode题目：https://leetcode.com/problems/minimum-cost-to-merge-stones/
public class Code05_MinimumCostToMergeStones {

    //arr[L....R]一定要整出P份，合并的最小代价，返回！
    //P:当前的区间需要分配的份数
//    public static int f(int[] arr, int K, int L, int R, int P) {
//        if (从L到R不可能弄出P份) {
//            return Integer.MAX_VALUE;
//        }
//        //真的有可能的！
//        if (P == 1) {
//            return L == R ? 0 : (f(arr, K, L, R, K) + 最后一次大合并的代价);
//        }
//        int ans = Integer.MAX_VALUE;
//        //真的有可能 p > 1
//        for (int i = L; i < R; i++) {
//            //L....i(1份) i+1....R(P-1份)
//            int left = f(arr, K, L, i, 1);
//            if (left == Integer.MAX_VALUE) {
//                continue;
//            }
//            int right = f(arr, K, i + 1, R, P - 1);
//            if (right == Integer.MAX_VALUE) {
//                continue;
//            }
//            int all = left + right;
//            ans = Math.min(ans, all);
//        }
//        return ans;
//    }

    public static int mergeStones1(int[] stones, int K) {
        int n = stones.length;
        //检查是否有可能合并成功
        if ((n - 1) % (K - 1) > 0) {
            return -1;
        }
        //计算前缀和
        int[] preSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            preSum[i + 1] = preSum[i] + stones[i];
        }
        return process1(0, n - 1, 1, stones, K, preSum);
    }

    public static int process1(int L, int R, int P, int[] arr, int K, int[] preSum) {
        //如果区间内只有一个石头
        if (L == R) {
            return P == 1 ? 0 : -1;
        }
        //如果目标是合并为1堆
        if (P == 1) {
            //合并为1堆，需要先合并成K堆
            int next = process1(L, R, K, arr, K, preSum);
            if (next == -1) {
                return -1;// 无法先合并成K堆，所以也无法合并成1堆
            } else {
                // 合并K堆的代价 = 合并成K堆的代价 + 这K堆的总和（最后一次合并的代价）
                return next + preSum[R + 1] - preSum[L];
            }
            //下面是目标合并成P堆
        } else {
            int ans = Integer.MAX_VALUE;
            //尝试所有可能的分割点，每次跳K-1步
            for (int mid = L; mid < R; mid += K - 1) {
                int next1 = process1(L, mid, 1, arr, K, preSum);//左边合成1堆
                int next2 = process1(mid + 1, R, P - 1, arr, K, preSum);//右边合成P-1堆
                if (next1 != -1 && next2 != -1) {
                    ans = Math.min(ans, next1 + next2);
                }
            }
            return ans;
        }
    }

    //本版本通过记忆化搜索将时间复杂度降低到 O (n³K) 级别
    public static int mergeStones2(int[] stones, int K) {
        int n = stones.length;
        if ((n - 1) % (K - 1) > 0) {
            return -1;
        }
        int[] preSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            preSum[i + 1] = preSum[i] + stones[i];
        }
        //dp[L][R][P] 表示将区间 [L, R] 的石头合并成 P 堆的最小代价。
        int[][][] dp = new int[n][n][K + 1];
        return process2(0, n - 1, 1, stones, K, preSum, dp);
    }

    // 因为上游调用的时候，一定确保都是有效的调用
    // 核心是这一句 : for (int mid = L; mid < R; mid += K - 1) { ... }
    // 这一句保证了一定都是有效调用
    // 所以不需要写很多边界条件的判断，因为任何调用，都一定会返回正常答案
    // mid每次跳K-1个位置，这保证了左侧递归、右侧递归都是有效的
    // 根本不会返回无效解的
    public static int process2(int L, int R, int P, int[] arr, int K, int[] preSum, int[][][] dp) {
        if (dp[L][R][P] != 0) {
            return dp[L][R][P];
        }
        if (L == R) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        if (P == 1) {
            ans = process2(L, R, K, arr, K, preSum, dp) + preSum[R + 1] - preSum[L];//将上区间总和，作为最后一次合并的代价
        } else {
            for (int mid = L; mid < R; mid += K - 1) {
                int next1 = process2(L, mid, 1, arr, K, preSum, dp);
                int next2 = process2(mid + 1, R, P - 1, arr, K, preSum, dp);
                ans = Math.min(ans, next1 + next2);
            }
        }
        dp[L][R][P] = ans;
        return ans;
    }

    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) (maxSize * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random());
        }
        return arr;
    }

    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int maxSize = 12;
        int maxValue = 100;
        System.out.println("test begin!");
        for (int testTime = 0; testTime < 100000; testTime++) {
            int[] arr = generateRandomArray(maxSize, maxValue);
            int K = (int) (Math.random() * 7) + 2;
            int ans1 = mergeStones1(arr, K);
            int ans2 = mergeStones2(arr, K);
            if (ans1 != ans2) {
                System.out.println(ans1);
                System.out.println(ans2);
            }
        }
    }
}
