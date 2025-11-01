package class_2022_08_2;
/*
算法的时间复杂度 O (N (logN)²)，我们需要从分治策略的递归关系入手，结合数学归纳法进行证明。以下是详细推导过程：
一、算法核心步骤与递归关系
该算法解决「平面最近点对」问题，核心是分治策略，步骤可概括为：
拆分：将点集按 x 坐标排序后，递归处理左半部分和右半部分。
合并：计算左右两部分的最短距离，再检查跨两部分的点对是否存在更短距离（这一步需对候选点按 y 排序，再逐个比较）。
设总点数为 N，算法的时间复杂度函数为 T (N)，则递归关系为：
plaintext
T(N) = 2·T(N/2) + O(N logN)
2·T(N/2)：递归处理左右两个子问题（每个子问题规模为 N/2）。
O(N logN)：合并步骤的代价（包括收集候选点、按 y 排序、检查跨区间点对）。
二、递归关系的展开与求和
为简化分析，我们忽略低阶项和常数系数，将递归关系改写为：
plaintext
T(N) = 2·T(N/2) + N logN  （N ≥ 2）
T(1) = O(1)  （基线条件：单点无距离计算）
我们通过递归树展开或迭代法求解该递归关系。这里用迭代法：
第一层展开（N 规模）：
plaintext
T(N) = 2·T(N/2) + N logN
第二层展开（代入 T (N/2)）：
plaintext
T(N/2) = 2·T(N/4) + (N/2) log(N/2)
代入得：T(N) = 2·[2·T(N/4) + (N/2) log(N/2)] + N logN
             = 2²·T(N/4) + N log(N/2) + N logN
第三层展开（代入 T (N/4)）：
plaintext
T(N/4) = 2·T(N/8) + (N/4) log(N/4)
代入得：T(N) = 2³·T(N/8) + N log(N/4) + N log(N/2) + N logN
第 k 层展开：经过 k 次展开后，式子变为：
plaintext
T(N) = 2ᵏ·T(N/2ᵏ) + N·[ log(N/2ᵏ⁻¹) + log(N/2ᵏ⁻²) + ... + logN ]
终止条件：当 N/2ᵏ = 1 时（即 k = log₂N），递归终止，此时 2ᵏ = N，T (N/2ᵏ) = T (1) = O (1)，因此：
plaintext
T(N) = N·O(1) + N·[ log(N/2ᵏ⁻¹) + ... + logN ]
三、求和项的化简
核心是计算求和项 S = log (N/2ᵏ⁻¹) + log (N/2ᵏ⁻²) + ... + logN，其中 k = log₂N。
将 k = log₂N 代入，求和项的项数为 k = log₂N，每项可改写为：
第 1 项：log (N/2ᵏ⁻¹) = logN - (k-1) log2 （因为 log (a/b) = loga - logb，且 log2ᵐ = m log2）
第 2 项：log (N/2ᵏ⁻²) = logN - (k-2) log2
...
第 k 项：logN = logN - 0・log2
因此求和项 S 可表示为：
plaintext
S = [logN - (k-1) log2] + [logN - (k-2) log2] + ... + [logN - 0·log2]
  = k·logN - log2·[0 + 1 + 2 + ... + (k-1)]
其中：
首项 k・logN 是 k 个 logN 相加。
末项是 log2 乘以 0 到 k-1 的和，即 log2・k (k-1)/2（等差数列求和公式）。
四、代入 k = log₂N 化简
由于 k = log₂N（即 log2 = 1/k・logN，因为 2ᵏ = N ⇒ logN = k log2 ⇒ log2 = logN /k），代入上式：
首项：k・logN = logN・log₂N （因为 k = log₂N）。
末项：log2・k (k-1)/2 ≈ (logN /k)・k²/2 = (k・logN)/2 = (log₂N・logN)/2 （忽略低阶项 k-1 ≈ k）。
因此求和项 S 近似为：
plaintext
S ≈ logN · log₂N - (log₂N · logN)/2 = (logN · log₂N)/2
由于 logN 和 log₂N 仅相差常数系数（logN = log₂N・log2），可统一用 logN 表示，因此：
plaintext
S = O( (logN)² )
五、最终时间复杂度
将求和项 S 代入 T (N) 的表达式：
plaintext
T(N) = N·O(1) + N·S = O(N) + N·O( (logN)² ) = O(N (logN)² )
结论
该分治算法的时间复杂度为 O(N (logN)²)，其中：
递归拆分贡献 O (logN) 层。
每一层的合并步骤（尤其是排序）贡献 O (N logN) 的代价。
这一复杂度优于暴力枚举的 O (N²)，适用于大规模点集（如 N=2e5）的最近点对求解。
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

//给定平面上n个点，x和y坐标都是整数
//找出其中的一对点的距离，使得在这n个点的所有点对中，该距离为所有点对中最小的
//返回最短距离，精确到小数点后面4位
//测试链接 : https://www.luogu.com.cn/problem/P1429
// T(N) = 2*T(N/2) + O(N*logN)
// 这个表达式的时间复杂度是O(N*(logN的平方))
// 复杂度证明 : https://math.stackexchange.com/questions/159720/
public class Code04_ClosestTwoPoints1 {

    public static class Point {
        public double x;
        public double y;

        public Point(double a, double b) {
            x = a;
            y = b;
        }
    }

    public static int N = 200001;//最大点的数量
    //存储所有点
    public static Point[] points = new Point[N];
    //存储中间处理的点
    public static Point[] deals = new Point[N];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            for (int i = 0; i < n; i++) {
                in.nextToken();
                double x = (double) in.nval;
                in.nextToken();
                double y = (double) in.nval;
                points[i] = new Point(x, y);
            }
            //按照x坐标升序排序
            Arrays.sort(points, 0, n, (a, b) -> a.x <= b.x ? -1 : 1);
            double ans = nearest(0, n - 1);
            out.println(String.format("%.4f", ans));
            out.flush();
        }
    }

    public static double nearest(int left, int right) {
        double ans = Double.MAX_VALUE;
        //单点无距离
        if (left == right) {
            return ans;
        }
        int mid = (right + left) / 2;
        ans = Math.min(nearest(left, mid), nearest(mid + 1, right));
        //合并,检查跨左右区间的点对
        int l = mid;
        int r = mid + 1;
        int size = 0;
        // 收集左区间中x距离mid点x不超过ans的点
        while (l >= left && points[mid].x - points[l].x <= ans) {
            deals[size++] = points[l--];
        }
        // 收集右区间中x距离mid点x不超过ans的点
        while (r <= right && points[r].x - points[mid].x <= ans) {
            deals[size++] = points[r++];
        }
        //将收集到的点按照y轴排序
        Arrays.sort(deals, 0, size, (a, b) -> a.y <= b.y ? -1 : 1);
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (deals[j].y - deals[i].y >= ans) {
                    break;
                }
                ans = Math.min(ans, distance(deals[i], deals[j]));
            }
        }
        return ans;
    }

    public static double distance(Point a, Point b) {
        double x = a.x - b.x;
        double y = a.y - b.y;
        return Math.sqrt(x * x + y * y);
    }
}
