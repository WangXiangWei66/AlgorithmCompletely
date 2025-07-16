package Class18;
//给定一个矩阵matrix，先从左上角开始，每一步只能往右或者往下走，走到右下角
// 然后从右下角出发，每一步只能往上或者往左走，再回到左上角。任何一个位置的数字，只能获得一遍。返回最大路径和。
//输入描述:
//第一行输入两个整数M和N，M,N<=200
//接下来M行，每行N个整数，表示矩阵中元素
//输出描述:
//输出一个整数，表示最大路径和
//牛客网题目：https://www.nowcoder.com/questionTerminal/8ecfe02124674e908b2aae65aad4efdf
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 把如下的全部代码拷贝进java编辑器
// 把文件大类名字改成Main，可以直接通过

import java.io.*;

public class Code03_CherryPickup {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//高效读取标准输入流。
        StreamTokenizer in = new StreamTokenizer(br);//将输入流解析为标记（tokens），方便处理整数、字符串等。
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));//格式化输出结果到标准输出流。
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int N = (int) in.nval;
            in.nextToken();
            int M = (int) in.nval;
            int[][] matrix = new int[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    in.nextToken();
                    matrix[i][j] = (int) in.nval;//读取每个单元格的值
                }
            }
            out.println(cherryPickup(matrix));
            out.flush();
        }
    }

    // 如下方法，在leetcode上提交也能通过
    // 测试链接 : https://leetcode.cn/problems/cherry-pickup/
    //在一个N * M 的网格中：
    //0 表示空单元格，可通行；
    //1 表示樱桃，收集后变为 0；
    //-1 表示障碍物，不可通行。
    public static int cherryPickup(int[][] grid) {
        int N = grid.length;
        int M = grid[0].length;
        int[][][] dp = new int[N][M][N];
        //dp[i][j][k] 的含义：
        //假设两个机器人同时从 (0,0) 出发：
        //第一个机器人到达位置 (i, j)；
        //第二个机器人到达位置 (k, l)（其中 l 通过 i+j = k+l 计算得出，确保步数相同）；
        //此时路径上能收集的最大樱桃数为 dp[i][j][k]。
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                for (int k = 0; k < N; k++) {
                    dp[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }

        int ans = process(grid, 0, 0, 0, dp);
        return ans < 0 ? 0 : ans;
    }

    //第一个机器人从 (x1, y1) 出发；
    //第二个机器人从 (x2, y2) 出发，其中 y2 = x1 + y1 - x2（确保步数相同）。
    public static int process(int[][] grid, int x1, int y1, int x2, int[][][] dp) {
        //如果任何机器人超过网格边界，返回负无穷
        if (x1 == grid.length || y1 == grid[0].length || x2 == grid.length || x1 + y1 - x2 == grid[0].length) {
            return Integer.MIN_VALUE;
        }
        if (dp[x1][y1][x2] != Integer.MIN_VALUE) {
            return dp[x1][y1][x2];
        }
        //如果第一个机器人已经到达了右下角，直接返回该位置的樱桃数（无论第二个机器人到哪了）
        if (x1 == grid.length - 1 && y1 == grid[0].length - 1) {
            dp[x1][y1][x2] = grid[x1][y1];
            return dp[x1][y1][x2];
        }
        //下面递归计算所有可能的下一步
        int next = Integer.MIN_VALUE;
        next = Math.max(next, process(grid, x1 + 1, y1, x2 + 1, dp));//下下
        next = Math.max(next, process(grid, x1 + 1, y1, x2, dp));//下右
        next = Math.max(next, process(grid, x1, y1 + 1, x2, dp));//右下
        next = Math.max(next, process(grid, x1, y1 + 1, x2 + 1, dp));//右右
        //如果有障碍物，则标记为不可达
        if (grid[x1][y1] == -1 || grid[x2][x1 + y1 - x2] == -1 || next == -1) {
            dp[x1][y1][x2] = -1;
            return dp[x1][y1][x2];
        }
        //如果两个机器人位置相同，则只收集一次樱桃
        if (x1 == x2) {
            dp[x1][y1][x2] = grid[x1][y1] + next;
            return dp[x1][y1][x2];
        }
        //否则将两个位置的樱桃进行累加
        dp[x1][y1][x2] = grid[x1][y1] + grid[x2][x1 + y1 - x2] + next;
        return dp[x1][y1][x2];
    }
}
