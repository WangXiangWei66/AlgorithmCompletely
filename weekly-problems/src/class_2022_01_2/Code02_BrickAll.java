package class_2022_01_2;

import java.util.Arrays;

//给定一个正数数组arr，其中每个值代表砖块长度
//所有砖块等高等宽，只有长度有区别
//每一层可以用1块或者2块砖来摆
//要求每一层的长度一样
//要求必须使用所有的砖块
//请问最多摆几层
public class Code02_BrickAll {

    public static int maxLevels(int[] arr) {
        if (arr == null) {
            return 0;
        }
        int n = arr.length;
        if (n < 2) {
            return n;
        }
        Arrays.sort(arr);
        int p1 = levels(arr, arr[n - 1]);//最大的砖单独为一层
        int p2 = levels(arr, arr[n - 1] + arr[0]);//最大和最小的砖为一层
        return Math.max(p1, p2);
    }
    //以指定长度len摆层，最多有几层
    public static int levels(int[] arr, int len) {
        int ans = 0;
        int L = 0;
        int R = arr.length - 1;
        while (L <= R) {
            if (arr[R] == len) {
                R--;
                ans++;
            } else if (L < R && arr[L] + arr[R] == len) {
                L++;
                R--;
                ans++;
            } else {
                return -1;
            }
        }
        return ans;
    }
}
