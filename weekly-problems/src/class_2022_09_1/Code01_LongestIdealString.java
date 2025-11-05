package class_2022_09_1;

//给你一个由小写字母组成的字符串 s ，和一个整数 k
//如果满足下述条件，则可以将字符串 t 视作是 理想字符串 ：
//t 是字符串 s 的一个子序列。
//t 中每两个 相邻 字母在字母表中位次的绝对差值小于或等于 k 。
//返回 最长 理想字符串的长度。
//字符串的子序列同样是一个字符串，并且子序列还满足：
//可以经由其他字符串删除某些字符（也可以不删除）但不改变剩余字符的顺序得到。
//注意：字母表顺序不会循环
//例如，'a' 和 'z' 在字母表中位次的绝对差值是 25，而不是 1 。
//测试链接 : https://leetcode.cn/problems/longest-ideal-subsequence/
public class Code01_LongestIdealString {
    // 二维动态规划的解
    // N为字符串长度，E为字符集大小，K为差值要求
    // 时间复杂度O(N*E)
    // 空间复杂度O(N*E)
    public static int longestIdealString1(String s, int k) {
        int n = s.length();
        //将字符串s转化为整数数组arr
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = s.charAt(i) - 'a';
        }
        //i表示当前处理到字符串的第i个字符
        //p表示上一个选中的字符的位次
        int[][] dp = new int[n][26];//dp[i][p]表示 “从第i个字符开始，前置字符为p时，后续能找到的最长理想子序列长度”。
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= 26; j++) {
                dp[i][j] = -1;
            }
        }
        //p=26:表示无前置选中字符
        return f(arr, 0, 26, k, dp);
    }

    // 数组s中所有的值都在0~25对应a~z
    // 当前在s[i...]选择数字, 并且前一个数字是p
    // 如果p<26，说明选择的前一个数字是p
    // 如果p==26，说明之前没有选过任何数字
    // 返回在前一个数字是p的情况下，在s[i...]上选择数字，最长理想子序列能是多长
    // dp仅仅是缓存结构，暴力递归改动态规划常规技巧

    //s:转化后的字符位次数组
    //i:当前处理到的索引
    //p:上一个选中字符的位次
    //k:允许的最大位次差
    public static int f(int[] s, int i, int p, int k, int[][] dp) {
        if (i == s.length) {
            return 0;
        }
        if (dp[i][p] != -1) {
            return dp[i][p];
        }
        //不选当前字符
        int p1 = f(s, i + 1, p, k, dp);
        int p2 = 0;
        if (p == 26 || Math.abs(s[i] - p) <= k) {
            p2 = 1 + f(s, i + 1, s[i], k, dp);
        }
        int ans = Math.max(p1, p2);
        dp[i][p] = ans;
        return ans;
    }

    // 一维动态规划从左往右递推版
    // N为字符串长度，E为字符集大小，K为差值要求
    // 时间复杂度O(N*K)
    // 空间复杂度O(E)

    public static int longestIdealString2(String s, int k) {
        //dp[p]:截止当前处理的所有字符,以p结尾的最长理想子序列长度
        int[] dp = new int[26];
        //c:当前处理字符的字母位次
        //l:当前字符允许相邻的左边界
        //r:当前字符允许相邻的右边界
        //pre:在[l,r]范围内,所有字符结尾的最长子序列长度
        //ans:全局最长理想子序列长度
        int c, l, r, pre, ans = 0;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i) - 'a';
            l = Math.max(c - k, 0);
            r = Math.min(c + k, 25);
            pre = 0;
            for (int j = l; j <= r; j++) {
                pre = Math.max(pre, dp[j]);
            }
            dp[c] = l + pre;
            ans = Math.max(ans, dp[c]);
        }
        return ans;
    }

    // 从左往右递推 + 线段树优化
    // N为字符串长度，E为字符集大小，K为差值要求
    // 时间复杂度O(N * logE)
    // 空间复杂度O(E)

    public static class SegmentTree {
        private int n;//线段树区间的长度
        private int[] max;//存储每个节点对应区间的最大值

        public SegmentTree(int maxSize) {
            n = maxSize + 1;
            max = new int[n << 2];
        }

        public void update(int index, int c) {
            update(index, index, c, 1, n, 1);
        }

        public int max(int left, int right) {
            return max(left, right, 1, n, 1);
        }

        private void pushUp(int rt) {
            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }

        private void update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && r <= R) {
                max[rt] = C;
                return;
            }
            int mid = (l + r) >> 1;
            if (L <= mid) {
                update(L, R, C, l, mid, rt << 1);
            }
            if (R > mid) {
                update(L, R, C, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        private int max(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return max[rt];
            }
            int mid = (l + r) >> 1;
            int ans = 0;
            if (L <= mid) {
                ans = Math.max(ans, max(L, R, l, mid, rt << 1));
            }
            if (R > mid) {
                ans = Math.max(ans, max(L, R, mid + 1, r, rt << 1 | 1));
            }
            return ans;
        }
    }

    public static int longestIdealString3(String s, int k) {
        SegmentTree st = new SegmentTree(26);
        //c:当前字符的位次
        //pre:当前要查询的区间
        //ans:全局最长理想子序列的长度
        int c, pre, ans = 0;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i) - 'a' + 1;//+1是为了适配线段树的索引
            pre = st.max(Math.max(c - k, 1), Math.min(c + k, 26));
            ans = Math.max(ans, 1 + pre);
            st.update(c, 1 + pre);
        }
        return ans;
    }
}
