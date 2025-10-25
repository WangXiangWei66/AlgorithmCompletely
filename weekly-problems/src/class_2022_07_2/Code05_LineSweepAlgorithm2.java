package class_2022_07_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

//我们给出了一个（轴对齐的）二维矩形列表rectangles。
//对于rectangle[i] = [x1, y1, x2, y2]，其中（x1，y1）是矩形i左下角的坐标
//(xi1, yi1)是该矩形 左下角 的坐标，(xi2, yi2)是该矩形右上角 的坐标。
//计算平面中所有rectangles所覆盖的 总面积 。
//任何被两个或多个矩形覆盖的区域应只计算 一次 。
//返回 总面积 。因为答案可能太大，返回10^9+ 7 的模。
//Leetcode测试链接 : https://leetcode.cn/problems/rectangle-area-ii/
//洛谷测试链接 : https://www.luogu.com.cn/problem/P5490
public class Code05_LineSweepAlgorithm2 {

    public static int maxn = 300001;//最大数据规模
    public static long[][] arr = new long[maxn][4];// 存储事件：[x, y1, y2, 覆盖类型(1/-1)]
    public static long[] orderedY = new long[maxn];// 存储所有y坐标（用于离散化）
    //用于构建线段树
    public static long[] cover = new long[maxn << 2];//每个节点的覆盖次数
    public static long[] realLength = new long[maxn << 2];//每个节点的有效覆盖长度
    public static long[] left = new long[maxn << 2];
    public static long[] right = new long[maxn << 2];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                int x1 = (int) in.nval;
                in.nextToken();
                int y1 = (int) in.nval;
                in.nextToken();
                int x2 = (int) in.nval;
                in.nextToken();
                int y2 = (int) in.nval;
                //收集所有y轴坐标
                orderedY[i] = y1;
                orderedY[i + n] = y2;
                arr[i][0] = x1;
                arr[i][1] = y1;
                arr[i][2] = y2;
                arr[i][3] = 1;
                arr[i + n][0] = x2;
                arr[i + n][1] = y1;
                arr[i + n][2] = y2;
                arr[i + n][3] = -1;
            }
            out.println(coverArea(n << 1));
            out.flush();
        }
    }

    public static long coverArea(int n) {
        Arrays.sort(orderedY, 1, n + 1);
        Arrays.sort(arr, 1, n + 1, (a, b) -> a[0] <= b[0] ? -1 : 1);
        build(1, n, 1);
        long preX = 0;
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            ans += realLength[1] * (arr[i][0] - preX);
            preX = arr[i][0];
            add(arr[i][1], arr[i][2], (int) arr[i][3], 1);
        }
        return ans;
    }

    private static void build(int l, int r, int i) {
        if (r - l > 1) {
            int m = (l + r) >> 1;
            build(l, m, i << 1);
            build(m, r, (i << 1) | 1);
        }
        //节点的左右边界为离散化后的y值
        left[i] = orderedY[l];
        right[i] = orderedY[r];
    }

    private static void add(long L, long R, long C, int i) {
        long l = left[i];
        long r = right[i];
        if (L <= l && R >= r) {
            cover[i] += C;
        } else {
            if (L < right[i << 1]) {
                add(L, R, C, i << 1);
            }
            if (R > left[(i << 1) | 1]) {
                add(L, R, C, (i << 1 | 1));
            }
        }
        pushUp(i);
    }

    public static void pushUp(int i) {
        if (cover[i] > 0) {
            realLength[i] = right[i] - left[i];
        } else {
            realLength[i] = realLength[i << 1] + realLength[(i << 1) | 1];
        }
    }
}
