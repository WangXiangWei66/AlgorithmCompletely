package Class48;

//给定一个二维数组代表迷宫，0代表路，1代表障碍
//给定一个球的位置，给定一个洞的位置
//你每次可以拨动球往上、下、左、右四个方向中的一个移动，但是球只有撞倒边界或者障碍才会停，只有球停了，你才能再次拨动球。
//你的目标是让球进洞，球在移动的过程中只要来到洞的位置，就认为球直接掉进洞里。你需要先保证球进洞的过程中，球移动的距离最短
//如果只有一种方案，直接返回这种方案的决定。
//如果有多个距离最短的方案，你需要返回其中字典序最小的决定。
//比如，假设如下两个方案，球的移动距离都是最小的:
//先往左拨动，球撞了墙之后，再往上拨，结果球进洞了，那么决定就是"lu" -> left up
//先往上拨动，球撞了墙之后，再往左拨，结果球进洞了，那么决定就是"ul" -> up left
//这两个方案如果都是移动距离最小的，那么应该返回lu，因为ul的字典序大
//leetcode题目：https://leetcode.com/problems/the-maze-iii/
public class Problem_0499_TheMazeIII {

    public static class Node {
        public int r;//当前行坐标
        public int c;//当前列坐标
        public int d;//当前移动方向
        public String p;//到达当前位置的路径

        public Node(int row, int col, int dir, String path) {
            r = row;
            c = col;
            d = dir;
            p = path;
        }
    }
    //ball[]:存储求的起始位置
    public static String findShortestWay(int[][] maze, int[] ball, int[] hole) {
        int n = maze.length;
        int m = maze[0].length;
        //用两个数组作为队列，实现双向BFS和分层BFS
        Node[] q1 = new Node[n * m], q2 = new Node[n * m];
        int s1 = 0, s2 = 0;//队列中元素的数量
        boolean[][][] visited = new boolean[maze.length][maze[0].length][4];//某个位置是否已经被访问过了
        //初始化队列，将球的起始位置加入队列
        s1 = spread(maze, n, m, new Node(ball[0], ball[1], 4, ""), visited, q1, s1);
        while (s1 != 0) {
            for (int i = 0; i < s1; i++) {
                Node cur = q1[i];
                //当前节点的位置就是洞的位置
                if (hole[0] == cur.r && hole[1] == cur.c) {
                    return cur.p;
                }
                s2 = spread(maze, n, m, cur, visited, q2, s2);
            }
            Node[] tmp = q1;
            q1 = q2;
            q2 = tmp;
            s1 = s2;
            s2 = 0;//重置下一层的计数器
        }
        return "impossible";
    }
    //下左右上
    public static int[][] to = {{1, 0}, {0, -1}, {0, 1}, {-1, 0}, {0, 0}};
    public static String[] re = {"d", "l", "r", "u"};

    public static int spread(int[][] maze, int n, int m, Node cur, boolean[][][] v, Node[] q, int s) {
        int d = cur.d;//当前的方向
        int r = cur.r + to[d][0];
        int c = cur.c + to[d][1];
        //需要停下，选择新的方向
        if (d == 4 || r < 0 || r == n || c < 0 || c == m || maze[r][c] != 0) {
            for (int i = 0; i < 4; i++) {
                if (i != d) {
                    r = cur.r + to[i][0];
                    c = cur.c + to[i][1];
                    if (r >= 0 && r < n && c >= 0 && c < m && maze[r][c] == 0 && !v[r][c][i]) {
                        v[r][c][i] = true;
                        Node next = new Node(r, c, i, cur.p + re[i]);
                        q[s++] = next;
                    }
                }
            }
            //继续沿着新方向移动
        } else {
            if (!v[r][c][d]) {
                v[r][c][d] = true;
                q[s++] = new Node(r, c, d, cur.p);
            }
        }
        return s;
    }
}
