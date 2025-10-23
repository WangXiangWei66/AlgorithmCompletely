package class_2021_12_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//给你一个整数 n ，表示有 n 个专家从 0 到 n - 1 编号。
//另外给一个下标从 0 开始的二维整数数组 meetings ，
//其中 meetings[i] = [xi, yi, timei] 表示专家 xi 和专家 yi 在时间 timei 要开一场会。
//一个专家可以同时参加 多场会议 。最后，给你一个整数 firstPerson 。
//专家 0 有一个 秘密 ，最初，他在时间0 将这个秘密分享给了专家 firstPerson 。
//这个秘密会在每次有知晓这个秘密的专家参加会议时进行传播。更正式的表达是，每次会议，
//如果专家 xi 在时间 timei 时知晓这个秘密，那么他将会与专家 yi 分享这个秘密，反之亦然。
//秘密共享是 瞬时发生 的。也就是说，在同一时间，一个专家不光可以接收到秘密，还能在其他会议上与其他专家分享。
//在所有会议都结束之后，返回所有知晓这个秘密的专家列表。你可以按 任何顺序 返回答案。
//Leetcode链接 : https://leetcode.com/problems/find-all-people-with-secret/
public class Code01_FindAllPeopleWithSecret {

    public List<Integer> findAllPeople(int n, int[][] meetings, int firstPerson) {
        UnionFind uf = new UnionFind(n, firstPerson);
        int m = meetings.length;
        Arrays.sort(meetings, (a, b) -> (a[2] - b[2]));
        int[] help = new int[m << 1];//同一时间的会议参与者
        help[0] = meetings[0][0];
        help[1] = meetings[0][1];
        int size = 2;//辅助数组中有效元素的数量
        //将同一时间的会议参与者放入help数组
        for (int i = 1; i < m; i++) {
            if (meetings[i][2] != meetings[i - 1][2]) {
                share(help, size, uf);//处理help数组中的会议
                help[0] = meetings[i][0];
                help[1] = meetings[i][1];
                size = 2;
            } else {
                help[size++] = meetings[i][0];
                help[size++] = meetings[i][1];
            }
        }
        share(help, size, uf);
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (uf.know(i)) {
                ans.add(i);
            }
        }
        return ans;
    }

    public static void share(int[] help, int size, UnionFind uf) {
        for (int i = 0; i < size; i += 2) {
            uf.union(help[i], help[i + 1]);
        }
        for (int i = 0; i < size; i++) {
            if (!uf.know(help[i])) {
                //将不知道秘密的专家隔离
                uf.isolate(help[i]);
            }
        }
    }

    public static class UnionFind {
        public int[] father;
        public boolean[] sect;//每个集合是否知晓秘密
        public int[] help;

        public UnionFind(int n, int first) {
            father = new int[n];
            sect = new boolean[n];
            help = new int[n];
            for (int i = 1; i < n; i++) {
                father[i] = i;
            }
            father[first] = 0;
            sect[0] = true;
        }

        private int find(int i) {
            int hi = 0;
            while (i != father[i]) {
                help[hi++] = i;
                i = father[i];
            }
            for (hi--; hi >= 0; hi--) {
                father[help[hi]] = i;
            }
            return i;
        }

        public void union(int i, int j) {
            int fatheri = find(i);
            int fatherj = find(j);
            if (fatheri != fatherj) {
                sect[fatheri] |= sect[fatherj];
            }
        }
        //专家是否知晓秘密
        public boolean know(int i) {
            return sect[find(i)];
        }
        //使专家成为独立的集合
        public void isolate(int i) {
            father[i] = i;
        }
    }
}
