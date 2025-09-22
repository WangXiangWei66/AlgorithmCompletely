package class_2022_02_2;

//从点(x, y)可以转换到(x, x+y) 或者(x+y, y)。
//给定一个起点(sx, sy)和一个终点(tx, ty)，
//如果通过一系列的转换可以从起点到达终点，
//则返回 True，否则返回False。
//leetcode链接 : https://leetcode.com/problems/reaching-points/
public class Code04_ReachingPoints {
    //逆向转换的思路
    public static boolean reachingPoints1(int sx, int sy, int tx, int ty) {
        while (tx != ty) {
            if (tx < ty) {
                ty -= tx;
            } else {
                tx -= ty;
            }
            if (sx == tx && sy == ty) {
                return true;
            }
        }
        return false;
    }
    //取模运算 ty %= tx 等价于多次执行 ty -= tx
    public static boolean reachingPoints2(int sx, int sy, int tx, int ty) {
        while (sx < tx && sy < ty) {
            if (tx < ty) {
                ty %= tx;
            } else {
                tx %= ty;
            }
        }
        return (sx == tx && sy <= ty && (ty - sy) % sx == 0)
                || (sy == ty && sx <= tx && (tx - sx) % sy == 0);
    }
}
