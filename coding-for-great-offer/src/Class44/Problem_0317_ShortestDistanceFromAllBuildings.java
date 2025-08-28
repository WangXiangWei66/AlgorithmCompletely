package Class44;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

//给定一个二维矩阵，其中的值0代表路，1代表人，2代表障碍
//每个人都可以上下左右移动，但是只能走值为0的格子
//想把所有的人聚集在某个值为0的地方开会，希望所有人到会议点的总距离最短
//返回最短的开会总距离
//如果无论如何都无法让所有的人聚集到一起，返回-1
//leetcode题目 : https://leetcode.com/problems/shortest-distance-from-all-buildings/
public class Problem_0317_ShortestDistanceFromAllBuildings {

    public static int shortestDistance1(int[][] grid) {
        int ans = Integer.MAX_VALUE;
        int N = grid.length;
        int M = grid[0].length;
        int buildings = 0;//建筑物的总数
        //存储每个各自的坐标和值
        Position[][] positions = new Position[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (grid[i][j] == 1) {
                    buildings++;
                }
                positions[i][j] = new Position(i, j, grid[i][j]);
            }
        }
        if (buildings == 0) {
            return 0;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                ans = Math.min(ans, bfs(positions, buildings, i, j));
            }
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    //对指定的地块进行广度优先搜索
    public static int bfs(Position[][] positions, int buildings, int i, int j) {
        if (positions[i][j].v != 0) {
            return Integer.MAX_VALUE;
        }
        //存储每个Position到起点的距离，避免重复访问
        HashMap<Position, Integer> levels = new HashMap<>();
        //按层级遍历格子
        Queue<Position> queue = new LinkedList<>();
        //起点：当前空地块
        Position from = positions[i][j];
        levels.put(from, 0);
        queue.add(from);
        int ans = 0;//到所有建筑物的总距离
        int solved = 0;//已找到的建筑物数量
        //队列不为空，且未找到建筑物时继续
        while (!queue.isEmpty() && solved != buildings) {
            Position cur = queue.poll();
            int level = levels.get(cur);//当前各自到起点的距离
            if (cur.v == 1) {
                ans += level;
                solved++;
            } else {
                add(queue, levels, positions, cur.r - 1, cur.c, level + 1);
                add(queue, levels, positions, cur.r + 1, cur.c, level + 1);
                add(queue, levels, positions, cur.r, cur.c - 1, level + 1);
                add(queue, levels, positions, cur.r, cur.c + 1, level + 1);
            }
        }
        return solved == buildings ? ans : Integer.MAX_VALUE;
    }

    public static class Position {
        public int r;
        public int c;
        public int v;

        public Position(int row, int col, int value) {
            r = row;
            c = col;
            v = value;
        }
    }

    public static void add(Queue<Position> q, HashMap<Position, Integer> l, Position[][] p, int i, int j, int level) {
        if (i >= 0 && i < p.length && j >= 0 && j < p[0].length && p[i][j].v != 2 && !l.containsKey(p[i][j])) {
            l.put(p[i][j], level);
            q.add(p[i][j]);
        }
    }

