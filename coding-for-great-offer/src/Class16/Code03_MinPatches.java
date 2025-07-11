package Class16;

import java.util.Arrays;

//给定一个已排序的正整数数组 nums，和一个正整数n 。
//从[1, n]区间内选取任意个数字补充到nums中，
//使得[1, n]区间内的任何数字都可以用nums中某几个数字的和来表示，
// 请输出满足上述要求的最少需要补充的数字个数

//本代码使用了贪心
public class Code03_MinPatches {
    //arr请保持有序，且正数
    public static int minPatches(int[] arr, int aim) {
        int patches = 0; // 需要添加的元素数量
        long range = 0; //已经完成了1~range的目标，使用这个变量来记录已经覆盖的范围
        Arrays.sort(arr);//将数组按照升序排列
        for (int i = 0; i != arr.length; i++) {
            //要求：1~arr[i]-1范围被搞定
            while (arr[i] - 1 > range) {
                //需要添加range+1来扩展范围
                range += range + 1;
                patches++;
                if (range >= aim) {
                    return patches; //已经将目标全部覆盖，返回结果
                }
            }
            //当前元素已经被纳入了范围，将他直接扩展
            range += arr[i];
            if (range >= aim) {
                return patches;
            }
        }
        //处理遍历一遍数组，仍然没有把数全部覆盖的情况
        while (aim >= range + 1) {
            range += range + 1;
            patches++;
        }
        return patches;
    }
    //本代码相比方法一，增加了整数溢出检查以处理极端情况下的边界情况
    public static int minPatches2(int[] arr, int K) {
        int patches = 0;
        int range = 0;
        for (int i = 0; i != arr.length; i++) {
            while (arr[i] > range + 1) {
                // 检查溢出：若range+1会溢出，直接返回（添加一个极大值）
                if (range > Integer.MAX_VALUE - range - 1) {
                    return patches + 1;
                }
                range += range + 1;
                patches++;
                if (range >= K) {
                    return patches;
                }
            }
            //累加当前元素前，检查溢出
            if (range > Integer.MAX_VALUE - arr[i]) {
                return patches;//无法继续添加，直接返回
            }
            range += arr[i];
            if (range >= K) {
                return patches;
            }
        }
        while (K >= range + 1) {
            if (K == range && K == Integer.MAX_VALUE) {
                return patches;
            }
            if (range > Integer.MAX_VALUE - range - 1) {
                return patches + 1;
            }
            range += range + 1;
            patches++;
        }
        return patches;
    }


    public static void main(String[] args) {
        int[] test = {1, 2, 31, 33};
        int n = 2147483647;
        System.out.println(minPatches(test, n));
        System.out.println(minPatches2(test, n));
    }
}
