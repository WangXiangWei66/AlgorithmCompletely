package class_2022_03_5;

import java.util.Arrays;

//给你一个长度为n的整数数组nums和一个整数numSlots，满足2 * numSlots >= n
//总共有numSlots个篮子，编号为1到numSlots
//你需要把所有n个整数分到这些篮子中，且每个篮子 至多有 2 个整数
//一种分配方案的与和定义为每个数与它所在篮子编号的 按位与运算结果之和。
//比方说，将数字[1, 3]放入篮子1中，[4, 6] 放入篮子2中，
//这个方案的与和为(1 AND 1) + (3 AND 1) + (4 AND 2) + (6 AND 2) = 1 + 1 + 0 + 2 = 4。
//请你返回将 nums中所有数放入numSlots个篮子中的最大与和。
//Leetcode链接：https://leetcode.com/problems/maximum-and-sum-of-array
public class Code03_MaximumAndSumOfArray {

    public static int km(int[][] graph) {
        int N = graph.length;
        int[] match = new int[N];
        int[] lx = new int[N];
        int[] ly = new int[N];
        boolean[] x = new boolean[N];
        boolean[] y = new boolean[N];
        int[] slack = new int[N];
        int invalid = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            match[i] = -1;
            lx[i] = -invalid;
            for (int j = 0; j < N; j++) {
                lx[i] = Math.max(lx[i], graph[i][j]);
            }
            ly[i] = 0;
        }

        for (int from = 0; from < N; from++) {
            for (int i = 0; i < N; i++) {
                slack[i] = invalid;
            }
            Arrays.fill(x, false);
            Arrays.fill(y, false);
            while (!dfs(from, x, y, lx, ly, match, slack, graph)) {
                int d = invalid;
                for (int i = 0; i < N; i++) {
                    if (!y[i] && slack[i] < d) {
                        d = slack[i];
                    }
                }
                for (int i = 0; i < N; i++) {
                    if (x[i]) {
                        lx[i] = lx[i] - d;
                    }
                    if (y[i]) {
                        ly[i] = ly[i] + d;
                    }
                }
                Arrays.fill(x, false);
                Arrays.fill(y, false);
            }
        }
        int ans = 0;
        for (int i = 0; i < N; i++) {
            ans += (lx[i] + ly[i]);
        }
        return ans;
    }

    public static boolean dfs(int from, boolean[] x, boolean[] y, int[] lx, int[] ly, int[] match, int[] slack, int[][] map) {
        int N = map.length;
        x[from] = true;
        for (int to = 0; to < N; to++) {
            if (!y[to]) {
                int d = lx[from] + ly[to] - map[from][to];
                if (d != 0) {
                    slack[to] = Math.min(slack[to], d);
                } else {
                    y[to] = true;
                    //match[to]号节点可以换一个节点匹配
                    if (match[to] == -1 || dfs(match[to], x, y, lx, ly, match, slack, map)) {
                        match[to] = from;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //m:篮子的个数
    public static int maximumANDSum(int[] arr, int m) {
        m <<= 1;//1个篮子最多放两个元素，形成虚拟节点
        //构建m*m的权重矩阵
        int[][] graph = new int[m][m];
        for (int i = 0; i < arr.length; i++) {
            //遍历每个篮子的两个虚拟节点
            for (int j = 0, num = 1; j < m; num++, j += 2) {
                graph[i][j] = arr[i] & num;
                graph[i][j + 1] = arr[i] & num;
            }
        }
        //权重为元素与篮子编号的AND值
        return km(graph);
    }
}
