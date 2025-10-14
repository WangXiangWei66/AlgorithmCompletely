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
            graph.get(roads[i][1]).add(new int[]{roads[i][0], roads[i][2]});
            //添加所有原始边
            for (int[] r : roads) {
                graph.get(r[0]).add(new int[]{r[1], r[2]});
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
        heap.add(new int[]{1, 0});
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
                    heap.add(new int[]{to, cur[1] + weight});
                }
            }
        }
        return ans;
    }


    public static int min2(int n, int[][] roads) {
        ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] r : roads) {
            graph.get(r[0]).add(new int[]{0, r[1], r[2]});
            //1表示为反方向的边，1：表示已经使用魔法
            graph.get(r[1]).add(new int[]{1, r[0], r[2]});
        }
        //[是否使用过魔法, 当前节点, 累计距离]
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[2] - b[2]);
        //访问标记数组：visited[0][u]表示未使用魔法到达u的状态已访问
        //visited[1][u]表示已使用魔法到达u的状态已访问
        boolean[][] visited = new boolean[2][n + 1];
        heap.add(new int[]{0, 1, 0});
        int ans = Integer.MAX_VALUE;
        while (!heap.isEmpty()) {
            int[] cur = heap.poll();
            if (visited[cur[0]][cur[1]]) {
                continue;
            }
            visited[cur[0]][cur[1]] = true;
            if (cur[1] == n) {
                ans = Math.min(ans, cur[2]);
                if (visited[0][n] && visited[1][n]) {
                    break;
                }
            }
            for (int[] edge : graph.get(cur[1])) {
                //未用魔法，且走的是原始方向的边
                if (cur[0] + edge[0] == 0) {
                    if (!visited[0][edge[1]]) {
                        heap.add(new int[]{0, edge[1], cur[2] + edge[2]});
                    }
                }
                //两种情况：未用魔法，走反方向的边、已用魔法，走原始边
                if (cur[0] + edge[0] == 1) {
                    if (!visited[1][edge[1]]) {
                        heap.add(new int[]{1, edge[1], cur[2] + edge[2]});
                    }
                }
            }
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    public static int[][] randomRoads(int n, int v) {
        int m = (int) (Math.random() * (n * (n - 1) / 2)) + 1;
        int[][] roads = new int[m][3];
        for (int i = 0; i < m; i++) {
            roads[i][0] = (int) (Math.random() * n) + 1;
            roads[i][1] = (int) (Math.random() * n) + 1;
            roads[i][2] = (int) (Math.random() * v) + 1;
        }
        return roads;
    }

    // 为了测试
    public static void main(String[] args) {
        int N = 20;
        int V = 30;
        int testTime = 20000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int n = (int) (Math.random() * N) + 1;
            int[][] roads = randomRoads(n, V);
            if (min1(n, roads) != min2(n, roads)) {
                System.out.println("出错了!");
            }
        }
        System.out.println("测试结束");

        // 点的数量10^5
        int n = 100000;
        // 边的数量2 * 10^5
        int m = 200000;
        // 时间复杂度很明显和边的权重是没关系的
        // 所以这里设置小一点，防止出现溢出的解
        int v = 100;
        int[][] roads = new int[m][3];
        for (int i = 0; i < m; i++) {
            roads[i][0] = (int) (Math.random() * n) + 1;
            roads[i][1] = (int) (Math.random() * n) + 1;
            roads[i][2] = (int) (Math.random() * v) + 1;
        }
        long start = System.currentTimeMillis();
        System.out.println("运行结果 : " + min2(n, roads));
        long end = System.currentTimeMillis();
        System.out.println("运行时间(毫秒) : " + (end - start));
    }

}
