package class_2022_04_1;

import java.util.Arrays;

//来自小红书
//小红书第二题：
//薯队长最近在参加了一个活动，主办方提供了N个礼物以供挑选，
//每个礼物有一个价值，范围在0 ~ 10^9之间，
//薯队长可以从中挑选k个礼物
//返回：其中价值最接近的两件礼物之间相差值尽可能大的结果
public class Code05_PickKnumbersNearTowNumberMaxDiff {

    public static int maxNear(int[] arr, int k) {
        if (arr.length < k) {
            return -1;
        }
        Arrays.sort(arr);
        int n = arr.length;
        int l = 0;//最小的可能差值为0
        int r = arr[n - 1] - arr[0];//最大的可能差值
        int m = 0;
        int ans = 0;
        while (l <= r) {
            m = (l + r) / 2;
            if (year(arr, k, m)) {
                ans = m;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return ans;
    }
    //limit：当前礼物与上一个挑选的礼物之间的差值不小于limit
    public static boolean year(int[] arr, int k, int limit) {
        int last = arr[0];
        int pick = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] - last >= limit) {
                pick++;
                last = arr[i];
            }
        }
        return pick >= k;
    }
}
