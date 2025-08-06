package Class31;

//给你一个 m x n 的矩阵 board ，由若干字符 'X' 和 'O' ，找到所有被 'X' 围绕的区域，并将这些区域里所有的 'O' 用 'X' 填充。
//Leetcode题目 : https://leetcode.com/problems/surrounded-regions/
public class Problem_0130_SurroundedRegions {
    //1表示o，0表示X
    public static void infect(int[][] m, int i, int j) {
        if (i < 0 || i == m.length || j < 0 || j == m[0].length || m[i][j] != 1) {
            return;
        }
        m[i][j] = 2;//O与边界连接，没有被完全包围，要被保留
        infect(m, i - 1, j);
        infect(m, i + 1, j);
        infect(m, i, j - 1);
        infect(m, i, j + 1);
    }

    public static void solve1(char[][] board) {
        boolean[] ans = new boolean[1];//用于在递归中传递是否触达边界信息
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 'O') {
                    ans[0] = true;//假设O已经被包围
                    can(board, i, j, ans);//是否与边界相连
                    board[i][j] = ans[0] ? 'T' : 'F';
                }
            }
        }
        //再次遍历，准备进行最终替换
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                char can = board[i][j];
                if (can == 'T' || can == 'F') {
                    board[i][j] = '.';
                    change(board, i, j, can);
                }
            }
        }
    }

    public static void can(char[][] board, int i, int j, boolean[] ans) {
        if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
            ans[0] = false;
            return;
        }
        if (board[i][j] == 'O') {
            board[i][j] = '.';//避免出现重复处理
            can(board, i - 1, j, ans);
            can(board, i + 1, j, ans);
            can(board, i, j - 1, ans);
            can(board, i, j + 1, ans);
        }
    }

    //由之前的标记来修改整个的连通区域
    //还传入了标记字符
    public static void change(char[][] board, int i, int j, char can) {
        if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
            return;
        }
        if (board[i][j] == '.') {
            board[i][j] = can == 'T' ? 'X' : 'O';
            change(board, i - 1, j, can);
            change(board, i + 1, j, can);
            change(board, i, j - 1, can);
            change(board, i, j + 1, can);
        }
    }

    public static void solve2(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0 || board[0] == null) {
            return;
        }
        int N = board.length;
        int M = board[0].length;
        //遍历第一行和最后一行
        for (int j = 0; j < M; j++) {
            if (board[0][j] == 'O') {
                free(board, 0, j);
            }
            if (board[N - 1][j] == 'O') {
                free(board, N - 1, j);
            }
        }
        //遍历第一列和最后一列
        for (int i = 1; i < N - 1; i++) {
            if (board[i][0] == 'O') {
                free(board, i, 0);
            }
            if (board[i][M - 1] == 'O') {
                free(board, i, M - 1);
            }
        }
        //遍历整个矩阵
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
                if (board[i][j] == 'F') {
                    board[i][j] = 'O';
                }
            }
        }
    }
    //找到所有与边界O相连的O
    public static void free(char[][] board, int i, int j) {
        if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != 'O') {
            return;
        }
        board[i][j] = 'F';
        free(board, i + 1, j);
        free(board, i - 1, j);
        free(board, i, j + 1);
        free(board, i, j - 1);
    }
}
