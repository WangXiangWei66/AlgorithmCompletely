package Class24;

import java.util.ArrayList;
import java.util.List;

//N * M的棋盘（N和M是输入参数），每种颜色的格子数必须相同的
//上下左右的格子算相邻，相邻格子染的颜色必须不同，所有格子必须染色，返回至少多少种颜色可以完成任务
public class Code04_Painting {

    public static int minColors(int N, int M) {
        for (int i = 2; i < N * M; i++) {
            int[][] matrix = new int[N][M];
            //检查条件：总单元格数能被当前颜色数i整除，且能成功填充矩阵
            if ((N * M) % i == 0 && can(matrix, N, M, i)) {
                return i;
            }
        }
        return N * M; //如果所有尝试都失败，返回最大可能值（每个单元格一种颜色）
    }

    //判断使用pNum种颜色能否满足条件
    public static boolean can(int[][] matrix, int N, int M, int pNum) {
        int all = N * M;//总单元格数量
        int every = all / pNum;//每种颜色需要使用的次数
        ArrayList<Integer> rest = new ArrayList<>(); // 记录每种颜色剩余可用次数的列表
        rest.add(0);//索引0先占个位，颜色从1开始编写
        for (int i = 1; i <= pNum; i++) {
            rest.add(every); //每种颜色的初始使用次数都是every
        }
        return process(matrix, N, M, pNum, 0, 0, rest);
    }

    // 递归尝试填充矩阵，判断能否用pNum种颜色完成填充
    public static boolean process(int[][] matrix, int N, int M, int pNum, int row, int col, List<Integer> rest) {
        if (row == N) {
            return true;
        }
        if (col == M) {
            return process(matrix, N, M, pNum, row + 1, 0, rest);
        }
        int left = col == 0 ? 0 : matrix[row][col - 1];
        int up = row == 0 ? 0 : matrix[row - 1][col];
        //尝试使用每种颜色
        for (int color = 1; color <= pNum; color++) {
            if (color != left && color != up && rest.get(color) > 0) {
                int count = rest.get(color);
                rest.set(color, count - 1);
                matrix[row][col] = color;//填充一下当前的单元格
                if (process(matrix, N, M, pNum, row, col + 1, rest)) {
                    return true;
                }
                rest.set(color, count);//回溯，恢复剩余次数
                matrix[row][col] = 0;//回溯：清空当前单元格颜色
            }
        }
        return false;
    }

    public static void main(String[] args) {
        for (int N = 2; N < 10; N++) {
            for (int M = 2; M < 10; M++) {
                System.out.println("N = " + N);
                System.out.println("M = " + M);
                System.out.println("ans = " + minColors(N, M));
                System.out.println("==============");
            }
        }
    }
}
