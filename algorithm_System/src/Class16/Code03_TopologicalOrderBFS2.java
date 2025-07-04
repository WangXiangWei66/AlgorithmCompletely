package Class16;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Code03_TopologicalOrderBFS2 {

    public static int MAXN = 200001;

    public static int[] queue = new int[MAXN];

    public static int[] inDegree = new int[MAXN];

    public static int[] ans = new int[MAXN];

    public static int n, m, from, to;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            n = (int) in.nval;
            in.nextToken();
            m = (int) in.nval;
            ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int i = 0; i < m; i++) {
                in.nextToken();
                from = (int) in.nval;
                in.nextToken();
                to = (int) in.nval;
                graph.get(from).add(to);
            }
            if (!topoSort(graph)) {
                out.println(-1);
            } else {
                for (int i = 0; i < n - 1; i++) {
                    out.print(ans[i] + " ");
                }
                out.println(ans[n - 1]);
            }
            out.flush();
        }
    }

    //有拓扑排序返回true

    /// 无拓扑排序返回false
    public static boolean topoSort(ArrayList<ArrayList<Integer>> graph) {
        Arrays.fill(inDegree, 1, n + 1, 0);
        for (ArrayList<Integer> nexts : graph) {
            for (int next : nexts) {
                inDegree[next]++;
            }
        }
        int l = 0;
        int r = 0;
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                queue[r++] = i;
            }
        }
        int cnt = 0;
        while (l < r) {
            int cur = queue[l++];
            ans[cnt++] = cur;
            for (int next : graph.get(cur)) {
                if (--inDegree[next] == 0) {
                    queue[r++] = next;
                }
            }
        }
        return cnt == n;
    }
}
