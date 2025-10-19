package class_2022_06_2;
//作为国王的统治者，你有一支巫师军队听你指挥。
//给你一个下标从 0开始的整数数组strength，
//其中strength[i]表示第i位巫师的力量值。
//对于连续的一组巫师（也就是这些巫师的力量值是strength的子数组），
//总力量定义为以下两个值的乘积：
//巫师中 最弱的能力值 * 组中所有巫师的个人力量值 之和。
//请你返回 所有巫师组的 总力量之和。由于答案可能很大，请将答案对109 + 7取余后返回。
//子数组是一个数组里 非空连续子序列。
//测试链接 : https://leetcode.cn/problems/sum-of-total-strength-of-wizards/
public class Code02_SumOfTotalStrengthOfWizards {

    public static final long mod = 1000000007;

    public static int totalStrength(int[] arr) {
        int n = arr.length;
        long preSum = arr[0];//记录当前的累加和（前缀和）
        long[] sumSum = new long[n];//前缀和之和数组
        sumSum[0] = arr[0];
        for (int i = 1; i < n; i++) {
            preSum += arr[i];
            sumSum[i] = (sumSum[i - 1] + preSum) % mod;
        }
        //维持栈内单调递增的数组下标
        int[] stack = new int[n];
        int size = 0;//栈的实际大小
        long ans = 0;
        for (int i = 0; i < n; i++) {
            while (size > 0 && arr[stack[size - 1]] >= arr[i]) {
                int m = stack[--size];//栈顶元素弹出
                int l = size > 0 ? stack[size - 1] : -1;
                ans += magicSum(arr, sumSum, l, m, i);//计算以m为最小值的子数组的总力量
                ans %= mod;
            }
            stack[size++] = i;
        }
        while (size > 0) {
            int m = stack[--size];
            int l = size > 0 ? stack[size - 1] : -1;
            ans += magicSum(arr, sumSum, l, m, n);
            ans %= mod;
        }
        return (int) ans;
    }
    //m为最小值，左边界l到右边界r之间的所有子数组的总力量之和
    public static long magicSum(int[] arr, long[] sumSum, int l, int m, int r) {
        //计算左部分：所有包含m且右边界在[m, r-1]的子数组的和的总和
        //公式推导：(m - l) * (sumSum[r-1] - sumSum[m-1])
        long left = (long) (m - l) * (sumSum[r - 1] - (m - 1 >= 0 ? sumSum[m - 1] : 0) + mod) % mod;
        // 计算右部分：所有包含m且左边界在[l+1, m]的子数组的和的总和
        // 公式推导：(r - m) * (sumSum[m-1] - sumSum[l-1])
        long right = (long) (r - m) * ((m - 1 >= 0 ? sumSum[m - 1] : 0) - (l - 1 >= 0 ? sumSum[l - 1] : 0) + mod) % mod;
        return (long) arr[m] * ((left - right + mod) % mod);
    }
}
