package class_2022_10_1;

import java.util.HashMap;

//来自美团
//两种颜色的球，蓝色和红色，都按1～n编号，共计2n个
//为方便放在一个数组中，红球编号取负，篮球不变，并打乱顺序，
//要求同一种颜色的球按编号升序排列，可以进行如下操作：
//交换相邻两个球，求最少操作次数。
//[3,-3,1,-4,2,-2,-1,4]
//最终交换结果为
//[1,2,3,-1,-2,-3,-4,4]
//最少交换次数为10
//n <= 1000
//递归+回溯+树状数组(使用贪心选择+代价计算)
public class Code04_TwoTeamsSortedMinSwap {

    // [3,-3,1,-4,2,-2,-1,4]
    //    -3   -4   -2 -1   -> -1 -2 -3 -4
    //  3    1    2       4 ->  1  2  3  4

    // 这个题写对数器太麻烦了
    // 所以这就是正式解
    public static int minSwaps(int[] arr) {
        int n = arr.length;
        //键为篮球的编号,值为该编号在数组中的下标
        HashMap<Integer, Integer> map = new HashMap<>();
        //篮球和红球的最大编号
        int topA = 0;
        int topB = 0;
        //寻找两个球的最大编号(负数取绝对值)
        for (int i = 0; i < n; i++) {
            if (arr[i] > 0) {
                topA = Math.max(topA, arr[i]);
            } else {
                topB = Math.max(topB, Math.abs(arr[i]));
            }
            //记录每个球的原始下标
            map.put(arr[i], i);
        }
        IndexTree it = new IndexTree(n);
        //初始化树状数组的值为 1 ,表示未占用
        for (int i = 0; i < n; i++) {
            it.add(i, 1);
        }
        return f(topA, topB, it, n - 1, map);
    }

    // 可以改二维动态规划！
    // 因为it的状态，只由topA和topB决定
    // 所以it的状态不用作为可变参数！
    // TopA:当前剩余位置篮球的最大编号
    //TopB:当前剩余位置红球的最大编号
    public static int f(int topA, int topB,
                        IndexTree it, int end,
                        HashMap<Integer, Integer> map) {
        if (topA == 0 && topB == 0) {
            return 0;
        }
        int p1 = Integer.MAX_VALUE;
        int p2 = Integer.MAX_VALUE;
        int index, cost, next;
        if (topA != 0) {
            //获取TopA号篮球的原始下标
            index = map.get(topA);
            //移动代价
            cost = it.sum(index, end) - 1;
            //标记该位置已经被占用
            it.add(index, -1);
            //处理下一个篮球
            next = f(topA - 1, topB, it, end, map);
            it.add(index, 1);
            p1 = cost + next;
        }
        if (topB != 0) {
            index = map.get(-topB);
            cost = it.sum(index, end) - 1;
            it.add(index, -1);
            next = f(topA, topB - 1, it, end, map);
            it.add(index, 1);
            p2 = cost + next;
        }
        return Math.min(p1, p2);
    }

    public static class IndexTree {
        //树状数组的下标要保证从1开始
        private int[] tree;
        private int N;

        public IndexTree(int size) {
            N = size;
            tree = new int[N + 1];
        }
        //数组中「下标为 i」的位置增加 v（单点更新）
        public void add(int i, int v) {
            //树状数组的下标是从1开始的
            i++;
            while (i <= N) {
                tree[i] += v;
                //找到当前节点的父节点
                i += i & -i;
            }
        }

        public int sum(int l, int r) {
            return l == 0 ? sum(r + 1) : (sum(r + 1) - sum(l));
        }
        //前缀和查询方法
        private int sum(int index) {
            int ans = 0;
            while (index > 0) {
                ans += tree[index];
                index -= index & -index;
            }
            return ans;
        }

    }

    public static void main(String[] args) {
        int[] arr = { 3, -3, 1, -4, 2, -2, -1, 4 };
        System.out.println(minSwaps(arr));
    }
}
