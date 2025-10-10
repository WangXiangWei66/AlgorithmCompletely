package class_2022_05_1;

import java.util.ArrayList;
import java.util.HashMap;

//来自蔚来汽车
//给你一个整数数组arr，你一开始在数组的第一个元素处（下标为 0）。
//每一步，你可以从下标i跳到下标i + 1 、i - 1 或者 j ：
//i + 1 需满足：i + 1 < arr.length
//i - 1需满足：i - 1 >= 0
//j需满足：arr[i] == arr[j]且i != j
//请你返回到达数组最后一个元素的下标处所需的最少操作次数。
//注意：任何时候你都不能跳到数组外面。
//leetcode测试链接 : https://leetcode-cn.com/problems/jump-game-iv/
public class Code01_JumMinSameValue {

    public static int minJumps(int[] arr) {
        int n = arr.length;
        // 创建值到索引列表的映射，存储相同值的所有下标
        HashMap<Integer, ArrayList<Integer>> valueIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (!valueIndex.containsKey(arr[i])) {
                valueIndex.put(arr[i], new ArrayList<>());
            }
            valueIndex.get(arr[i]).add(i);
        }
        boolean[] visited = new boolean[n];
        //数组实现队列，存储待访问索引
        int[] queue = new int[n];
        int l = 0;
        int r = 0;
        queue[r++] = 0;
        visited[0] = true;
        int jump = 0;//记录跳跃次数
        while (l != r) {
            int tmp = r;
            for (; l < tmp; l++) {
                int cur = queue[l];
                if (cur == n - 1) {
                    return jump;
                }
                if (cur + 1 < n && !visited[cur + 1]) {
                    visited[cur + 1] = true;
                    queue[r++] = cur + 1;
                }
                if (cur > 0 && !visited[cur - 1]) {
                    visited[cur - 1] = true;
                    queue[r++] = cur - 1;
                }
                for (int next : valueIndex.get(arr[cur])) {
                    if (!visited[next]) {
                        visited[next] = true;
                        queue[r++] = next;
                    }
                }
                //清空当前值对应的索引
                valueIndex.get(arr[cur]).clear();
            }
            jump++;
        }
        return -1;
    }
}
