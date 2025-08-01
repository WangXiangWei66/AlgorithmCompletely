package Class28;

//请你判断一个9x9 的数独是否有效。只需要根据以下规则 ，验证已经填入的数字是否有效即可。
//数字1-9在每一行只能出现一次。
//数字1-9在每一列只能出现一次。
//数字1-9在每一个以粗实线分隔的3x3宫内只能出现一次。（请参考示例图）
//数独部分空格内已填入了数字，空白格用'.'表示。
//注意：
//一个有效的数独（部分已被填充）不一定是可解的。
//只需要根据以上规则，验证已经填入的数字是否有效即可。
//Leetcode题目：https://leetcode.com/problems/valid-sudoku/
public class Problem_0036_ValidSudoku {
    //最后返回布尔值 表示是否有效
    public static boolean isValidSudoku(char[][] board) {
        //创建三个二维数组用于记录数字出现的状态，数组第二维大小为10，方便直接使用数字1-9进行索引
        boolean[][] row = new boolean[9][10];//第i行中数字num是否出现
        boolean[][] col = new boolean[9][10];//第j列中数字num是否出现
        boolean[][] bucket = new boolean[9][10];//记录第 k个 3x3 小九宫格中数字 num 是否已出现
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int bid = 3 * (i / 3) + (j / 3);//计算当前单元格所属的3X3小九宫格编号
                if (board[i][j] != '.') {//判断当前位置是否已经填入了数字
                    int num = board[i][j] - '0';
                    if (row[i][num] || col[j][num] || bucket[bid][num]) {//检查当前数字是否在所在行、列或小九宫格中已出现
                        return false;
                    }
                    //未出现，则把他标记为已出现
                    row[i][num] = true;
                    col[j][num] = true;
                    bucket[bid][num] = true;
                }
            }
        }
        return true;
    }
}
