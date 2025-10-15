package class_2022_05_3;

import java.util.Arrays;

//来自学员问题
//为了给刷题的同学一些奖励，力扣团队引入了一个弹簧游戏机
//游戏机由 N 个特殊弹簧排成一排，编号为 0 到 N-1
//初始有一个小球在编号 0 的弹簧处。若小球在编号为 i 的弹簧处
//通过按动弹簧，可以选择把小球向右弹射jump[i] 的距离，或者向左弹射到任意左侧弹簧的位置
//也就是说，在编号为 i 弹簧处按动弹簧，
//小球可以弹向 0 到 i-1 中任意弹簧或者 i+jump[i] 的弹簧（若 i+jump[i]>=N ，则表示小球弹出了机器）
//小球位于编号 0 处的弹簧时不能再向左弹。
//为了获得奖励，你需要将小球弹出机器。
//请求出最少需要按动多少次弹簧，可以将小球从编号 0 弹簧弹出整个机器，即向右越过编号 N-1 的弹簧。
//测试链接 : https://leetcode-cn.com/problems/zui-xiao-tiao-yue-ci-shu/
public class Code04_MinJumpUsePre {

    public int minJump(int[] jump) {
        int n = jump.length;
        //用数组实现队列，存储待处理的弹簧的位置
        int[] queue = new int[n];
        int l = 0;
        int r = 0;
        //初始时，小球在0位置
        queue[r++] = 0;
        IndexTree it = new IndexTree(n);
        //初始，1~n-1号弹簧都是为访问过的
        for (int i = 1; i < n; i++) {
            it.add(i, 1);
        }
        int step = 0;//按动次数
        //当前步骤可达的所有位置
        while (l != r) {
            int tmp = r;//当前轮次的队列尾
            for (; l < tmp; l++) {
                int cur = queue[l];
                int forward = cur + jump[cur];
                if (forward >= n) {
                    return step + 1;
                }
                //目标位置没有访问过
                if (it.value(forward) != 0) {
                    queue[r++] = forward;
                    it.add(forward, -1);
                }
                //往左弹射
                while (it.sum(cur - 1) != 0) {
                    //最左侧的未访问位置
                    int find = find(it, cur - 1);
                    it.add(find, -1);
                    queue[r++] = find;
                }
            }
            step++;
        }
        return -1;
    }

    // 在[0, right]范围内找到第一个未访问的位置（二分查找）
    public static int find(IndexTree it, int right) {
        int left = 0;
        int mid = 0;
        int find = 0;
        while (left <= right) {
            mid = (left + right) / 2;
            if (it.sum(mid) > 0) {
                find = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return find;
    }


    //解决前缀和查询和单点更新
    public static class IndexTree {
        //存储实际数量的树的底层数组
        private int[] tree;
        private int N;

        public IndexTree(int size) {
            N = size;
            tree = new int[N + 1];
        }

        //获取指定索引位置的值
        public int value(int index) {
            if (index == 0) {
                return sum(0);
            } else {
                return sum(index) - sum(index - 1);
            }
        }

        //计算前i个元素的前缀和
        public int sum(int i) {
            //转化为索引树的内部索引
            int index = i + 1;
            int ret = 0;
            while (index > 0) {
                //减去二进制中最后一个1所代表的值
                ret += tree[index];
                index -= index & -index;
            }
            return ret;
        }

        public void add(int i, int d) {
            int index = i + 1;
            while (index <= N) {
                tree[index] += d;
                index += index & -index;
            }
        }
    }

    public int minJump2(int[] jump) {
        int N = jump.length;
        int ans = N;
        //从0号弹簧往右跳
        int next = jump[0];
        if (next >= N) {
            return 1;
        }
        if (next + jump[next] >= N) {
            return 2;
        }
        //dp[i]:到达i号弹簧所需的最小步数
        int[] dp = new int[N + 1];
        Arrays.fill(dp, N);
        //dis数组记录每步能到达的最远距离
        int[] dis = new int[N];
        dis[1] = next;
        dis[2] = next + jump[next];
        dp[next + jump[next]] = 2;
        int step = 1;//当前步数
        for (int i = 1; i < N; i++) {
            //当前位置超出了当前步数能到达的最远距离
            if (i > dis[step]) {
                step++;
            }
            dp[i] = Math.min(dp[i], step + 1);
            next = i + jump[i];
            if (next >= N) {
                ans = Math.min(ans, dp[i] + 1);
            //更新目标位置的最小步数
            } else if (dp[next] > dp[i] + 1) {
                dp[next] = dp[i] + 1;
                dis[dp[next]] = Math.max(dis[dp[next]], next);
            }
        }
        return ans;
    }
}