    public static int shortestDistance2(int[][] grid) {
        int N = grid.length;
        int M = grid[0].length;
        int ones = 0;//建筑物数量
        int zeroes = 0;//空地数量
        Info[][] infos = new Info[N][M];//存储每个格子的坐标、值和编号
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (grid[i][j] == 1) {
                    infos[i][j] = new Info(i, j, 1, ones++);
                } else if (grid[i][j] == 0) {
                    infos[i][j] = new Info(i, j, 0, zeroes++);
                } else {
                    infos[i][j] = new Info(i, j, 2, Integer.MAX_VALUE);//障碍物默认为无效值
                }
            }
        }
        if (ones == 0) {
            return 0;
        }
        int[][] distance = new int[ones][zeroes];//第x个建筑物到第y个空地的最短距离
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                //只对建筑物执行bfs
                if (infos[i][j].v == 1) {
                    bfs(infos, i, j, distance);
                }
            }
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < zeroes; i++) {
            int sum = 0;//第i个空地到所有建筑物的距离
            for (int j = 0; j < ones; j++) {
                if (distance[i][j] == 0) {
                    sum = Integer.MAX_VALUE;
                    break;
                } else {
                    sum += distance[i][j];
                }
            }
            ans = Math.min(ans, sum);
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    public static class Info {
        public int r;
        public int c;
        public int v;
        public int t;//编号

        public Info(int row, int col, int value, int th) {
            r = row;
            c = col;
            v = value;
            t = th;
        }
    }

    public static void bfs(Info[][] infos, int i, int j, int[][] distance) {
        //levels：记录每个空地到当前建筑物的距离
        HashMap<Info, Integer> levels = new HashMap<>();
        Queue<Info> queue = new LinkedList<>();//用于bfs遍历
        Info from = infos[i][j];
        add(queue, levels, infos, from.r - 1, from.c, 1);
        add(queue, levels, infos, from.r + 1, from.c, 1);
        add(queue, levels, infos, from.r, from.c - 1, 1);
        add(queue, levels, infos, from.r, from.c + 1, 1);
        while (!queue.isEmpty()) {
            Info cur = queue.poll();
            int level = levels.get(cur);//当前空地到建筑物的距离
            distance[from.t][cur.t] = level;
            add(queue, levels, infos, cur.r - 1, cur.c, level + 1);
            add(queue, levels, infos, cur.r + 1, cur.c, level + 1);
            add(queue, levels, infos, cur.r, cur.c + 1, level + 1);
            add(queue, levels, infos, cur.r, cur.c - 1, level + 1);
        }
    }

    public static void add(Queue<Info> q, HashMap<Info, Integer> l, Info[][] infos, int i, int j, int level) {
        //各自要保证还没有被访问过
        if (i >= 0 && i < infos.length && j >= 0 && j < infos[0].length && infos[i][j].v == 0 && !l.containsKey(infos[i][j])) {
            l.put(infos[i][j], level);
            q.add(infos[i][j]);
        }
    }
    //原地标记访问状态和实时累加距离
    public static int shortestDistance3(int[][] grid) {
        int[][] dist = new int[grid.length][grid[0].length];//存储空地到所有建筑物的距离
        int pass = 0;//当前bfs可访问的空地状态
        int step = Integer.MAX_VALUE;//实时记录当前最少总距离
        int[] trans = {0, 1, 0, -1, 0};
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    step = bfs(grid, dist, i, j, pass--, trans);
                    if (step == Integer.MAX_VALUE) {
                        return -1;
                    }
                }
            }
        }
        return step == Integer.MAX_VALUE ? -1 : step;
    }


    public static int bfs(int[][] grid, int[][] dist, int row, int col, int pass, int[] trans) {
        Queue<int[]> que = new LinkedList<int[]>();
        que.offer(new int[]{row, col});
        int level = 0;
        int ans = Integer.MAX_VALUE;
        while (!que.isEmpty()) {
            int size = que.size();
            level++;
            //遍历当前层的所有格子
            for (int k = 0; k < size; k++) {
                int[] node = que.poll();
                for (int i = 1; i < trans.length; i++) {
                    // trans[i-1] 是行偏移（0→上，1→右，2→下，3→左）
                    // trans[i] 是列偏移（1→右，0→下，-1→左，0→上）
                    int nextr = node[0] + trans[i - 1];
                    int nextc = node[1] + trans[i];
                    if (nextr >= 0 && nextr < grid.length && nextc >= 0 && nextc < grid[0].length && grid[nextr][nextc] == pass) {
                        que.offer(new int[]{nextr, nextc});
                        dist[nextr][nextc] += level;//累加距离
                        ans = Math.min(ans, dist[nextr][nextc]);
                        grid[nextr][nextc]--;
                    }
                }
            }
        }
        return ans;
    }
}
