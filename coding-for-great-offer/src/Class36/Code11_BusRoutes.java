package Class36;

import java.util.ArrayList;
import java.util.HashMap;

//来自三七互娱(Leetcode原题)
//给你一个数组 routes ，表示一系列公交线路，其中每个 routes[i] 表示一条公交线路，第 i 辆公交车将会在上面循环行驶。
//例如，路线 routes[0] = [1, 5, 7] 表示第 0 辆公交车会一直按序列 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ... 这样的车站路线行驶。
//现在从 source 车站出发（初始时不在公交车上），要前往 target 车站。 期间仅可乘坐公交车。
//求出 最少乘坐的公交车数量 。如果不可能到达终点车站，返回 -1 。
//Leetcode原题 : https://leetcode.com/problems/bus-routes/
public class Code11_BusRoutes {

    public static int numBusesToDestination(int[][] routes, int source, int target) {
        if (source == target) {
            return 0;
        }
        int n = routes.length;//公交线路数量
        //key：车站
        //value：经过该车站的所有公交线路
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {//每条公交线路
            for (int j = 0; j < routes[i].length; j++) {//线路上的每个车站
                if (!map.containsKey(routes[i][j])) {
                    map.put(routes[i][j], new ArrayList<>());
                }
                map.get(routes[i][j]).add(i);
            }
        }
        //BFS队列：存储当前可以乘坐的公交线路
        ArrayList<Integer> queue = new ArrayList<>();
        boolean[] set = new boolean[n];//记录已经乘坐过的公交线路
        //加入所有经过起点车站的公交线路
        for (int route : map.get(source)) {
            queue.add(route);
            set[route] = true;
        }
        int len = 1;//乘坐的公交车数量
        while (!queue.isEmpty()) {
            ArrayList<Integer> nextLevel = new ArrayList<>();//下一层要乘坐的线路
            for (int route : queue) {
                int[] bus = routes[route];//当前线路包含的车站
                for (int station : bus) {
                    if (station == target) {
                        return len;
                    }
                    //从当前车站可以换乘的其他线路
                    for (int nextRoute : map.get(station)) {
                        if (!set[nextRoute]) {
                            nextLevel.add(nextRoute);
                            set[nextRoute] = true;
                        }
                    }
                }
            }
            //进入下一层，同时公交车的数量+1
            queue = nextLevel;
            len++;
        }
        return -1;
    }
}
