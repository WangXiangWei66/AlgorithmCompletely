package Class17;

import java.util.Comparator;
import java.util.PriorityQueue;

//给定一个每一行有序、每一列也有序，整体可能无序的二维数组
// 再给定一个正数k，返回二维数组中第k小的数
//Leetcode题目：https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
public class Code02_KthSmallestElementInSortedMatrix {

    //堆的方法
    public static int kthSmallest1(int[][] matrix, int k) {
        int N = matrix.length;
        int M = matrix[0].length;
        //创建最小堆，按Node的value升序排列
        PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());
        boolean[][] set = new boolean[N][M];//用来标记元素是否已经加入过堆
        heap.add(new Node(matrix[0][0], 0, 0));//初始化堆，从左上角开始
        set[0][0] = true;
        int count = 0;//记录当前已经取出的元素个数
        Node ans = null;
        while (!heap.isEmpty()) {
            ans = heap.poll();
            if (++count == k) {
                break;
            }
            int row = ans.row;
            int col = ans.col;
            //分别将下方和右方的元素加入堆
            if (row + 1 < N && !set[row + 1][col]) {
                heap.add(new Node(matrix[row + 1][col], row + 1, col));
                set[row + 1][col] = true;
            }

            if (col + 1 < M && !set[row][col + 1]) {
                heap.add(new Node(matrix[row][col + 1], row, col + 1));
                set[row][col + 1] = true;
            }
        }
        return ans.value;
    }

    public static class Node {
        public int value;
        public int row;
        public int col;

        public Node(int v, int r, int c) {
            value = v;
            row = r;
            col = c;
        }
    }

    public static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o1.value - o2.value;
        }
    }

    //使用二分法
    public static class Info {
        public int near; // 矩阵中 <= value 的最大元素值
        public int num;  // 矩阵中 <= value 的元素个数

        public Info(int n1, int n2) {
            near = n1;
            num = n2;
        }
    }

    public static int kthSmallest2(int[][] matrix, int k) {
        int N = matrix.length;
        int M = matrix[0].length;
        //分别是矩阵的左右边界
        int left = matrix[0][0];
        int right = matrix[N - 1][M - 1];
        int ans = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);  // 防止溢出的中间值计算
            // <=mid 有几个 <= mid 在矩阵中真实出现的数，谁最接近mid
            Info info = noMoreNum(matrix, mid);
            if (info.num < k) {
                left = mid + 1; // 数量不足，右移左边界
            } else {
                ans = info.near; // 记录当前最接近的值
                right = mid - 1;// 数量足够，左移右边界
            }
        }
        return ans;
    }

    public static Info noMoreNum(int[][] matrix, int value) {
        int near = Integer.MIN_VALUE;//对最接近的值进行初始化
        int num = 0;
        int N = matrix.length;
        int M = matrix[0].length;
        int row = 0;
        int col = M - 1;
        while (row < N && col >= 0) {
            if (matrix[row][col] <= value) {
                // 当前元素 <= value，更新 near 并累加计数
                near = Math.max(near, matrix[row][col]);
                num += col + 1;
                row++;
            } else {
                col--;
            }
        }
        return new Info(near, num);
    }
}
