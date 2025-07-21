package Class22;
//本体的时间复杂度为O(N * M *log(N*M))
//空间复杂度为O(N * M)
import java.util.PriorityQueue;

//给你一个 m x n 的矩阵
//其中的值均为非负整数
//代表二维高度图每个单元的高度
//请计算图中形状最多能接多少体积的雨水。
//Leetcode题目：https://leetcode.com/problems/trapping-rain-water-ii/
public class Code03_TrappingRainWaterII {
    //算法核心使用了优先队列（最小堆） + 边界扩张
    //二维的边界是变化的：
    //初始边界为矩阵的最外层（边缘位置无法存水，因为水会从边缘流走）。
    //每次从当前边界中选择高度最低的位置作为突破口（因为水会从低处流向更低处），向内部扩张。
    //扩张时，计算新位置能接住的雨水（由当前已知的最高边界高度决定），并将新位置加入边界。
    public static class Node {//封装的信息，用于在优先级队列中存储和比较
        public int value;//该位置的高度
        public int row;//行索引
        public int col;//列索引

        public Node(int v, int r, int c) {
            value = v;
            row = r;
            col = c;
        }
    }

    public static int trapRainWater(int[][] heightMap) {
        // 边界条件：空矩阵或行数/列数不足2（无法存水）
        if (heightMap == null || heightMap.length == 0 || heightMap[0] == null || heightMap[0].length == 0) {
            return 0;
        }
        int N = heightMap.length;//矩阵的行数
        int M = heightMap[0].length;//矩阵的列数
        // 标记位置是否已加入边界（避免重复处理）
        boolean[][] isEnter = new boolean[N][M];
        // 优先队列（最小堆）：用于存储边界位置，按高度升序排列（每次取出最低的边界）
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> a.value - b.value);
        //填充最外层到优先队列
        // 上边界（第一行，从左到右，不包含右上角，避免重复）
        for (int col = 0; col < M - 1; col++) {
            isEnter[0][col] = true;
            heap.add(new Node(heightMap[0][col], 0, col));
        }
        // 右边界（最后一列，从上到下，不包含右下角，避免重复）
        for (int row = 0; row < N - 1; row++) {
            isEnter[row][M - 1] = true;
            heap.add(new Node(heightMap[row][M - 1], row, M - 1));
        }
        // 下边界（最后一行，从右到左，不包含左下角，避免重复）
        for (int col = M - 1; col > 0; col--) {
            isEnter[N - 1][col] = true;
            heap.add(new Node(heightMap[N - 1][col], N - 1, col));
        }
        // 左边界（第一列，从下到上，不包含左上角，避免重复）
        for (int row = N - 1; row > 0; row--) {
            isEnter[row][0] = true;
            heap.add(new Node(heightMap[row][0], row, 0));
        }
        //核心：通过优先队列扩张并计算存水量
        int water = 0;//累计接水量
        int max = 0;//记录当前已知的最高边界高度（决定新位置的存水量）
        while (!heap.isEmpty()) {//边界如果不为空，则持续扩张
            Node cur = heap.poll();// 取出当前边界中高度最低的位置（最小堆的特性）
            max = Math.max(max, cur.value);// 更新当前最高边界高度（新位置的存水量由它决定）
            //获得当前位置的行与列
            int r = cur.row;
            int c = cur.col;
            //对当前位置的四个相邻位置进行处理，计算存水量并将新位置加入边界
            //上方相邻位置
            if (r > 0 && !isEnter[r - 1][c]) {
                // 存水量 = max(0, 当前最高边界高度 - 新位置高度)
                water += Math.max(0, max - heightMap[r - 1][c]);
                isEnter[r - 1][c] = true;//标记为已加入高度
                heap.add(new Node(heightMap[r - 1][c], r - 1, c));//并将他加入队列
            }
            //下方相邻位置
            if (r < N - 1 && !isEnter[r + 1][c]) {
                water += Math.max(0, max - heightMap[r + 1][c]);
                isEnter[r + 1][c] = true;
                heap.add(new Node(heightMap[r + 1][c], r + 1, c));
            }
            //左侧相邻位置
            if (c > 0 && !isEnter[r][c - 1]) {
                water += Math.max(0, max - heightMap[r][c - 1]);
                isEnter[r][c - 1] = true;
                heap.add(new Node(heightMap[r][c - 1], c, c - 1));
            }
            //下方相邻位置
            if (c < M - 1 && !isEnter[r][c + 1]) {
                water += Math.max(0, max - heightMap[r][c + 1]);
                isEnter[r][c + 1] = true;
                heap.add(new Node(heightMap[r][c + 1], r, c + 1));
            }
        }
        return water;
    }
}
