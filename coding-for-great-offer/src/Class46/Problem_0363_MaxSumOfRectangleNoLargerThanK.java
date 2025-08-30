package Class46;

import java.util.TreeSet;

//给你一个 m x n 的矩阵 matrix 和一个整数 k ，找出并返回矩阵内部矩形区域的不超过 k 的最大数值和。
//题目数据保证总会存在一个数值和不超过 k 的矩形区域。
//leetcode题目：https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
public class Problem_0363_MaxSumOfRectangleNoLargerThanK {
    //时间复杂度为O(n * logN)
    public static int nearK(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return Integer.MAX_VALUE;
        }
        //创建一个TreeSet集合，用于存储前缀和，TreeSet的特性是有序性且支持二分查找
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);
        int ans = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            Integer find = set.ceiling(sum - k);
            if (find != null) {
                int curAns = sum - find;
                ans = Math.max(ans, curAns);
            }
            set.add(sum);
        }
        return ans;
    }

    public static int maxSumSubMatrix(int[][] matrix, int k) {
        if (matrix == null || matrix[0] == null) {
            return 0;
        }
        if (matrix.length > matrix[0].length) {
            matrix = rotate(matrix);
        }
        int row = matrix.length;
        int col = matrix[0].length;
        int res = Integer.MIN_VALUE;
        TreeSet<Integer> sumSet = new TreeSet<>();//存储前缀和的有序集合
        for (int s = 0; s < row; s++) {
            int[] colSum = new int[col];//矩阵中每列从S行到当前行的累加和
            for (int e = s; e < row; e++) {
                sumSet.add(0);//每次处理新的下边界时，重置集合并加入初始0
                int rowSum = 0;//当前范围内的前缀和
                for (int c = 0; c < col; c++) {
                    colSum[c] += matrix[e][c];//累加当前列从s行到e行的和
                    rowSum += colSum[c];//记录矩阵的横向累加和
                    Integer it = sumSet.ceiling(rowSum - k);
                    if (it != null) {
                        res = Math.max(res, rowSum - it);
                    }
                    sumSet.add(rowSum);
                }
                sumSet.clear();
            }
        }
        return res;
    }

    public static int[][] rotate(int[][] matrix) {
        int N = matrix.length;
        int M = matrix[0].length;
        int[][] r = new int[M][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                r[j][i] = matrix[i][j];
            }
        }
        return r;
    }
}
