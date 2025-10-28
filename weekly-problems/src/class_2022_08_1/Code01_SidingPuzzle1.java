package class_2022_08_1;

import java.util.HashSet;
import java.util.PriorityQueue;

//在一个 2 * 3 的板上（board）有 5 块砖瓦，用数字 1~5 来表示,
//以及一块空缺用0来表示。一次 移动 定义为选择0与一个相邻的数字（上下左右）进行交换.
//最终当板board的结果是[[1,2,3],[4,5,0]]谜板被解开。
//给出一个谜板的初始状态board，
//返回最少可以通过多少次移动解开谜板，如果不能解开谜板，则返回 -1 。
//测试链接 : https://leetcode.cn/problems/sliding-puzzle/
public class Code01_SidingPuzzle1 {
    //将网格进行状态压缩
    public static int b6 = 100000;  // 十万位权重（对应网格[0][0]）
    public static int b5 = 10000;   // 万位权重（对应网格[0][1]）
    public static int b4 = 1000;    // 千位权重（对应网格[0][2]）
    public static int b3 = 100;     // 百位权重（对应网格[1][0]）
    public static int b2 = 10;      // 十位权重（对应网格[1][1]）

    public static int[] nexts = new int[3];//查看当前状态的所有可能的下一步状态
    //num在目标状态中的坐标
    public static int[][] end = {{1, 2}, {0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}};

    public static int slidingPuzzle(int[][] m) {
        //记录已访问过的状态
        HashSet<Integer> set = new HashSet<>();
        //将初始网格转化为整数
        int from = m[0][0] * b6 + m[0][1] * b5 + m[0][2] * b4 + m[1][0] * b3 + m[1][1] * b2 + m[1][2];
        // 优先级队列：A*算法的核心，按“已移动步数 + 预估剩余步数”排序（小的优先）
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] + a[1] - (b[0] + b[1]));
        heap.add(new int[]{0, distance(from), from});// 初始状态入队：[已移动0步, 预估剩余步数, 当前状态]
        int ans = -1;//初始化结果,默认无法解开
        while (!heap.isEmpty()) {
            int[] arr = heap.poll();
            int distance = arr[0];//已移动步数
            int cur = arr[2];//当前状态
            if (set.contains(cur)) {
                continue;
            }
            //当前状态是目标状态
            if (cur == 123450) {
                ans = distance;
                break;
            }
            set.add(cur);
            int nextSize = nexts(cur);//当前状态的所有可能下一状态
            for (int i = 0; i < nextSize; i++) {
                int next = nexts[i];
                if (!set.contains(next)) {
                    heap.add(new int[]{distance + 1, distance(next), next});
                }
            }
        }
        return ans;
    }

    public static int nexts(int from) {
        //解析当前状态的6个位置
        int a = from / b6;
        int b = (from / b5) % 10;
        int c = (from / b4) % 10;
        int d = (from / b3) % 10;
        int e = (from / b2) % 10;
        int f = from % 10;

        //本质是 “通过权重差快速更新压缩状态”，通用公式可归纳为：
        //当交换位置 X（权重 Wx，当前值 Vx）和位置 Y（权重 Wy，当前值 Vy）时，
        //新状态 =当前状态 + (Vy - Vx)×Wx + (Vx - Vy)×Wy
        //因为交换后，位置 X 的贡献从Vx×Wx变为Vy×Wx（变化量(Vy-Vx)×Wx），位置 Y 的贡献从Vy×Wy变为Vx×Wy（变化量(Vx-Vy)×Wy）。
        if (a == 0) {// 0在[0][0]位置，可与右侧[b]或下方[d]交换
            nexts[0] = from + (b - a) * b6 + (a - b) * b5;
            nexts[1] = from + (d - a) * b6 + (a - d) * b3;
            return 2;
        } else if (b == 0) {
            nexts[0] = from + (b - a) * b6 + (a - b) * b5;
            nexts[1] = from + (c - b) * b5 + (b - c) * b4;
            nexts[2] = from + (e - b) * b5 + (b - e) * b2;
            return 3;
        } else if (c == 0) {
            nexts[0] = from + (b - c) * b4 + (c - b) * b5;
            nexts[1] = from + (f - c) * b4 + (c - f);
            return 2;
        } else if (d == 0) {
            nexts[0] = from + (a - d) * b3 + (d - a) * b6;
            nexts[1] = from + (e - d) * b3 + (d - e) * b2;
            return 2;
        } else if (e == 0) {
            nexts[0] = from + (b - e) * b2 + (e - b) * b5;
            nexts[1] = from + (d - e) * b2 + (e - d) * b3;
            nexts[2] = from + (f - e) * b2 + (e - f);
            return 3;
        } else {
            nexts[0] = from + (e - f) + (f - e) * b2;
            nexts[1] = from + (c - f) + (f - c) * b4;
            return 2;
        }
    }
    //预估剩余步数
    public static int distance(int num) {
        // 计算每个数字当前位置与目标位置的曼哈顿距离之和（预估剩余步数）
        int ans = end[num / b6][0] + end[num / b6][1];  // a的距离（[0][0]位置的数字）
        ans += end[(num / b5) % 10][0] + Math.abs(end[(num / b5) % 10][1] - 1);  // b的距离（[0][1]）
        ans += end[(num / b4) % 10][0] + Math.abs(end[(num / b4) % 10][1] - 2);  // c的距离（[0][2]）
        ans += end[(num / b3) % 10][1] + Math.abs(end[(num / b3) % 10][0] - 1);  // d的距离（[1][0]）
        ans += Math.abs(end[(num / b2) % 10][0] - 1) + Math.abs(end[(num / b2) % 10][1] - 1);  // e的距离（[1][1]）
        ans += Math.abs(end[num % 10][0] - 1) + Math.abs(end[num % 10][1] - 2);  // f的距离（[1][2]）
        return ans;
    }

}
