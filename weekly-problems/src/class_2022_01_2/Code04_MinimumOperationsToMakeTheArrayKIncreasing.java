package class_2022_01_2;

//给你一个下标从 0开始包含 n个正整数的数组arr，和一个正整数k。
//如果对于每个满足k <= i <= n-1的下标i，都有arr[i-k] <= arr[i]，那么我们称arr是 K递增 的。
//比方说，arr = [4, 1, 5, 2, 6, 2]对于k = 2是 K 递增的，因为：
//arr[0] <= arr[2] (4 <= 5)
//arr[1] <= arr[3] (1 <= 2)
//arr[2] <= arr[4] (5 <= 6)
//arr[3] <= arr[5] (2 <= 2)
//但是，相同的数组arr对于k = 1不是 K 递增的（因为arr[0] > arr[1]），
//对于k = 3也不是 K 递增的（因为arr[0] > arr[3]）。
//每一次 操作中，你可以选择一个下标i 并将arr[i] 改成任意正整数。
//请你返回对于给定的 k，使数组变成 K 递增的 最少操作次数。
//leetcode链接 : https://leetcode.com/problems/minimum-operations-to-make-the-array-k-increasing/
public class Code04_MinimumOperationsToMakeTheArrayKIncreasing {

    public static int kIncreasing(int[] arr, int k) {
        int n = arr.length;
        int[] help = new int[(n + k - 1) / k];//向上取整
        int ans = 0;
        //对k个独立的子序列单独处理
        for (int i = 0; i < k; i++) {
            ans += need(arr, help, n, i, k);
        }
        return ans;
    }
    //help：计算最长递减子序列
    //start：子序列的索引位置
    public static int need(int[] arr, int[] help, int n, int start, int k) {
        int j = 0;//当前子序列的元素的个数
        int size = 0;//最长非递减子序列的长度
        for (; start < n; start += k, j++) {
            size = insert(help, size, arr[start]);
        }
        return j - size;
    }
    //维持最长非递减子序列
    public static int insert(int[] help, int size, int num) {
        int l = 0;
        int r = size - 1;
        int m = 0;
        int ans = size;//默认插入位置为数组的末尾
        while (l <= r) {
            m = (l + r) / 2;
            if (help[m] > num) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        help[ans] = num;
        return ans == size ? size + 1 : size;
    }
}
