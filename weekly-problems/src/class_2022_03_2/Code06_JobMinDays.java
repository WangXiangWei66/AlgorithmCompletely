package class_2022_03_2;

//来自微软
//给定一个正数数组arr，长度为N，依次代表N个任务的难度，给定一个正数k
//你只能从0任务开始，依次处理到N-1号任务结束，就是一定要从左往右处理任务
//只不过，难度差距绝对值不超过k的任务，可以在一天之内都完成
//返回完成所有任务的最少天数
public class Code06_JobMinDays {

    public static int minDays1(int[] arr, int k) {
        int n = arr.length;
        //dp[i]:完成前i+1个任务所需的最少天数
        int[] dp = new int[n];
        dp[0] = 1;
        for (int i = 1; i < n; i++) {
            dp[i] = dp[i - 1] + 1;//当前任务需要单独作为新的一天
            int min = arr[i];
            int max = arr[i];
            for (int j = i - 1; j >= 0; j--) {
                min = Math.min(min, arr[j]);
                max = Math.max(max, arr[j]);
                if (max - min <= k) {
                    dp[i] = Math.min(dp[i], 1 + (j - 1 >= 0 ? dp[j - 1] : 0));
                } else {
                    break;
                }
            }
        }
        return dp[n - 1];
    }
    //动态规划+滑动窗口+单调队列
    public static int minDays2(int[] arr, int k) {
        int n = arr.length;
        int[] dp = new int[n];
        int[] windowMax = new int[n];//维护滑动窗口的最大值
        int[] windowMin = new int[n];//维护滑动窗口的最小值
        //单调队列的边界指针
        int maxL = 0;
        int maxR = 0;
        int minL = 0;
        int minR = 0;
        int L = 0;//滑动窗口的左边界
        for (int i = 0; i < n; i++) {
            while (maxL < maxR && arr[windowMax[maxR - 1]] <= arr[i]) {
                maxR--;
            }
            windowMax[maxR++] = i;
            while (minL < minR && arr[windowMin[minR - 1]] >= arr[i]) {
                minR--;
            }
            windowMin[minR++] = i;
            //调整窗口左边界
            while (arr[windowMax[maxL]] - arr[windowMin[minL]] > k) {
                if (windowMax[maxL] == L) {
                    maxL++;
                }
                if (windowMin[minL] == L) {
                    minL++;
                }
                L++;//左边界右移，缩小窗口
            }
            dp[i] = 1 + (L - 1 >= 0 ? dp[L - 1] : 0);
        }
        return dp[n - 1];
    }

    public static int[] randomArray(int n, int v) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * v);
        }
        return arr;
    }

    // 为了测试
    public static void main(String[] args) {
        int len = 50;
        int value = 20;
        int testTime = 20000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * len) + 1;
            int[] arr = randomArray(n, value);
            int k = (int) (Math.random() * value);
            int ans1 = minDays1(arr, k);
            int ans2 = minDays2(arr, k);
            if (ans1 != ans2) {
                System.out.println("出错了!");
                for (int num : arr) {
                    System.out.print(num + " ");
                }
                System.out.println();
                System.out.println("k = " + k);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("测试结束");
    }
}
