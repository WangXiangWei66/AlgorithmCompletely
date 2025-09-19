package class_2022_01_2;

//来自美团
//小美有一个长度为n的数组，
//为了使得这个数组的和尽量大，她向会魔法的小团进行求助
//小团可以选择数组中至多两个不相交的子数组，
//并将区间里的数全都变为原来的10倍
//小团想知道他的魔法最多可以帮助小美将数组的和变大到多少?
public class Code05_MagicTowSubarrayMakeMaxSum {

    public static int maxSum1(int[] arr) {
        int n = arr.length;
        int[] preSum = preSum(arr);
        //不使用魔法或只使用一个子数组的魔法所能得到的最大和
        int ans = maxSumAtMostOneRangeMagic(preSum, 0, n - 1);
        for (int split = 0; split < n - 1; split++) {
            ans = Math.max(ans, maxSumAtMostOneRangeMagic(preSum, 0, split) + maxSumAtMostOneRangeMagic(preSum, split + 1, n - 1));
        }
        return ans;
    }

    //在[l...r]范围内，最多选择一个子数组进行10倍处理能得到的最大和
    public static int maxSumAtMostOneRangeMagic(int[] preSum, int l, int r) {
        if (l > r) {
            return 0;
        }
        int ans = sum(preSum, l, r);//不使用魔法
        for (int s = l; s <= r; s++) {
            for (int e = s; e <= r; e++) {
                ans = Math.max(ans, sum(preSum, l, s - 1) + sum(preSum, s, e) * 10 + sum(preSum, e + 1, r));
            }
        }
        return ans;
    }

    public static int[] preSum(int[] arr) {
        int n = arr.length;
        int[] preSum = new int[n];
        preSum[0] = arr[0];
        for (int i = 1; i < n; i++) {
            preSum[i] = preSum[i - 1] + arr[i];
        }
        return preSum;
    }

    public static int sum(int[] preSum, int l, int r) {
        return l > r ? 0 : (l == 0 ? preSum[r] : (preSum[r] - preSum[l - 1]));
    }

    public static int maxSum2(int[] arr) {
        int n = arr.length;
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return Math.max(arr[0], arr[0] * 10);
        }
        //初始化右半部分计算所需的变量
        int sum = arr[n - 1];//从最右侧元素开始累计
        int magic = sum * 10;//从最右侧元素开始使用魔法
        int[] right = new int[n];//right[i]:i到n-1范围内的最大可能和
        right[n - 1] = Math.max(sum, sum * 10);
        for (int i = n - 2; i >= 0; i--) {
            magic = 10 * arr[i] + Math.max(sum, magic);
            sum += arr[i];
            //三种情况：不使用魔法    右侧计算的最大值加上当前元素   使用魔法计算的总和
            right[i] = Math.max(Math.max(sum, right[i + 1] + arr[i]), magic);
        }
        int ans = right[0];//初始时只考虑右半部分的最大值
        sum = arr[0];
        magic = sum * 10;
        int dp = Math.max(sum, sum * 10);
        ans = Math.max(ans, dp + right[1]);
        for (int i = 1; i < n - 1; i++) {
            magic = 10 * arr[i] + Math.max(sum, magic);
            sum += arr[i];
            dp = Math.max(Math.max(sum, dp + arr[i]), magic);
            ans = Math.max(ans, dp + right[i + 1]);
        }
        return ans;
    }

    public static int[] randomArray(int len, int value) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * value) - (int) (Math.random() * value);
        }
        return arr;
    }

    // 为了测试
    public static void main(String[] args) {
        int len = 30;
        int value = 100;
        int testTime = 500000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * len) + 1;
            int[] arr = randomArray(n, value);
            int ans1 = maxSum1(arr);
            int ans2 = maxSum2(arr);
            if (ans1 != ans2) {
                System.out.println("出错了!");
                break;
            }
        }
        System.out.println("测试结束");
    }

}
