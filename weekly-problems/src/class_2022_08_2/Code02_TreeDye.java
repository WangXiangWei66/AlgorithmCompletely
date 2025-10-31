package class_2022_08_2;

import java.util.ArrayList;
import java.util.Arrays;

//来自米哈游
//给定一个正数n，表示有多少个节点
//给定一个二维数组edges，表示所有无向边
//edges[i] = {a, b} 表示a到b有一条无向边
//edges一定表示的是一个无环无向图，也就是树结构
//每个节点可以染1、2、3三种颜色
//要求 : 非叶节点的相邻点一定要至少有两种和自己不同颜色的点
//返回一种达标的染色方案，也就是一个数组，表示每个节点的染色状况
//1 <= 节点数量 <= 10的5次方
public class Code02_TreeDye {
    //定义染色规则
    public static int[] rule1 = {1, 2, 3};

    public static int[] rule2 = {1, 3, 2};

    public static int[] dye(int n, int[][] edges) {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        //添加无向边
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        //寻找一个非叶节点作为根节点
        int head = -1;
        for (int i = 0; i < n; i++) {
            if (graph.get(i).size() >= 2) {
                head = i;
                break;
            }
        }
        //初始化颜色数组
        int[] colors = new int[n];
        if (head == -1) {
            Arrays.fill(colors, 1);
        } else {
            colors[head] = 1;
            dfs(graph, graph.get(head).get(0), 1, rule1, colors);
            //其他子节点使用规则2染色
            for (int i = 1; i < graph.get(head).size(); i++) {
                dfs(graph, graph.get(head).get(i), 1, rule2, colors);
            }
        }
        return colors;
    }

    //level:当前深度
    public static void dfs(ArrayList<ArrayList<Integer>> graph, int head, int level, int[] rule, int[] colors) {
        //根据当前深度与染色规则确定颜色
        colors[head] = rule[level % 3];
        for (int next : graph.get(head)) {
            if (colors[next] == 0) {
                dfs(graph, next, level + 1, rule, colors);
            }
        }
    }

    public static int[][] randomEdges(int n) {
        //用于存储节点的编号
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        for (int i = n - 1; i >= 0; i--) {
            swap(order, i, (int) (Math.random() * (i + 1)));
        }
        // 树有n-1条边，创建存储边的数组
        int[][] edges = new int[n - 1][2];
        for (int i = 1; i < n; i++) {
            edges[i - 1][0] = order[i];
            edges[i - 1][1] = order[(int) (Math.random() * i)];
        }
        return edges;
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static boolean rightAnswer(int n, int[][] edges, int[] colors) {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        //记录颜色是否出现(0不用)
        boolean[] hasColors = new boolean[4];
        //遍历每个节点检查是否符合染色要求
        for (int i = 0, colorCnt = 1; i < n; i++, colorCnt = 1) {
            if (colors[i] == 0) {
                return false;
            }
            //叶子节点不需要检查
            if (graph.get(i).size() <= 1) {
                continue;
            }
            hasColors[colors[i]] = true;
            for (int near : graph.get(i)) {
                if (!hasColors[colors[near]]) {
                    hasColors[colors[near]] = true;
                    colorCnt++;
                }
            }
            if (colorCnt != 3) {
                return false;
            }
            Arrays.fill(hasColors, false);
        }
        return true;
    }

    public static void main(String[] args) {
//		int n = 7;
//		int[][] edges = randomEdges(n);
//		for (int[] edge : edges) {
//			System.out.println(edge[0] + " , " + edge[1]);
//		}

        int N = 100;
        int testTimes = 1000;
        System.out.println("测试开始");
        for (int i = 0; i < testTimes; i++) {
            int n = (int) (Math.random() * N) + 1;
            int[][] edges = randomEdges(n);
            int[] ans = dye(n, edges);
            if (!rightAnswer(n, edges, ans)) {
                System.out.println("出错了");
            }
        }
        System.out.println("测试结束");
    }
}
