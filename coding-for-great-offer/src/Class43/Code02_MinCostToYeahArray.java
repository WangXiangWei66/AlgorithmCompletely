package Class43;

import java.util.Arrays;

//来自360笔试
//给定一个正数数组arr，长度为n，下标0~n-1
//arr中的0、n-1位置不需要达标，它们分别是最左、最右的位置
//中间位置i需要达标，达标的条件是 : arr[i-1] > arr[i] 或者 arr[i+1] > arr[i]哪个都可以
//你每一步可以进行如下操作：对任何位置的数让其-1
//你的目的是让arr[1~n-2]都达标，这时arr称之为yeah！数组
//返回至少要多少步可以让arr变成yeah！数组
//数据规模 : 数组长度 <= 10000，数组中的值<=500
public class Code02_MinCostToYeahArray {

    public static final int INVALID = Integer.MAX_VALUE;

    public static int minCost0(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int n = arr.length;
        //寻找数组中元素的最小值
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int base = min - n;//元素可以较少到的最低值
        return process0(arr, base, 0);
    }

    public static int process0(int[] arr, int base, int index) {
        if (index == arr.length) {
            for (int i = 1; i < arr.length - 1; i++) {
                if (arr[i - 1] <= arr[i] && arr[i] >= arr[i + 1]) {
                    return INVALID;
                }
            }
            return 0;
        } else {
            int ans = INVALID;
            int tmp = arr[index];//保存当前元素的原始值
            //将当前元素减少到不同值
            for (int cost = 0; arr[index] >= base; cost++, arr[index]--) {
                int next = process0(arr, base, index + 1);
                if (next != INVALID) {
                    ans = Math.min(ans, cost + next);
                }
            }
            arr[index] = tmp;
            return ans;
        }
    }

    public static int minCost1(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        //转换数组值，对数组进行了一次偏移处理
        for (int i = 0; i < arr.length; i++) {
            arr[i] += arr.length - min;
        }
        //从索引1开始处理，前一个元素是arr[0],初始状态preOk为true
        return process1(arr, 1, arr[0], true);
    }

    //preOk:前一个位置的元素是否达标
    public static int process1(int[] arr, int index, int pre, boolean preOk) {
        if (index == arr.length - 1) {
            return preOk || pre < arr[index] ? 0 : INVALID;
        }
        int ans = INVALID;
        //前一个位置的元素已经达标了
        if (preOk) {
            //当前元素可以任意减小直到为0
            for (int cur = arr[index]; cur >= 0; cur--) {
                int next = process1(arr, index + 1, cur, cur < pre);
                if (next != INVALID) {
                    ans = Math.min(ans, arr[index] - cur + next);
                }
            }
        } else {
            for (int cur = arr[index]; cur > pre; cur--) {
                int next = process1(arr, index + 1, cur, false);
                if (next != INVALID) {
                    ans = Math.min(ans, arr[index] - cur + next);
                }
            }
        }
        return ans;
    }

