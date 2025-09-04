package Class50;

import java.util.Arrays;

//凸包问题经典题
//在一个二维的花园中，有一些用 (x, y) 坐标表示的树。由于安装费用十分昂贵，你的任务是先用最短的绳子围起所有的树。只有当所有的树都被绳子包围时，花园才能围好栅栏。你需要找到正好位于栅栏边界上的树的坐标。
//leetcode题目：https://leetcode.com/problems/erect-the-fence/
public class Problem_0587_ErectTheFence {

    //凸包：平面上所有点的最小凸多边形，满足所有点都在多边形内部或边界上。
    //叉积（Cross Product）：判断三个点的转向（顺时针 / 逆时针 / 共线），是凸包算法的核心。
    //对于点 a(x1,y1)、b(x2,y2)、c(x3,y3)，叉积计算为：
    //cross = (y2 - y1) * (x3 - x2) - (x2 - x1) * (y3 - y2)
    //若 cross > 0：a→b→c 是逆时针转向（三点构成 “左转”，可能不在凸包上，需弹出栈）；
    //若 cross = 0：三点共线（均在凸包边界上，需保留）；
    //若 cross < 0：a→b→c 是顺时针转向（三点构成 “右转”，符合凸包边界，需入栈）。
    public static int[][] outerTrees(int[][] points) {
        int n = points.length;
        int s = 0;//栈指针，记录栈中元素
        int[][] stack = new int[n << 1][];//单调栈，凸包点数量最多等于原点点数
        //将点按从左到右、从下到上的顺序排列，为后续分两次构建凸包（下边界 + 上边界）做准备。
        Arrays.sort(points, (a, b) -> (a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]));
        //从左往右，构建凸包下边界
        for (int i = 0; i < n; i++) {
            while (s > 1 && cross(stack[s - 2], stack[s - 1], points[i]) > 0) {
                s--;//逆时针，弹出
            }
            stack[s++] = points[i];
        }
        //从右往左，构建凸包上边界
        for (int i = n - 2; i >= 0; i--) {
            while (s > 1 && cross(stack[s - 2], stack[s - 1], points[i]) > 0) {
                s--;
            }
            stack[s++] = points[i];
        }
        Arrays.sort(stack, 0, s, (a, b) -> (b[0] == a[0] ? b[1] - a[1] : b[0] - a[0]));
        n = 1;//去重后点的数量
        for (int i = 1; i < s; i++) {
            if (stack[i][0] != stack[i - 1][0] || stack[i][1] != stack[i - 1][1]) {
                stack[n++] = stack[i];//当前点与点一个点不同，保留
            }
        }
        return Arrays.copyOf(stack, n);
    }

    public static int cross(int[] a, int[] b, int[] c) {
        // 叉积公式：(y2 - y1)*(x3 - x2) - (x2 - x1)*(y3 - y2)
        return (b[1] - a[1]) * (c[0] - b[0]) - (b[0] - a[0]) * (c[1] - b[1]);
    }

    public static void main(String[] args) {
        int[] a = {4, 4};
        int[] b = {1, 1};
        int[] c = {1, 5};
        System.out.println(cross(a, b, c));
    }
}
