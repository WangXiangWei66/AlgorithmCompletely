package class_2022_10_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//力扣数据中心有n台服务器，分别按从0到n-1的方式进行了编号
//它们之间以「服务器到服务器」点对点的形式相互连接组成了一个内部集群
//其中连接connections 是无向的
//从形式上讲，connections[i] = [a, b]表示服务器 a和 b之间形成连接
//任何服务器都可以直接或者间接地通过网络到达任何其他服务器。
//"关键连接"是在该集群中的重要连接，也就是说，假如我们将它移除
//便会导致某些服务器无法访问其他服务器。
//请你以任意顺序返回该集群内的所有"关键连接"
//测试链接 : https://leetcode.cn/problems/critical-connections-in-a-network/
public class Code01_CriticalConnectionsInANetwork {
    //记录节点在 DFS 中首次被访问的顺序编号（发现时间），初始为 0 表示未访问。
    public static int[] dfn = new int[100010];
    //记录节点通过非父节点的边能到达的最早发现时间，用于判断是否存在回边。
    public static int[] low = new int[100010];
    //全局计数器，为节点分配唯一的 dfn 编号，从 1 开始递增。
    public static int dfnCnt = 0;

    public static List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        //构建无向图连接表
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (List<Integer> edge : connections) {
            graph.get(edge.get(0)).add(edge.get(1));
            graph.get(edge.get(1)).add(edge.get(0));
        }
        //初始化dfn和low数组
        Arrays.fill(dfn, 0, n, 0);
        Arrays.fill(low, 0, n, 0);
        //先将发现时间计数器重置
        dfnCnt = 0;
        List<List<Integer>> ans = new ArrayList<>();
        //调用Tarjan算法，从节点0开始（父节点为-1，表示无父节点）
        tarjan(0, -1, graph, ans);
        return ans;
    }

    // tarjan  dfs过程
    // 点的编号是cur，dfn X low X
    public static void tarjan(int cur, int father,
                              ArrayList<ArrayList<Integer>> graph,
                              List<List<Integer>> ans) {
        // 第一次来到cur
        // 分配dfn、low序号
        dfn[cur] = low[cur] = ++dfnCnt;
        //遍历cur的所有邻接节点next
        for (Integer next : graph.get(cur)) {
            if (next != father) {
                if (dfn[next] == 0) { // 下级的节点没跑过，就去跑
                    tarjan(next, cur, graph, ans);
                    // 回溯时更新cur的low：取cur当前low和next的low的最小值
                    low[cur] = Math.min(low[cur], low[next]);
                } else { // 下级的节点跑过了，直接更新low
                    low[cur] = Math.min(low[cur], dfn[next]);
                }
            }
        }
        //判断cur到father的边是否为桥
        if (low[cur] == dfn[cur] && cur != 0) {
            ans.add(Arrays.asList(father, cur));
        }
    }
}
