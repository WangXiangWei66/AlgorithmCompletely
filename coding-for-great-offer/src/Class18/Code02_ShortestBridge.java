package Class18;

//在给定的二维二进制数组A中，存在两座岛。（岛是由四面相连的 1 形成的一个最大组。）
//现在，我们可以将0变为1，以使两座岛连接起来，变成一座岛。
//返回必须翻转的0 的最小数目。（可以保证答案至少是1）
//Leetcode题目：https://leetcode.com/problems/shortest-bridge/
public class Code02_ShortestBridge {

    public static int shortestBridge(int[][] m) {
        int N = m.length;
        int M = m[0].length;
        int all = N * M;
        int island = 0;// 当前处理的岛屿编号（0或1）
        // curs和nexts数组用于BFS的队列
        int[] curs = new int[all]; // 当前层的单元格队列
        int[] nexts = new int[all];// 下一层的单元格队列
        // 记录两个岛屿中每个单元格到各自岛屿的距离
        int[][] records = new int[2][all];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (m[i][j] == 1) { // 发现一个未处理的岛屿，将他变为2，避免后续重复处理
                    // 感染这个岛屿的所有单元格（将1变为2），并初始化BFS队列
                    int queueSize = infect(m, i, j, N, M, curs, 0, records[island]); //接受当前岛屿中所有陆地单元格的数量
                    int V = 1; // 当前层的距离值
                    // BFS扩展，计算每个单元格到当前岛屿的距离
                    while (queueSize != 0) {
                        V++;//距离增加
                        // 处理当前层的所有单元格，生成下一层队列
                        queueSize = bfs(N, M, all, V, curs, queueSize, nexts, records[island]);
                        // 交换当前队列和下一层队列
                        int[] tmp = curs;
                        curs = nexts;
                        nexts = tmp;
                    }
                    island++;//处理下一个岛屿
                }
            }
        }
        //寻找两个岛屿的最短距离
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < all; i++) {
            min = Math.min(min, records[0][i] + records[1][i]);
        }
        // 最终结果需要减3，因为两个岛屿的距离各算1，中间桥梁的距离需要减2
        return min - 3;
    }

    // 当前来到m[i][j] , 总行数是N，总列数是M
    // m[i][j]感染出去(找到这一片岛所有的1),把每一个1的坐标，放入到int[] curs队列！
    // 1 (a,b) -> curs[index++] = (a * M + b)
    // 1 (c,d) -> curs[index++] = (c * M + d)
    // 二维已经变成一维了， 1 (a,b) -> a * M + b
    // 设置距离record[a * M +b ] = 1
    //curs：存储当前岛屿所有陆地单元格队列
    //index：当前队列的下一个可用位置索引，下一个陆地单元格在curs应该放的位置
    //record：记录每个单元格到当前岛屿的最短距离的数组
    public static int infect(int[][] m, int i, int j, int N, int M, int[] curs, int index, int[] record) {
        //进行边界检查或非陆地则返回
        if (i < 0 || i == N || j < 0 || j == M || m[i][j] != 1) {
            return index;//返回的是当前已收集到的岛屿单元格数量
        }
        // m[i][j] 不越界，且m[i][j] == 1
        m[i][j] = 2; //将当前陆地标记为已访问
        int p = i * M + j; //将二维坐标转化为一维索引
        record[p] = 1;//将距离初始化为1
        // 收集到不同的1
        curs[index++] = p;//将当前陆地加入队列
        //递归感染上下左右四个方向
        index = infect(m, i - 1, j, N, M, curs, index, record);
        index = infect(m, i + 1, j, N, M, curs, index, record);
        index = infect(m, i, j - 1, N, M, curs, index, record);
        index = infect(m, i, j + 1, N, M, curs, index, record);
        return index;
    }

    // 二维原始矩阵中，N总行数，M总列数
    // all 总 all = N * M
    // V：当前要记录的距离值（表示距离岛屿的第V层）
    // record里面拿距离
    //代码的作用是从岛屿触发，逐层拓展并计算网格中每个单元格到该岛屿的最短距离
    //size：当前层队列（curs）中的有效元素数量
    public static int bfs(int N, int M, int all, int V, int[] curs, int size, int[] nexts, int[] record) {
        int nexti = 0;// 下一层队列的索引
        for (int i = 0; i < size; i++) {
            //计算上下左右四个方向的邻居索引
            int up = curs[i] < M ? -1 : curs[i] - M;
            int down = curs[i] + M >= all ? -1 : curs[i] + M;
            //curs[i]%M:处理列坐标
            int left = curs[i] % M == 0 ? -1 : curs[i] - 1;
            int right = curs[i] % M == M - 1 ? -1 : curs[i] + 1;
            //检查每个邻居，判断是否越界与是否被访问过
            //更新距离记录，并构建下一个BFS队列
            if (up != -1 && record[up] == 0) {
                record[up] = V;
                nexts[nexti] = up;
            }
            if (down != -1 && record[down] == 0) {
                record[down] = V;
                nexts[nexti] = down;
            }

            if (left != -1 && record[left] == 0) {
                record[left] = V;
                nexts[nexti] = left;
            }
            if (right != -1 && record[right] == 0) {
                record[right] = V;
                nexts[nexti++] = right;
            }
        }
        return nexti;//返回下一层队列的大小，便于后续继续扩展
    }
}
