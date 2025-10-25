package class_2022_07_2;
//使用了扫描线算法：将矩阵的左右边界视为事件，将x坐标排序后从左到右扫描
//在扫描过程中，通过线段树维护当前y轴方向被覆盖的长度，再结合相邻x坐标的差值计算面积贡献
import java.util.Arrays;

//我们给出了一个（轴对齐的）二维矩形列表rectangles。
//对于rectangle[i] = [x1, y1, x2, y2]，其中（x1，y1）是矩形i左下角的坐标
//(xi1, yi1)是该矩形 左下角 的坐标，(xi2, yi2)是该矩形右上角 的坐标。
//计算平面中所有rectangles所覆盖的 总面积 。
//任何被两个或多个矩形覆盖的区域应只计算 一次 。
//返回 总面积 。因为答案可能太大，返回10^9+ 7 的模。
//Leetcode测试链接 : https://leetcode.cn/problems/rectangle-area-ii/
//洛谷测试链接 : https://www.luogu.com.cn/problem/P5490
public class Code05_LineSweepAlgorithm1 {

    public static class Node {
        public long cover;//当前区间被矩形覆盖的次数
        public long len;//当前区间被覆盖的有效长度
        public Node left;
        public Node right;
    }
    //动态线段树用于高效维护 y 轴方向的覆盖长度，支持动态创建节点
    public static class DynamicSegmentTree {
        public Node root;
        public long size;//线段树覆盖的y轴最大值

        public DynamicSegmentTree(long max) {
            root = new Node();
            size = max;
        }

        public void add(long L, long R, long cover) {
            add(root, 1, size, L, R, cover);
        }

        private void add(Node cur, long l, long r, long L, long R, long cover) {
            if (L <= l && R >= r) {
                cur.cover += cover;
            } else {
                if (cur.left == null) {
                    cur.left = new Node();
                }
                if (cur.right == null) {
                    cur.right = new Node();
                }
                long m = l + ((r - l) >> 1);
                if (L <= m) {
                    add(cur.left, l, m, L, R, cover);
                }
                if (R > m) {
                    add(cur.right, m + 1, r, L, R, cover);
                }
            }
            pushUp(cur, l, r);//更新当前节点的有效长度
        }

        private void pushUp(Node cur, long l, long r) {
            if (cur.cover > 0) {
                cur.len = r - l + 1;
                //有效长度为左右节点有效长度之和
            } else {
                cur.len = (cur.left != null ? cur.left.len : 0) + (cur.right != null ? cur.right.len : 0);
            }
        }
        //当前y轴被覆盖的总长度
        public long query() {
            return root.len;
        }
    }

    public static int rectangleArea(int[][] rectangles) {
        int n = rectangles.length;
        //每个矩形生成左右两个边界
        long[][] arr = new long[n << 1][4];// 每个事件：[x坐标, y1, y2, 覆盖类型(1/-1)]
        long max = 0;//记录最大y坐标，用于初始化线段树范围
        for (int i = 0; i < n; i++) {
            int x1 = rectangles[i][0];
            int y1 = rectangles[i][1] + 1;//为了处理闭区间
            int x2 = rectangles[i][2];
            int y2 = rectangles[i][3];
            // 左边界事件：x=x1时，在[y1, y2]区间添加覆盖（+1）
            arr[i][0] = x1;
            arr[i][1] = y1;
            arr[i][2] = y2;
            arr[i][3] = 1;
            // 右边界事件：x=x2时，在[y1, y2]区间移除覆盖（-1）
            arr[i + n][0] = x2;
            arr[i + n][1] = y1;
            arr[i + n][2] = y2;
            arr[i + n][3] = -1;
            max = Math.max(max, y2);
        }
        return coverArea(arr, n << 1, max);
    }

    public static int coverArea(long[][] arr, int n, long max) {
        Arrays.sort(arr, 0, n, (a, b) -> a[0] <= b[0] ? -1 : 1);//升序排序
        DynamicSegmentTree dst = new DynamicSegmentTree(max);
        long preX = 0;//上一个事件的x坐标
        long ans = 0;//总面积
        for (int i = 0; i < n; i++) {
            ans += dst.query() * (arr[i][0] - preX);
            ans %= 1000000007;
            preX = arr[i][0];
            dst.add(arr[i][1], arr[i][2], arr[i][3]);
        }
        return (int) ans;
    }
}
