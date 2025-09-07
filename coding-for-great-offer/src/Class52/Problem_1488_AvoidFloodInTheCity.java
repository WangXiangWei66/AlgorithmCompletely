package Class52;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

//你的国家有无数个湖泊，所有湖泊一开始都是空的。当第 n个湖泊下雨的时候，如果第 n个湖泊是空的，那么它就会装满水，否则这个湖泊会发生洪水。你的目标是避免任意一个湖泊发生洪水。
//给你一个整数数组rains，其中：
//rains[i] > 0表示第 i天时，第 rains[i]个湖泊会下雨。
//rains[i] == 0表示第 i天没有湖泊会下雨，你可以选择 一个湖泊并 抽干这个湖泊的水。
//请返回一个数组ans，满足：
//ans.length == rains.length
//如果rains[i] > 0 ，那么ans[i] == -1。
//如果rains[i] == 0，ans[i]是你第i天选择抽干的湖泊。
//如果有多种可行解，请返回它们中的 任意一个。如果没办法阻止洪水，请返回一个 空的数组。
//leetcode题目：https://leetcode.com/problems/avoid-flood-in-the-city/
public class Problem_1488_AvoidFloodInTheCity {
    //需要处理的湖泊信息
    public static class Work implements Comparable<Work> {

        public int lake;//湖泊编号
        public int nextRain;//湖泊下一次下雨的日期

        public Work(int l, int p) {
            lake = l;
            nextRain = p;
        }

        @Override
        public int compareTo(Work o) {
            return nextRain - o.nextRain;//按照下一次下雨的日期优先排序
        }
    }

    public static int[] avoidFlood(int[] rains) {
        int n = rains.length;
        int[] ans = new int[n];//存储每天的处理方案
        int[] invalid = new int[0];//无法避免洪水时返回
        //存储每个湖泊所有下雨的日期
        HashMap<Integer, LinkedList<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (rains[i] != 0) {
                if (!map.containsKey(rains[i])) {
                    map.put(rains[i], new LinkedList<>());
                }
                map.get(rains[i]).addLast(i);
            }
        }
        HashSet<Integer> set = new HashSet<>();//记录已经充满水的湖泊
        PriorityQueue<Work> heap = new PriorityQueue<>();//优先级队列，按照下一次下雨的日期排序
        for (int i = 0; i < n; i++) {
            if (rains[i] != 0) {
                if (set.contains(rains[i])) {
                    return invalid;
                }
                set.add(rains[i]);
                map.get(rains[i]).pollFirst();
                if (!map.get(rains[i]).isEmpty()) {
                    //如果湖泊未来还有下雨的日期，创建一个Work对象并加入优先级队列
                    heap.add(new Work(rains[i], map.get(rains[i]).peekFirst()));
                }
                ans[i] = -1;
            } else {
                if (heap.isEmpty()) {
                    ans[i] = 1;
                } else {
                    Work cur = heap.poll();
                    set.remove(cur.lake);
                    ans[i] = cur.lake;
                }
            }
        }
        return ans;
    }
}
