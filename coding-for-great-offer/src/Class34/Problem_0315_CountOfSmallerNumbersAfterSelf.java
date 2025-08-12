package Class34;

import java.util.ArrayList;
import java.util.List;

//给定一个整数数组 nums，按要求返回一个新数组counts。数组 counts 有该性质： counts[i] 的值是 nums[i] 右侧小于nums[i] 的元素的数量。
//示例：
//输入：nums = [5,2,6,1]
//输出：[2,1,1,0]
//解释：
//5 的右侧有 2 个更小的元素 (2 和 1)
//2 的右侧仅有 1 个更小的元素 (1)
//6 的右侧有 1 个更小的元素 (1)
//1 的右侧有 0 个更小的元素
//Leetcode题目 : https://leetcode.com/problems/count-of-smaller-numbers-after-self/
public class Problem_0315_CountOfSmallerNumbersAfterSelf {

    public static class Node {
        public int value;
        public int index;//元素在原始数组中的索引

        public Node(int v, int i) {
            value = v;
            index = i;
        }
    }

    public static List<Integer> countSmaller(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        if (nums == null) {
            return ans;
        }
        //初始化结果列表，每个元素都先设为0
        for (int i = 0; i < nums.length; i++) {
            ans.add(0);
        }
        if (nums.length < 2) {
            return ans;
        }
        Node[] arr = new Node[nums.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Node(nums[i], i);
        }
        //调归并排序，在排序中计算结果
        process(arr, 0, arr.length - 1, ans);
        return ans;
    }

    public static void process(Node[] arr, int l, int r, List<Integer> ans) {
        if (l == r) {
            return;
        }
        int mid = l + ((r - l) >> 1);
        process(arr, l, mid, ans);
        process(arr, mid + 1, r, ans);
        merge(arr, l, mid, r, ans);
    }

    public static void merge(Node[] arr, int l, int m, int r, List<Integer> ans) {
        Node[] help = new Node[r - l + 1];//用于合并的辅助数组
        int i = help.length - 1;//从右往左填
        int p1 = m;
        int p2 = r;
        while (p1 >= l && p2 >= m + 1) {
            if (arr[p1].value > arr[p2].value) {
                //当左半部分元素大于右半部分元素时，右半部分中剩余的所有元素都小于当前左元素
                ans.set(arr[p1].index, ans.get(arr[p1].index) + p2 - m);
            }
            //将较大的元素，放入辅助数组的当前位置
            help[i--] = arr[p1].value > arr[p2].value ? arr[p1--] : arr[p2--];
        }
        while (p1 >= l) {
            help[i--] = arr[p1--];
        }
        while (p2 >= m + 1) {
            help[i--] = arr[p2--];
        }
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
    }

}
