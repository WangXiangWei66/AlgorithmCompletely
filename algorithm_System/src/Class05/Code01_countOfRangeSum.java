package Class05;


public class Code01_countOfRangeSum {
    //本题求的是数量
    public static int countRangeSum(int[] Nums, int lower, int upper) {
        if (Nums == null || Nums.length == 0) {
            return 0;
        }
        long[] sum = new long[Nums.length];
        sum[0] = Nums[0];
        for (int i = 1; i < Nums.length; i++) {
            sum[i] = sum[i - 1] + Nums[i];
        }
        return process(sum, 0, sum.length - 1, lower, upper);
    }

    public static int process(long[] sum, int L, int R, int lower, int upper) {
        if (L == R) {
            return sum[L] >= lower && sum[L] <= upper ? 1 : 0;
        }
        int m = L + ((R - L) >> 1);
        return process(sum, L, m, lower, upper) + process(sum, m + 1, R, lower, upper) + merge(sum, L, m, R, lower, upper);
    }

    public static int merge(long[] arr, int L, int M, int R, int lower, int upper) {
        int ans = 0;
        int windowL = L;
        int windowR = L;
        //窗口的范围是[windowL,windowR)
        for (int i = M + 1; i <= R; i++) {
            //这个arr已经是加工成了前缀和数组
            //求右组中的每个数Low与Upp，判断左边有多少个达标，最后总体累加
            long min = arr[i] - upper;
            long max = arr[i] - lower;
            while (windowR <= M && arr[windowR] <= max) {
                windowR++;
            }
            while (windowL <= M && arr[windowL] >= min) {
                windowL++;
            }
            ans += windowR - windowL;
        }
        long[] help = new long[R - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = M + 1;
        while (p1 <= M && p2 <= R) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= M) {
            help[i++] = arr[p1++];
        }
        while (p2 <= R) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++) {
            arr[L + i] = help[i];
        }
        return ans;
    }
}
