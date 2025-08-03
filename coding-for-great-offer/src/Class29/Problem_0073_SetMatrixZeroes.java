package Class29;

//给定一个m x n 的矩阵，如果一个元素为 0 ，则将其所在行和列的所有元素都设为 0 。请使用原地算法。
//进阶：
//一个直观的解决方案是使用 O(m * n)的额外空间，但这并不是一个好的解决方案。
//一个简单的改进方案是使用 O(m+n) 的额外空间，但这仍然不是最好的解决方案。
//你能想出一个仅使用常量空间的解决方案吗？
//Leetcode题目：https://leetcode.com/problems/set-matrix-zeroes/
public class Problem_0073_SetMatrixZeroes {
    //方法一实现了常量的空间复杂度
    public static void setZeroes1(int[][] matrix) {
        //两个boolean变量，记录第一行与第一列是否原本就有0
        boolean row0Zero = false;
        boolean col0Zero = false;
        //定义循环要用的索引变量
        int i = 0;
        int j = 0;
        //判断第一行是否有零
        for (i = 0; i < matrix[0].length; i++) {
            if (matrix[0][i] == 0) {
                row0Zero = true;
                break;
            }
        }
        //判断第一列是否有0
        for (i = 0; i < matrix.length; i++) {
            if (matrix[i][0] == 0) {
                col0Zero = true;
                break;
            }
        }
        //遍历矩阵剩余位置，一旦发现有零，就把这个 元素所在行和列的第一个元素设为0
        for (i = 1; i < matrix.length; i++) {
            for (j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        //再次遍历矩阵，根据第一次遍历的标记，将需要设为0的元素设为0
        for (i = 1; i < matrix.length; i++) {
            for (j = 1; j < matrix[0].length; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        //如果原始第一行有0，就把第一行所有元素都设为0
        if (row0Zero) {
            for (i = 0; i < matrix[0].length; i++) {
                matrix[0][i] = 0;
            }
        }
        //如果原始第一列有0，就把第一列所有元素都设为0
        if (col0Zero) {
            for (i = 0; i < matrix.length; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    public static void setZeroes2(int[][] matrix) {
        boolean col0 = false;
        int i = 0;
        int j = 0;
        for (i = 0; i < matrix.length; i++) {
            for (j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;//将当前行的第一个元素设为0
                    if (j == 0) {//如果0在第一列
                        col0 = true;
                    } else {
                        matrix[0][j] = 0;
                    }
                }
            }
        }
        //从下往上，从左往右遍历
        for (i = matrix.length - 1; i >= 0; i--) {
            for (j = 1; j < matrix[0].length; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        if (col0) {
            for (i = 0; i < matrix.length; i++) {
                matrix[i][0] = 0;
            }
        }
    }
}
