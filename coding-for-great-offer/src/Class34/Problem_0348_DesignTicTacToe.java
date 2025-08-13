package Class34;

//tic-tac-toe游戏，不知道的同学可以自行搜索。请实现以下类TicTacToe。
//构造方法：TicTacToe(int n) : TicTacToe游戏的类，n表示目前在n*n的棋盘上玩游戏。
//内部方法：int move(int i, int j, int p) : p只可能是1和2，表示玩家1还是玩家2。当前玩家在i行j列上走了一步。返回值只可能是0、1、2，0表示没有玩家赢；1表示玩家1赢了；2表示玩家2赢了。
//Leetcode题目 : https://leetcode.com/problems/design-tic-tac-toe/
public class Problem_0348_DesignTicTacToe {

    class TicTacToe {
        private int[][] rows;//每行中玩家 1 和玩家 2 的落子数量，rows [i][p] 表示第 i 行玩家 p 的落子数
        private int[][] cols;//每列中玩家 1 和玩家 2 的落子数量，cols [j][p] 表示第 j 列玩家 p 的落子数
        private int[] leftUp;//左对角线（从左上角到右下角）上玩家 1 和玩家 2 的落子数量
        private int[] rightUp;//右对角线（从右上角到左下角）上玩家 1 和玩家 2 的落子数量
        private boolean[][] matrix;//棋盘上是否已被落子
        private int N;

        public TicTacToe(int n) {
            rows = new int[n][3];
            cols = new int[n][3];
            leftUp = new int[3];
            rightUp = new int[3];
            matrix = new boolean[n][n];
            N = n;
        }

        public int move(int row, int col, int player) {
            if (matrix[row][col]) {
                return 0;//已经落子了
            }
            matrix[row][col] = true;
            rows[row][player]++;
            cols[col][player]++;
            if (row == col) {
                leftUp[player]++;//当前落子在左对角线
            }
            if (row + col == N - 1) {
                rightUp[player]++;
            }
            if (rows[row][player] == N || cols[col][player] == N || leftUp[player] == N || rightUp[player] == N) {
                return player;
            }
            return 0;
        }
    }
}
