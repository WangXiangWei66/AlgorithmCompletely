package class_2022_02_2;

import java.util.Arrays;

//Alice 有一个下标从 0 开始的数组 arr ，由 n 个正整数组成。
//她会选择一个任意的 正整数 k 并按下述方式创建两个下标从 0 开始的新整数数组 lower 和 higher ：
//对每个满足 0 <= i < n 的下标 i ，lower[i] = arr[i] - k
//对每个满足 0 <= i < n 的下标 i ，higher[i] = arr[i] + k
//不幸地是，Alice 丢失了全部三个数组。但是，她记住了在数组 lower 和 higher 中出现的整数
//但不知道每个整数属于哪个数组。请你帮助 Alice 还原原数组。
//给你一个由 2n 个整数组成的整数数组 nums ，其中 恰好 n 个整数出现在 lower ，剩下的出现在 higher ，
//还原并返回 原数组 arr 。如果出现答案不唯一的情况，返回 任一 有效数组。
//注意：生成的测试用例保证存在 至少一个 有效数组 arr 。
//leetcode链接 : https://leetcode.com/problems/recover-the-original-array/
public class Code05_RecoverTheOriginalArray {

    public static int[] recoverArray(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int m = n >> 1;
        //遍历可能的第一个配对元素
        for (int first = 1; first <= m; first++) {
            int d = nums[first] - nums[0];//first来自higher元素，0来自lower元素，最终差值为2k
            if (d > 0 && (d & 1) == 0) {
                int[] ans = new int[m];
                int i = 0;//ans的数组索引
                boolean[] set = new boolean[n];//标记已经使用过的数组
                int k = d >> 1;
                int l = 0;
                int r = first;
                while (r < n) {
                    while (set[l]) {
                        l++;
                    }
                    if (l == r) {
                        r++;
                    } else if (nums[r] - nums[l] > d) {
                        break;
                    } else if (nums[r] - nums[l] < d) {
                        r++;
                    } else {
                        set[r++] = true;
                        ans[i++] = nums[l++] + k;
                    }
                }
                if (i == m) {
                    return ans;
                }
            }
        }
        return null;
    }
}
