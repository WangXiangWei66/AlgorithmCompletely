package Class33;

//给定一个二维数组，实现二维数组的迭代器，包含hasNext()和next()两个迭代器常见方法。
//Leetcode题目 : https://leetcode.com/problems/flatten-2d-vector/
public class Problem_0251_Flatten2DVector {
    //时间复杂度为O(n)
    //空间复杂度为O(1)
    public static class Vector2D {
        private int[][] matrix;//存储输入的二维数组
        private int row;
        private int col;
        private boolean curUse;//当前位置的元素是否已经被使用

        public Vector2D(int[][] v) {
            matrix = v;
            row = 0;
            col = -1;//需要先移动到第一个有效元素
            curUse = true;
            hasNext();//寻找下一个有效的元素
        }
        //获取下一个元素
        public int next() {
            int ans = matrix[row][col];
            curUse = true;
            hasNext();//以前去寻找下一个有效的元素
            return ans;
        }
        //判断是否有下一个元素
        public boolean hasNext() {
            if (row == matrix.length) {
                return false;
            }
            if (!curUse) {
                return true;
            }
            if (col < matrix[row].length - 1) {
                col++;
            } else {//当前行已经遍历完毕，移动到下一行
                col = 0;
                do {
                    row++;
                } while (row < matrix.length && matrix[row].length == 0);
            }
            if (row != matrix.length) {
                curUse = false;
                return true;
            } else {
                return false;
            }
        }
    }
}