    public static int minCost2(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            arr[i] += n - min;
        }
        //dp[index][state][pre]:
        //index:当前处理的元素索引
        //state:状态标志
        //pre；前一个元素的最终值
        int[][][] dp = new int[n][2][];
        for (int i = 1; i < n; i++) {
            //第二维的元素是0或1，第三位为前一个元素的可能值范围
            dp[i][0] = new int[arr[i - 1] + 1];
            dp[i][1] = new int[arr[i - 1] + 1];
            //将达标与不达标的数组，都设置为无效值
            Arrays.fill(dp[i][0], INVALID);//初始化为无效值
            Arrays.fill(dp[i][1], INVALID);
        }
        for (int pre = 0; pre <= arr[n - 2]; pre++) {
            dp[n - 1][0][pre] = pre < arr[n - 1] ? 0 : INVALID;
            dp[n - 1][1][pre] = 0;
        }
        //DP表从后往前填
        for (int index = n - 2; index >= 1; index--) {
            for (int pre = 0; pre <= arr[index - 1]; pre++) {
                //处理当前元素未达标的情况
                for (int cur = arr[index]; cur > pre; cur--) {
                    int next = dp[index + 1][0][cur];
                    if (next != INVALID) {
                        dp[index][0][pre] = Math.min(dp[index][0][pre], arr[index] - cur + next);
                    }
                }
                for (int cur = arr[index]; cur >= 0; cur--) {
                    int next = dp[index + 1][cur < pre ? 1 : 0][cur];
                    if (next != INVALID) {
                        dp[index][1][pre] = Math.min(dp[index][1][pre], arr[index] - cur + next);
                    }
                }
            }
        }
        return dp[1][1][arr[0]];
    }

    public static int minCost3(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            arr[i] += n - min;
        }
        //[]:前一个元素的最终值
        int[][][] dp = new int[n][2][];
        for (int i = 1; i < n; i++) {
            dp[i][0] = new int[arr[i - 1] + 1];
            dp[i][1] = new int[arr[i - 1] + 1];
        }
        //填充倒数第二个元素
        for (int p = 0; p <= arr[n - 2]; p++) {
            dp[n - 1][0][p] = p < arr[n - 1] ? 0 : INVALID;
        }
        //best：预处理问题的最优解，避免主循环中重复计算
        int[][] best = best(dp, n - 1, arr[n - 2]);
        for (int i = n - 2; i >= 1; i--) {
            //p:前一个元素i-1的最终值
            for (int p = 0; p <= arr[i - 1]; p++) {
                if (arr[i] < p) {
                    dp[i][1][p] = best[1][arr[i]];
                } else {
                    dp[i][1][p] = Math.min(best[0][p], p > 0 ? best[1][p - 1] : INVALID);
                }
                dp[i][0][p] = arr[i] <= p ? INVALID : best[0][p + 1];
            }
            //更新best数组，为上一层计算做准备
            best = best(dp, i, arr[i - 1]);
        }
        return dp[1][1][arr[0]];
    }

    //best[0][p]:0状态下的从p开始的最小代价
    //best[1][p]:1状态下的从p开始的最小代价
    public static int[][] best(int[][][] dp, int i, int v) {
        int[][] best = new int[2][v + 1];//存储前一个元素两种状态的最优解
        //从最大值往最小值遍历
        best[0][v] = dp[i][0][v];
        for (int p = v - 1; p >= 0; p--) {
            best[0][p] = dp[i][0][p] == INVALID ? INVALID : v - p + dp[i][0][p];
            best[0][p] = Math.min(best[0][p], best[0][p + 1]);
        }
        //从最小值往最大值遍历
        best[1][0] = dp[i][1][0] == INVALID ? INVALID : v + dp[i][1][0];
        for (int p = 1; p <= v; p++) {
            best[1][p] = dp[i][1][p] == INVALID ? INVALID : v - p + dp[i][1][p];
            best[1][p] = Math.min(best[1][p], best[1][p - 1]);
        }
        return best;
    }

    //贪心的最优解，时间复杂度为O(n)
    public static int year(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int n = arr.length;
        int[] nums = new int[n + 2];//扩展数组，在首尾各添加一个极大值
        nums[0] = Integer.MAX_VALUE;
        nums[n + 1] = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            nums[i + 1] = arr[i];//将原始的数组移动到中间的位置
        }
        int[] leftCost = new int[n + 2];//leftCost[i]:使1~i位置严格递减的最小代价
        int pre = nums[0];//前一个元素的最终值，初始化为左侧极大值
        int change = 0;
        for (int i = 1; i <= n; i++) {
            // 当前元素的最大可能值 = min(前一个元素值-1, 原始值)
            // 确保严格递减（当前元素 < 前一个元素），且不超过原始值（只能减小）
            change = Math.min(pre - 1, nums[i]);
            leftCost[i] = nums[i] - change + leftCost[i - 1];
            pre = change;
        }
        int[] rightCost = new int[n + 2];//rightCost[i]:表示i~n位置严格递减的最小代价
        pre = nums[n + 1];
        for (int i = n; i >= 1; i--) {
            // 当前元素的最大可能值 = min(后一个元素值-1, 原始值)
            // 确保严格递减（当前元素 < 后一个元素），即从右往左看是严格递增
            change = Math.min(pre - 1, nums[i]);
            rightCost[i] = nums[i] - change + rightCost[i + 1];
            pre = change;
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++) {
            ans = Math.min(ans, leftCost[i] + rightCost[i + 1]);
        }
        return ans;
    }

    public static int[] randomArray(int len, int v) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * v) + 1;
        }
        return arr;
    }

    public static int[] copyArray(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int len = 7;
        int v = 10;
        int testTime = 100;
        System.out.println("=============");
        System.out.println("功能测试开始！");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * len) + 1;
            int[] arr = randomArray(n, v);
            int[] arr0 = copyArray(arr);
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            int[] arr3 = copyArray(arr);
            int[] arr4 = copyArray(arr);
            int ans0 = minCost0(arr0);
            int ans1 = minCost1(arr1);
            int ans2 = minCost2(arr2);
            int ans3 = minCost3(arr3);
            int ans4 = year(arr4);
            if (ans0 != ans1 || ans0 != ans2 || ans0 != ans3 || ans0 != ans4) {
                System.out.println("Oops");
                break;
            }
        }
        System.out.println("功能测试结束！");
        System.out.println("==========");

        System.out.println("性能测试开始！");
        len = 10000;
        v = 500;
        System.out.println("生成随机数组长度：" + len);
        System.out.println("申城随机数组值的范围：[1," + v + "]");
        int[] arr = randomArray(len, v);
        int[] arr3 = copyArray(arr);
        int[] arrYear = copyArray(arr);
        long start;
        long end;
        start = System.currentTimeMillis();
        int ans3 = minCost3(arr3);
        end = System.currentTimeMillis();
        System.out.println("minCost3方法：");
        System.out.println("运送结果：" + ans3 + ",时间（毫秒）：" + (end - start));

        start = System.currentTimeMillis();
        int ansYear = year(arrYear);
        end = System.currentTimeMillis();
        System.out.println("year方法：");
        System.out.println("运送结果：" + ans3 + ",时间（毫秒）：" + (end - start));

        System.out.println("性能测试结束！");
        System.out.println("=============");
    }
}
