package Class32;

//给定一个数组，将数组中的元素向右移动k个位置，其中k是非负数。
//进阶：
//尽可能想出更多的解决方案，至少有三种不同的方法可以解决这个问题。
//你可以使用空间复杂度为O(1) 的原地算法解决这个问题吗？
//示例 1:
//输入: nums = [1,2,3,4,5,6,7], k = 3
//输出: [5,6,7,1,2,3,4]
//解释:
//向右旋转 1 步: [7,1,2,3,4,5,6]
//向右旋转 2 步: [6,7,1,2,3,4,5]
//向右旋转 3 步: [5,6,7,1,2,3,4]
//示例2:
//输入：nums = [-1,-100,3,99], k = 2
//输出：[3,99,-1,-100]
//解释:
//向右旋转 1 步: [99,-1,-100,3]
//向右旋转 2 步: [3,99,-1,-100]
//Leetcode题目 : https://leetcode.com/problems/rotate-array/
public class Problem_0189_RotateArray {
    //时间复杂度为O(n),额外空间复杂度为O(1)
    public void rotate1(int[] nums, int k) {
        int N = nums.length;
        k = k % N;//如果k大于了数组的长度，旋转k次就等价于旋转了k%n次
        reverse(nums, 0, N - k - 1);
        reverse(nums, N - k, N - 1);
        reverse(nums, 0, N - 1);
    }

    public static void reverse(int[] nums, int L, int R) {
        while (L < R) {
            int tmp = nums[L];
            nums[L++] = nums[R];
            nums[R--] = tmp;
        }
    }

    public static void rotate2(int[] nums, int k) {
        int N = nums.length;
        k = k % N;
        if (k == 0) {
            return;
        }
        int L = 0;//左分区的起始索引
        int R = N - 1;//右分区的结束索引
        int lPart = N - k;//左分区的长度
        int rPart = k;//右分区的长度
        int same = Math.min(lPart, rPart);
        int diff = lPart - rPart;
        //首次交换相同长度的分区
        exchange(nums, L, R, same);
        //退出循环：两分区的长度差为0
        while (diff != 0) {
            if (diff > 0) {
                L += same;
                lPart = diff;
            } else {
                R -= same;
                rPart = -diff;
            }
            same = Math.min(lPart, rPart);
            diff = lPart - rPart;
            exchange(nums, L, R, same);
        }
    }

    public static void exchange(int[] nums, int start, int end, int size) {
        int i = end - size + 1;//右分区待交换元素的起始索引
        int tmp = 0;
        while (size-- != 0) {
            tmp = nums[start];
            nums[start] = nums[i];
            nums[i] = tmp;
            start++;
            i++;
        }
    }
}
