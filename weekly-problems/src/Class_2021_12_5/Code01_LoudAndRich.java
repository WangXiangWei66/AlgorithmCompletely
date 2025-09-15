package Class_2021_12_5;

import java.util.ArrayList;//用于构建邻接表

//有一组 n 个人作为实验对象，从 0 到 n - 1 编号，其中每个人都有不同数目的钱，
//以及不同程度的安静值（quietness）
//为了方便起见，我们将编号为x的人简称为 "personx"。
//给你一个数组 richer ，其中 richer[i] = [ai, bi] 表示 personai比 personbi更有钱
//另给你一个整数数组 quiet ，其中quiet[i] 是 person i 的安静值
//richer 中所给出的数据 逻辑自洽
//也就是说，在 person x 比 person y 更有钱的同时，不会出现 person y 比 person x 更有钱的情况
//现在，返回一个整数数组 answer 作为答案，其中answer[x] = y的前提是：
//在所有拥有的钱肯定不少于personx的人中，person是最安静的人（也就是安静值quiet[y]最小的人）。
//leetcode链接 : https://leetcode.com/problems/loud-and-rich/
public class Code01_LoudAndRich {

    public static int[] loudAndRich(int[][] richer, int[] quiet) {
        int N = quiet.length;
        //存储更富有的人指向较不富有的人的关系
        ArrayList<ArrayList<Integer>> nexts = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            nexts.add(new ArrayList<>());
        }
        //存储每个节点有多少个更富有的人
        int[] degree = new int[N];
        for (int[] r : richer) {
            nexts.get(r[0]).add(r[1]);
            degree[r[1]]++;
        }
        //存储最富有的那些人
        int[] zeroQueue = new int[N];
        int l = 0;
        int r = 0;
        for (int i = 0; i < N; i++) {
            if (degree[i] == 0) {
                zeroQueue[r++] = i;
            }
        }
        //初始化答案数组，每个人的刚开始都是自己
        int[] ans = new int[N];
        for (int i = 0; i < N; i++) {
            ans[i] = i;
        }
        while (l < r) {
            int cur = zeroQueue[l++];//取出队头元素，并移动队头指针
            for (int next : nexts.get(cur)) {
                if (quiet[ans[next]] > quiet[ans[cur]]) {
                    ans[next] = ans[cur];
                }
                if (--degree[next] == 0) {
                    zeroQueue[r++] = next;
                }
            }
        }
        return ans;
    }
}
