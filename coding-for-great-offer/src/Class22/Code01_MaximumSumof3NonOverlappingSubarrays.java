package Class22;

//给定数组nums由正整数组成
//找到三个互不重叠的子数组的最大和
//每个子数组的长度为k，我们要使这3*k个项的和最大化
//返回每个区间起始索引的列表（索引从 0 开始）。如果有多个结果，返回字典序最小的一个。
//Leetcode题目：https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/
public class Code01_MaximumSumof3NonOverlappingSubarrays {
    //计算数组中所有可能子数组的最大累加和
    public static int[] maxSumArray1(int[] arr) {
        int N = arr.length;
        int[] help = new int[N];//help[i]:子数组必须以i位置结尾的情况下，累加和最大是多少？
        help[0] = arr[0]; // 初始化：以第一个元素结尾的子数组最大和就是该元素本身
        for (int i = 1; i < N; i++) {
            //这个循环来填充子数组
            int p1 = arr[i];//只包含当前元素
            int p2 = arr[i] + help[i - 1];//包含当前元素和以前一个元素结尾的最大子数组
            help[i] = Math.max(p1, p2);//两种选择中取较大值
        }
        // dp数组用于存储从开始到每个位置i的所有子数组中的最大累加和
        int[] dp = new int[N];
        dp[0] = help[0]; // 初始化：第一个位置的最大子数组和就是help[0]
        for (int i = 1; i < N; i++) {
            int p1 = help[i];//当前位置结尾的最大子数组和
            int p2 = dp[i - 1];//前一个位置的最大子数组和
            dp[i] = Math.max(p1, p2);//取两种选择中的较大值
        }
        return dp;
    }

    //计算长度为k的子数组的最大累加和
    public static int maxSumLenK(int[] arr, int k) {
        int N = arr.length;
        //子数组必须以i位置的数结尾，长度一定是K，累加和最大时多少？
        //计算前k-1个元素的和
        int sum = 0;
        for (int i = 0; i < k - 1; i++) {
            sum += arr[i];
        }
        int[] help = new int[N];
        //滑动窗口计算每个长度为k的子数组的和
        for (int i = k - 1; i < N; i++) {
            sum += arr[i];// 加入当前元素
            help[i] = sum;// 当前窗口的和存入help数组
            sum -= arr[i - k + 1]; // 移除窗口最左边的元素，为下一次迭代做准备
        }
        //遍历数组找到最大值返回
        int max = Integer.MIN_VALUE;
        for (int value : help) {
            max = Math.max(max, value);
        }
        return max;
    }
    //本代码分了三个步骤：
    //计算所有可能的长度为k的子数组的和
    //预处理每个位置左侧和右侧的最大子数组起始位置
    //遍历所有可能的中间子数组位置，组合左右两侧的最大子数组，找到总和最大的组合
    public static int[] maxSumOfThreeSubArrays(int[] nums, int k) {
        int N = nums.length;
        int[] range = new int[N];//存储每个长度为k的子数组的和
        int[] left = new int[N];//存储每个位置左侧最大子数组的起始位置
        int sum = 0;
        //计算第一个子数组的和并初始化为range数组
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        range[0] = sum; // 第一个长度为k的子数组和
        left[k - 1] = 0;// 位置k-1左侧最大子数组的起始位置是0
        int max = sum;// 初始化当前最大值
        //滑动窗口计算所有子数组的和，并填充left数组
        for (int i = k; i < N; i++) {
            // 滑动窗口：减去窗口最左边元素，加上新元素
            sum = sum - nums[i - k] + nums[i];
            range[i - k + 1] = sum;//存储当前窗口的和
            left[i] = left[i - 1];// 继承前一个位置的最大子数组起始位置
            //如果当前窗口的和更大，则更新最大值和起始位置
            if (sum > max) {
                max = sum;
                left[i] = i - k + 1;
            }
        }
        //计算最右侧子数组的和并初始化为right数组
        sum = 0;
        // 计算最后一个长度为k的子数组的和
        for (int i = N - 1; i >= N-k; i--) {
            sum += nums[i];
        }
        max = sum;
        int[] right = new int[N];
        right[N - k] = N - k;//最右侧子数组的起始位置
        //从右往左滑动窗口，填充right数组
        for (int i = N - k - 1; i >= 0; i--) {
            sum = sum - nums[i + k] + nums[i];
            right[i] = right[i + 1]; // 继承后一个位置的最大子数组起始位置
            if (sum >= max) {//保证了字典序最小的条件
                max = sum;
                right[i] = i;
            }
        }
        //遍历所有可能的中间子数组位置，找到三个子数组的最大和
        int a = 0;
        int b = 0;
        int c = 0;//三个子数组的起始位置
        max = 0;//三个子数组的最大和
        //i是中间子数组的起始位置，需要保证左右两侧有足够空间
        for (int i = k; i < N - 2 * k + 1; i++) {
            int part1 = range[left[i - 1]];
            int part2 = range[i];
            int part3 = range[right[i + k]];
            if (part1 + part2 + part3 > max) {
                max = part1 + part2 + part3;
                a = left[i - 1];
                b = i;
                c = right[i + k];
            }
        }
        return new int[]{a, b, c};
    }
}
