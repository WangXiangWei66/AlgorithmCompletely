package class_2022_01_3;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

//在一个10^6 * 10^6的网格中，
//source = [sx, sy]是出发位置，target = [tx, ty]是目标位置
//数组blocked是封锁的方格列表，被禁止的方格数量不超过200
//blocked[i] = [xi, yi] 表示(xi, yi)的方格是禁止通行的
//每次移动都可以走上、下、左、右四个方向
//但是来到的位置不能在封锁列表blocked上
//同时不允许走出网格
//如果从source能到达target返回true。否则返回false。
public class Code02_EscapeALargeMaze {

    public static long offset = 1000000;//将二维坐标编码为一维长整数偏移量

    public boolean isEscapePossible(int[][] blocked, int[] source, int[] target) {
        int n = blocked.length;
        int maxPoints = n * (n - 1) / 2;//最大的可能封锁点数
        HashSet<Long> blockSet = new HashSet<>();
        //将封锁的坐标编码后存入集合
        for (int i = 0; i < n; i++) {
            blockSet.add((long) blocked[i][0] * offset + blocked[i][1]);
        }
        //双向搜索
        return bfs(source[0], source[1], target[0], target[1], maxPoints, blockSet)
                && bfs(target[0], target[1], source[0], source[1], maxPoints, blockSet);
    }

    public static boolean bfs(int fromX, int fromY, int toX, int toY, int maxPoints, HashSet<Long> blockSet) {
        HashSet<Long> visited = new HashSet<>();//是否已经被访问
        Queue<Long> queue = new LinkedList<>();//BFS队列
        visited.add((long) fromX * offset + fromY);
        queue.add((long) fromX * offset + fromY);
        while (!queue.isEmpty() && (visited.size() <= maxPoints)) {
            long cur = queue.poll();
            long curX = cur / offset;
            long curY = cur - curX * offset;
            if (findAndAdd(curX - 1, curY, toX, toY, blockSet, visited, queue)
                    || findAndAdd(curX + 1, curY, toX, toY, blockSet, visited, queue)
                    || findAndAdd(curX, curY - 1, toX, toY, blockSet, visited, queue)
                    || findAndAdd(curX, curY + 1, toX, toY, blockSet, visited, queue)
            ) {
                return true;
            }
        }
        return visited.size() > maxPoints;
    }

    public static boolean findAndAdd(long row, long col, int toX, int toY, HashSet<Long> blockSet, HashSet<Long> visited, Queue<Long> queue) {
        if (row < 0 || row == offset || col < 0 || col == offset) {
            return false;
        }
        if (row == toX && col == toY) {
            return true;
        }
        long value = row * offset + col;
        if (!blockSet.contains(value) && !visited.contains(value)) {
            visited.add(value);
            queue.add(value);
        }
        return false;
    }
}
