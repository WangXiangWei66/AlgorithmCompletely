package class_2022_03_4;
//拓扑排序专门用来解决有向无环图（DAG）
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

//来自理想汽车
//a -> b，代表a在食物链中被b捕食
//给定一个有向无环图，返回
//这个图中从最初级动物到最顶级捕食者的食物链有几条
//线上测试链接 : https://www.luogu.com.cn/problem/P4017
public class Code05_HowManyWaysFromBottomToTop {

    public static int[] in = new int[5001];//每个节点的入度
    public static boolean[] out = new boolean[5001];//节点是否有出度
    public static int[] lines = new int[5001];//到每个节点的路径数
    public static int[] headEdge = new int[5001];//邻接表头指针
    public static int[] queue = new int[5001];//拓扑排序用的队列
    public static int mod = 80112002;//结果取模值
    public static int n = 0;//节点总数

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer pin = new StreamTokenizer(br);
        PrintWriter pout = new PrintWriter(new OutputStreamWriter(System.out));
        while (pin.nextToken() != StreamTokenizer.TT_EOF) {
            n = (int) pin.nval;
            pin.nextToken();
            int m = (int) pin.nval;//读取边数
            //采用邻接表的链式存储来存储边的信息
            int[] preEdge = new int[m + 1];//记录前一条边的索引
            int[] edgesTo = new int[m + 1];//边指向的节点
            for (int i = 1; i <= m; i++) {
                pin.nextToken();
                int from = (int) pin.nval;
                pin.nextToken();
                int to = (int) pin.nval;
                edgesTo[i] = to;
                preEdge[i] = headEdge[from];
                headEdge[from] = i;//更新起点的表头为当前边
                out[from] = true;
                in[to]++;
            }
            pout.println(howManyWays(preEdge, edgesTo));
            pout.flush();
        }
    }

    public static int howManyWays(int[] preEdge, int[] edgesTo) {
        int ql = 0;
        int qr = 0;
        for (int i = 1; i <= n; i++) {
            if (in[i] == 0) {
                queue[qr++] = i;
                lines[i] = 1;
            }
        }
        //拓扑排序开始
        while (ql < qr) {
            int cur = queue[ql++];
            //获取当前节点的第一条边
            int edge = headEdge[cur];
            while (edge != 0) {
                int next = edgesTo[edge];
                lines[next] = (lines[next] + lines[cur]) % mod;
                if (--in[next] == 0) {
                    queue[qr++] = next;
                }
                edge = preEdge[edge];
            }
        }
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            if (!out[i]) {
                ans = (ans + lines[i]) % mod;
            }
        }
        return ans;
    }
}
