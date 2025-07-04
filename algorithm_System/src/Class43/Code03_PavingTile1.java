package Class43;

import java.io.*;

public class Code03_PavingTile1 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int m = (int) in.nval;
            if (n != 0 || m != 0) {
                long ans = ways(n, m);
                out.println(ans);
                out.flush();
            }
        }
    }

    public static long ways(int N, int M) {
        if (N < 1 || M < 1 || ((N * M) & 1) != 0) {
            return 0;
        }
        if (N == 1 || M == 1) {
            return 1;
        }
        int big = N > M ? N : M;
        int small = big == N ? M : N;
        int sn = 1 << small;
        int limit = sn - 1;
        long[] dp = new long[sn];
        dp[limit] = 1;
        long[] cur = new long[sn];
        for (int level = 0; level < big; level++) {
            for (int status = 0; status < sn; status++) {
                if (dp[status] != 0) {
                    int op = (~status) & limit;
                    dfs(dp[status], op, 0, small - 1, cur);
                }
            }
            for (int i = 0; i < sn; i++) {
                dp[i] = 0;
            }
            long[] tmp = dp;
            dp = cur;
            cur = tmp;
        }
        return dp[limit];
    }

    public static void dfs(long ways, int op, int index, int end, long[] cur) {
        if (index == end) {//op为当前行要填充的状态
            cur[op] += ways;
        } else {
            dfs(ways, op, index + 1, end, cur);
            if (((3 << index) & op) == 0) {
                dfs(ways, op | (3 << index), index + 1, end, cur);
            }
        }
    }
}
