package class_2022_07_3;

import java.util.HashMap;
import java.util.Map.*;

//给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。
//你可以按 任意顺序 返回答案。
//要求时间复杂度O(N)
//测试链接 : https://leetcode.cn/problems/top-k-frequent-elements/
public class Code03_TopKFrequentElements {
    //哈希表统计频率+快速选择
    public static int[] topKFrequent(int[] nums, int k) {
        //统计每个元素出现的频率
        HashMap<Integer, Integer> map = new HashMap<>();//存储元素及其出现的次数
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        //获取不同元素的数量
        int i = map.size();
        //二维数组：每行存储 [元素, 频率]
        int[][] arr = new int[i][2];
        //遍历哈希表，将键值对存入二维数组
        for (Entry<Integer, Integer> entry : map.entrySet()) {
            arr[--i][0] = entry.getKey();
            arr[i][1] = entry.getValue();
        }
        //对二维数组进行快速选择
        moreLess(arr, 0, arr.length - 1, k);
        int[] ans = new int[k];
        for (; i < k; i++) {
            ans[i] = arr[i][0];
        }
        return ans;
    }
    //函数作用：在arr[l..r]范围内，筛选出频率前k高的元素
    //最终结果：arr[l..l+k-1]即为频率前k高的元素
    public static void moreLess(int[][] arr, int l, int r, int k) {
        if (k == r - l + 1) {
            return;
        }
        //随机选择一个元素作为基准，并将他与末尾元素进行交换
        swap(arr, r, l + (int) (Math.random() * (r - l + 1)));
        int pivot = partition(arr, l, r);//获取基准元素的最终位置
        if (pivot - l == k) {
            return;
        } else if (pivot - l > k) {
            moreLess(arr, l, pivot - 1, k);
        } else {
            moreLess(arr, pivot, r, k - pivot + l);
        }
    }
    // 分区逻辑：以arr[r][1]（基准频率）为标准，将数组分为两部分
    // 左部分：频率 > 基准频率（高频元素）
    // 右部分：频率 <= 基准频率（低频元素）
    // 返回基准元素的最终索引
    public static int partition(int[][] arr, int l, int r) {
        int left = l - 1;
        int index = l;//遍历指针
        while (index < r) {
            if (arr[index][1] <= arr[r][1]) {
                index++;
            } else {
                swap(arr, ++left, index++);
            }
        }
        swap(arr, ++left, r);
        return left;
    }

    public static void swap(int[][] arr, int i, int j) {
        int[] tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
