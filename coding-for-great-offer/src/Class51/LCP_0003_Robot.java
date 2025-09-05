package Class51;

import java.util.Arrays;
import java.util.HashSet;

//力扣团队买了一个可编程机器人，机器人初始位置在原点(0, 0)。小伙伴事先给机器人输入一串指令command，机器人就会无限循环这条指令的步骤进行移动。指令有两种：
//U: 向y轴正方向移动一格
//R: 向x轴正方向移动一格。
//不幸的是，在 xy 平面上还有一些障碍物，他们的坐标用obstacles表示。机器人一旦碰到障碍物就会被损毁。
//给定终点坐标(x, y)，返回机器人能否完好地到达终点。如果能，返回true；否则返回false。
//示例 1：
//输入：command = "URR", obstacles = [], x = 3, y = 2
//输出：true
//解释：U(0, 1) -> R(1, 1) -> R(2, 1) -> U(2, 2) -> R(3, 2)。
//示例 2：
//输入：command = "URR", obstacles = [[2, 2]], x = 3, y = 2
//输出：false
//解释：机器人在到达终点前会碰到(2, 2)的障碍物。
//示例 3：
//输入：command = "URR", obstacles = [[4, 2]], x = 3, y = 2
//输出：true
//解释：到达终点后，再碰到障碍物也不影响返回结果。
//限制：
//2 <= command的长度 <= 1000
//command由U，R构成，且至少有一个U，至少有一个R
//0 <= x <= 1e9, 0 <= y <= 1e9
//0 <= obstacles的长度 <= 1000
//obstacles[i]不为原点或者终点
//链接：https://leetcode-cn.com/problems/programmable-robot/
public class LCP_0003_Robot {

    public static boolean robot1(String command, int[][] obstacles, int x, int y) {
        int X = 0;//一轮指令后x方向的总移动距离
        int Y = 0;//一轮指令后Y方向的总移动距离
        HashSet<Integer> set = new HashSet<>();//存储一轮指令后经过的所有坐标
        set.add(0);//加入初始位置
        for (char c : command.toCharArray()) {
            X += c == 'R' ? 1 : 0;
            Y += c == 'U' ? 1 : 0;
            set.add((X << 10) | Y);//将指令经过的坐标编码为整数存储
        }
        if (!meet1(x, y, X, Y, set)) {
            return false;
        }
        //检查所有的障碍物是否在机器人到达终点的路径上
        for (int[] ob : obstacles) {
            if (ob[0] <= x && ob[1] <= y && meet1(ob[0], ob[1], X, Y, set)) {
                return false;
            }
        }
        return true;
    }

    public static boolean meet1(int x, int y, int X, int Y, HashSet<Integer> set) {
        if (X == 0) {
            return x == 0;
        }
        if (Y == 0) {
            return y == 0;
        }
        //计算最少需要完成多少轮指令
        int atLeast = Math.min(x / X, y / Y);
        //执行atLeast轮后，剩余需要移动的距离
        int rx = x - atLeast * X;
        int ry = y - atLeast * Y;
        return set.contains((rx << 10) | ry);//计算剩余的是否为单次执行的指令
    }

    //使用为数组来代替哈希
    public static final int bit = 10;//单个坐标分量的二进制位数
    public static int bits = (1 << (bit << 1));//编码后的坐标的总二进制位数
    //位数组：用int数组模拟位存储，1个int占32位，因此数组长度 = 总状态数 / 32 = bits >> 5
    public static int[] set = new int[bits >> 5];


    public static boolean robot2(String command, int[][] obstacles, int x, int y) {
        Arrays.fill(set, 0);
        set[0] = 1;
        int X = 0;
        int Y = 0;
        for (char c : command.toCharArray()) {
            X += c == 'R' ? 1 : 0;
            Y += c == 'U' ? 1 : 0;
            add((X << 10) | Y);
        }
        if (!meet2(x, y, X, Y)) {
            return false;
        }
        for (int[] ob : obstacles) {
            if (ob[0] <= x && ob[1] <= y && meet2(ob[0], ob[1], X, Y)) {
                return false;
            }
        }
        return true;
    }

    public static boolean meet2(int x, int y, int X, int Y) {
        if (X == 0) {
            return x == 0;
        }
        if (Y == 0) {
            return y == 0;
        }
        int alLeast = Math.min(x / X, y / Y);
        int rx = x - alLeast * X;
        int ry = y - alLeast * Y;
        return contains((rx << 10) | ry);
    }

    public static void add(int status) {
        set[status >> 5] |= 1 << (status & 31);
    }
    //查询坐标编码的状态是否存在
    //status是否超出可能的状态
    public static boolean contains(int status) {
        return (status < bits) && (set[status >> 5] & (1 << (status & 31))) != 0;
    }

    public static void printBinary(int num) {
        for(int i = 31;i>=0;i--) {
            System.out.print((num & (1 << i)) != 0 ?"1" : "0");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int x = 7;
        printBinary(x);

        int y = 4;
        printBinary(y);

        int c = (x << 10) | y;
        printBinary(c);

        System.out.println(c);
    }
}
