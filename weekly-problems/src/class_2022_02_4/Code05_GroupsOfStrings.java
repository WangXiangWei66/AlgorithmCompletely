package class_2022_02_4;

import java.util.HashMap;

//给你一个下标从0开始的字符串数组words。每个字符串都只包含小写英文字母
//words中任意一个子串中，每个字母都至多只出现一次。
//如果通过以下操作之一，我们可以从 s1的字母集合得到 s2的字母集合，那么我们称这两个字符串为 关联的：
//往s1的字母集合中添加一个字母。从s1的字母集合中删去一个字母
//将 s1中的一个字母替换成另外任意一个字母（也可以替换为这个字母本身）。
//数组words可以分为一个或者多个无交集的组。如果一个字符串与另一个字符串关联，那么它们应当属于同一个组
//你需要确保分好组后，一个组内的任一字符串与其他组的字符串都不关联。可以证明在这个条件下，分组方案是唯一的
//请你返回一个长度为 2的数组ans：
//ans[0]是words分组后的总组数。
//ans[1]是字符串数目最多的组所包含的字符串数目。
//leetcode链接 : https://leetcode.com/problems/groups-of-strings/
public class Code05_GroupsOfStrings {

    public static int[] groupStrings1(String[] words) {
        int n = words.length;
        UnionFind uf = new UnionFind(n);//初始化并查集，管理n个字符串的分组
        int[] strs = new int[n];//记录每个字符串的字符状态
        //记录每个状态对应的首个字符串索引
        HashMap<Integer, Integer> stands = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int status = 0;
            for (char c : words[i].toCharArray()) {
                status |= 1 << (c - 'a');
            }
            strs[i] = status;//保存当前字符串的位掩码
            if (stands.containsKey(status)) {
                uf.union(stands.get(status), i);
            } else {
                stands.put(status, i);
            }
        }
        //添加、删除、替换字符
        for (int i = 0; i < n; i++) {
            int status = strs[i];
            //添加一个字符
            for (int j = 0; j < 26; j++) {
                uf.union(i, stands.get(status | (1 << j)));
            }
            //删除一个字符
            for (int j = 0; j < 26; j++) {
                if ((status & (1 << j)) != 0) {
                    uf.union(i, stands.get(status ^ (1 << j)));
                }
            }
            //替换一个字符
            for (int has = 0; has < 26; has++) {
                if ((status & (1 << has)) != 0) {
                    status ^= 1 << has;
                    for (int replace = 0; replace < 26; replace++) {
                        uf.union(i, stands.get(status | (1 << replace)));
                    }
                    status |= 1 << has;//恢复原状态，进行下一轮的替换
                }
            }
        }
        return new int[]{uf.sets(), uf.maxSize()};
    }


    public static class UnionFind {
        private int[] parent;
        private int[] size;//记录每个集合的大小
        private int[] help;//路径压缩的辅助数组

        public UnionFind(int N) {
            parent = new int[N];
            size = new int[N];
            help = new int[N];
            for (int i = 0; i < N; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        private int find(int i) {
            int hi = 0;
            while (i != parent[i]) {
                help[hi++] = i;
                i = parent[i];
            }
            for (hi--; hi >= 0; hi--) {
                parent[help[hi]] = i;
            }
            return i;
        }

        public void union(Integer i, Integer j) {
            if (i == null || j == null) {
                return;
            }
            int f1 = find(i);
            int f2 = find(j);
            if (f1 != f2) {
                if (size[f1] >= size[f2]) {
                    size[f1] += size[f2];
                    parent[f2] = f1;
                } else {
                    size[f2] = size[f1];
                    parent[f1] = f2;
                }
            }
        }

        //统计集合数量（每个集合中根节点的数量）
        public int sets() {
            int ans = 0;
            for (int i = 0; i < parent.length; i++) {
                ans += parent[i] == i ? 1 : 0;
            }
            return ans;
        }

        //查询最大集合的大小
        public int maxSize() {
            int ans = 0;
            for (int s : size) {
                ans = Math.max(ans, s);
            }
            return ans;
        }
    }

    public static int[] groupStrings2(String[] words) {
        int n = words.length;
        UnionFind uf = new UnionFind(n);
        int[] strs = new int[n];
        HashMap<Integer, Integer> stands = new HashMap<>();//状态→首个索引映射
        for (int i = 0; i < n; i++) {
            int status = 0;
            //将字符串转化为位掩码
            for (char c : words[i].toCharArray()) {
                status |= 1 << (c - 'a');
            }
            strs[i] = status;
            //相同的字符串直接合并
            if (stands.containsKey(status)) {
                uf.union(stands.get(status), i);
            } else {
                stands.put(status, i);
            }
        }
        for (int i = 0; i < n; i++) {
            int yes = strs[i];//当前字符串的位掩码
            int no = (~yes) & ((1 << 26) - 1);//当前字符串中不包含的字符
            int tmpYes = yes;
            int tmpNo = no;
            int rightOneYes = 0;
            int rightOneNo = 0;
            while (tmpYes != 0) {
                rightOneYes = tmpYes & (-tmpYes);//提取最右侧的1
                uf.union(i, stands.get(yes ^ rightOneYes));//删除该字符后的状态
                tmpYes ^= rightOneYes;//将已处理的1清除掉
            }
            while (tmpNo != 0) {
                rightOneNo = tmpNo & (-tmpNo);
                uf.union(i, stands.get(yes | rightOneNo));
                tmpNo ^= rightOneNo;
            }
            //替换一个字符
            tmpYes = yes;
            while (tmpYes != 0) {
                rightOneYes = tmpYes & (-tmpYes);//提取要删除的字符
                tmpNo = no;
                while (tmpNo != 0) {
                    rightOneNo = tmpNo & (-tmpNo);//提取要添加的字符
                    uf.union(i, stands.get((yes ^ rightOneYes) | rightOneNo));
                    tmpNo ^= rightOneNo;
                }
                tmpYes ^= rightOneYes;
            }
        }
        return new int[]{uf.sets(), uf.maxSize()};
    }
}
