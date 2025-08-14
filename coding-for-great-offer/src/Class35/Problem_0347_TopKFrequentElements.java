package Class35;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

//给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
//示例 1:
//输入: nums = [1,1,1,2,2,3], k = 2
//输出: [1,2]
//示例 2:
//输入: nums = [1], k = 1
//输出: [1]
//提示：
//1 <= nums.length <= 10^5
//k 的取值范围是 [1, 数组中不相同的元素的个数]
//题目数据保证答案唯一，换句话说，数组中前k个高频元素的集合是唯一的
//进阶：你所设计算法的时间复杂度必须优于O(n log n) ，其中 n是数组大小。
//Leetcode题目 : https://leetcode.com/problems/top-k-frequent-elements/
public class Problem_0347_TopKFrequentElements {

    public static class Node {
        public int num;
        public int count;//数字出现的次数

        public Node(int k) {
            num = k;
            count = 1;
        }
    }

    public static class CountComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o1.count - o2.count;
        }
    }

    //时间复杂度为O(n*logN)
    //空间复杂度为O(n)
    public static int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer, Node> map = new HashMap<>();//统计每个数字出现的次数
        for (int num : nums) {
            if (!map.containsKey(num)) {
                map.put(num, new Node(num));
            } else {
                map.get(num).count++;
            }
        }
        //筛选前k个高频的元素
        PriorityQueue<Node> heap = new PriorityQueue<>(new CountComparator());
        for (Node node : map.values()) {
            //如果堆还没有满，或者当前节点的频率高于堆顶节点的频率，则加入堆
            if (heap.size() < k || (heap.size() == k && node.count > heap.peek().count)) {
                heap.add(node);
            }
            if (heap.size() > k) {
                heap.poll();
            }
        }
        int[] ans = new int[k];
        int index = 0;
        while (!heap.isEmpty()) {
            ans[index++] = heap.poll().num;
        }
        return ans;
    }
}
