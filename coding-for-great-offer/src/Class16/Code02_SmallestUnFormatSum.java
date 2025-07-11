package Class16;

import java.util.Arrays;
import java.util.HashSet;

//给定一个正数数组arr，
//返回arr的子集不能累加出的最小正数
//1）正常怎么做？
//2）如果arr中肯定有1这个值，怎么做？
public class Code02_SmallestUnFormatSum {

    public static int UnformedSum1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 1;//空数组则默认返回1
        }
        HashSet<Integer> set = new HashSet<Integer>();//存储计算出来的每一个子集的和
        process(arr, 0, 0, set);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i != arr.length; i++) {
            min = Math.min(min, arr[i]);//找到数组的最小元素，作为起始检查点
        }
        //从最小元素加1开始递增检查，找到第一个不在HashSet中的正整数
        for (int i = min + 1; i != Integer.MIN_VALUE; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }
        return 0;
    }

    //递归生成所有的可能的和
    public static void process(int[] arr, int i, int sum, HashSet<Integer> set) {
        if (i == arr.length) {
            set.add(sum);//递归终止条件，处理完所有元素，记录当前和
            return;
        }
        //不选择当前的元素
        process(arr, i + 1, sum, set);
        //选择当前的元素
        process(arr, i + 1, sum + arr[i], set);
    }

    public static int unformedSum2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 1;
        }
        int sum = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            min = Math.min(min, arr[i]);
        }
        int N = arr.length;
        //dp[i][j]：表示前i个元素，能否组成和为j
        boolean[][] dp = new boolean[N][sum + 1];
        //所有元素都能组成0
        for (int i = 0; i < N; i++) {
            dp[i][0] = true;
        }
        //第一个元素，只能组成自己的值
        dp[0][arr[0]] = true;
        for (int i = 1; i < N; i++) {
            for (int j = 1; j <= sum; j++) {
                //两种情况：
                //1）不选择当前元素，继承
                //2）选择当前元素，检查前i-1个元素是否组成j-arr[i]
                dp[i][j] = dp[i - 1][j] || ((j - arr[i] >= 0) ? dp[i - 1][j - arr[i]] : false);
            }
        }
        for (int j = min; j <= sum; j++) {
            if (!dp[N - 1][j]) {
                return j;
            }
        }
        return sum + 1;
    }

    //已知，arr中肯定有1这个数
    //本算法的时间复杂度为O(N*logN) —— 来自排序操作
    public static int unformedSum3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        Arrays.sort(arr);
        int range = 1;//初始范围为1，表示从1开始检查
        for (int i = 1; i != arr.length; i++) {
            if (arr[i] > range + 1) {
                return range + 1;
            } else {
                range += arr[i];
            }
        }
        return range + 1;//表示所有的元素都能被纳入，直接返回下一个
    }

    public static int[] generateArray(int len, int maxValue) {
        int[] res = new int[len];
        for (int i = 0; i != res.length; i++) {
            res[i] = (int) (Math.random() * maxValue) + 1;
        }
        return res;
    }

    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int len = 27;
        int max = 30;
        int[] arr = generateArray(len, max);
        printArray(arr);
        long start = System.currentTimeMillis();
        System.out.println(UnformedSum1(arr));
        long end = System.currentTimeMillis();
        System.out.println("cost time:" + (end - start) + "ms");
        System.out.println("-------------");

        start = System.currentTimeMillis();
        System.out.println(unformedSum2(arr));
        end = System.currentTimeMillis();
        System.out.println("cost time:" + (end - start) + "ms");
        System.out.println("-------------");
        System.out.println("set arr[0] to 1");
        arr[0] = 1;
        start = System.currentTimeMillis();
        System.out.println(unformedSum3(arr));
        end = System.currentTimeMillis();
        System.out.println("cost time:" + (end - start) + "ms");
        System.out.println("-------------");
    }
}
