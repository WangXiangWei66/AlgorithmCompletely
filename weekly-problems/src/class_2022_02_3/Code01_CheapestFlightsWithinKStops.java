package class_2022_02_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;//Map的中的一个内部接口，表示哈希表中的键值对

//有 n 个城市通过一些航班连接。给你一个数组flights ，
//其中flights[i] = [fromi, toi, pricei] ，
//表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
//现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，
//你的任务是找到出一条最多经过 k站中转的路线，
//使得从 src 到 dst 的 价格最便宜 ，并返回该价格。
//如果不存在这样的路线，则输出 -1。
//leetcode链接: https://leetcode.com/problems/cheapest-flights-within-k-stops/
public class Code01_CheapestFlightsWithinKStops {

    public static int findCheapestPrice1(int n, int[][] flights, int src, int dst, int k) {
        //构建临界表来表示航班图
        ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<int[]>());
        }
        for (int[] line : flights) {
            graph.get(line[0]).add(new int[]{line[1], line[2]});
        }
        int[] cost = new int[n];//起点到每个城市的最低成本
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[src] = 0;
        //记录当前可达的城市及其最低成本
        HashMap<Integer, Integer> curMap = new HashMap<>();
        curMap.put(src, 0);
        //最多乘坐k+1个航班
        for (int i = 0; i <= k; i++) {
            HashMap<Integer, Integer> nextMap = new HashMap<>();
            for (Entry<Integer, Integer> entry : curMap.entrySet()) {
                int from = entry.getKey();
                int preCost = entry.getValue();
                for (int[] line : graph.get(from)) {
                    int to = line[0];
                    int curCost = line[1];
                    cost[0] = Math.min(cost[to], preCost + curCost);
                    nextMap.put(to, Math.min(nextMap.getOrDefault(to, Integer.MAX_VALUE), preCost + curCost));
                }
            }
            curMap = nextMap;
        }
        return cost[dst] == Integer.MAX_VALUE ? -1 : cost[dst];
    }
}
