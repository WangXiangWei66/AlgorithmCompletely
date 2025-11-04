package class_2022_08_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

//给定n棵树，和两个长度为n的数组a和b
//i号棵树的初始重量为a[i]，i号树每天的增长重量为b[i]
//你每天最多能砍1棵树，这天收益 = 砍的树初始重量 + 砍的树增长到这天的总增重
//给定m，表示你有m天，返回m天内你获得的最大收益
//本题测试链接 : https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367873
public class Code04_DreamCity {
    //tree[i][0] = a[i]（初始重量），tree[1][0] = b[i]（日增长）
    public static int[][] tree = new int[250][2];
    //在前i+1棵树中,用j+1天砍树的最大收益
    public static int[][] dp = new int[250][250];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();//读取测试用例数
        int testCases = (int) in.nval;//读取读取的数组
        for (int i = 0; i < testCases; i++) {
            in.nextToken();
            int n = (int) in.nval;//树的数量
            in.nextToken();
            int m = (int) in.nval;//读取天数
            for (int j = 0; j < n; j++) {
                in.nextToken();
                tree[j][0] = (int) in.nval;
            }
            for (int j = 0; j < n; j++) {
                in.nextToken();
                tree[j][1] = (int) in.nval;
            }
            out.println(maxWeight(n, m));
            out.flush();
        }
    }

    // tree[][]
    // i棵树，初始重量 ， tree[i][0]
    // i棵树，每天的增长重量 ，tree[i][1]
    public static int maxWeight(int n, int m) {
        //根据树的增长速度升序排序
        Arrays.sort(tree, 0, n, ((o1, o2) -> o1[1] - o2[1]));
        dp[0][0] = tree[0][0];//只有第0棵树,用1天砍
        //只有一天砍,去找初始重量最大的
        for (int i = 1; i < n; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], tree[i][0]);
        }
        //只砍一颗树
        for (int j = 1; j < m; j++) {
            dp[0][j] = dp[0][j - 1] + tree[0][1];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                // 决策1：不砍第i棵树 → 收益=前i棵树用j+1天的最大收益（dp[i-1][j]）
                // 决策2：砍第i棵树 → 收益=前i棵树用j天的最大收益（dp[i-1][j-1]） + 第i棵树在第j+1天砍的收益
                // 第i棵树在第j+1天砍的收益：初始重量a[i] + 增长j天的增重（b[i]×j）
                dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - 1] + tree[i][0] + tree[i][1] * j);
            }
        }
        return dp[n - 1][m - 1];
    }
}
