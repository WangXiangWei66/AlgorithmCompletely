package class_2022_05_2;

import java.util.ArrayList;
import java.util.PriorityQueue;

//来自网易
//给出一个有n个点，m条有向边的图
//你可以施展魔法，把有向边，变成无向边
//比如A到B的有向边，权重为7。施展魔法之后，A和B通过该边到达彼此的代价都是7。
//求，允许施展一次魔法的情况下，1到n的最短路，如果不能到达，输出-1。
//n为点数, 每条边用(a,b,v)表示，含义是a到b的这条边，权值为v
//点的数量 <= 10^5，边的数量 <= 2 * 10^5，1 <= 边的权值 <= 10^6
public class Code04_OneEdgeMagicMinPathSum {

    public static int min1(int n, int[][] roads) {
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < roads.length; i++) {
            ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
            for (int j = 0; j <= n; j++) {
                graph.add(new ArrayList<>());
            }
            //对第i条边添加反向边
            graph.get(roads[i][1]).add(new int[] { roads[i][0], roads[i][2] });
            //添加所有原始边
            for (int[] r : roads) {
                graph.get(r[0]).add(new int[] { r[1], r[2] });
            }
            ans = Math.min(ans, dijkstra1(n, graph));
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    public static int dijkstra1(int n, ArrayList<ArrayList<int[]>> graph) {
        // 使用优先队列（最小堆）实现Dijkstra算法，存储[节点, 距离]，按距离排序
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        boolean[] visited = new boolean[n + 1];
        //起点是1，初始距离为0
        heap.add(new int[] { 1, 0 });
        int ans = Integer.MAX_VALUE;
        while (!heap.isEmpty()) {
            int[] cur = heap.poll();
            if (cur[0] == n) {
                ans = cur[1];
                break;
            }
            if (visited[cur[0]]) {
                continue;
            }
            visited[cur[0]] = true;
            for (int[] edge : graph.get(cur[0])) {
                int to = edge[0];
                int weight = edge[1];
                if (!visited[to]) {
                    heap.add(new int[] { to, cur[1] + weight });
                }
            }
        }
        return ans;
    }

    public static int[][]randomRoads(int n,int v){

    }
}
