package Class34;

//给你一个整数数组 nums ，设计算法来打乱一个没有重复元素的数组。
//实现 Solution class:
//Solution(int[] nums) 使用整数数组 nums 初始化对象
//int[] reset() 重设数组到它的初始状态并返回
//int[] shuffle() 返回数组随机打乱后的结果
//Leetcode题目 : https://leetcode.com/problems/shuffle-an-array/
public class Problem_0384_ShuffleAnArray {

    class Solution {
        private int[] origin;//原始数组
        private int[] shuffle;//存储打乱之后的数组
        private int N;

        public Solution(int[] nums) {
            origin = nums;
            N = nums.length;
            shuffle = new int[N];
            for (int i = 0; i < N; i++) {
                shuffle[i] = origin[i];
            }
        }
        //返回数组的起始状态
        public int[] reset() {
            return origin;
        }
        //返回随机打乱的数组
        public int[] shuffle() {
            //费雪 - 耶茨洗牌算法（Fisher-Yates Shuffle），从数组末尾开始向前遍历
            for (int i = N - 1; i >= 0; i--) {
                int r = (int) (Math.random() * (i + 1));
                int tmp = shuffle[r];
                shuffle[r] = shuffle[i];
                shuffle[i] = tmp;
            }
            return shuffle;
        }
    }
}
