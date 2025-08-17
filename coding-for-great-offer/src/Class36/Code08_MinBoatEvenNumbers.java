package Class36;
//双指针策略是解决 "配对最大化利用资源" 类问题的经典方法
//通过贪心思想（让最重的人优先匹配最轻的人）实现最优解。
import java.util.Arrays;

//来自腾讯
//给定一个正数数组arr，代表每个人的体重。给定一个正数limit代表船的载重，所有船都是同样的载重量
//每个人的体重都一定不大于船的载重
//要求：
//1, 可以1个人单独一搜船
//2, 一艘船如果坐2人，两个人的体重相加需要是偶数，且总体重不能超过船的载重
//3, 一艘船最多坐2人
//返回如果想所有人同时坐船，船的最小数量
public class Code08_MinBoatEvenNumbers {
    //时间复杂度为O(N * logN)
    public static int minBoat(int[] arr, int limit) {
        Arrays.sort(arr);
        int odd = 0;
        int even = 0;
        for (int num : arr) {
            if ((num & 1) == 0) {
                even++;
            } else {
                odd++;
            }
        }
        int[] odds = new int[odd];
        int[] evens = new int[even];
        for (int i = arr.length - 1; i >= 0; i--) {
            //先减后存，实现从0开始填充
            if ((arr[i] & 1) == 0) {
                evens[--even] = arr[i];
            } else {
                odds[--odd] = arr[i];
            }
        }
        //分别计算奇数数组和偶数数组所需的最小船只数，然后求和
        return min(odds, limit) + min(evens, limit);
    }

    public static int min(int[] arr, int limit) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int N = arr.length;
        //预防有超重的人
        if (arr[N - 1] > limit) {
            return -1;
        }
        //寻找最大的小于等于limit/2的元素
        int lessR = -1;
        for (int i = N - 1; i >= 0; i--) {
            if (arr[i] <= (limit / 2)) {
                lessR = i;
                break;
            }
        }
        //每个人都要单独坐一张船
        if (lessR == -1) {
            return N;
        }
        //使用了双指针法
        int L = lessR;
        int R = lessR + 1;
        int noUsed = 0;//无法配对的小体重者的数量
        while (L >= 0) {
            int solved = 0;//当前L能匹配的最大体重者
            while (R < N && arr[L] + arr[R] <= limit) {
                R++;
                solved++;
            }
            if (solved == 0) {
                noUsed++;
                L--;
            } else {
                L = Math.max(-1, L - solved);
            }
        }
        int all = lessR + 1;//小体重者的总数量
        int used = all - noUsed;
        int moreUnsolved = (N - all) - used;
        return used + ((noUsed + 1) >> 1) + moreUnsolved;
    }

    public static int numRescueBoats2(int[] people, int limit) {
        Arrays.sort(people);
        int ans = 0;
        int l = 0;//左指针
        int r = people.length - 1;//右指针
        int sum = 0;//两人的体重和
        while (l <= r) {
            sum = l == r ? people[l] : people[l] + people[r];
            if (sum > limit) {
                r--;
            } else {//两人可以同乘一艘船
                l++;
                r--;
            }
            ans++;
        }
        return ans;
    }
}
