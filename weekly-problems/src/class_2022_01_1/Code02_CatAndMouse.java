package class_2022_01_1;

import java.util.ArrayList;
import java.util.Arrays;

//在一张 无向 图上，节点编号0~N-1。老鼠开始在1节点，猫在2节点，0号节点是洞，老鼠想进洞
//老鼠第先出发，猫后出发，轮流行动。
//在每个玩家的行动中，他们 必须 沿着图中与所在当前位置连通的一条边移动
//此外猫无法移动到洞中（节点 0）。
//然后，游戏在出现以下三种情形之一时结束：
//如果猫和老鼠出现在同一个节点，猫获胜。
//如果老鼠到达洞中，老鼠获胜。
//如果某一位置重复出现（即，玩家的位置和移动顺序都与上一次行动相同），游戏平局。
//给你一张图 graph ，并假设两位玩家都都以最佳状态参与游戏，返回谁获胜
//leetcode链接 : https://leetcode.com/problems/cat-and-mouse/
public class Code02_CatAndMouse {

    public static int catMouseGame1(int[][] graph) {
        int n = graph.length;//获取图中节点的数量
        int limit = ((n * (n - 1)) << 1) + 1;
        // 创建三维DP表：dp[猫的位置][老鼠的位置][回合数] = 游戏结果
        // 结果可能为：0-平局，1-老鼠赢，2-猫赢
        int[][][] dp = new int[n][n][limit];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        return process(graph, limit, 2, 1, 1, dp);
    }

    //limit：限制最大回合数
    //turn：当前回合数
    public static int process(int[][] graph, int limit, int cat, int mouse, int turn, int[][][] dp) {
        if (turn == limit) {
            return 0;
        }
        if (dp[cat][mouse][turn] != -1) {
            return dp[cat][mouse][turn];
        }
        int ans = 0;
        if (cat == mouse) {
            ans = 2;
        } else if (mouse == 0) {
            ans = 1;
        } else {
            //判断当前是谁的回合
            if ((turn & 1) == 1) {//老鼠的回合
                ans = 2;//默认情况下先让猫赢
                for (int next : graph[mouse]) {
                    int p = process(graph, limit, cat, next, turn + 1, dp);
                    ans = p == 1 ? 1 : (p == 0 ? 0 : ans);
                    if (ans == 1) {
                        break;
                    }
                }
            } else {//猫的回合
                ans = 1;
                for (int next : graph[cat]) {
                    if (next != 0) {
                        int p = process(graph, limit, next, mouse, turn + 1, dp);
                        ans = p == 2 ? 2 : (p == 0 ? 0 : ans);
                        if (ans == 2) {
                            break;
                        }
                    }
                }
            }
        }
        dp[cat][mouse][turn] = ans;
        return ans;
    }

    public static int catMouseGame2(int[][] graph) {
        int n = graph.length;
        int limit = (n << 1) + 12;
        int[][][] dp = new int[n][n][limit];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        return process(graph, limit, 2, 1, 1, dp);
    }

    public static int right(int[][] graph) {
        int n = graph.length;
        // 创建三维布尔数组记录路径：path[猫位置][老鼠位置][回合状态]
        // turn用0和1表示：1为老鼠回合，0为猫回合（与前一种实现不同）
        boolean[][][] path = new boolean[n][n][2];
        return win(graph, 2, 1, 1, path);
    }

    public static int win(int[][] graph, int cat, int mouse, int turn, boolean[][][] path) {
        if (path[cat][mouse][turn]) {
            return 0;
        }
        path[cat][mouse][turn] = true;//当前状态标记为已访问
        int ans = 0;
        if (cat == mouse) {
            ans = 2;
        } else if (mouse == 0) {
            ans = 1;
        } else {
            //老鼠的回合
            if (turn == 1) {
                ans = 2;
                for (int next : graph[mouse]) {
                    int p = win(graph, cat, next, 0, path);
                    ans = p == 1 ? 1 : (p == 0 ? 0 : ans);
                    if (ans == 1) {
                        break;
                    }
                }
            } else {
                ans = 1;
                for (int next : graph[cat]) {
                    //首先强调猫不能进洞
                    //turn ^ 1：0和1进行切换
                    if (next != 0) {
                        int p = win(graph, next, mouse, turn ^ 1, path);
                        if (ans == 2) {
                            break;
                        }
                    }
                }
            }
        }
        path[cat][mouse][turn] = false;
        return ans;
    }

    public static int[][] randomGraph(int n) {
        ArrayList<ArrayList<Integer>> g = new ArrayList<>();
        //初始化每个节点的邻接列表
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.random() > 0.5) {
                    g.get(i).add(j);
                    g.get(j).add(i);
                }
            }
        }
        //将ArrayList形式的邻接表转化为int数组的形式
        int[][] graph = new int[n][];
        for (int i = 0; i < n; i++) {
            int m = g.get(i).size();
            graph[i] = new int[m];
            for (int j = 0; j < m; j++) {
                graph[i][j] = g.get(i).get(j);
            }
        }
        return graph;
    }

    public static void main(String[] args) {
        System.out.println("对数器验证贪心");
        int N = 7;
        int testTimes = 300000;
        System.out.println("test begin!");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 3;
            int[][] graph = randomGraph(n);
            int ans1 = catMouseGame1(graph);
            int ans2 = catMouseGame2(graph);
            if (ans1 != ans2) {
                for (int row = 0; row < graph.length; row++) {
                    System.out.print(row + ":");
                    for (int next : graph[row]) {
                        System.out.print(next + "");
                    }
                    System.out.println();
                }
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("测试结束");

        // 错误例子
        int[][] graph = {
                // 0 :
                {2, 6, 7},
                // 1 :
                {3, 4, 5, 7},
                // 2 :
                {0, 3, 4, 7},
                // 3 :
                {1, 2, 5, 6},
                // 4 :
                {1, 2, 5, 7},
                // 5 :
                {1, 3, 4, 6},
                // 6 :
                {0, 3, 5, 7},
                // 7 :
                {0, 1, 2, 4, 6}};
        System.out.println(catMouseGame1(graph));
        System.out.println(catMouseGame2(graph));

    }
}
