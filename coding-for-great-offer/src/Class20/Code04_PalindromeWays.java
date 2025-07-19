package Class20;

//给定一个字符串str，当然可以生成很多子序列，返回有多少个子序列是回文子序列，空序列不算回文
//比如，str = “aba”，回文子序列：{a}、{a}、 {a,a}、 {b}、{a,b,a}，返回5
public class Code04_PalindromeWays {
    //暴力解法，时间复杂度为O(2 ^ n * n)
    public static int ways1(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        char[] s = str.toCharArray(); //输入字符串的字符数组
        char[] path = new char[s.length];//存储当前递归路径生成的子序列
        return process(str.toCharArray(), 0, path, 0);
    }

    //递归生成所有的子序列，并在字符串遍历结束时检查是否为回文
    //si：当前处理的字符索引
    //pi：path数组的当前填充位置
    public static int process(char[] s, int si, char[] path, int pi) {
        if (si == s.length) {
            return isP(path, pi) ? 1 : 0;
        }
        int ans = process(s, si + 1, path, pi);//不选择当前的字符
        path[pi] = s[si];//选择当前的字符，并把它加入到正在构建的子序列中
        ans += process(s, si + 1, path, pi + 1);//选择当前的字符
        return ans;
    }

    //本代码用来检查字符数组path从索引0到pi-1的子序列是否构成一个非空的回文串
    public static boolean isP(char[] path, int pi) {
        if (pi == 0) {
            return false;//空序列，直接返回false
        }
        //设置左右指针，分别指向序列的收尾
        int L = 0;
        int R = pi - 1;
        while (L < R) {
            if (path[L++] != path[R--]) {
                return false;
            }
        }
        return true;
    }

    //动态规划，时间复杂度为o(n^2)
    public static int ways2(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        char[] s = str.toCharArray();
        int n = s.length;
        //dp[i][j]:字符串s[i...j]中非空回文子序列的数量
        int[][] dp = new int[n][n];
        //如果两端祖父不相等
        //dp[i+1][j]：包含左边界的回文子序列数。
        //dp[i][j-1]：包含右边界的回文子序列数。
        //dp[i+1][j-1]：被重复计算的中间部分，需减去。

        //如果两端的字符是相等的：dp[i][j] += dp[i+1][j-1] + 1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;//单个字符是一个回文
        }
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = s[i] == s[i + 1] ? 3 : 2;//双字符，如果两个字符相等，则返回三个，否则返回两个
        }
        //下面进行动态规划填表
        for (int L = n - 3; L >= 0; L--) {
            for (int R = L + 2; R < n; R++) {
                // 基础转移：左右部分之和减去重复部分
                dp[L][R] = dp[L + 1][R] + dp[L][R - 1] - dp[L + 1][R - 1];
                // 若两端字符相等，新增中间部分的回文数+1
                if (s[L] == s[R]) {
                    dp[L][R] += dp[L + 1][R - 1] + 1;
                }
            }
        }
        return dp[0][n - 1];//整个字符串的回文字符列数
    }

    public static String randomString(int len, int types) {
        char[] str = new char[len];
        for (int i = 0; i < str.length; i++) {
            str[i] = (char) ('a' + (int) (Math.random() * types));
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int N = 10;
        int types = 5;
        int testTimes = 100000;
        System.out.println("test begin!");
        for (int i = 0; i < testTimes; i++) {
            int len = (int) (Math.random() * N);
            String str = randomString(len, types);
            int ans1 = ways1(str);
            int ans2 = ways2(str);
            if (ans1 != ans2) {
                System.out.println(str);
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println("Ops");
                break;
            }
        }
        System.out.println("test finish!");
    }
}
