package class_2021_12_1;

import java.util.PriorityQueue;

//int n, int[][] roads, int x, int y
//n表示城市数量，城市编号0~n-1
//roads[i][j] == distance，表示城市i到城市j距离为distance(无向图)
//求城市x到城市y的最短距离
public class Code01_XtoYMinDistance {

    public static int minDistance1(int n, int[][] roads, int x, int y) {
        int[][] map = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                map[i][j] = Integer.MAX_VALUE;
            }
        }
        //填充道路信息到地图矩阵
        for (int[] road : roads) {
            map[road[0]][road[1]] = Math.min(map[road[0]][road[1]], road[2]);
            map[road[1]][road[0]] = Math.min(map[road[1]][road[0]], road[2]);
        }
        //记录城市是否已访问
        boolean[] visited = new boolean[n + 1];
        return process(x, y, n, map, visited);
    }

    public static int process(int cur, int aim, int n, int[][] map, boolean[] visited) {
        if (visited[cur]) {
            return Integer.MAX_VALUE;
        }
        if (cur == aim) {
            return 0;
        }
        visited[cur] = true;
        int ans = Integer.MAX_VALUE;
        for (int next = 1; next <= n; next++) {
            if (next != cur && map[cur][next] != Integer.MAX_VALUE) {
                //递归求解从next到aim的最短距离
                int rest = process(next, aim, n, map, visited);
                //可达便更新距离
                if (rest != Integer.MAX_VALUE) {
                    ans = Math.min(ans, map[cur][next] + rest);
                }
            }
        }
        visited[cur] = false;
        return ans;
    }

    public static int minDistance2(int n, int[][] roads, int x, int y) {
        int[][] map = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                map[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int[] road : roads) {
            map[road[0]][road[1]] = Math.min(map[road[0]][road[1]], road[2]);
            map[road[1]][road[0]] = Math.min(map[road[1]][road[0]], road[2]);
        }
        //记录当前城市是否已经计算出了最短路径
        boolean[] computed = new boolean[n + 1];
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> (a.pathSum - b.pathSum));
        heap.add(new Node(x, 0));
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            if (computed[cur.city]) {
                continue;
            }
            if (cur.city == y) {
                return cur.pathSum;
            }
            computed[cur.city] = true;
            for (int next = 1; next <= n; next++) {
                if (next != cur.city && map[cur.city][next] != Integer.MAX_VALUE && !computed[next]) {
                    heap.add(new Node(next, cur.pathSum + map[cur.city][next]));
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    public static class Node {
        public int city;
        public int pathSum;

        public Node(int c, int p) {
            city = c;
            pathSum = p;
        }
    }

    public static int[][] randomRoads(int n, int m, int v) {
        int[][] roads = new int[m][3];
        for (int i = 0; i < m; i++) {
            int from = (int) (Math.random() * n) + 1;
            int to = (int) (Math.random() * n) + 1;
            int distance = (int) (Math.random() * v) + 1;
            roads[i] = new int[]{from, to, distance};
        }
        return roads;
    }

    public static void main(String[] args) {
        int n = 4;
        int m = 4;
        int[][] roads = new int[m][3];
        roads[0] = new int[]{1, 2, 4};
        roads[1] = new int[]{1, 3, 1};
        roads[2] = new int[]{1, 4, 1};
        roads[3] = new int[]{2, 3, 1};
        int x = 2;
        int y = 4;
        System.out.println(minDistance1(n, roads, x, y));
        System.out.println(minDistance2(n, roads, x, y));

        int cityMaxSize = 12;
        int pathMax = 30;
        int testTimes = 20000;
        System.out.println("test begin!");
        for (int i = 0; i < testTimes; i++) {
            n = (int) (Math.random() * cityMaxSize) + 1;
            m = (int) (Math.random() * n * (n - 1) / 2) + 1;
            roads = randomRoads(n, m, pathMax);
            x = (int) (Math.random() * n) + 1;
            y = (int) (Math.random() * n) + 1;
            int ans1 = minDistance1(n, roads, x, y);
            int ans2 = minDistance1(n, roads, x, y);
            if (ans1 != ans2) {
                System.out.println("Oops");
                break;
            }
        }
        System.out.println("test finish!");
    }
}
