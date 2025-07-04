package Class14;

import java.io.*;

public class Code06_UnionFindArray {

    public static int MAXN = 10000001;

    public static int[] father = new int[MAXN];

    public static int[] size = new int[MAXN];

    public static int[] help = new int[MAXN];

    public static void init(int n) {
        for (int i = 0; i <= n; i++) {
            father[i] = i;
            size[i] = 1;
        }
    }

    public static int find(int i) {
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

    public static boolean isSameSet(int x, int y) {
        return find(x) == find(y);
    }

    public static void union(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx != fy) {
            if (size[fx] >= size[fy]) {
                size[fx] += size[fy];
                father[fy] = fx;
            } else {
                size[fy] += size[fx];
                father[fx] = fy;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            init(n);
            in.nextToken();
            int m = (int) in.nval;
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int op = (int) in.nval;
                in.nextToken();
                int x = (int) in.nval;
                in.nextToken();
                int y = (int) in.nval;
                if (op == 1) {
                    out.println(isSameSet(x, y) ? "yes" : "no");
                    out.flush();
                } else {
                    union(x, y);
                }
            }
        }
    }
}
