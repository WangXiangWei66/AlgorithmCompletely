package Class28;

//编写一个程序，通过填充空格来解决数独问题。本代码使用了回溯（深度优先）方法来填充数独中的空白格
//数独的解法需遵循如下规则：
//数字1-9在每一行只能出现一次。
//数字1-9在每一列只能出现一次。
//数字1-9在每一个以粗实线分隔的3x3宫内只能出现一次
//数独部分空格内已填入了数字，空白格用'.'表示。
//Leetcode题目：https://leetcode.com/problems/sudoku-solver/
public class Problem_0037_SudokuSolver {

    public static void solveSudoku(char[][] board) {
        boolean[][] row = new boolean[9][10];
        boolean[][] col = new boolean[9][10];
        boolean[][] bucket = new boolean[9][10];
        initMaps(board, row, col, bucket);//初始化这三个数组，根据数独中已有数字标记使用情况
        process(board, 0, 0, row, col, bucket);
    }

    public static void initMaps(char[][] board, boolean[][] row, boolean[][] col, boolean[][] bucket) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int bid = 3 * (i / 3) + (j / 3);//计算当前单元格所属的3X3小九宫格编号
                if (board[i][j] != '.') {//如果单元格已经有数字了
                    int num = board[i][j] - '0';
                    row[i][num] = true;
                    col[j][num] = true;
                    bucket[bid][num] = true;
                }
            }
        }
    }

    //定义递归处理方法来尝试填充数独
    //(i,j):当前处理的单元格坐标
    public static boolean process(char[][] board, int i, int j, boolean[][] row, boolean[][] col, boolean[][] bucket) {
        if (i == 9) {
            return true;
        }
        //计算下一个要处理的单元格坐标，遵循从左往右，从上往下的顺序
        int nexti = j != 8 ? i : i + 1;
        int nextj = j != 8 ? j + 1 : 0;
        //如果当前单元格已经有数字了，则去处理下一个单元格
        if (board[i][j] != '.') {
            return process(board, nexti, nextj, row, col, bucket);
        } else {
            int bid = 3 * (i / 3) + (j / 3);
            for (int num = 1; num <= 9; num++) {//尝试为当前单元格填充1~9的数字
                if ((!row[i][num]) && (!col[j][num]) && (!bucket[bid][num])) {
                    row[i][num] = true;
                    col[j][num] = true;
                    bucket[bid][num] = true;
                    board[i][j] = (char) (num + '0');//在数独中填入数字，并转化为字符型
                    if (process(board, nexti, nextj, row, col, bucket)) {
                        return true;
                    }
                    row[i][num] = false;
                    col[j][num] = false;
                    bucket[bid][num] = false;
                    board[i][j] = '.';
                }
            }
            return false;
        }
    }
}
