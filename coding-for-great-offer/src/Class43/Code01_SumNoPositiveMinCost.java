package Class43;

import java.util.Arrays;

//来自微软面试
//给定一个正数数组arr长度为n、正数x、正数y
//你的目标是让arr整体的累加和<=0
//你可以对数组中的数num执行以下三种操作中的一种，且每个数最多能执行一次操作 :
//1）不变
//2）可以选择让num变成0，承担x的代价
//3）可以选择让num变成-num，承担y的代价
//返回你达到目标的最小代价
//数据规模 : 面试时面试官没有说数据规模
public class Code01_SumNoPositiveMinCost {

    public static int minOpStep1(int[] arr, int x, int y) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return process1(arr, x, y, 0, sum);
    }

    public static int process1(int[] arr, int x, int y, int i, int sum) {
        if (sum <= 0) {
            return 0;
        }
        if (i == arr.length) {
            return Integer.MAX_VALUE;
        }
        //对当前元素不做任何选择
        int p1 = process1(arr, x, y, i + 1, sum);
        //将答盎前元素变为0，代价为X
        int p2 = Integer.MAX_VALUE;
        int next2 = process1(arr, x, y, i + 1, sum - arr[i]);
        if (next2 != Integer.MAX_VALUE) {
            p2 = x + next2;
        }
        //将当前元素变为-num，代价为y
        int p3 = Integer.MAX_VALUE;
        int next3 = process1(arr, x, y, i + 1, sum - (arr[i] << 1));//累加和会减少2*arr[i]
        if (next3 != Integer.MAX_VALUE) {
            p3 = y + next3;
        }
        return Math.min(p1, Math.min(p2, p3));
    }


    public static int minOpStep2(int[] arr, int x, int y) {
        Arrays.sort(arr);
        int n = arr.length;
        //反转数组
        for (int l = 0, r = n - 1; l <= r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        //优先取反
        if (x >= y) {
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }
            int cost = 0;
            for (int i = 0; i < n && sum > 0; i++) {
                sum -= arr[i] << 1;
                cost += y;
            }
            return cost;
        } else {
            //计算后缀和，arr[i]表示从i到n-1元素的总和
            for (int i = n - 2; i >= 0; i--) {
                arr[i] += arr[i + 1];
            }
            int benefit = 0;
            //找到最小的left，使从left到n-1的元素和<=benefit
            int left = mostLeft(arr, 0, benefit);
            int cost = left * x;//初始代价,对0~left-1执行清零操作
            for (int i = 0; i < n - 1; i++) {
                benefit += arr[i] - arr[i + 1];
                left = mostLeft(arr, i + 1, benefit);
                cost = Math.min(cost, (i + 1) * y + (left - i - 1) * x);
            }
            return cost;
        }
    }

    public static int mostLeft(int[] arr, int l, int v) {
        int r = arr.length - 1;
        int m = 0;
        int ans = arr.length;//默认所有元素都需要处理
        while (l <= r) {
            m = (l + r) / 2;
            if (arr[m] <= v) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }

    //使用了滑动窗口来替换二分
    public static int minOpStep3(int[] arr, int x, int y) {
        Arrays.sort(arr);
        int n = arr.length;
        for (int l = 0, r = n - 1; l <= r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        if (x >= y) {
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }
            int cost = 0;
            for (int i = 0; i < n && sum > 0; i++) {
                sum -= arr[i] << 1;
                cost += y;
            }
            return cost;
        } else {
            int benefit = 0;//记录取反操作带来的收益
            int cost = arr.length * x;
            int holdSum = 0;//当前不需要处理的元素
            //yRight取反操作的右边界
            //holdLeft：需要保留元素的左边界
            for (int yRight = 0, holdLeft = n; yRight < holdLeft - 1; yRight++) {
                benefit += arr[yRight];
                //移动holdLeft指针，找到最大范围的可保留元素（这些元素的总和<=取反带来的收益）
                while (holdLeft - 1 > yRight && holdSum + arr[holdLeft - 1] <= benefit) {
                    holdSum += arr[holdLeft - 1];
                    holdLeft--;
                }
                cost = Math.min(cost, (yRight + 1) * y + (holdLeft - yRight - 1) * x);
            }
            return cost;
        }
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
        int n = 12;
        int v = 20;
        int c = 10;
        int testTime = 10000;
        System.out.println("test begin!");
        for (int i = 0; i < testTime; i++) {
            int len = (int) (Math.random() * n);
            int[] arr = randomArray(len, v);
            int[] arr1 = copyArray(arr);
            int[] arr2 = copyArray(arr);
            int[] arr3 = copyArray(arr);
            int x = (int) (Math.random() * c);
            int y = (int) (Math.random() * c);
            int ans1 = minOpStep1(arr1, x, y);
            int ans2 = minOpStep2(arr2, x, y);
            int ans3 = minOpStep3(arr3, x, y);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Oops");
            }
        }
        System.out.println("test end!");
    }
}
