package Class48;

import java.util.Arrays;

// 来自学员问题
// 比如{ 5, 3, 1, 4 }
// 全部数字对是：(5,3)、(5,1)、(5,4)、(3,1)、(3,4)、(1,4)
// 数字对的差值绝对值： 2、4、1、2、1、3
// 差值绝对值排序后：1、1、2、2、3、4
// 给定一个数组arr，和一个正数k
// 返回arr中所有数字对差值的绝对值，第k小的是多少
// arr = { 5, 3, 1, 4 }, k = 4
// 返回2
public class Code01_MinKthPairMinusABS {

    public static int kthABS1(int[] arr, int k) {
        int n = arr.length;
        int m = ((n - 1) * n) >> 1;
        if (m == 0 || k < 1 || k > m) {
            return -1;
        }
        int[] abs = new int[m];
        int size = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                abs[size++] = Math.abs(arr[i] - arr[j]);
            }
        }
        Arrays.sort(abs);
        return abs[k - 1];
    }

    public static int kthABS2(int[] arr, int k) {
        int n = arr.length;
        if (n < 2 || k < 1 || k > ((n * (n - 1)) >> 1)) {
            return -1;
        }
        Arrays.sort(arr);
        int left = 0;//最小可能的差值
        int right = arr[n - 1] - arr[0];//最大可能的差值
        int mid = 0;//试探当前的差值
        int rightTest = -1;//符合条件的最大中间值
        while (left <= right) {
            mid = (left + right) / 2;
            if (valid(arr, mid, k)) {
                rightTest = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return rightTest + 1;
    }

    //判断差值小于等于limit的对数是否小于k
    // 假设arr中的所有数字对，差值绝对值<=limit的个数为x
    // 如果 x < k，达标，返回true
    // 如果 x >= k，不达标，返回false
    public static boolean valid(int[] arr, int limit, int k) {
        int x = 0;
        for (int l = 0, r = 1; l < arr.length; r = Math.max(r, ++l)) {
            while (r < arr.length && arr[r] - arr[l] <= limit) {
                r++;
            }
            x += r - l - 1;
        }
        return x < k;
    }

    public static int[] randomArray(int n, int v) {
        int[] ans = new int[n];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (Math.random() * v);
        }
        return ans;
    }

    public static void main(String[] args) {
        int size = 100;
        int value = 100;
        int testTime = 10000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * size);
            int k = (int) (Math.random() * (n * (n - 1) / 2)) + 1;
            int[] arr = randomArray(n, value);
            int ans1 = kthABS1(arr, k);
            int ans2 = kthABS2(arr, k);
            if (ans1 != ans2) {
                System.out.print("arr:");
                for (int num : arr) {
                    System.out.println(num + " ");
                }
                System.out.println();
                System.out.println("k : " + k);
                System.out.println("ans1 : " + ans1);
                System.out.println("ans2 : " + ans2);
                System.out.println("Oops");
                break;
            }
        }
        System.out.println("test end!");
    }
}
