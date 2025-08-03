package Class30;

//给定一个m x n 二维字符网格board 和一个字符串单词word 。如果word 存在于网格中，返回 true ；否则，返回 false 。
//单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。
//Leetcode题目 : https://leetcode.com/problems/word-search/
public class Problem_0079_WordSearch {

    public static boolean exist(char[][] board, String word) {
        char[] w = word.toCharArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (f(board, i, j, w, 0)) {//进行深度优先索搜
                    return true;
                }
            }
        }
        return false;
    }

    //k：当前需要匹配的字符在w中的索引
    public static boolean f(char[][] board, int i, int j, char[] w, int k) {
        if (k == w.length) {
            return true;
        }
        if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
            return false;
        }
        if (board[i][j] != w[k]) {
            return false;
        }
        //标记当前单元格已经被使用了
        char tmp = board[i][j];
        board[i][j] = 0;
        //递归去搜索四个方向
        boolean ans = f(board, i - 1, j, w, k + 1) ||
                f(board, i + 1, j, w, k + 1) ||
                f(board, i, j - 1, w, k + 1) ||
                f(board, i, j + 1, w, k + 1);
        board[i][j] = tmp;//回溯操作，进行恢复现场
        return ans;
    }
}
