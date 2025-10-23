package class_2021_12_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

//有m个同样的苹果，认为苹果之间无差别
//有n个同样的盘子，认为盘子之间也无差别
//还有，比如5个苹果如果放进3个盘子，
//那么1、3、1和1、1、3和3、1、1的放置方法，也认为是一种方法
//如上的设定下，返回有多少种放置方法
public class Code05_SplitApples {

    public static int ways1(int apples, int plates) {
        return process1(1, apples, plates);
    }

    public static int process1(int pre, int apples, int plates) {
        if (apples == 0) {
            return 1;
        }
        if (plates == 0) {
            return 0;
        }
        if (pre > apples) {
            return 0;
        }
        int ways = 0;
        for (int cur = pre; cur <= apples; cur++) {
            ways += process1(cur, apples - cur, plates - 1);
        }
        return ways;
    }

    public static int ways2(int apples, int plates) {
        if (apples == 0) {
            return 1;
        }
        if (plates == 0) {
            return 0;
        }
        if (plates > apples) {
            return ways2(apples, apples);
        } else {
            return ways2(apples, plates - 1) + ways2(apples - plates, plates);
        }
    }

    public static int[][] dp = null;

    public static int ways3(int apples, int plates) {
        if (dp == null) {
            dp = new int[11][11];
            for (int i = 0; i <= 10; i++) {
                Arrays.fill(dp[i], -1);
            }
        }
        return process3(apples, plates, dp);
    }

    public static int process3(int apples, int plates, int[][] dp) {
        if (dp[apples][plates] != -1) {
            return dp[apples][plates];
        }
        int ans = 0;
        if (apples == 0) {
            ans = 1;
        } else if (plates == 0) {
            ans = 0;
        } else if (plates > apples) {
            ans = process3(apples, apples, dp);
        } else {
            ans = process3(apples, plates - 1, dp) + process3(apples - plates, plates, dp);
        }
        dp[apples][plates] = ans;
        return ans;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int m = (int) in.nval;
            in.nextToken();
            int n = (int) in.nval;
            out.println(ways3(m, n));
            out.flush();
        }
    }
}
