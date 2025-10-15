package class_2022_05_3;

//来自京东
//4.2笔试
//给定一个数组arr，长度为N，arr中所有的值都在1~K范围上
//你可以删除数字，目的是让arr的最长递增子序列长度小于K
//返回至少删除几个数字能达到目的
//N <= 10^4，K <= 10^2
public class Code02_RemoveNumbersNotIncreasingAll {

    public static int minRemove1(int[] arr, int k) {
        return process1(arr, 0, new int[arr.length], 0, k);
    }

    //size：路径的大小
    public static int process1(int[] arr, int index, int[] path, int size, int k) {
        if (index == arr.length) {
            return lengthOfLIS(path, size) < k ? (arr.length - size) : Integer.MAX_VALUE;
        } else {
            int p1 = process1(arr, index + 1, path, size, k);
            path[size] = arr[index];
            int p2 = process1(arr, index + 1, path, size + 1, k);
            return Math.min(p1, p2);
        }
    }

    //计算最长递增子序列
    public static int lengthOfLIS(int[] arr, int size) {
        if (size == 0) {
            return 0;
        }
        //保存不同长度的递增子序列的最小可能尾部值
        int[] ends = new int[size];
        ends[0] = arr[0];
        int right = 0;//ends数组中有效元素的右边界
        int l = 0;
        int r = 0;
        int m = 0;
        int max = 1;//最长递增子序列长度
        for (int i = 1; i < size; i++) {
            l = 0;
            r = right;
            while (l <= r) {
                m = (l + r) / 2;
                if (arr[i] > ends[m]) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
            right = Math.max(right, l);
            ends[l] = arr[i];
            max = Math.max(max, l + 1);
        }
        return max;
    }

    //len：当前已形成的递增子序列长度
    public static int wang(int[] arr, int index, int len, int k) {
        if (len == k) {
            return Integer.MAX_VALUE;
        }
        if (index == arr.length) {
            return 0;
        }
        int cur = arr[index];
        if (len >= cur || len + 1 < cur) {
            return wang(arr, index + 1, len, k);
        }
        //将当前元素加入子序列
        int p1 = wang(arr, index + 1, len + 1, k);
        int p2 = Integer.MAX_VALUE;
        //删除当前元素
        int next2 = wang(arr, index + 1, len, k);
        if (next2 != Integer.MAX_VALUE) {
            p2 = 1 + next2;
        }
        return Math.min(p1, p2);
    }

    public static int minRemove2(int[] arr, int k) {
        int n = arr.length;
        // dp[i][j]表示：处理到第i个元素，当前递增子序列长度为j时，需要的最少删除数
        int[][] dp = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                dp[i][j] = -1;
            }
        }
        return process2(arr, k, 0, 0, dp);
    }

    public static int process2(int[] arr, int k, int index, int range, int[][] dp) {
        if (range == k) {
            return Integer.MAX_VALUE;
        }
        if (index == arr.length) {
            return 0;
        }
        if (dp[index][range] != -1) {
            return dp[index][range];
        }
        int ans = 0;
        //当前元素正好可以延长递增子序列
        if (arr[index] == range + 1) {
            //删除当前元素
            int p1 = process2(arr, k, index + 1, range, dp);
            p1 += p1 != Integer.MAX_VALUE ? 1 : 0;
            int p2 = process2(arr, k, index + 1, range + 1, dp);
            ans = Math.min(p1, p2);
        } else {
            ans = process2(arr, k, index + 1, range, dp);
        }
        dp[index][range] = ans;
        return ans;
    }

    public static int[] randomArray(int len, int k) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * k) + 1;
        }
        return arr;
    }

    // 为了验证
    public static void main(String[] args) {
        int N = 15;
        int K = 6;
        int testTime = 10000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int len = (int) (Math.random() * N) + 1;
            int k = (int) (Math.random() * K) + 1;
            int[] arr = randomArray(len, k);
            int ans1 = minRemove1(arr, k);
            int ans2 = minRemove2(arr, k);
            if (ans1 != ans2) {
                System.out.println("出错了！");
                for (int num : arr) {
                    System.out.print(num + " ");
                }
                System.out.println();
                System.out.println("k : " + k);
                System.out.println("ans1 : " + ans1);
                System.out.println("ans2 : " + ans2);
                break;
            }
        }
        System.out.println("测试结束");
    }
}
