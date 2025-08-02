package Class29;
//整数数组 nums 按升序排列，数组中的值互不相同 。
//在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了 旋转
//使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标 从 0 开始 计数
//例如，[0,1,2,4,5,6,7] 在下标 3 处经旋转后可能变为[4,5,6,7,0,1,2]
//给你旋转后的数组nums和一个整数target，如果 nums 中存在这个目标值 target ，则返回它的下标，否则返回-1
//Leetcode题目：https://leetcode.com/problems/search-in-rotated-sorted-array/

//已知存在一个按非降序排列的整数数组 nums ，数组中的值不必互不相同。
//在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了旋转
//使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标从0开始计数)
//例如， [0,1,2,4,4,4,5,6,6,7] 在下标 5 处经旋转后可能变为 [4,5,6,6,7,0,1,2,4,4]
//给你旋转后的数组nums和一个整数target，请你编写一个函数来判断给定的目标值是否存在于数组中
//如果 nums 中存在这个目标值 target ，则返回 true ，否则返回 false 。
//你必须尽可能减少整个操作步骤。
//Leetcode题目：https://leetcode.cn/problems/search-in-rotated-sorted-array-ii/
public class Problem_0033_SearchInRotateSortedArray {

    public static int search(int[] arr, int num) {
        int L = 0;//左指针，指向数组的初始位置
        int R = arr.length - 1;//右指针，指向数组最后一个位置
        int M = 0;//中间指针，用于二分
        while (L <= R) {
            M = (L + R) / 2;
            //如果中间元素直接等于目标值，直接返回
            if (arr[M] == num) {
                return M;
            }
            //处理特殊情况，左中右三个位置的元素值都相等
            if (arr[L] == arr[M] && arr[M] == arr[R]) {
                //左到中范围内，跳过与中间元素相等的元素
                while (L != M && arr[L] == arr[M]) {
                    L++;
                }
                //左指针已经移动到中间位置，说明左半部分元素都相同，移动左指针到中间位置右侧，继续下一轮循环
                if (L == M) {
                    L = M + 1;
                    continue;
                }
            }
            if (arr[L] != arr[M]) {
                if (arr[M] > arr[L]) {
                    //目标元素在左半部分的范围内
                    if (num >= arr[L] && num < arr[M]) {
                        R = M - 1;
                    } else {
                        L = M + 1;
                    }
                } else {
                    if (num > arr[M] && num <= arr[R]) {
                        L = M + 1;
                    }
                }
            } else {
                if (arr[M] < arr[R]) {
                    if (num > arr[M] && num <= arr[R]) {
                        L = M + 1;
                    } else {
                        R = M - 1;
                    }
                } else {
                    if (num >= arr[L] && num < arr[M]) {
                        R = M - 1;
                    } else {
                        L = M + 1;
                    }
                }
            }
        }
        return -1;
    }
}
