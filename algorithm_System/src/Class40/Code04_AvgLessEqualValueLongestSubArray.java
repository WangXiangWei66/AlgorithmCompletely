package Class40;

import java.util.TreeMap;

public class Code04_AvgLessEqualValueLongestSubArray {

    public static int ways1(int[] arr, int V) {
        int ans = 0;
        for (int L = 0; L < arr.length; L++) {
            for (int R = L; R < arr.length; R++) {
                int sum = 0;
                int K = R - L + 1;
                for (int i = L; i <= R; i++) {
                    sum += arr[i];
                }
                double avg = (double) sum / (double) K;
                if (avg <= V) {
                    ans = Math.max(ans, K);
                }
            }
        }
        return ans;
    }

    public static int ways2(int[] arr, int V) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= V;
        }
        TreeMap<Integer, Integer> sortedMap = new TreeMap<>();
        sortedMap.put(0, -1);
        int sum = 0;
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            Integer ceiling = sortedMap.ceilingKey(sum);
            if (ceiling != null) {
                len = Math.max(len, i - sortedMap.get(ceiling));
            } else {
                sortedMap.put(sum, i);
            }
        }
        return len;
    }

    public static int ways3(int[] arr, int V) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= V;
        }
        return maxLengthAweSome(arr, 0);
    }

    public static int maxLengthAweSome(int[] arr, int K) {
        int N = arr.length;
        int[] sums = new int[N];
        int[] ends = new int[N];
        sums[N - 1] = arr[N - 1];
        ends[N - 1] = N - 1;
        for (int i = N - 2; i >= 0; i--) {
            if (sums[i + 1] < 0) {
                sums[i] = arr[i] + sums[i + 1];
                ends[i] = ends[i + 1];
            } else {
                sums[i] = arr[i];
                ends[i] = i;
            }
        }
        int end = 0;
        int sum = 0;
        int res = 0;
        for (int i = 0; i < N; i++) {
            while (end < N && sum + sums[end] <= K) {
                sum += sums[end];
                end = ends[end] + 1;
            }
            res = Math.max(res, end - i);
            if (end > i) {
                sum -= arr[i];
            } else {
                end = i + 1;
            }
        }
        return res;
    }

    public static int[] generateRandomArray(int maxLen, int maxValue) {
        int[] arr = new int[(int) (maxLen * Math.random())+1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (maxValue * Math.random());
        }
        return arr;
    }

    public static int[] copyArray(int[] arr) {
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
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
        int testTime = 100000;
        int maxLen = 100;
        int maxValue = 100;
        System.out.println("测试开始!");
        for (int i = 0; i < testTime; i++) {
            int[] arr = generateRandomArray(maxLen, maxValue);
            int value = (int) (Math.random() * maxValue);
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            int[] arr3 = copyArray(arr);
            int ans1 = ways1(arr1, value);
            int ans2 = ways2(arr2, value);
            int ans3 = ways3(arr3, value);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("出错了!");
                printArray(arr);
                System.out.println(value);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println(ans3);
                break;
            }
        }
        System.out.println("测试结束!");
    }
}
