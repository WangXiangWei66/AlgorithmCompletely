package class_2022_01_3;

//来自字节跳动
//给定一个数组arr，其中的值有可能正、负、0
//给定一个正数k
//返回累加和>=k的所有子数组中，最短的子数组长度
public class Code03_ShortestSubarrayWithSumAtLeastK {
    //前缀和+单调栈
    public static int shortestSubarray1(int[] arr, int k) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        int n = arr.length + 1;
        long[] sum = new long[n];//默认将sum[0]设为了0
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + arr[i - 1];
        }
        int[] stack = new int[n];//单调栈，存储前缀和数组的索引，保持sum值递增
        int size = 1;//栈的大小初始化为0，因为sum[0]开始的时候填了一个0
        int ans = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            int mostRight = mostRight(sum, stack, size, sum[i] - k);
            ans = Math.min(ans, mostRight == -1 ? Integer.MAX_VALUE : (i - mostRight));
            while (size > 0 && sum[stack[size - 1]] >= sum[i]) {
                size--;
            }
            stack[size++] = i;
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
    //stack：前缀和数组索引的单调栈
    //aim：判断子数组的和等是否大于于k
    public static int mostRight(long[] sum, int[] stack, int size, long aim) {
        int l = 0;
        int r = size - 1;
        int m = 0;
        int ans = -1;
        while (l <= r) {
            m = (l + r) / 2;
            if (sum[stack[m]] <= aim) {
                ans = stack[m];
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return ans;
    }
}
