package class_2022_09_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//来自美团
//某天小美进入了一个迷宫探险，根据地图所示，这个迷宫里有无数个房间
//序号分别为1、2、3、...入口房间的序号为1
//任意序号为正整数x的房间，都与序号 2*x 和 2*x + 1 的房间之间各有一条路径
//但是这些路径是单向的，即只能从序号为x的房间去到序号为 2*x 或 2*x+1 的房间
//而不能从 2*x 或 2*x+1 的房间去到序号为x的房间
//在任何时刻小美都可以选择结束探险并离开迷宫，但是离开之后将无法再次进入迷宫
//小美还提前了解了迷宫中宝藏的信息
//已知宝藏共有n个，其中第i个宝藏在序号为pi的房间，价值为wi
//且一个房间中可能有多个宝藏
//小美为了得到更多的宝藏，需要精心规划路线，她找到你帮忙
//想请你帮她计算一下，能获得的宝藏价值和最大值为多少
//第一行一个正整数n，表示宝藏数量。
//第二行为n个正整数p1, p2,...... pn，其中pi表示第 i 个宝藏在序号为pi的房间。
//第三行为n个正整数w1, w2,...... wn，其中wi表示第i个宝藏的价值为wi。
//1 <= n <= 40000, 1 <= pi < 2^30, 1 <= wi <= 10^6。
public class Code02_EntryRoomGetMoney {

    public static int maxMoney1(int n, int[] p, int[] w) {
        int[][] rooms = new int[n][2];
        for (int i = 0; i < n; i++) {
            rooms[i][0] = p[i];
            rooms[i][1] = w[i];
        }
        Arrays.sort(rooms, (a, b) -> a[0] - b[0]);
        int ans = 0;
        //缓存每个宝藏作为起点的最大价值
        int[] dp = new int[n];
        Arrays.fill(dp, -1);
        //遍历每个宝藏,计算以其为起点的最大价值
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, process1(i, rooms, n, dp));
        }
        return ans;
    }

    public static int process1(int index, int[][] rooms, int n, int[] dp) {
        if (dp[index] != -1) {
            return dp[index];
        }
        //存储后续可达宝藏的最大价值
        int next = 0;
        for (int i = index + 1; i < n; i++) {
            //判断当前宝藏的房间（rooms[index][0]）能否到达第i个宝藏的房间（rooms[i][0]）
            if (reach(rooms[index][0], rooms[i][0])) {
                //如果能到达,就去计算第i个宝藏的最大价值
                next = Math.max(next, process1(i, rooms, n, dp));
            }
        }
        int ans = rooms[index][1] + next;
        dp[index] = ans;
        return dp[index];
    }

    //可达性判断
    public static boolean reach(int from, int to) {
        while (to >= from) {
            if (from == to) {
                //找到祖先节点,可达
                return true;
            } else {
                to /= 2;
            }
        }
        return false;
    }
    //构造祖先 + 后代邻接图 +逆序DP
    public static int maxMoney2(int n, int[] p, int[] w) {
        int[][] rooms = new int[n][2];
        for (int i = 0; i < n; i++) {
            rooms[i][0] = p[i];
            rooms[i][1] = w[i];
        }
        Arrays.sort(rooms, (a, b) -> a[0] - b[0]);
        // 房间pi→首个宝藏索引
        HashMap<Integer, Integer> first = new HashMap<>();
        //每个宝藏仅关联其最近的有宝藏的祖先
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int to = rooms[i][0];
            //向上回溯到当前房间的所有祖先
            while (to > 0) {
                if (first.containsKey(to)) {
                    //找到最近的祖先,将当前索引i加入祖先的邻接表
                    graph.get(first.get(to)).add(i);
                    break;//仅关联最近的节点
                } else {
                    to >>= 1;
                }
            }
            //为当前索引i初始化邻接表
            graph.add(new ArrayList<>());
            if (!first.containsKey(rooms[i][0])) {
                first.put(rooms[i][0], i);
            }
        }
        int ans = 0;
        //索引i的宝藏为起点,能收集的最大价值
        int[] dp = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            //存储后代中最大的DP值
            int post = 0;
            for (int next : graph.get(i)) {
                if (rooms[next][0] == rooms[i][0]) {
                    //同一房间的后代,直接累加dp[next]
                    dp[i] += dp[next];
                } else {
                    post = Math.max(post, dp[next]);
                }
            }
            dp[i] += post + rooms[i][1];
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }

    public static int[] randomArray(int n, int v) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = (int) (Math.random() * v) + 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 100;
        int P = 5000;
        int W = 5000;
        int testTimes = 5000;
        System.out.println("功能测试开始");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 1;
            int[] p = randomArray(n, P);
            int[] w = randomArray(n, W);
            int ans1 = maxMoney1(n, p, w);
            int ans2 = maxMoney2(n, p, w);
            if (ans1 != ans2) {
                System.out.println("出错了");
            }
        }
        System.out.println("功能测试结束");

        System.out.println("性能测试开始");
        N = 50000;
        P = 2000000000;
        W = 1000000;
        int[] p = randomArray(N, P);
        int[] w = randomArray(N, W);
        System.out.println("房间个数 : " + N);
        System.out.println("位置范围 : " + P);
        System.out.println("价值范围 : " + W);
        long start = System.currentTimeMillis();
        maxMoney2(N, p, w);
        long end = System.currentTimeMillis();
        System.out.println("运行时间 : " + (end - start) + " 毫秒");
        System.out.println("性能测试结束");

    }

}
