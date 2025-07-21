package Class22;

//给定 n 个非负整数表示每个宽度为 1 的柱子的高度图
//计算按此排列的柱子，下雨之后能接多少雨水
//Leetcode题目：https://leetcode.com/problems/trapping-rain-water/
public class Code02_TrappingRainWater {
    //本体使用了双指针法高效的计算数组中能够接住的雨水量
    //双指针法：通过维护左右两个指针和对应的最大高度，逐步向中间移动并计算每个位置的雨水量，而无需预先计算整个数组的左右两个最大高度数组
    public static int trap(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int N = arr.length;
        int L = 1; // 左指针，从第二个元素开始
        int leftMax = arr[0];// 左指针左侧的最大高度，初始为第一个元素
        int R = N - 2;// 右指针，从倒数第二个元素开始
        int rightMax = arr[N - 1];// 右指针右侧的最大高度，初始为最后一个元素
        int water = 0; // 累计雨水量
        while (L <= R) {
            if (leftMax <= rightMax) {
                water += Math.max(0, leftMax - arr[L]);// 左指针一侧的最大高度较小，处理左指针位置
                leftMax = Math.max(leftMax, arr[L++]);// 更新左指针左侧的最大高度，并向右移动左指针
            } else {
                water += Math.max(0, rightMax - arr[R]);
                rightMax = Math.max(rightMax, arr[R--]);
            }
        }
        return water;
    }
}
