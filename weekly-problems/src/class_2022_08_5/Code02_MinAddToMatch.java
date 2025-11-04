package class_2022_08_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//给定一个由 '[' ，']'，'('，‘)’ 组成的字符串
//请问最少插入多少个括号就能使这个字符串的所有括号左右配对
//例如当前串是 "([[])"，那么插入一个']'即可满足
//输出最少插入多少个括号
//测试链接 : https://www.nowcoder.com/practice/e391767d80d942d29e6095a935a5b96b
public class Code02_MinAddToMatch {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;//存储每次读取的一行输入
        while ((line = br.readLine()) != null) {
            System.out.println(minAdd(line.toCharArray()));
        }
    }

    public static int minAdd(char[] s) {
        int n = s.length;
        //dp[l][r]:字符串从索引 l 到 r（闭区间）的子串，最少需要插入多少个括号才能合法
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -1;
            }
        }
        return process(s, 0, s.length - 1, dp);
    }

    public static int process(char[] s, int l, int r, int[][] dp) {

        if(l > r){
            return 0;
        }
        if (l == r) {
            return 1;
        }
        if (l == r - 1) {
            if ((s[l] == '(' && s[r] == ')') || (s[l] == '[' && s[r] == ']')) {
                return 0;
            }
            return 2;
        }
        if (dp[l][r] != -1) {
            return dp[l][r];
        }
        int p1 = 1 + process(s, l + 1, r, dp);
        int p2 = 1 + process(s, l, r - 1, dp);
        int p3 = Integer.MAX_VALUE;
        if ((s[l] == '(' && s[r] == ')') || (s[l] == '[' && s[r] == ']')) {
            p3 = process(s, l + 1, r - 1, dp);
        }
        int ans = Math.min(p1, Math.min(p2, p3));
        for (int split = 1; split < r; split++) {
            ans = Math.min(ans, process(s, l, split, dp) + process(s, split + 1, r, dp));
        }
        dp[l][r] = ans;
        return ans;
    }
}
