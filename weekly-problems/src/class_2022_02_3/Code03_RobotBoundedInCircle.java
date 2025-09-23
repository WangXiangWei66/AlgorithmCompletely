package class_2022_02_3;

//在无限的平面上，机器人最初位于(0, 0)处，面朝北方
//机器人可以接受下列三条指令之一：
//"G"：直走 1 个单位
//"L"：左转 90 度
//"R"：右转 90 度
//机器人按顺序执行指令instructions，并一直重复它们。
//只有在平面中存在环使得机器人永远无法离开时，返回true
//否则，返回 false。
//leetcode链接 : https://leetcode.com/problems/robot-bounded-in-circle/
public class Code03_RobotBoundedInCircle {

    public static boolean isRobotBounded(String ins) {
        int row = 0;
        int col = 0;
        int direction = 0;//记录机器人当前的朝向
        char[] str = ins.toCharArray();
        for (char cur : str) {
            if (cur == 'R') {
                direction = right(direction);
            } else if (cur == 'L') {
                direction = left(direction);
            } else {
                row = row(direction, row);
                col = col(direction, col);
            }
        }
        return row == 0 && col == 0 || direction != 0;
    }
    //0:北, 1:东, 2:南, 3:西
    public static int left(int direction) {
        return direction == 0 ? 3 : (direction - 1);
    }

    public static int right(int direction) {
        return direction == 3 ? 0 : (direction + 1);
    }

    public static int row(int direction, int r) {
        return (direction == 1 || direction == 3) ? r : (r + (direction == 0 ? 1 : -1));
    }

    public static int col(int direction, int c) {
        return (direction == 0 || direction == 2) ? c : (c + (direction == 1 ? 1 : -1));
    }
}
