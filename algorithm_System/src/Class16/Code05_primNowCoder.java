package Class16;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Code05_primNowCoder {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
            int n = (int) in.nval;
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            in.nextToken();
            int m = (int) in.nval;
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int A = (int) in.nval;
                in.nextToken();
                int B = (int) in.nval;
                in.nextToken();
                int cost = (int) in.nval;
                graph.get(A).add(new int[]{B, cost});
                graph.get(B).add(new int[]{A, cost});
            }
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> (a[1] - b[1]));
            boolean[] visited = new boolean[n + 1];
            for (int[] edge : graph.get(1)) {
                heap.add(edge);
            }
            visited[1] = true;
            int ans = 0;
            while (!heap.isEmpty()) {
                int[] edge = heap.poll();
                int next = edge[0];
                int cost = edge[1];
                if (!visited[next]) {
                    visited[next] = true;
                    ans += cost;
                    for (int[] e : graph.get(next)) {
                        heap.add(e);
                    }
                }
            }
            out.println(ans);
            out.flush();
        }
    }
}
